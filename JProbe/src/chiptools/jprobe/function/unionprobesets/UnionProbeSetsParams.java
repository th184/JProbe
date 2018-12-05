package chiptools.jprobe.function.unionprobesets;

import java.util.List;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.ProbeListParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class UnionProbeSetsParams implements ProbeListParam{
//	public final List<String> PROBE_NAMES = new ArrayList<String>();
	private List<Probes> m_ProbeSets = null;
	private Metadata m_Metadata = null;
	private String m_OutputName = null;
	
	@Override
	public void setProbeSet(List<Probes> probes) {
		m_ProbeSets = probes;
	}

	@Override
	public List<Probes> getProbeSet() {
		return m_ProbeSets;
	}

	public Metadata getMetadata() {
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Union probe sets"));
		m_Metadata.put(Metadata.Field.PROBE_LIST, new MetaObject(m_ProbeSets));
		return m_Metadata;
	}

	@Override
	public void setOutputName(String n) {
		m_OutputName = n;
	}

	@Override
	public String getOutputName() {
		return m_OutputName;
	}
}
