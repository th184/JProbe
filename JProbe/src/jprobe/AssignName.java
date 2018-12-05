package jprobe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jprobe.services.data.Data;

public class AssignName {
private final int OCCUPANCY_ARRAY_SIZE = 5; // change this later!!!
	
	public Map<String, boolean[]> m_Index = new HashMap<>()
	{{	// boolean array = "occupied?"; initialize to all false
		put("JoinedProbes", new boolean[OCCUPANCY_ARRAY_SIZE]);
		put("GenProbes", new boolean[OCCUPANCY_ARRAY_SIZE]);
		put("NegCtrl", new boolean[OCCUPANCY_ARRAY_SIZE]);
	}};	

	
	private Map<String, Boolean> m_Standard = new HashMap<>()
	{{ //boolean: indicate whether in group_num
		put("JoinedProbes",true); put("GenProbes", true); put("NegCtrl",true);
		put("PeakSeqs", false); put("BindingProfile", false); 
		put("mut", false); put("GRunMut", false); put("filtered", false);
	}};
	
	public static Map<String, String> m_Standard_Num = new HashMap<String, String>()
	{{	// prefix_n 
		put("ProbeJoiner", "JoinedProbes");
		put("ProbeGenerator", "GenProbes");
		put("NegativeControlGenerator", "NegCtrl");
	}};
	
	public static Map<String, String> m_Standard_Prefix = new HashMap<String, String>()
	{{
		// prefix_fileName   
		put("PeakFinder", "PeakSeqs");
		put("BindingProfiler", "BindingProfile");
	}};
	
	public static Map<String, String> m_Standard_Suffix = new HashMap<String, String>()
	{{
		// fileName_suffix 
		put("ProbeMutator", "mut");
		put("GCRunMutator", "GRunMut");  
		put("PeakFilter", "filtered");
		put("ProbeFilter", "filtered");	
	}};
	
	/*
	 * return the next available index from m_Index
	 */
	private int nextIndex(String name, boolean group_num) { 
		boolean[] occupied = m_Index.get(name);
		System.out.println(name+": "+ Arrays.toString(occupied));
		for(int i = 0; i<occupied.length; i++) {
			if(!occupied[i]) {
				occupied[i] = true;
				System.out.println(name+": "+ Arrays.toString(occupied));
//				if(group_num) return i+1;
//				return i; 
				return i+1;
			}
		}
		return -1; // array all full - need to handle this later (get a bigger array)
	}
	private void updateArray(String name, int num, boolean group_num) {
//		System.out.println("NAME: "+name);
//		System.out.println("group_num: "+group_num);
		int i;
		if(group_num) {
			i = num-1;
		}else {
			i = num;
		}
		if(!m_Index.containsKey(name)) {
//			System.out.println("adding name to m_Index");
			m_Index.put(name, new boolean[OCCUPANCY_ARRAY_SIZE]);
		}
//		System.out.println("updating index: "+i);
		boolean[] array = m_Index.get(name);
		array[i] = !array[i];
//		System.out.println(Arrays.toString(array));
	}
	
	private String assignName(Data d, String func) {
		String name = null;
		if(m_Standard_Num.containsKey(func)) {
			String standardName = m_Standard_Num.get(func);
			name = standardName +"_"+ nextIndex(standardName, true);
			System.out.println("next index: "+ nextIndex(standardName, true));
//			name = standardName +"_"+ m_Counts.get(standardName);
//			m_Counts.put(standardName, m_Counts.get(standardName)+1); 
		}
		return name;
	}
	private String assignName(Data d, String func, String name) {
		if(m_Standard_Prefix.containsKey(func)) {
			name = m_Standard_Prefix.get(func) +"_"+ name;
		}else {
			name = name +"_"+ m_Standard_Suffix.get(func);
		}
		
		if(!m_Index.containsKey(name)) {
			
			m_Index.put(name, new boolean[OCCUPANCY_ARRAY_SIZE]);
			System.out.println("putting "+name+" in m_Index");
		}else {
			System.out.println("m_Index contains name: "+name);
			int nextInd = nextIndex(name, false);
			System.out.println("next index: "+ nextInd);
//			if(nextInd==0) {
//				name = name;
//			}else {
				name = name +"_"+ nextInd;
//			}
		}
		
		return name;
	}
	private boolean getTag(String pre, String suff) {
		
		if(m_Standard.get(suff) != null) {
			return m_Standard.get(suff);
		}
		return m_Standard.get(pre);
	}
}
