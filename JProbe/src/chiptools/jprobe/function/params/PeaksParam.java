package chiptools.jprobe.function.params;

import chiptools.jprobe.data.Peaks;

public interface PeaksParam {
	
	public void setPeaks(Peaks p);
	public Peaks getPeaks();
	public void setOutputName(String n);
	public String getOutputName();
	public void setPeaksName(String n);
	public String getPeaksName();
	

}
