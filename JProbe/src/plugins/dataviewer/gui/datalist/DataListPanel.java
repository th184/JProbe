package plugins.dataviewer.gui.datalist;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import plugins.dataviewer.gui.DataTabPane;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;
import jprobe.services.data.AbstractFinalData.DataType;

public class DataListPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private DataListTable m_Table;
	
	public DataListPanel(JProbeCore core, JProbeGUI gui, DataTabPane tabPane, DataType type){
		super(new BorderLayout());
		m_Table = new DataListTable(core, gui, tabPane, type) { 
			//Implement table cell tool tips.           
	        public String getToolTipText(MouseEvent e) {
	                String tip = null;
	                java.awt.Point p = e.getPoint();
	                int rowIndex = rowAtPoint(p);
	                int colIndex = columnAtPoint(p);
	                try {
	                    tip = getValueAt(rowIndex, colIndex).toString();
	                } catch (RuntimeException e1) {
	                    //catch null pointer exception if mouse is over an empty line
	                }
	                return tip;
	            }
	        };
		
		this.add(m_Table.getTableHeader(), BorderLayout.NORTH);
		this.add(m_Table, BorderLayout.CENTER);
		
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener(){
	        @Override
	        public void eventDispatched(AWTEvent event) {
	            if(event.getID() == MouseEvent.MOUSE_CLICKED) {
	                MouseEvent m_event = (MouseEvent) event;
	                 int row = m_Table.rowAtPoint(m_event.getPoint());
	                 if(row == -1) {
	                     m_Table.clearSelection();
	                 }
	            }               
	        }           
	    }, AWTEvent.MOUSE_EVENT_MASK);
		
	}
	
	public void cleanup(){
		m_Table.cleanup();
	}
	
	public GridBagConstraints getGridBagConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridy = 0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.gridwidth = 1;
		gbc.weighty = 0.7;
		gbc.weightx = 0;
		return gbc;
	}
	
}
