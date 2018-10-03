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
	
	private String m_ProbesName = null;
	private String m_PrimerName = null;
	private String m_KmerName = null;
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

	@Override
	public void setKmersName(String name) { m_KmerName = name; }

	@Override
	public void setProbesName(String n) { m_ProbesName = n; }

	@Override
	public String getProbesName() { return m_ProbesName; }
	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put("Data", "output name");
		m_Metadata.put("Type", "data type");
		m_Metadata.put("Function", "probe mutator");
		m_Metadata.put("Probes", m_ProbesName);
		m_Metadata.put("K-mer", m_KmerName);
		m_Metadata.put("E-score", String.valueOf(m_Escore));
		m_Metadata.put("Primer", m_PrimerName);
		m_Metadata.put("Overlap", String.valueOf(MAXIMUM_OVERLAP));
		m_Metadata.put("Binding site barrier", String.valueOf(BINDING_SITE_BARRIER));
		m_Metadata.put("Mutate binding site", String.valueOf(MUTATE_BINDING_SITES));
		
		return m_Metadata;
		
	}

}
