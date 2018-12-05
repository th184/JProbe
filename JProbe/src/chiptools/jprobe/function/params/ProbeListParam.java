package chiptools.jprobe.function.params;

import java.util.List;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.Probes;

public interface ProbeListParam {
	public void setProbeSet(List<Probes> probes);
	public List<Probes> getProbeSet();
	public void setOutputName(String n);
	public String getOutputName();
}
