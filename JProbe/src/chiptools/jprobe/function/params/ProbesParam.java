package chiptools.jprobe.function.params;

import chiptools.jprobe.data.Probes;

public interface ProbesParam {
	
	public void setProbes(Probes p);
	public Probes getProbes();
	public void setOutputName(String n);
	public String getOutputName();
	public void setProbesName(String n);
	public String getProbesName();
}
