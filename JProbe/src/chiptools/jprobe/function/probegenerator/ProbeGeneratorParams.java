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
import jprobe.services.JProbeCore;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class ProbeGeneratorParams implements PeakSeqsParam, KmerParam, PWMParam, EscoreParam, ProbeLenParam {
	
	public int BINDINGSITE = 9;
	public int WINDOWSIZE = 3;
	
	private PeakSequences m_PeakSeqs = null;
	private Kmer m_Kmers = null;
	private PWM m_PWM = null;
	private double m_Escore = 0.4;
	private int m_ProbeLen = 36;
	
	private Metadata m_Metadata = null;
	
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

	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Probe generator"));
		m_Metadata.put(Metadata.Field.PEAK_SEQ, new MetaObject(m_PeakSeqs)); 
		m_Metadata.put(Metadata.Field.KMER, new MetaObject(m_Kmers)); 
		m_Metadata.put(Metadata.Field.PWM, new MetaObject(m_PWM) ); 
		m_Metadata.put(Metadata.Field.PROBE_LEN, new MetaObject(m_ProbeLen));
		m_Metadata.put(Metadata.Field.BINDING_SITE_SIZE, new MetaObject(BINDINGSITE));
		m_Metadata.put(Metadata.Field.WINDOW_SIZE, new MetaObject(WINDOWSIZE));
		m_Metadata.put(Metadata.Field.E_SCORE, new MetaObject(m_Escore));
		return m_Metadata;
	}

	

}
