package plugins.dataviewer.gui.datalist;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import plugins.dataviewer.gui.Constants;
import plugins.dataviewer.gui.DataTabPane;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.data.Data;

public class DataListTable extends JTable implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	DataListModel m_Model;
	DataPopupMenu m_PopupMenu;

	
	public DataListTable(JProbeCore core, JProbeGUI gui, DataTabPane tabPane, DataType type){
		super();
		m_PopupMenu = new DataPopupMenu(core, gui, tabPane);
		m_Model = new DataListModel(core, type);
		
		this.setModel(m_Model);
		this.setDragEnabled(false);
		this.setShowGrid(true);
//		for(int i=0; i<this.getColumnCount(); i++){
//			this.getColumnModel().getColumn(i).setMinWidth(Constants.DATALIST_MIN_COL_WIDTH);
//		}
//		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
//		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		TableColumn col = columnModel.getColumn(1);
//		TableCellRenderer renderer = col.getHeaderRenderer();
//		Component comp = renderer.getTableCellRendererComponent(this, this.getValueAt(0, 1),false, false, 0, 1);
//		col.setPreferredWidth(comp.getPreferredSize().width);
//		columnModel.getColumn(1).setPreferredWidth(10);
		
		this.setPreferredScrollableViewportSize(this.getPreferredSize());
		TableColumn col_0 = this.getColumnModel().getColumn(0);
		col_0.setPreferredWidth(120);
		TableColumn col_1 = this.getColumnModel().getColumn(1);
		col_1.setPreferredWidth(80);
		
		this.addMouseListener(this);
		this.setAutoCreateRowSorter(true);
		
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
	}
	
	public void cleanup(){
		m_Model.cleanup();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//do nothing
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON3){
			
			int[] rows = this.getSelectedRows();
			List<Data> selected = m_Model.getData(rows);
			if(selected != null) {
				m_PopupMenu.setData(selected);
				m_PopupMenu.show(this, event.getX(), event.getY());
			}
		}
	}

}

