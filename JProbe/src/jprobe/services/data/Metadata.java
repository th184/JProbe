package jprobe.services.data;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Metadata implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Map<Field, MetaObject> m_Metadata = new LinkedHashMap<>();
	
	public enum Field{
		// Data object
		DATA("Data name"), PROBE_SET("Probe set"), PEAK_SET("Peak set"), 
		KMER("Kmer"), PWM("PWM"), PEAK_SEQ("Peak seq"), PROFILE("Profile"),
		// list object
		INC_PEAK_LIST("Included peak set(s)"), EXC_PEAK_LIST("Excluded peak set(s)"),
		PROBE_LIST("Probe set(s)"), KMER_LIST("Kmer(s)"), PWM_LIST("PWM(s)"),
		// string
		DATA_TYPE("Data type"), FUNC("Function"), GENOME("Genome"), 
		INC_CHROM("Included chromosomes"), EXC_CHROM("Excluded chromosomes"), PRIMER("Primer"),
		INC_SEQ("Included sequences"), EXC_SEQ("Excluded sequences"), ARRAY_NAME("Agilent array"),
		AVG_PROBE_PER_PEAK("Avg probes gen per peak"),REPEAT_SCORE("Repeat score"),
		REPEAT_LEN("Repeat length"),
		// int
		SUMMIT("Summit"), WINDOW_SIZE("Window size"),PROBE_LEN("Probe length"), 
		NUM_PROBE_GEN("Num probes generated"), 
		DISTICT_NUM_PROBE_GEN("Distinct num probes generated"),
		TOTAL_NUM_PROBE_GEN("Total num probes generated"),
		MIN_MUT("Min mutations"), MAX_MUT("Max mutations"),
		MIN_BINDING_DIST("Min binding dist"), MAX_BINDING_DIST("Max binding dist"),
		MIN_BINDING_SITE("Min binding site"), MAX_BINDING_SITE("Max binding site"),
		MIN_SITE_DIST("Min site distance"), MAX_SITE_DIST("Max site distance"),
		BINDING_SITE_SIZE("Binding site size"), NUM_BINDING_SITE("Num binding site"), 
		BINDING_SITE_BARRIER("Binding site barrier"), 
		NUM_PROBE_REMOVED("Num probes removed"), RANDOM_SEED("Random seed"),
		FWD_REP("Forward replicates"), RVS_REP("Reverse replicates"),
		ROW_COUNT("Row count"), 
		// double
		E_SCORE("E-score"),MIN_Q("Min Q value"), MAX_Q("Max Q value"), 
		MIN_P("Min P value"), MAX_P("Max P value"), OVERLAP("Overlap"),
		
		// boolean 
		MUTATE_BINDING_SITE("Mutate binding site"); 
		private final String text;
		Field(final String text){
			this.text = text;
		}
		@Override
		public String toString() {
			return text;
		}
	}
	
	public enum FieldType{
		DATA, DATA_SET, STRING, INT, DOUBLE, BOOLEAN
	}
	
	public FieldType getFieldType(Field f) {
		EnumSet<Field> typeData = EnumSet.of(
				Field.DATA, Field.PROBE_SET, Field.PEAK_SET, 
				Field.KMER, Field.PWM, Field.PEAK_SEQ, Field.PROFILE
				);
		EnumSet<Field> typeDataList = EnumSet.of(
				Field.INC_PEAK_LIST, Field.EXC_PEAK_LIST, 
				Field.PROBE_LIST, Field.KMER_LIST, Field.PWM_LIST
				);
		EnumSet<Field> typeString = EnumSet.of(
				Field.DATA_TYPE, Field.FUNC, Field.GENOME, Field.PRIMER,
				Field.INC_CHROM, Field.EXC_CHROM, // not sure about these two
				Field.INC_SEQ, Field.EXC_SEQ, Field.ARRAY_NAME,
				Field.AVG_PROBE_PER_PEAK, Field.REPEAT_SCORE, Field.REPEAT_LEN
				);
		EnumSet<Field> typeInt = EnumSet.of(
				Field.SUMMIT, Field.PROBE_LEN, Field.NUM_PROBE_GEN,
				Field.DISTICT_NUM_PROBE_GEN, Field.TOTAL_NUM_PROBE_GEN,
				Field.MIN_MUT, Field.MAX_MUT, Field.FWD_REP, Field.RVS_REP,
				Field.MIN_BINDING_SITE, Field.MAX_BINDING_SITE,
				Field.BINDING_SITE_SIZE, Field.NUM_BINDING_SITE, Field.WINDOW_SIZE,
				Field.MIN_SITE_DIST, Field.MAX_SITE_DIST, 
				Field.BINDING_SITE_BARRIER, Field.NUM_PROBE_REMOVED, Field.RANDOM_SEED, 
				Field.ROW_COUNT
				);
		EnumSet<Field> typeDouble = EnumSet.of(
				Field.E_SCORE, Field.MIN_Q, Field.MAX_Q, Field.MIN_P, Field.MAX_P,
				Field.OVERLAP
				);
		
		EnumSet<Field> typeBoolean = EnumSet.of(Field.MUTATE_BINDING_SITE);
		
		if(typeData.contains(f)) {
			return FieldType.DATA;
		}else if(typeDataList.contains(f)) {
			return FieldType.DATA_SET;
		}else if(typeString.contains(f)) {
			return FieldType.STRING;
		}else if(typeInt.contains(f)){
			return FieldType.INT;
		}else if(typeDouble.contains(f)){
			return FieldType.DOUBLE;
		}		
		return FieldType.BOOLEAN;
		
	}
	
	public void put(Field key, MetaObject val) {
		m_Metadata.put(key, val);
	}
	
	public Set<String> stringKeySet(){
		Set<String> stringKeys = new HashSet<>();
		for(Field k: m_Metadata.keySet()) {
			stringKeys.add(k.toString());
		}
		return stringKeys;
	}
	
	public Set<Field> keySet(){
		return m_Metadata.keySet();
	}
	
	public MetaObject get(Field key) {
		return m_Metadata.get(key);
	}
	
	public Set<Entry<Field, MetaObject>> entrySet() {
		return m_Metadata.entrySet();
	}
	


}
