package plugins.dataviewer.gui.datalist;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import plugins.dataviewer.gui.Constants;
import plugins.dataviewer.gui.DataTabPane;
import plugins.dataviewer.gui.DataUtils;
import plugins.dataviewer.gui.DataviewerActivator;
import plugins.dataviewer.gui.ViewTabPane;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.data.Data;

public class DataListTable extends JTable implements MouseListener, KeyListener{
	private static final long serialVersionUID = 1L;
	
	DataListModel m_Model;
	DataPopupMenu m_PopupMenu;
	DataTabPane m_TabPane; //added 
	DataPopupMenuWithRename m_PopupMenuRename; //added
	private JProbeCore m_Core; //added

	
	public DataListTable(JProbeCore core, JProbeGUI gui,DataTabPane tabPane, DataType type, ViewTabPane viewTabPane){
		super();
		
		m_Model = new DataListModel(core, type, viewTabPane);
		m_PopupMenu = new DataPopupMenu(core, gui, tabPane);
		m_PopupMenuRename = new DataPopupMenuWithRename(core, gui, tabPane, m_Model);
		m_TabPane = tabPane;//added
		m_Core = core;
		
		this.setModel(m_Model);
		this.setDragEnabled(false);
		this.setShowGrid(true);
//		for(int i=0; i<this.getColumnCount(); i++){
//			this.getColumnModel().getColumn(i).setMinWidth(Constants.DATALIST_MIN_COL_WIDTH);
//		}
//		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
//		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		TableColumn col = columnModel.getColumn(1);
//		TableCellRenderer renderer = col.getHeaderRenderer();
//		Component comp = renderer.getTableCellRendererComponent(this, this.getValueAt(0, 1),false, false, 0, 1);
//		col.setPreferredWidth(comp.getPreferredSize().width);
//		columnModel.getColumn(1).setPreferredWidth(10);
		
		this.setPreferredScrollableViewportSize(this.getPreferredSize());
		TableColumn col_0 = this.getColumnModel().getColumn(0);
		col_0.setPreferredWidth(140);
		TableColumn col_1 = this.getColumnModel().getColumn(1);
		col_1.setPreferredWidth(60);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		this.addMouseListener(this);
		this.setAutoCreateRowSorter(true);
		
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setDefaultRenderer(Object.class, new MyRenderer());
		this.addKeyListener(this);
	}
	
	public void cleanup(){
		m_Model.cleanup();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2){
	        int row = this.rowAtPoint(e.getPoint());
	        Data selected = m_Model.getData(convertRow(row));
	        m_TabPane.selectData(selected);
       }
	}
	@Override
	public void mouseReleased(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON3){ //right click
			if(this.getSelectedRowCount()==1) {
				int row = convertRow(this.getSelectedRow());
				int col = this.getSelectedColumn();
				Data selected = m_Model.getData(row);
				m_Model.setSelectedCell(row, col); 
				m_PopupMenuRename.setData(new ArrayList<>(Arrays.asList(selected)));
				m_PopupMenuRename.show(this, event.getX(), event.getY());
			} else {
				int[] rows = convertRows(this.getSelectedRows());
				List<Data> selected = m_Model.getData(rows);
				if(selected != null) {
					m_PopupMenu.setData(selected);
					m_PopupMenu.show(this, event.getX(), event.getY());
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	
	private int[] convertRows(int[] viewRows) {
		int[] rows = new int[viewRows.length];
		for(int i=0; i<viewRows.length; i++) {
			rows[i] = this.convertRowIndexToModel(viewRows[i]);
		}
		return rows;
	}
	
	private int convertRow(int viewRow) {
		return this.convertRowIndexToModel(viewRow);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(this.getSelectedRowCount() != 0 && e.getKeyCode() == KeyEvent.VK_DELETE) {
			int[] viewRows = this.getSelectedRows();
			int[] rows = convertRows(viewRows);
			List<Data> selected = m_Model.getData(rows);
			DataUtils.delete(selected, m_Core, DataviewerActivator.getGUIFrame());
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public class MyRenderer extends DefaultTableCellRenderer {
	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
	        setBorder(noFocusBorder);
	        return this;
	    }
	} 
	
}

