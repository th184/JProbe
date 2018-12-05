package chiptools.jprobe.function.unionprobesets;

import java.util.List;
import java.util.StringJoiner;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.ChiptoolsDataArg;
import jprobe.services.function.Function;

public class ProbeSetsArgument extends ChiptoolsDataArg<UnionProbeSetsParams, Probes>{
	public ProbeSetsArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ProbeSetsArgument.class,
				Probes.class,
				optional, 
				0,
				Integer.MAX_VALUE,
				false
				);
	}
	@Override
	protected void process(UnionProbeSetsParams params, List<Probes> data) {
		params.setProbeSet(data);
	}

}



	
	
