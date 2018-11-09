package chiptools.jprobe.function.bindingprofiler;

import java.util.List;

import util.progress.ProgressListener;
import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.function.ChiptoolsDataArg;
import jprobe.services.function.Function;

public class BindingKmerArgument extends ChiptoolsDataArg<BindingProfileParams, Kmer>{

	public BindingKmerArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				BindingKmerArgument.class,
				Kmer.class,
				optional,
				0,
				Integer.MAX_VALUE,
				false
				);
	}

	@Override
	protected void process(BindingProfileParams params, List<Kmer> data) {
		params.setKmers(data);
		for(int i=0; i<data.size();i++) {
			params.addKmerName(data.get(i).getVarName());
		}
//		params.setKmerListName(data.get(0).getVarName());// change this
		
	}
	
	// this parse function is never called...
	@Override
	public void parse(ProgressListener l, BindingProfileParams params, String[] args){
		params.KMER_NAMES.clear();
		for(String s : args){
			params.KMER_NAMES.add(s);
		}
		super.parse(l, params, args);
	}



}
