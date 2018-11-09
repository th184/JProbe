package chiptools.jprobe.function.bindingprofilerplotter;

import java.util.List;

import chiptools.jprobe.data.BindingProfile;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.ChiptoolsDataArg;
import chiptools.jprobe.function.args.ProbesArgument;
import chiptools.jprobe.function.params.ProbesParam;
import chiptools.jprobe.function.params.ProfileParam;
import jprobe.services.function.Function;

public class ProfileArgument extends ChiptoolsDataArg<ProfileParam, BindingProfile>{
	public ProfileArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ProfileArgument.class,
				BindingProfile.class,
				optional,
				1,
				1,
				false
			);
	}

	@Override
	protected void process(ProfileParam params, List<BindingProfile> data) {
		params.setProfile(data.get(0));
	}

}
