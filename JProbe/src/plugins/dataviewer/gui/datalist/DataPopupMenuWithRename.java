package plugins.dataviewer.gui.datalist;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import plugins.dataviewer.gui.DataTabPane;
import plugins.jprobe.gui.services.JProbeGUI;

public class DataPopupMenuWithRename extends JPopupMenu{
	private static final long serialVersionUID = 1L;

	private List<AbstractDataMenuItem> m_MenuItems;

	public DataPopupMenuWithRename(JProbeCore core, JProbeGUI gui, DataTabPane tabPane, DataListModel model, List<Data> data){
		super();
		m_MenuItems = new ArrayList<AbstractDataMenuItem>();
		this.initMenuItems(core, gui, tabPane, data, m_MenuItems, model);
		for(AbstractDataMenuItem item : m_MenuItems){
			this.add(item);
		}
	}
	
	public DataPopupMenuWithRename(JProbeCore core, JProbeGUI gui, DataTabPane tabPane, DataListModel model){
		this(core, gui, tabPane, model, null);
	}
	
	
	private void initMenuItems(
			final JProbeCore core, final JProbeGUI gui, 
			final DataTabPane tabPane, final List<Data> data, 
			List<AbstractDataMenuItem> items, DataListModel model){
				items.add(new DeleteDataMenuItem(core, data));
				items.add(new ExportDataMenuItem(core, gui, data));
				items.add(new RenameDataMenuItem(model));
				items.add(new ViewDataMenuItem(tabPane, data));
	}
	
	public void setData(List<Data> data){
		for(AbstractDataMenuItem item : m_MenuItems){
			item.setDataList(data);
		}
	}
}
