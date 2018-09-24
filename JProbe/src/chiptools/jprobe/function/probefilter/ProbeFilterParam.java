package chiptools.jprobe.function.probefilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.ProbesParam;
import util.genome.probe.Probe;

public class ProbeFilterParam implements util.genome.probe.ProbeUtils.Filter, ProbesParam {
	
	private final List<util.genome.probe.ProbeUtils.Filter> m_Filters = new ArrayList<util.genome.probe.ProbeUtils.Filter>();
	private Probes m_Probes = null;
	private Random m_Random = new Random();
	private int m_Remove = 0;
	
	public Integer MINMUT = null; // the default is "off"
	public Integer MAXMUT = null;
	public Integer MINBINDDIST = null;
	public Integer MAXBINDDIST = null;
	public Integer MINBINDSITE = null;
	public Integer MAXBINDSITE = null;
	public Integer RANDOMSEED = null;
	
	private String m_OutputName = null;
	private String m_ProbesName = null;
	private String m_IncludedChroms = null;
	private String m_ExcludedChroms = null;
	private String m_IncludedSeqs = null;
	private String m_ExcludedSeqs = null;
	private Map<String, String> m_Metadata = null;
	
	
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

	@Override
	public void setProbesName(String n) { m_ProbesName = n; }

	@Override
	public String getProbesName() { return m_ProbesName; }
	
	public void setIncludedChroms(String input) { m_IncludedChroms = input; }
	
	public void setExcludedChroms(String input) { m_ExcludedChroms = input; }
	
	public void setIncludedSeqs(String input) { m_IncludedSeqs = input; }
	
	public void setExcludedSeqs(String input) { m_ExcludedSeqs = input; }
	
	public Map<String, String> getMetadata(){
		m_Metadata = new LinkedHashMap<>();
		m_Metadata.put("Generated data", "");
		m_Metadata.put("Function", "probe filter");
		m_Metadata.put("Probe set name", m_ProbesName);
		m_Metadata.put("Min mutations", check(MINMUT));
		m_Metadata.put("Max mutations", check(MAXMUT));
		m_Metadata.put("Min binding sites", check(MINBINDSITE));
		m_Metadata.put("Max binding sites", check(MAXBINDSITE));
		m_Metadata.put("Min binding distance", check(MINBINDDIST));
		m_Metadata.put("Max binding distance", check(MAXBINDDIST));
		m_Metadata.put("Included sequences", check(m_IncludedSeqs));
		m_Metadata.put("Excluded sequences", check(m_ExcludedSeqs));
		m_Metadata.put("Included chromosomes", check(m_IncludedChroms));
		m_Metadata.put("Excluded chromosomes", check(m_ExcludedChroms));
		m_Metadata.put("Num probes removed", check(m_Remove));
		m_Metadata.put("Random seed", check(RANDOMSEED));
		return m_Metadata;
		
	}
	private String check(String arg) {
		if(arg==null) return "N/A";
		return arg;
	}
	private String check(Integer arg) {
		if(arg==null) return "N/A";
		return String.valueOf(arg);
	}
}
