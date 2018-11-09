package chiptools.jprobe.function.bindingprofilerplotter;

import chiptools.jprobe.data.BindingProfile;
import chiptools.jprobe.function.params.ProfileParam;

public class BindingProfilePlotterParams implements ProfileParam {
	private BindingProfile m_Profile = null;

	@Override
	public void setProfile(BindingProfile p) {
		m_Profile = p;
	}

	@Override
	public BindingProfile getProfile() {
		return m_Profile;
	}

}
