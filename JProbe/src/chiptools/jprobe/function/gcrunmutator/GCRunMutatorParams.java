package chiptools.jprobe.function.gcrunmutator;

import java.util.HashMap;
import java.util.Map;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PrimerParam;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class GCRunMutatorParams implements ProbesParam, PrimerParam{
	
	private String m_Primer = null;
	private Probes m_Probes = null;
	
	private String m_PrimerName = null;
	private Metadata m_Metadata = null;
	
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
	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("G-runs mutator"));
		m_Metadata.put(Metadata.Field.PROBE_SET, new MetaObject(m_Probes));
		m_Metadata.put(Metadata.Field.PRIMER, new MetaObject(m_PrimerName));
		return m_Metadata;
	}

}
