package chiptools.jprobe.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import util.genome.Sequences.Profile;
import jprobe.services.data.AbstractFinalData;
import jprobe.services.data.Metadata;

public class BindingProfile extends AbstractFinalData implements Iterable<Profile>{
	private static final long serialVersionUID = 1L;
	
	private static int getLongestProfileLength(List<Profile> profiles){
		int len = 0;
		for(Profile p : profiles){
			for(int i=0; i<p.numEntries(); i++){
					double[] entry = p.getEntry(i);
					if(entry.length > len){
						len = entry.length;
					}
			}
		}
		return len;
	}
	
	private static int countRows(List<Profile> profiles){
		int count = 0;
		for(Profile p : profiles){
			count += p.numEntries();
		}
		
		return count;
	}
	
	private static final int NAME = 0;
	private static final int WORD_LEN = 1;
	
	private final List<Profile> m_Profiles;
	private final Map<Integer, Profile> m_ProfileStarts = new HashMap<Integer, Profile>();

	public BindingProfile(List<Profile> bindingProfiles, DataType type, String outputName, Metadata metadata) {
		super(getLongestProfileLength(bindingProfiles) + 1, countRows(bindingProfiles), type, outputName, metadata);
		m_Profiles = bindingProfiles; // size of m_Profiles = num rows generated
		int start = 0;
		for(Profile p : m_Profiles){ // one profile = one row 
			m_ProfileStarts.put(start, p);
			start += p.numEntries();
		}
		computeAverage(); 
	}
	// len of list = number of metrics used
	// for each metric, compute avg of score for all positions across all profiles
	public List<double[]> computeAverage() {
		Profile profile_1 = m_Profiles.get(0);
		int num_metrics = profile_1.size();
		List<double[]> ret_avg = new ArrayList<>();
		int num_profiles = m_Profiles.size();
		for(int metric =0; metric<num_metrics; metric++) {
			int num_pos = profile_1.getEntry(metric).length;
			double[] avg = new double[num_pos];
			for(int profile = 0; profile<num_profiles;profile++) {
				double[] entries = m_Profiles.get(profile).getEntry(metric);
				for(int pos=0; pos<num_pos;pos++) {
					avg[pos]+=entries[pos];
					if(profile==num_profiles-1) {
						avg[pos] = avg[pos]/num_profiles;
					}
				}
			}
			ret_avg.add(avg);
		}
		return ret_avg;
	}
	
/* Compute avg and plot for each scoring metric
 * Profile profile_1 = m_Profiles.get(0)
 * profile_1.size() = number of scoring metrics used
 * 
 * profile_1.getEntry().length = number of positions
 */
	
	@Override
	public String toString(){
		String s = "";
		for(Profile p : m_Profiles){
			s += p.toString();
		}
		return s;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch(col){
		case NAME: return String.class;
		case WORD_LEN: return Integer.class;
		default: return Double.class;
		}
	}

	@Override
	public String getColumnName(int col) {
		switch(col){
		case NAME: return "Name";
		case WORD_LEN: return "Word length";
		default: return String.valueOf(col);
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		int profile = row;
		while(!m_ProfileStarts.containsKey(profile) && profile >= 0){
			--profile;
		}
		Profile p = m_ProfileStarts.get(profile);
		if(p == null) return null;
		int e = row - profile;
		switch(col){
		case NAME: return p.getEntryName(e);
		case WORD_LEN: return p.getWordLen(e);
		default:
			double[] entries = p.getEntry(e);
			int index = col - 1; // subtract 2?
			if(index >= entries.length){
				return null;
			}else{
				return entries[index];
			}
		}
	}

	@Override
	public Iterator<Profile> iterator() {
		return m_Profiles.iterator();
	}

	@Override
	public void dispose() {
		//do nothing
	}

}
