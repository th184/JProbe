package plugins.jprobe.gui.filemenu;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bepler.crossplatform.OS;
import bepler.crossplatform.Platform;
import plugins.jprobe.gui.JProbeGUIFrame;
import jprobe.services.JProbeCore;
import plugins.jprobe.gui.filemenu.*;

public class FileMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	
	public FileMenu(JProbeGUIFrame parentFrame, JProbeCore core){
		super("File");
		//this.add(formatMI.formatMenuItem(new NewMenuItem(core)));
		this.add(new NewMenuItem(core));
		this.add(new OpenMenuItem(core));
		this.addSeparator();
		this.add(new SaveMenuItem(core));
		this.add(new SaveAsMenuItem(core));
		this.addSeparator();
		this.add(new ImportMenu(core, parentFrame.getImportChooser()));
		this.add(new ExportMenu(core, parentFrame.getExportSingleFileChooser(), parentFrame.getExportMultiFileChooser()));
		//only add the quit menu if not on Mac
		if(Platform.getInstance().getOperatingSystem() != OS.MAC){
			this.addSeparator();
			this.add(new QuitMenuItem(parentFrame));
		}
		
	}

	
	
//	public void MenuLayout(FileMenu menu) {
//		System.out.println("Format menu...");
//		
//		GridBagConstraints gbc = new GridBagConstraints();
//		  gbc.gridheight = 1;
//		  gbc.gridwidth  = 1;
//		  gbc.gridy = 0;
//		  gbc.gridx = 0;
//		  gbc.insets = new Insets(0, 0, 0, 4);
//
//		  gbc.weightx = 1d;
//		  gbc.fill = GridBagConstraints.HORIZONTAL;
//		 // item.add(Box.createHorizontalGlue(), gbc);
//		  gbc.gridx = 1;
//		  gbc.fill = GridBagConstraints.NONE;
//		  gbc.weightx = 0d;
//		  gbc.anchor = GridBagConstraints.EAST;
//		  
//		  this.add(menu, gbc);
//
////		  item.setMnemonic(mi.getMnemonic());
////		  item.setAccelerator(mi.getAccelerator());
		
	//}
	
	
}
