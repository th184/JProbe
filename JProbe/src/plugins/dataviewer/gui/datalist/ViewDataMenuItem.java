package plugins.dataviewer.gui.datalist;

import java.awt.event.ActionEvent;
import java.util.List;

import jprobe.services.data.Data;
import plugins.dataviewer.gui.DataTabPane;

public class ViewDataMenuItem extends AbstractDataMenuItem{
	private static final long serialVersionUID = 1L;

	private DataTabPane m_TabPane;
	
	public ViewDataMenuItem(DataTabPane tabPane, List<Data> data){
		super("View",  data);
		m_TabPane = tabPane;
	}
	
	public ViewDataMenuItem(DataTabPane tabPane){
		this(tabPane, null);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		List<Data> data = this.getData();
		if(data != null){
			for(int i=0; i<data.size();i++) {
				m_TabPane.selectData(data.get(i));
			}
		}
	}
	
}
