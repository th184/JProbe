package chiptools.jprobe.function.bindingprofilerplotter;

import java.util.ArrayList;
import java.util.Collection;

import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.ProbesArgument;
import chiptools.jprobe.function.bindingprofiler.BindingProfileParams;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.progress.ProgressListener;

public class BindingProfilePlotter extends AbstractChiptoolsFunction<BindingProfilePlotterParams>{

	public BindingProfilePlotter() {
		super(BindingProfilePlotterParams.class);
	}

	@Override
	public Collection<Argument<? super BindingProfilePlotterParams>> getArguments() {
		Collection<Argument<? super BindingProfilePlotterParams>> args = new ArrayList<Argument<? super BindingProfilePlotterParams>>();
		args.add(new ProfileArgument(this, false));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, BindingProfilePlotterParams params) throws Exception {
		
		return null;
	}

}
