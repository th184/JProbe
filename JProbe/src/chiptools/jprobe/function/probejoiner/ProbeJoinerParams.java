package chiptools.jprobe.function.probejoiner;

import java.util.LinkedHashMap;
import java.util.Map;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.ProbeLenParam;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class ProbeJoinerParams implements ProbesParam, ProbeLenParam {
	
	public int NUMBINDINGSITES = -1;
	public int MINSITEDIST = 2;
	public int MAXSITEDIST = 16;
	
	private Probes m_Probes = null;
	private int m_ProbeLen = -1;
	
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

	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Probe joiner"));
		m_Metadata.put(Metadata.Field.PROBE_SET, new MetaObject(m_Probes));
		m_Metadata.put(Metadata.Field.PROBE_LEN, new MetaObject(m_ProbeLen));
		m_Metadata.put(Metadata.Field.NUM_BINDING_SITE, new MetaObject(NUMBINDINGSITES));
		m_Metadata.put(Metadata.Field.MIN_SITE_DIST, new MetaObject(MINSITEDIST));
		m_Metadata.put(Metadata.Field.MAX_SITE_DIST, new MetaObject(MAXSITEDIST));
		return m_Metadata;
	}


}
