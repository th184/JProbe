package chiptools.jprobe.function.agilentformatter;

import chiptools.jprobe.function.ChiptoolsTextArg;
import jprobe.services.function.Function;

public class ProbeStartArgument extends ChiptoolsTextArg<AgilentFormatterParams>{
	public ProbeStartArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ProbeStartArgument.class,
				"Probe",
				optional,
				"Probe"
				);
	}

	@Override
	protected boolean isValid(String s) {
		return s.length() <= 9;
	}

	@Override
	protected void process(AgilentFormatterParams params, String s) {
		params.PROBE_START = s;
	}
}
