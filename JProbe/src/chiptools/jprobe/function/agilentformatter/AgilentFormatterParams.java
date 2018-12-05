package chiptools.jprobe.function.agilentformatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PrimerParam;
import jprobe.services.data.Data;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class AgilentFormatterParams implements PrimerParam{
	
	public List<DataCategory<Probes>> PROBE_CATEGORIES = null;
	
	
	public String ARRAY_NAME = null;
	public String PROBE_START = null;
	
	private String m_Primer = null;
	private Metadata m_Metadata = null;
	private String m_PrimerName = null;
	public int FWD_REPS = 3;
	public int RVS_REPS = 3;
	
	@Override
	public void setPrimer(String sequence) {
		m_Primer = sequence;
	}

	@Override
	public String getPrimer() {
		return m_Primer;
	}


	// As opposed to creating metadata in the -Param class like other function, 
	// AgilentFormatter's metadata is created in AgilentFormatter class.
	
	public Metadata getMetadata(){ 
		// different from all other metadata; 
		// make all args in string
		int num_probes = countProbes(PROBE_CATEGORIES);
		int total_num = num_probes*(FWD_REPS+RVS_REPS);
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.ARRAY_NAME, new MetaObject(ARRAY_NAME));
		m_Metadata.put(Metadata.Field.PRIMER, new MetaObject(addName(m_Primer))); 
		m_Metadata.put(Metadata.Field.PROBE_LIST, new MetaObject(getProbeSet(PROBE_CATEGORIES)));
		m_Metadata.put(Metadata.Field.PROBE_LEN, new MetaObject(probeLen(PROBE_CATEGORIES)));
		m_Metadata.put(Metadata.Field.DISTICT_NUM_PROBE_GEN, new MetaObject(String.format("%,d", num_probes)));
		m_Metadata.put(Metadata.Field.FWD_REP, new MetaObject(String.valueOf(FWD_REPS)));
		m_Metadata.put(Metadata.Field.RVS_REP, new MetaObject(String.valueOf(RVS_REPS)));
		m_Metadata.put(Metadata.Field.TOTAL_NUM_PROBE_GEN, new MetaObject(String.format("%,d", total_num)));
		return m_Metadata;
	}  

	private String probeLen(List<DataCategory<Probes>> probe_cats) {
		List<Integer> probe_lens = new ArrayList<>();
		for(int i=0; i<probe_cats.size();i++){
			int len = probe_cats.get(i).DATA.getProbeGroup().getProbe(0).getLength();
			probe_lens.add(len);
		}
		boolean same = true;
		// check if all lens are the same
		for(int i=0; i<probe_lens.size()-1;i++) {
			for(int j=0; j<probe_lens.size();j++) {
				if(probe_lens.get(i)!=probe_lens.get(j)) {
					same = false;
				}
			}
		}
		String probeLen="";
		int primer_len=0;
		if(m_Primer!=null) {
			primer_len = m_Primer.length();
		}
		if(same && probe_lens.size()>1) {
			probeLen = String.valueOf(probe_lens.get(0)+primer_len)+ " (probe sets have probes of equal length)";
		}else if(same && probe_lens.size()==1) {
			probeLen = String.valueOf(probe_lens.get(0)+primer_len);
		}else {
			StringJoiner temp = new StringJoiner(",");
			for(int i=0;i<probe_lens.size();i++) {
				temp.add(String.valueOf(probe_lens.get(i)+primer_len));
			}
			probeLen = temp.toString();
			probeLen += " (for the corresponding probe sets)";
			
		}
		return probeLen;
	}

	private String addName(String primer) {
		if(m_Primer!=null && m_PrimerName!=null) {
			primer = primer+" ("+m_PrimerName+")";
		}
		return primer;
	}

	private int countProbes(List<DataCategory<Probes>> probe_cats) {
		int count = 0;
		for(int i=0; i<probe_cats.size();i++) {
			count = count + probe_cats.get(i).DATA.getRowCount();
		}
		return count;
	}
	

	private String getProbeSet(List<DataCategory<Probes>> probe_cats) {
		StringJoiner join = new StringJoiner(", ");
		for(int i=0; i<probe_cats.size(); i++) {
			join.add(probe_cats.get(i).DATA.getVarName());
		}
		return join.toString();
	}
	@Override
	public void setPrimerName(String p) {
		m_PrimerName = p;
	}
	@Override
	public String getPrimerName() {
		return m_PrimerName;
	}

	


	
}
