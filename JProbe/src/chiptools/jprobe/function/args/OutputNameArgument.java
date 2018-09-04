package chiptools.jprobe.function.args;

import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import chiptools.jprobe.function.ChiptoolsTextArg;
import chiptools.jprobe.function.params.OutputNameParam;
import jprobe.services.function.Function;
import util.Subject;

public class OutputNameArgument extends ChiptoolsTextArg<OutputNameParam> {
	public static String m_inputFilename = null;
	
	public final static Map<String, String> m_Standard = new HashMap<String, String>()
	{{	// prefix_n 
		put("ProbeJoiner", "JoinedProbes");
		put("ProbeGenerator", "GenProbes");
		put("NegativeControlGenerator", "NegCtrl");
		// prefix_fileName   
		put("PeakFinder", "PeakSeqs");
		put("BindingProfiler", "BindingProfile");
		// fileName_suffix 
		put("ProbeMutator", "mut");
		put("GCRunMutator", "GRun_mut");  
		put("PeakFilter", "filtered");
		put("ProbeFilter", "filtered");	
	}};
	
	public final static Map<String, Integer> m_Counts = new HashMap<String, Integer>()
	{{	
		put("ProbeJoiner", 1);
		put("ProbeGenerator", 1);
		put("NegativeControlGenerator", 1);
	}};
	
	public final static Set<String> m_Prefix = new HashSet<String>()
	{{
		add("PeakFinder");
		add("BindingProfiler");
		
	}};
	
	
	public static void updateCount(Function<?> f) {
		String func = f.getClass().getSimpleName();
		m_Counts.put(func, m_Counts.get(func)-1);
		
	}
	
	public void setInputFilename(String fileName) {
		m_inputFilename = fileName;
	}
	
// ADD TO FILE FIELD
	
//	@Override
//	public JComponent getComponent(){
//		JTextField comp = (JTextField) super.getComponent();
//		comp.getDocument().addDocumentListener(new DocumentListener() {
//			  public void changedUpdate(DocumentEvent e) {
//				    setOutputName();
//				  }
//				  public void removeUpdate(DocumentEvent e) {
//				  }
//				  public void insertUpdate(DocumentEvent e) {
//				    setOutputName();
//				  }
//			});
//		return comp;
//	}
	private static String generatePrototype(Function<?> f) {
		String name = null;
		String func = f.getClass().getSimpleName();
		
		if(func.equals("AgilentFormatter")) {
			// fill in later
			
		}else if(m_Counts.containsKey(func)) {
			name = m_Standard.get(func) +"_"+ m_Counts.get(func);
			m_Counts.put(func, m_Counts.get(func)+1);
			
		}else if(m_Prefix.contains(func)){
			// get filename
			name = m_Standard.get(func) +"_"+ m_inputFilename;
//			name = "";
			
		}else {
			// get filename
			name = m_inputFilename +"_"+ m_Standard.get(func);
//			name = "";
		}
		
		return name;
		     
	}
	
	
	public OutputNameArgument(Function<?> parent, boolean optional) {
		super( 
				parent.getClass(),
				OutputNameArgument.class,
				generatePrototype(parent),
				optional,
				generatePrototype(parent)
				);
	}
	
	
	@Override
	protected boolean isValid(String s) {
		return s.length() <= 15;
	}

	@Override
	protected void process(OutputNameParam params, String s) {
		params.setOutputName(s);
	}

	

	
}

