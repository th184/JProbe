package chiptools.jprobe.data;

import java.io.BufferedWriter;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import chiptools.jprobe.function.agilentformatter.AgilentFormatter.Pair;
import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;

public class AgilentArrayWriter implements DataWriter{

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return new FileNameExtensionFilter[]{
				new FileNameExtensionFilter("Agilent array file (.txt, .*)", "txt", "*")	
		};
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return AgilentArray.class;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
//		if(data instanceof AgilentArray && format.equals(null)) {
//		if(format.equals(null)) {
//			System.out.println("in IF");
//			List<String> metadata = data.getAgilentMetadata();
//			for(int i=0;i<metadata.size(); i++) {
//				out.write(metadata.get(i));
//				out.write("\n");
//			}
//		}else {
			AgilentArray array = (AgilentArray)	data;
			for(int i=0; i<array.size(); i++){
				out.write(array.toString(i));
				out.write("\n");
//			}
		}
			
//		} else if(data instanceof AgilentArray){
//			AgilentArray array = (AgilentArray)	data;
//			for(int i=0; i<array.size(); i++){
//				out.write(array.toString(i));
//				out.write("\n");
//			}
//		}
	}
	

}
