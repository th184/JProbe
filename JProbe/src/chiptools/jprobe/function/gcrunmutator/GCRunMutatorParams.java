package chiptools.jprobe.function.gcrunmutator;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PrimerParam;
import chiptools.jprobe.function.params.ProbesParam;

public class GCRunMutatorParams implements ProbesParam, PrimerParam, OutputNameParam{
	
	private String m_Primer = null;
	private Probes m_Probes = null;
	
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

}
