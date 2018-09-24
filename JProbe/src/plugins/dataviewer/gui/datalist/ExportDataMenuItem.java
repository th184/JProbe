package plugins.dataviewer.gui.datalist;

import java.awt.event.ActionEvent;
import java.util.List;

import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;


public class ExportDataMenuItem extends AbstractDataMenuItem{
	private static final long serialVersionUID = 1L;
	
	private JProbeGUI m_Gui;
	private JProbeCore m_Core;
	
	public ExportDataMenuItem(JProbeCore core, JProbeGUI gui) {
		this(core, gui, null);
	}
	
	public ExportDataMenuItem(JProbeCore core, JProbeGUI gui, List<Data> data){
		super("Export", data);
		m_Gui = gui;
		m_Core = core;
		
//		if(data == null){
//			System.out.println("data is null");
//			this.setVisible(false);
//		}else{
//			this.setVisible(m_Core.getDataManager().isWritable(data.getClass()));
//			this.setVisible(true);
//		}
	}
	
	public void setData(List<Data> data){
		super.setDataList(data);
		if(data == null){
			this.setVisible(false);
		}else{
			this.setVisible(true);
			//this.setVisible(m_Core.getDataManager().isWritable(data.getClass()));
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.getDataList() != null){
			m_Gui.write(this.getDataList());
		}
	}
	
	
	
}
