package plugins.dataviewer.gui.datalist;

import java.awt.event.ActionEvent;
import java.util.List;

import plugins.dataviewer.gui.DataUtils;
import plugins.dataviewer.gui.DataviewerActivator;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DeleteDataMenuItem extends AbstractDataMenuItem{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	
	public DeleteDataMenuItem(JProbeCore core, List<Data> data){
		super("Delete", data);
		m_Core = core;
	}
	
	public DeleteDataMenuItem(JProbeCore core){
		this(core, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.getDataList()!=null){
			DataUtils.delete(this.getDataList(), m_Core, DataviewerActivator.getGUIFrame());
		}
	}

}
