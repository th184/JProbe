package chiptools.jprobe.function.probegenerator;

import java.util.LinkedHashMap;
import java.util.Map;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.function.params.EscoreParam;
import chiptools.jprobe.function.params.KmerParam;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PWMParam;
import chiptools.jprobe.function.params.PeakSeqsParam;
import chiptools.jprobe.function.params.ProbeLenParam;

public class ProbeGeneratorParams implements PeakSeqsParam, KmerParam, PWMParam, EscoreParam, ProbeLenParam {
	
	public int BINDINGSITE = 9;
	public int WINDOWSIZE = 3;
	
	private PeakSequences m_PeakSeqs = null;
	private Kmer m_Kmers = null;
	private PWM m_PWM = null;
	private double m_Escore = 0.4;
	private int m_ProbeLen = 36;
	
	private String m_PeakSeqsName = null;
	private String m_KmerName = null;
	private String m_PWMName = null;
	private Map<String, String> m_Metadata = null;
	
	@Override
	public void setKmers(Kmer k) {
		m_Kmers = k;
	}

	@Override
	public Kmer getKmers() {
		return m_Kmers;
	}

	@Override
	public void setPeakSeqs(PeakSequences peakSeqs) {
		m_PeakSeqs = peakSeqs;
	}

	@Override
	public PeakSequences getPeakSeqs() {
		return m_PeakSeqs;
	}

	@Override
	public void setPWM(PWM p) {
		m_PWM = p;
	}

	@Override
	public PWM getPWM() {
		return m_PWM;
	}

	@Override
	public void setEscore(double escore) {
		m_Escore = escore;
	}

	@Override
	public double getEscore() {
		return m_Escore;
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
	public void setPWMName(String name) { m_PWMName = name; }

	@Override
	public void setKmersName(String name) { m_KmerName = name; }

	@Override
	public void setPeakSeqsName(String n) { m_PeakSeqsName = n; }
	
	public Map<String, String> getMetadata(){
		m_Metadata = new LinkedHashMap<>();
		m_Metadata.put("Generated data", "");
		m_Metadata.put("Function", "probe generator");
		m_Metadata.put("Peak seqs", m_PeakSeqsName);
		m_Metadata.put("Kmer", m_KmerName);
		m_Metadata.put("PWM", m_PWMName);
		m_Metadata.put("Probe length", String.valueOf(m_ProbeLen));
		m_Metadata.put("Binding site size", String.valueOf(BINDINGSITE));
		m_Metadata.put("Window size", String.valueOf(WINDOWSIZE));
		m_Metadata.put("E-score", String.valueOf(m_Escore));
				
		return m_Metadata;
	}

}
