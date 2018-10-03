package chiptools.jprobe.function.probejoiner;

import java.util.LinkedHashMap;
import java.util.Map;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.ProbeLenParam;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.data.Metadata;

public class ProbeJoinerParams implements ProbesParam, ProbeLenParam {
	
	public int NUMBINDINGSITES = -1;
	public int MINSITEDIST = 2;
	public int MAXSITEDIST = 16;
	
	private Probes m_Probes = null;
	private int m_ProbeLen = -1;
	
	private String m_ProbesName = null;
	private Metadata m_Metadata = null;
	
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

	
	@Override
	public void setProbesName(String name) {
		m_ProbesName = name;
	}

	@Override
	public String getProbesName() {
		return m_ProbesName;
	}
	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put("Data", "output name");
		m_Metadata.put("Type", "data type");
		m_Metadata.put("Function used", "probe joiner");
		m_Metadata.put("Probe set name", m_ProbesName);
		m_Metadata.put("Length of probe", String.valueOf(m_ProbeLen));
		m_Metadata.put("Num binding sites", String.valueOf(NUMBINDINGSITES));
		m_Metadata.put("Min site distance", String.valueOf(MINSITEDIST));
		m_Metadata.put("Max site distance", String.valueOf(MAXSITEDIST));
		return m_Metadata;
	}


}
