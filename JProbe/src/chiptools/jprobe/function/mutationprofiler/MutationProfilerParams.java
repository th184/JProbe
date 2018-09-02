package chiptools.jprobe.function.mutationprofiler;

import java.io.File;

import chiptools.jprobe.function.params.OutputNameParam;

public class MutationProfilerParams {
	
	public double minEscore = Double.NEGATIVE_INFINITY;
	public double minDifference = Double.NEGATIVE_INFINITY;
	public int bindingSite = -1;
	
	public File kmerLibrary = null;
	public boolean recursive = false;
	
	public String seq1 = null;
	public String seq1Name = null;
	
	public String seq2 = null;
	public String seq2Name = null;
	
	//private String m_OutputName = null;
	
//	@Override
//	public void setOutputName(String name) {
//		m_OutputName = name;	
//	}
//	@Override
//	public String getOutputName() {
//		return m_OutputName;
//	}
	
}
