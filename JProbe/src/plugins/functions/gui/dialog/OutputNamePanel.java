package plugins.functions.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jprobe.services.function.Argument;
import jprobe.services.function.OutputNameListener;

public class OutputNamePanel<T> extends JPanel implements OutputNameListener{
	private final Argument<? super T> m_Arg;
	private final JComponent m_ArgComp;
	
	public OutputNamePanel(Argument<? super T> arg) {
		super(new GridBagLayout());
		m_Arg = arg;
		m_ArgComp = arg.getComponent();
		this.add(m_ArgComp, this.argComponentConstraints());
		
	}
	
	protected GridBagConstraints argComponentConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(0,4,0,4);
		return gbc;
	}
	@Override
	public void update(Argument<?> arg, boolean valid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
}