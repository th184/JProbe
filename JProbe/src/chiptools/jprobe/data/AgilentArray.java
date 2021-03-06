package chiptools.jprobe.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import chiptools.jprobe.function.agilentformatter.AgilentFormatterParams;
import chiptools.jprobe.function.agilentformatter.AgilentFormatter.Pair;
import jprobe.services.data.AbstractFinalData;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import jprobe.services.data.Metadata.Field;

public class AgilentArray extends AbstractFinalData{
	private static final long serialVersionUID = 1L;

	public static final int NUM_COLS = 1 + AgilentProbe.NUM_ENTRIES;
	public static final int NAME = 0;
	private String m_AgilentStart = "";
	
	private final List<AgilentProbe> m_Probes;
	
	public AgilentArray(AgilentFormatterParams params, List<AgilentProbe> probes, DataType type, StringBuilder AgilentMetadata) {
		super(NUM_COLS, probes.size(), type, params.ARRAY_NAME, params.getMetadata()); 
		m_Probes = probes;
		m_AgilentStart = params.PROBE_START;
		this.setAgilentMetadata(AgilentMetadata);
	}
	//	private List<String> parse(List<Pair> metadata){
//		List<String> str_metadata = new ArrayList<>();
//		for(int i=0;i<metadata.size();i++) {
//			String k = metadata.get(i).getKey();
//			String v = metadata.get(i).getValue();
//			String line;
//			if(!v.isEmpty()) {
//				line = k+": "+v;
//			}else if(!k.isEmpty()){
//				line = k;
//			}else {
//				line=""; //empty line separates probe set 
//			}
//			str_metadata.add(line);
//		}
//		return str_metadata;
//	}
	@Override
	public void dispose() {
		//do nothing;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return String.class;
	}

	@Override
	public String getColumnName(int col) {
		switch(col){
		case NAME:
			return "Name";
		default:
			return AgilentProbe.getEntryName(col - 1);
		}
	}
	
	public int size(){
		return m_Probes.size();
	}
	
	protected int getNumPlaces(){
		return 1 + (int) Math.log10(this.size());
	}

	protected String getName(int index){
		String format = "%s_%0"+this.getNumPlaces()+"d";
//		return String.format(format, m_Name, index+1);
		return String.format(format, m_AgilentStart, index+1);
	}
	
	public String toString(int index){
		return this.getName(index) + "\t" + m_Probes.get(index).toString(this.getNumPlaces());
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch(col){
		case NAME:
			return this.getName(row);
		default:
			return m_Probes.get(row).getEntry(col - 1, this.getNumPlaces());
		}
	}
	

}
