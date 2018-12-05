package util.genome;

import java.io.Serializable;
import java.util.*;

import util.genome.kmer.Kmer;
import util.genome.pwm.PWM;

public class Sequences {
	
	public static class Profile implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private final List<String> m_Names = new ArrayList<String>();
		private final List<String> m_Metric = new ArrayList<String>(); // added
		private final List<Integer> m_WordLens = new ArrayList<Integer>();
		private final List<double[]> m_Entries = new ArrayList<double[]>();
		
		public int size(){
			
			return m_Names.size();
		}
		
		public int numEntries(){
			return m_Entries.size();
		}
		
		public String getEntryName(int entry){
			return m_Names.get(entry);
		}
		public List<String> getNames(){ //temp
			return m_Names;
		}
		public String getMetric(int entry) {
			return m_Metric.get(entry);
		}
		public double[] getEntry(int entry){
			return m_Entries.get(entry);
		}
		
		public int getWordLen(int entry){
			return m_WordLens.get(entry);
		}
		
		public void put(String name, String metric, int wordLen, double[] entry){
			m_Names.add(name);
			m_Metric.add(metric);
			m_WordLens.add(wordLen);
			m_Entries.add(entry);
		}
		
		
		@Override
		public String toString(){
			String s = "";
			for(int i=0; i<this.size(); i++){ // this.size()==num of metrics used for scoring
				s += this.getEntryName(i);
				s += "\t" + this.getMetric(i);
				s += "\t" + this.getWordLen(i);
				for(double d : this.getEntry(i)){
					s += "\t" + d;
				}
				s += "\n";
			}
			return s;
		}
		
		
		
	}
	public static double[] getKmerScore(String seq, Kmer kmer) {
		// why is wordLength a list? Isn't it all the same number?
		int wordLen = kmer.getWordLengths()[0];
		double[] scores = new double[seq.length() - wordLen + 1];
		for(int start = 0; start<scores.length; start++){
			int end = start + wordLen;
			String subseq = seq.substring(start, end);
			scores[start] = kmer.escore(subseq);
		}
		return scores;
	}
	public static double[] getPWMScore(String seq, PWM pwm) {
		double[] scores = new double[seq.length() - pwm.length() + 1];
		for(int start = 0; start<scores.length; start++){
			int end = start + pwm.length();
			String subseq = seq.substring(start, end);
			scores[start] = pwm.scoreLogRatio(subseq);
		}
		
		return scores;
	}
	
	// CAN REMOVE. RE-WRITTEN IN BindingProfiler.java
	public static Profile profile(String seq, String seqName, Kmer[] kmers, String[] kmerNames, PWM[] pwms, String[] pwmNames){
		Profile p = new Profile();
		//for each kmer, score the sequence with each word size contained by the kmer
		for(int i=0; i<kmers.length; i++){
			Kmer kmer = kmers[i];
			if(kmer == null) continue; //
			String name = seqName + "_"; //
			name += i < kmerNames.length ? kmerNames[i] : "Kmer"+(i+1); //
			//score the sequence with words of each length contained by the kmer
			for(int wordLen : kmer.getWordLengths()){
				if(seq.length() < wordLen) continue;
				double[] scores = new double[seq.length() - wordLen + 1];
				for(int start = 0; start<scores.length; start++){
					int end = start + wordLen;
					String subseq = seq.substring(start, end);
					scores[start] = kmer.escore(subseq);
				}
				p.put(name, "Kmer", wordLen, scores);
			}
		}
		
		//for each pwm, score the sequence by each word of the size of the pwm
		for(int i=0; i<pwms.length; i++){
			PWM pwm = pwms[i];
			if(pwm == null) continue;
			String name = seqName + "_";
			name += i < pwmNames.length ? pwmNames[i] : "PWM"+(i+1);
			if(seq.length() < pwm.length()) continue;
			double[] scores = new double[seq.length() - pwm.length() + 1];
			for(int start = 0; start<scores.length; start++){
				int end = start + pwm.length();
				String subseq = seq.substring(start, end);
				scores[start] = pwm.scoreLogRatio(subseq);
			}
			p.put(name, "PWM", pwm.length(), scores);
		}
		return p;
	}

	
}
