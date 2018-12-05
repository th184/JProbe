package chiptools.jprobe.function.proberemover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import chiptools.jprobe.data.BindingProfile;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.EscoreArgument;
import chiptools.jprobe.function.args.ProbesArgument;
import chiptools.jprobe.function.args.ProfileArgument;
import jprobe.services.data.Data;
import jprobe.services.data.Metadata;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.function.Argument;
import util.genome.Sequences.Profile;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import util.progress.ProgressListener;

public class ProbeRemover extends AbstractChiptoolsFunction<ProbeRemoverParams>{

	public ProbeRemover() {
		super(ProbeRemoverParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeRemoverParams>> getArguments() {
		Collection<Argument<? super ProbeRemoverParams>> args = new ArrayList<Argument<? super ProbeRemoverParams>>();
		args.add(new ProbesArgument(this, false));
		args.add(new ProfileArgument(this, false));
		args.add(new EscoreArgument(this, true, 0.35));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, ProbeRemoverParams params) throws Exception {
		int center = 15; // middle of 29 --> change later to get from binding profile
		double cutoff = params.getEscore();
		BindingProfile bp = params.getProfile();
		
		
		ArrayList<double[]> entries = getScores(bp); // assume only ONE METRIC used
		
		int num_pos = entries.get(0).length;
		double[] max = new double[num_pos]; Arrays.fill(max, -1000);
		for(int profile=0;profile<entries.size();profile++) { // a profile = a row
			for(int pos=0;pos<num_pos;pos++) { 				  // a pos = a col
				double score = entries.get(profile)[pos];
				if(score>max[pos]) max[pos] = score;
			}
		}
		System.out.println("max: "+Arrays.toString(max));

		List<Integer> pos_above_max = new ArrayList<>();
		for(int i=0;i<max.length;i++) {
			if(max[i]>cutoff) { 
				pos_above_max.add(i);
			}
		}
		System.out.println("pos_above_max: "+Arrays.toString(pos_above_max.toArray()));
		// each list contained within is a continuous positions of a peak 
		List<ArrayList<Integer>> peaks_rm = new ArrayList<ArrayList<Integer>>();  // peaks to be removed
		ArrayList<Integer> peak = new ArrayList<>();
		for(int p=1;p<pos_above_max.size();p++) {
			
			peak.add(pos_above_max.get(p-1));
			if((pos_above_max.get(p)-1 != pos_above_max.get(p-1))) {
				if(!peak.contains(center)) {
					peaks_rm.add(peak);
//					System.out.println("peak: "+Arrays.toString(peak.toArray()));
					peak = new ArrayList<>();
				}else {
					peak = new ArrayList<>();
				}
			}
			// last item in pos_above_max and it's continuous to the previous item
			if(p==pos_above_max.size()-1) {
				peak.add(pos_above_max.get(p));
				if(!peak.contains(center)) {
					peaks_rm.add(peak);
				}
			}
		}
		for(ArrayList<Integer> peak_rm: peaks_rm) {
			System.out.println("peak_rm: "+Arrays.toString(peak_rm.toArray()));  
		}  
		
		Set<Integer> profile_to_rm = new TreeSet<>();
		
		for(ArrayList<Integer> pos_to_rm: peaks_rm) {
			for(int p=0; p<entries.size();p++) {
				for(int i=0;i<pos_to_rm.size();i++) {
					int pos = (int) pos_to_rm.get(i);
					if(entries.get(p)[pos]>cutoff) {
						profile_to_rm.add(p);
					}
				}
			}
		}
	
//		System.out.println("profile_to_rm: "+Arrays.toString(profile_to_rm.toArray())+" len = "+profile_to_rm.size());
		ProbeGroup probes = params.getProbes().getProbeGroup();
		ProbeGroup remained = ProbeUtils.remove(probes, profile_to_rm); // check
		String outputName = params.getOutputName();
		params.createMetadata();
		params.addMetadata(Metadata.Field.NUM_PROBE_REMOVED, (Integer)profile_to_rm.size());
		return new Probes(remained, DataType.OUTPUT, outputName, params.getMetadata());
		
	}

	private ArrayList<double[]> getScores(BindingProfile bp) {
		ArrayList<double[]> scores = new ArrayList<>();
		List<Profile> profiles = bp.getProfile();
	
		for(int i=0; i<profiles.size(); i++) {
			Profile p = profiles.get(i);
			if(p.getMetric(0)=="Kmer") { // used index 0 --> assume only 1 entry
				scores.add(p.getEntry(0));
			}
		}
		return scores;
	}
	

}
