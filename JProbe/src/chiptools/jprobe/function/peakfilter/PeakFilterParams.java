package chiptools.jprobe.function.peakfilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PeaksParam;
import util.genome.peak.Peak;
import util.genome.peak.PeakUtils.Filter;

public class PeakFilterParams implements Filter, PeaksParam{
	
	public double MINPVAL = 0;
	public double MAXPVAL = 0;
	public double MINQVAL = 0;
	public double MAXQVAL = 0;
	
	private Peaks m_Peaks = null;
	private String m_OutputName = null;
	
	private String m_PeaksName = null;
	private String m_IncludeChrom = null;
	private String m_ExcludeChrom = null;
	private Map<String, String> m_Metadata = null;
	
	private final List<Filter> m_Filters = new ArrayList<Filter>();
	
	public void addFilter(Filter f){
		m_Filters.add(f);
	}
	
	@Override
	public boolean keep(Peak p) {
		for(Filter f : m_Filters){
			if(!f.keep(p)) return false;
		}
		return true;
	}

	@Override
	public void setPeaks(Peaks p) {
		m_Peaks = p;
	}

	@Override
	public Peaks getPeaks() {
		return m_Peaks;
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
	public void setPeaksName(String name) {
		m_PeaksName = name;
	}

	public String getPeaksName() {
		return m_PeaksName;
	}
	
	public void setIncludeChrom(String input) {
		m_IncludeChrom = input;
	}
	
	public void setExcludeChrom(String input) {
		m_ExcludeChrom = input;
	}
	
	public Map<String, String> getMetadata(){
		m_Metadata = new LinkedHashMap<>();
		m_Metadata.put("Generated data", "");
		m_Metadata.put("Function", "peak filter");
		m_Metadata.put("Peak set name", m_PeaksName);
		m_Metadata.put("Included chroms", check(m_IncludeChrom));
		m_Metadata.put("Excluded chroms", check(m_ExcludeChrom));
		m_Metadata.put("Min Q-value", String.valueOf(MINQVAL));
		m_Metadata.put("Max Q-value", String.valueOf(MAXQVAL));
		m_Metadata.put("Min P-value", String.valueOf(MINPVAL));
		m_Metadata.put("Max P-value", String.valueOf(MAXPVAL));
		return m_Metadata;
	}
	private String check(String arg) {
		if(arg==null) return "N/A";
		return arg;
	}

}
