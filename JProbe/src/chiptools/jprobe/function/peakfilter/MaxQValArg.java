package chiptools.jprobe.function.peakfilter;

import jprobe.services.function.Function;
import util.genome.peak.Peak;
import util.genome.peak.PeakUtils.Filter;
import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MaxQValArg extends ChiptoolsDoubleArg<PeakFilterParams>{

	public MaxQValArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), MaxQValArg.class, "off", optional, 0, 0, Double.POSITIVE_INFINITY, 0.1);
	}

	@Override
	protected void process(PeakFilterParams params, Double value) {
		params.MAXQVAL = value;
		final double max = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Peak p) {
				return p.getQVal() <= max;
			}
			
		});
	}

}
