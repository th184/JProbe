package chiptools.jprobe.function.peakfinder;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.GenomeParam;
import chiptools.jprobe.function.params.PeaksParam;
import chiptools.jprobe.function.params.SummitParam;

public class PeakFinderParams implements GenomeParam, PeaksParam, SummitParam {
	
	private File m_Genome = null;
	private Peaks m_Peaks = null;
//	private int m_Summit = -1;
	private Integer m_Summit = null;
	private String m_OutputName = null;
	private String m_PeaksName = null;
	private String m_GenomeName = null;
	private Map<String, String> m_Metadata = null;
	
	@Override
	public void setGenomeFile(File f) {
		m_Genome = f;
	}

	@Override
	public File getGenomeFile() {
		return m_Genome;
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
	public void setSummit(int summit) {
		m_Summit = summit;
	}

	@Override
	public int getSummit() {
		return m_Summit;
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
	public void setPeaksName(String n) {
		m_PeaksName = n;
	}

	@Override
	public String getPeaksName() {
		return m_PeaksName;
	}

	@Override
	public void setGenomeFileName(String n) {
		m_GenomeName = n;
	}

	public Map<String, String> getMetadata(){
		m_Metadata = new LinkedHashMap<>();
		m_Metadata.put("Generated data", "");
		m_Metadata.put("Function", "probe filter");
		m_Metadata.put("Peaks", m_PeaksName);
		m_Metadata.put("Genome",m_GenomeName);
		m_Metadata.put("Summit", check(m_Summit));
		return m_Metadata;
		
	}
	private String check(Integer arg) {
		if(arg==null) return "N/A";
		return String.valueOf(arg);
	}
}
