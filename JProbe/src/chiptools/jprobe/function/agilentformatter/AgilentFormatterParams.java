package chiptools.jprobe.function.agilentformatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PrimerParam;

public class AgilentFormatterParams implements PrimerParam{
	
	public List<DataCategory<Probes>> PROBE_CATEGORIES = null;
	
	public String ARRAY_NAME = null;
	
	private String m_Primer = "";
	private String m_PrimerName = null;
	private Map<String, String> m_Metadata = null;
	
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

	@Override
	public void setPrimerName(String name) {
		m_PrimerName = name;
	}

//	@Override
//	public String getPrimerName() {
//		return m_PrimerName;
//	}

//	public Map<String, String> getMetadata(){
//		m_Metadata = new HashMap<>();
//		m_Metadata.put("Primer", m_PrimerName);
//		return m_Metadata;
//	}
	
	// As opposed to creating metadata in the -Param class like other function, 
	// AgilentFormatter's metadata is created in AgilentFormatter class.


	
}
