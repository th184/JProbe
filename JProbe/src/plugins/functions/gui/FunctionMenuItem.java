package plugins.functions.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.osgi.framework.Bundle;

import plugins.functions.gui.dialog.FunctionDialogHandler;
import plugins.functions.gui.dialog.FunctionPanel;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;
import jprobe.services.function.Function;


public class FunctionMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Bundle m_Bundle;
	private FunctionDialogHandler m_FunctionDialog;
	private FunctionPanel m_FunctionPanel;
	
	public FunctionMenuItem(JProbeCore core, Bundle bundle, Function<?> function, FunctionDialogHandler dialogWindow){
		super(function.getName());
		m_Bundle = bundle;
		m_FunctionDialog = dialogWindow;
		m_FunctionPanel = new FunctionPanel(function, core, bundle);		
				
		this.setEnabled(true);
		this.setVisible(true);
		this.setToolTipText(function.getDescription());
		this.addActionListener(this);
	}
	
	@Override
    public JToolTip createToolTip(){
        JMultiLineToolTip multiTool = new JMultiLineToolTip();
		multiTool.setFixedWidth(300);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		return multiTool; 
    }
	
	private void doFunction(){
		//code for executing function here
		if(Debug.getLevel() == Debug.FULL){
			Log.getInstance().write(m_Bundle, this.getText()+" clicked");
		}
		try {
			m_FunctionDialog.display(m_FunctionPanel);
			
			//FunctionExecutor ex = new SwingFunctionExecutor(function, new FunctionParam(null, null, null, null), dataManager, bundle);
			//ex.execute();
			
			//Data d = function.run(new FunctionParam(null, null, null, null));
			//dataManager.addData(d, bundle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.doFunction();
	}
	
}
