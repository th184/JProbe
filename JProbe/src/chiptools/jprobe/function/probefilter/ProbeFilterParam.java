package chiptools.jprobe.function.probefilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import util.genome.probe.Probe;

public class ProbeFilterParam implements util.genome.probe.ProbeUtils.Filter, ProbesParam {
	
	private final List<util.genome.probe.ProbeUtils.Filter> m_Filters = new ArrayList<util.genome.probe.ProbeUtils.Filter>();
	private Probes m_Probes = null;
	private Random m_Random = new Random();
	private int m_Remove = 0;
	
	public Integer MINMUT = -1; // the default is "off"; originally set to "null"
	public Integer MAXMUT = -1;
	public Integer MINBINDDIST = -1;
	public Integer MAXBINDDIST = -1;
	public Integer MINBINDSITE = -1;
	public Integer MAXBINDSITE = -1;
	public Integer RANDOMSEED = -1;
	
	private String m_OutputName = null;
	private String m_IncludedChroms = null;
	private String m_ExcludedChroms = null;
	private String m_IncludedSeqs = null;
	private String m_ExcludedSeqs = null;
	private Metadata m_Metadata = null;
	
	
	public void addFilter(util.genome.probe.ProbeUtils.Filter f){
		m_Filters.add(f);
	}
	
	@Override
	public boolean keep(Probe p) {
		for(util.genome.probe.ProbeUtils.Filter f : m_Filters){
			if(!f.keep(p)) return false;
		}
		return true;
	}
	
	public void setRandom(Random r){ m_Random = r; }
	
	public Random getRandom(){ return m_Random; }
	
	
	public void setRemove(int remove){ m_Remove = remove; }
	
	public int getRemove(){ return m_Remove; }

	@Override
	public void setProbes(Probes p) { m_Probes = p; }

	@Override
	public Probes getProbes() { return m_Probes; }

	@Override
	public void setOutputName(String name) { m_OutputName = name; }

	@Override
	public String getOutputName() { return m_OutputName; }

	
	public void setIncludedChroms(String input) { m_IncludedChroms = input; }
	
	public void setExcludedChroms(String input) { m_ExcludedChroms = input; }
	
	public void setIncludedSeqs(String input) { m_IncludedSeqs = input; }
	
	public void setExcludedSeqs(String input) { m_ExcludedSeqs = input; }
	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Probe filter"));
		m_Metadata.put(Metadata.Field.PROBE_SET, new MetaObject(m_Probes));
		m_Metadata.put(Metadata.Field.MIN_MUT, new MetaObject(MINMUT));
		m_Metadata.put(Metadata.Field.MAX_MUT, new MetaObject(MAXMUT));
		m_Metadata.put(Metadata.Field.MIN_BINDING_SITE, new MetaObject(MINBINDSITE));
		m_Metadata.put(Metadata.Field.MAX_BINDING_SITE, new MetaObject(MAXBINDSITE));
		m_Metadata.put(Metadata.Field.MIN_BINDING_DIST, new MetaObject(MINBINDDIST));
		m_Metadata.put(Metadata.Field.MAX_BINDING_DIST, new MetaObject(MAXBINDDIST));
		m_Metadata.put(Metadata.Field.INC_SEQ, new MetaObject(m_IncludedSeqs));
		m_Metadata.put(Metadata.Field.EXC_SEQ, new MetaObject(m_ExcludedSeqs));
		m_Metadata.put(Metadata.Field.INC_CHROM, new MetaObject(m_IncludedChroms));
		m_Metadata.put(Metadata.Field.EXC_CHROM, new MetaObject(m_ExcludedChroms));
		m_Metadata.put(Metadata.Field.NUM_PROBE_REMOVED, new MetaObject(m_Remove));
		m_Metadata.put(Metadata.Field.RANDOM_SEED, new MetaObject(RANDOMSEED));
		return m_Metadata;
		
	}
//	private String check(String arg) {
//		if(arg==null) return "N/A";
//		return arg;
//	}
//	private String check(Integer arg) {
//		if(arg==null) return "N/A";
//		return String.valueOf(arg);
//	}
}
