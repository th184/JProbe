package chiptools.jprobe.function.negativecontrolgen;

import chiptools.jprobe.function.ChiptoolsIntArg;
import jprobe.services.function.Function;

public class RepeatArg extends ChiptoolsIntArg<NegControlParams> {

		public RepeatArg(Function<?> parent, boolean optional) {
			super(
					parent.getClass(),
					RepeatArg.class,
					optional,
					5,
					0,
					Integer.MAX_VALUE,
					1
					);
		}

		@Override
		protected void process(NegControlParams params, Integer value) {
			params.setRepeat(value);
		}

	
}
