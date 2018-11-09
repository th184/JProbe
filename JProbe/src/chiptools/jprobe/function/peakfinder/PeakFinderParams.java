package chiptools.jprobe.function.peakfinder;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.GenomeParam;
import chiptools.jprobe.function.params.PeaksParam;
import chiptools.jprobe.function.params.SummitParam;
import jprobe.services.JProbeCore;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;

public class PeakFinderParams implements GenomeParam, PeaksParam, SummitParam {
	
	private File m_Genome = null;
	private Peaks m_Peaks = null;
	private int m_Summit = -1;
	private String m_OutputName = null;
	private String m_GenomeName = null;
	private Metadata m_Metadata = null;
	
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
	public void setGenomeFileName(String n) {
		m_GenomeName = n;
	}

	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Get peak sequences"));
		m_Metadata.put(Metadata.Field.PEAK_SET, new MetaObject(m_Peaks));
		m_Metadata.put(Metadata.Field.GENOME, new MetaObject(m_GenomeName));
		m_Metadata.put(Metadata.Field.SUMMIT, new MetaObject(m_Summit));
		return m_Metadata;
	}
}
