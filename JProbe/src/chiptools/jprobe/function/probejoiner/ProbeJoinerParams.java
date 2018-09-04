package chiptools.jprobe.function.probejoiner;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.ProbeLenParam;
import chiptools.jprobe.function.params.ProbesParam;

public class ProbeJoinerParams implements ProbesParam, ProbeLenParam {
	
	public int NUMBINDINGSITES = -1;
	public int MINSITEDIST = 2;
	public int MAXSITEDIST = 16;
	
	private Probes m_Probes = null;
	private int m_ProbeLen = -1;
	
	public String m_OutputName = null;
	
	@Override
	public void setProbes(Probes p) {
		m_Probes = p;
	}

	@Override
	public Probes getProbes() {
		return m_Probes;
	}

	@Override
	public void setProbeLength(int length) {
		m_ProbeLen = length;
	}

	@Override
	public int getProbeLength() {
		return m_ProbeLen;
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
