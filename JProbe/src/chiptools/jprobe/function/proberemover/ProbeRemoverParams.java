package chiptools.jprobe.function.proberemover;

import chiptools.jprobe.data.BindingProfile;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.EscoreParam;
import chiptools.jprobe.function.params.ProbesParam;
import chiptools.jprobe.function.params.ProfileParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class ProbeRemoverParams implements ProbesParam, EscoreParam, ProfileParam{

	private Probes m_Probes = null;
	private double m_Escore = 0.35;
	private BindingProfile m_Profile = null;
	private Metadata m_Metadata = null;
	private String m_OutputName = null;
	
	
	@Override
	public void setProbes(Probes p) { m_Probes = p; }

	@Override
	public Probes getProbes() { return m_Probes; }

	@Override
	public void setOutputName(String name) { m_OutputName = name; }

	@Override
	public String getOutputName() { return m_OutputName; }
	
	@Override
	public void setEscore(double escore) {
		m_Escore = escore;
	}

	@Override
	public double getEscore() {
		return m_Escore;
	}

	@Override
	public void setProfile(BindingProfile p) {
		m_Profile = p;
	}

	@Override
	public BindingProfile getProfile() {
		return m_Profile;
	}
	public void addMetadata(Metadata.Field f, Integer num_probe_removed) {
		m_Metadata.put(f, new MetaObject(num_probe_removed));
	}
	  
	public void createMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Probe Remover"));
		m_Metadata.put(Metadata.Field.PROBE_SET, new MetaObject(m_Probes));
		m_Metadata.put(Metadata.Field.PROFILE, new MetaObject(m_Profile));
		m_Metadata.put(Metadata.Field.E_SCORE, new MetaObject(m_Escore));
	}
	public Metadata getMetadata() {
		return m_Metadata;
	}
}

