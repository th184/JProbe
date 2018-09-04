package plugins.functions.gui.dialog;

import java.util.List;

import javax.swing.JComponent;

import chiptools.jprobe.function.args.OutputNameArgument;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import jprobe.services.function.ArgumentListener;
import jprobe.services.function.DataArgument;
import jprobe.services.function.components.DataArgsComponent;

public class OutputNameListener implements ArgumentListener{
	OutputNameArgument m_OutputNameArg = null;   
	
	public void setOutputNameArg(OutputNameArgument arg) {
		m_OutputNameArg = arg;
	}
	//arg = probes or peaks
	@Override
	public void update(Argument<?> arg, boolean valid) {
		System.out.println("arg changes");
//		ArgumentPanel outputNamePanel = ArgumentsPanel.getOutputNamePanel();
//		outputNamePanel.argComp
		if(arg instanceof DataArgument) {
			JComponent comp = ((DataArgument) arg).getComponent();
			if(comp instanceof DataArgsComponent) {
				List<Data> dataArgList = ((DataArgsComponent) comp).getDataArgs();
				if(dataArgList.size() != 0) {
					String m_inputName = dataArgList.get(0).getInputName();
					// here
					/*OutputNameArgument.m_inputFilename = m_inputName;
					if(m_OutputNameArg != null) {
						m_OutputNameArg.setInputFilename(m_inputName);
					}*/
				}
			}
			
			
			
		}
	}
	
}
