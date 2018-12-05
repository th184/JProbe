package chiptools.jprobe.function.unionprobesets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import jprobe.services.data.Data;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.function.Argument;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;

public class UnionProbeSets extends AbstractChiptoolsFunction<UnionProbeSetsParams>{
	public UnionProbeSets() {
		super(UnionProbeSetsParams.class);
	}

	@Override
	public Collection<Argument<? super UnionProbeSetsParams>> getArguments() {
		Collection<Argument<? super UnionProbeSetsParams>> args = new ArrayList<Argument<? super UnionProbeSetsParams>>();
		args.add(new ProbeSetsArgument(this, false));
		return args;
	}
	protected int fireProgressEvent(ProgressListener l, int progress, int maxProgress, int prevPercent){
		int percent = progress*100/maxProgress;
		if(percent != prevPercent){
			l.update(new ProgressEvent(this, Type.UPDATE, progress, maxProgress, "Merging probe sets..."));
		}
		return percent;
	}

	@Override
	public Data execute(ProgressListener l, UnionProbeSetsParams params) throws Exception {
		int count = 0;
		int total = 0;
		for(Probes p: params.getProbeSet()) {
			total+=p.getProbeGroup().size();
		}
		
		int prevPercent = this.fireProgressEvent(l, count, total, -1);
		
		Set<Probe> probes = new HashSet<>();
		for(Probes p: params.getProbeSet()) {
			for(int i=0; i<p.getProbeGroup().size();i++) {
				probes.add(p.getProbeGroup().getProbe(i));
				prevPercent = this.fireProgressEvent(l, ++count, total, prevPercent);
			}
			
		}
		ProbeGroup group = new ProbeGroup(probes);

		l.update(new ProgressEvent(this, Type.COMPLETED, "Done merging probe sets."));
		
		StringJoiner output_name = new StringJoiner("_");
		for(Probes p: params.getProbeSet()) {
			output_name.add(p.getVarName());
		}
		return new Probes(group, DataType.OUTPUT, output_name.toString(), params.getMetadata());
	}

}