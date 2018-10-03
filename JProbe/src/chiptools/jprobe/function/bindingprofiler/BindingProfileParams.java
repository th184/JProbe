package chiptools.jprobe.function.bindingprofiler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.KmerListParam;
import chiptools.jprobe.function.params.PWMListParam;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.data.Metadata;

public class BindingProfileParams implements ProbesParam, KmerListParam, PWMListParam{
	
	public final List<String> KMER_NAMES = new ArrayList<String>();
	public final List<String> PWM_NAMES = new ArrayList<String>();
	
	private Probes m_Probes = null;
	private List<Kmer> m_Kmers = new ArrayList<Kmer>();
	private List<PWM> m_PWMs = new ArrayList<PWM>();
	private String m_OutputName = null;
	// metadata info 
	private String m_ProbesName = null;
	private String m_KmerListName = null;
	private String m_PWMListName = null;
	private Metadata m_Metadata = null;
	
	@Override
	public void setProbes(Probes p) {
		m_Probes = p;
	}

	@Override
	public Probes getProbes() {
		return m_Probes;
	}

	@Override
	public void setPWMs(List<PWM> pwms) {
		m_PWMs = pwms;
	}

	@Override
	public List<PWM> getPWMs() {
		return m_PWMs;
	}

	@Override
	public void setKmers(List<Kmer> kmers) {
		m_Kmers = kmers;
	}

	@Override
	public List<Kmer> getKmers() {
		return m_Kmers;
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
	public void setProbesName(String name) {
		m_ProbesName = name;
	}

	@Override
	public String getProbesName() {
		return m_ProbesName;
	}

	@Override
	public void setPWMListName(String name) {
		m_PWMListName = name;
	}

	@Override
	public String getPWMListName() {
		return m_PWMListName;
	}

	@Override
	public void setKmerListName(String name) {
		m_KmerListName = name;
	}

	@Override
	public String getKmerListName() {
		return m_KmerListName;
	}
	
	public Metadata getMetadata(){
		m_Metadata = new Metadata();
		m_Metadata.put("Data", "output name");
		m_Metadata.put("Type", "data type");
		m_Metadata.put("Function", "binding profiler");
		m_Metadata.put("Probe", m_ProbesName);
		m_Metadata.put("Kmer", check(m_KmerListName));
		m_Metadata.put("PWM", check(m_PWMListName));
		return m_Metadata;
	}
	
	private String check(String arg) {
		if(arg == null) return "N/A";
		return arg;
	}
}
