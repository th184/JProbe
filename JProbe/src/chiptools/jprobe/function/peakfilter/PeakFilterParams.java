package chiptools.jprobe.function.peakfilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.OutputNameParam;
import chiptools.jprobe.function.params.PeaksParam;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import util.genome.peak.Peak;
import util.genome.peak.PeakUtils.Filter;

public class PeakFilterParams implements Filter, PeaksParam{
	
	public double MINPVAL = 0;
	public double MAXPVAL = 0;
	public double MINQVAL = 0;
	public double MAXQVAL = 0;
	
	private Peaks m_Peaks = null;
	private String m_OutputName = null;
	
	private String m_IncludeChrom = null;
	private String m_ExcludeChrom = null;
	private Metadata m_Metadata = null;
	
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

	public void setIncludeChrom(String input) {
		m_IncludeChrom = input;
	}
	
	public void setExcludeChrom(String input) {
		m_ExcludeChrom = input;
	}
	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Peak filter"));
		m_Metadata.put(Metadata.Field.PEAK_SET, new MetaObject(m_Peaks));
		m_Metadata.put(Metadata.Field.INC_CHROM, new MetaObject(m_IncludeChrom));
		m_Metadata.put(Metadata.Field.EXC_CHROM, new MetaObject(m_ExcludeChrom));
		m_Metadata.put(Metadata.Field.MIN_Q, new MetaObject(MINQVAL));
		m_Metadata.put(Metadata.Field.MAX_Q, new MetaObject(MAXQVAL));
		m_Metadata.put(Metadata.Field.MIN_P, new MetaObject(MINPVAL));
		m_Metadata.put(Metadata.Field.MAX_P, new MetaObject(MAXPVAL));
		return m_Metadata;
	}

}
