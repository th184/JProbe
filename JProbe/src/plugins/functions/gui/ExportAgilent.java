package plugins.functions.gui;

import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import chiptools.jprobe.data.AgilentArray;
import chiptools.jprobe.function.agilentformatter.AgilentFormatter.Pair;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;
import plugins.jprobe.gui.BackgroundThread;
import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.notification.ExportEvent;

public class ExportAgilent {
	public static final String WILDCARD = "*";
	public static final String FILE_NAME_WITH_EXTENSION_REGEX = "^.*\\..+$";
	
	public static void exportData(Data data, JProbeCore core){
		JFileChooser exportChooser = new JFileChooser(){
			@Override
	        public void approveSelection() {
	            File f = new File(this.getSelectedFile().toString()+".txt");
	            if (f.exists()) {
	                int result = JOptionPane.showConfirmDialog(this,
	                        "The file already exists. Do you wish overwrite?", 
	                        "Existing file",
	                        JOptionPane.YES_NO_CANCEL_OPTION, 
	                        JOptionPane.WARNING_MESSAGE); // "warning message" dictates the icon
	                switch (result) {
	                case JOptionPane.YES_OPTION:
	                    super.approveSelection();
	                    return;
	                case JOptionPane.CANCEL_OPTION:
	                    cancelSelection();
	                    return;
	                default:
	                    return;
	                }
	            }
	            super.approveSelection();
	        }
	    };
		// retrieve file formats for this data object
		DataWriter writer = core.getDataManager().getDataWriter(data.getClass()); // check here
		if(writer == null){
			ErrorHandler.getInstance().handleWarning("Data type \""+data.getClass()+"\" not writable.", GUIActivator.getBundle());
			return;
		}
		FileFilter[] formats = writer.getValidWriteFormats();
		//if there are none, then there is an error in the data writer. warn the user and return
		if(formats.length <= 0){
			ErrorHandler.getInstance().handleWarning("There are no writable formats for the data type: "+data.getClass().getSimpleName(), GUIActivator.getBundle());
			return;
		}
		//set the file chooser's file filters to those retreived above
		exportChooser.resetChoosableFileFilters();
		exportChooser.setAcceptAllFileFilterUsed(false);
		for(FileFilter format : formats){
			exportChooser.addChoosableFileFilter(format);
		}
		//show the file chooser and write the data to the selected file using the selected file format
		String fileName = data.getOutputName();
		exportChooser.setSelectedFile(new File(fileName));
		
		int returnVal = exportChooser.showDialog(null, "Export");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = exportChooser.getSelectedFile();
		    File path = file.getParentFile();
		    String name = file.getName();
		    if (!name.endsWith(".txt")) {
		        name += ".txt";
		    }
		    String metaName = name.substring(0, name.lastIndexOf("."));
		    metaName += "_metadata.txt";

		    File outputFile = new File(path, name);
		    File metaFile = new File(path, metaName);
		    FileNameExtensionFilter format = (FileNameExtensionFilter) exportChooser.getFileFilter();
		    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
				writer.write(data, format, bw);
				bw.close();
		        try (BufferedWriter metaBW = new BufferedWriter(new FileWriter(metaFile))) {
					metaBW.append(data.getAgilentMetadata());
					metaBW.close();
		        }
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    } catch (Throwable t){
		    	t.printStackTrace();
		    }
		}
		
	}
	public static void exportData(final JProbeCore core, final DataWriter writer, final File f, final File f2,
			final Data d, final FileNameExtensionFilter format){
//		BackgroundThread.getInstance().invokeLater(new Runnable(){
//			@Override
//			public void run() {
//				notifyObservers(new ExportEvent(ExportEvent.Type.EXPORTING, dataName, f));
				try{
					BufferedWriter out = new BufferedWriter(new FileWriter(f));
					writer.write(d, format, out);
					out.close();
					BufferedWriter md_out = new BufferedWriter(new FileWriter(f2));
//					String metadata = d.getAgilentMetadata();
//					for(int i=0;i<metadata.size(); i++) {
//						md_out.write(metadata.get(i));
//						md_out.write("\n");
//					}
					md_out.close();
//					notifyObservers(new ExportEvent(ExportEvent.Type.EXPORTED, dataName, f));
				}catch(Exception e){
//					notifyObservers(new ExportEvent(ExportEvent.Type.FAILED, dataName, f));
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
				}catch(Throwable t){
//					notifyObservers(new ExportEvent(ExportEvent.Type.FAILED, dataName, f));
					ErrorHandler.getInstance().handleException(new RuntimeException(t), GUIActivator.getBundle());
				}
//			}
//		});
	}
	
	public static boolean fileEndsInValidExtension(File file, FileNameExtensionFilter filter){
		for(String ext : filter.getExtensions()){
			if(file.toString().endsWith("."+ext) || (ext.equals(WILDCARD) && file.toString().matches(FILE_NAME_WITH_EXTENSION_REGEX))){
				return true;
			}
		}
		return false;
	}
	
}
