package chiptools.jprobe.function.probemutator;

import java.util.LinkedHashMap;
import java.util.Map;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.EscoreParam;
import chiptools.jprobe.function.params.KmerParam;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PrimerParam;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class ProbeMutatorParams implements ProbesParam, KmerParam, EscoreParam, PrimerParam {
	
	public int BINDING_SITE_BARRIER = 2;
	public boolean MUTATE_BINDING_SITES = false;
	public double MAXIMUM_OVERLAP = 0.5;
	
	
	private String m_Primer = null;
	private double m_Escore = 0.3;
	private Kmer m_Kmer = null;
	private Probes m_Probes = null;
	private String m_OutputName = null;
	
	private String m_PrimerName = null;
	private Metadata m_Metadata = null;
	
	
	@Override
	public void setEscore(double escore) {
		m_Escore = escore;
	}

	@Override
	public double getEscore() {
		return m_Escore;
	}

	@Override
	public void setKmers(Kmer k) {
		m_Kmer = k;
	}

	@Override
	public Kmer getKmers() {
		return m_Kmer;
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
	public void setPrimer(String sequence) {
		m_Primer = sequence;
	}

	@Override
	public String getPrimer() {
		return m_Primer;
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
	public void setPrimerName(String name) { m_PrimerName = name; }

	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Probe mutator"));
		m_Metadata.put(Metadata.Field.PROBE_SET, new MetaObject(m_Probes));
		m_Metadata.put(Metadata.Field.KMER, new MetaObject(m_Kmer));
		m_Metadata.put(Metadata.Field.E_SCORE, new MetaObject(m_Escore));
		m_Metadata.put(Metadata.Field.PRIMER, new MetaObject(m_PrimerName));
		m_Metadata.put(Metadata.Field.OVERLAP, new MetaObject(MAXIMUM_OVERLAP));
		m_Metadata.put(Metadata.Field.BINDING_SITE_BARRIER, new MetaObject(BINDING_SITE_BARRIER));
		m_Metadata.put(Metadata.Field.MUTATE_BINDING_SITE, new MetaObject(MUTATE_BINDING_SITES));
		return m_Metadata;
		
	}

}
