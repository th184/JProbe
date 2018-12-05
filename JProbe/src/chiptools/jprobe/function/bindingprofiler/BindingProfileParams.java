package chiptools.jprobe.function.bindingprofiler;

import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.KmerListParam;
import chiptools.jprobe.function.params.PWMListParam;
import chiptools.jprobe.function.params.PrimerParam;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.CoreDataManager;
import jprobe.services.JProbeCore;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import plugins.testDataAndFunction.DataFieldFunction;

public class BindingProfileParams implements ProbesParam, KmerListParam, PWMListParam, PrimerParam{
	public final List<String> KMER_NAMES = new ArrayList<String>();
	public final List<String> PWM_NAMES = new ArrayList<String>();
	
	
	private Probes m_Probes = null;
	private List<Kmer> m_Kmers = new ArrayList<Kmer>();
	private List<PWM> m_PWMs = new ArrayList<PWM>();
	private String m_Primer = null;
	private String m_PrimerName = null;
	private String m_OutputName = null;
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


	public Metadata getMetadata() {
		m_Metadata = new Metadata();
		m_Metadata.put(Metadata.Field.DATA, null);
		m_Metadata.put(Metadata.Field.DATA_TYPE, null);
		m_Metadata.put(Metadata.Field.FUNC, new MetaObject("Binding Profiler"));
		m_Metadata.put(Metadata.Field.PROBE_SET, new MetaObject(m_Probes));
		m_Metadata.put(Metadata.Field.PRIMER, new MetaObject(addName(m_Primer)));
		m_Metadata.put(Metadata.Field.KMER_LIST, new MetaObject(m_Kmers));  
		m_Metadata.put(Metadata.Field.PWM_LIST, new MetaObject(m_PWMs)); 
		return m_Metadata;
	}

	private String addName(String primer) {
		if(m_Primer!=null && m_PrimerName!=null) {
			primer = primer+" ("+m_PrimerName+")";
		}
		return primer;
	}
	public void addKmerName(String varName) {
		KMER_NAMES.add(varName);
	}

	public void addPWMName(String varName) {
		PWM_NAMES.add(varName);
	}

	@Override
	public void setPrimer(String sequence) {
		m_Primer = sequence;
	}

	@Override
	public String getPrimer() {
		return m_Primer;
	}

	@Override
	public void setPrimerName(String p) {
		m_PrimerName = p;
	}
	@Override
	public String getPrimerName() {
		return m_PrimerName;
	}

	


}
