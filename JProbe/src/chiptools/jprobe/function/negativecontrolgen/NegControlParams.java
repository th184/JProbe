package chiptools.jprobe.function.negativecontrolgen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.EscoreParam;
import chiptools.jprobe.function.params.GenomeParam;
import chiptools.jprobe.function.params.KmerListParam;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.ProbeLenParam;
import chiptools.jprobe.function.params.SummitParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class NegControlParams implements GenomeParam, SummitParam, KmerListParam, EscoreParam, ProbeLenParam{
	
	private List<Peaks> m_Include = new ArrayList<Peaks>();
	private List<Peaks> m_Exclude = new ArrayList<Peaks>();
	private File m_Genome = null;
	private int m_Summit = -1;
	private List<Kmer> m_Kmers = new ArrayList<Kmer>();
	private double m_Escore = 0.35;
	private int m_Num = 500; 
	private int m_Len = 36;
	private int m_Repeat = 5;
	
	private String m_GenomeName = null;
	private Metadata m_Metadata = null;
	
	public void setRepeat(int r) {
		m_Repeat = r;
	}
	public int getRepeat() {
		return m_Repeat;
	}
	public void setExcludePeaks(List<Peaks> exclude){
		m_Exclude = exclude;
	}
	
	public List<Peaks> getExcludePeaks(){
		return m_Exclude;
	}
	
	public void setIncludePeaks(List<Peaks> include){
		m_Include = include;
	}
	
	public List<Peaks> getIncludePeaks(){
		return m_Include;
	}
	
	
	@Override
	public void setGenomeFile(File f) {
		m_Genome = f;
	}

	@Override
	public File getGenomeFile() {
		return m_Genome;
	}

	@Override
	public void setSummit(int summit) {
		m_Summit = summit;
	}

	@Override
	public int getSummit() {
		return m_Summit;
	}

	@Override
	public void setKmers(List<Kmer> kmers) {
		m_Kmers = kmers;
	}

	@Override
	public List<Kmer> getKmers() {
		return m_Kmers;
	}

	@Override
	public void setEscore(double escore) {
		m_Escore = escore;
	}

	@Override
	public double getEscore() {
		return m_Escore;
	}
	
	public void setNumPeaks(int num){
		m_Num = num;
	}
	
	public int getNumPeaks(){
		return m_Num;
	}

	@Override
	public void setProbeLength(int length) {
		m_Len = length;
	}

	@Override
	public int getProbeLength() {
		return m_Len;
	}
	
	@Override
	public void setGenomeFileName(String name) {
		m_GenomeName = name;
	}

	
	public void createMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.GENOME, new MetaObject(m_GenomeName));
		m_Metadata.put(Metadata.Field.INC_PEAK_LIST, new MetaObject(m_Include));
		m_Metadata.put(Metadata.Field.EXC_PEAK_LIST, new MetaObject(m_Exclude));
		m_Metadata.put(Metadata.Field.SUMMIT, new MetaObject((Integer)m_Summit));
		m_Metadata.put(Metadata.Field.KMER_LIST, new MetaObject(m_Kmers));
		m_Metadata.put(Metadata.Field.E_SCORE, new MetaObject(m_Escore));
		m_Metadata.put(Metadata.Field.PROBE_LEN, new MetaObject((Integer)m_Len)); 
		m_Metadata.put(Metadata.Field.NUM_PROBE_GEN, new MetaObject((Integer)m_Num));
		m_Metadata.put(Metadata.Field.REPEAT_LEN, new MetaObject(String.valueOf(m_Repeat)+ " (for score calculation)"));
		// cast to prevent primitive widening conversion from int to double
	}
	public Metadata getMetadata() {
		return m_Metadata;
	}
	public void addMetadata(Metadata.Field f,String score) {
		m_Metadata.put(f, new MetaObject(score));
	}

}
