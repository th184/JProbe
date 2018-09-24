package chiptools.jprobe.function.gcrunmutator;

import java.util.HashMap;
import java.util.Map;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PrimerParam;
import chiptools.jprobe.function.params.ProbesParam;

public class GCRunMutatorParams implements ProbesParam, PrimerParam{
	
	private String m_Primer = null;
	private Probes m_Probes = null;
	
	private String m_PrimerName = null;
	private String m_ProbesName = null;
	private Map<String, String> m_Metadata = null;
	
	public String m_OutputName = null;
	
	@Override
	public void setPrimer(String sequence) {
		m_Primer = sequence;
	}

	@Override
	public String getPrimer() {
		return m_Primer;
	}

	@Override
	public void setProbes(Probes p) {
		m_Probes = p;
	}

	@Override
	public Probes getProbes() {
		return m_Probes;
	}

	@Override
	public void setOutputName(String name) {
		m_OutputName = name;
	}

	@Override
	public String getOutputName() {
		return m_OutputName;
	}

	@Override
	public void setPrimerName(String name) {
		m_PrimerName = name;
	}

	@Override
	public void setProbesName(String name) {
		m_ProbesName = name;
	}

	@Override
	public String getProbesName() {
		return m_ProbesName;
	}
	
	public Map<String, String> getMetadata(){
		m_Metadata = new HashMap<>();
		m_Metadata.put("Generated data", "");
		m_Metadata.put("Function", "G-Runs mutator");
		m_Metadata.put("Probes", m_ProbesName);
		m_Metadata.put("Primer", check(m_PrimerName));
		return m_Metadata;
	}
	public String check(String arg) {
		if(arg==null) return "N/A";
		return arg;
	}


}
