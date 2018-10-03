package jprobe.services.data;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import jprobe.Constants;
import jprobe.JProbeActivator;
import jprobe.services.ErrorHandler;
import plugins.dataviewer.gui.DataViewerSplitPane;
import util.gui.TableFormatter;


public abstract class AbstractFinalData implements Data{
	private static final long serialVersionUID = 1L;
	
	private transient Collection<TableModelListener> m_Listeners = new HashSet<TableModelListener>();
	
	private final int m_Cols;
	private final int m_Rows;
	public enum DataType {INPUT, OUTPUT, EXPORT}; // EXPORT = export .txt file directly
	private DataType m_Type;
	private String m_InputName = null; // file/var name for INPUT data
	private String m_OutputName = null; // file/var name for OUTPUT data
//	private Map<String, String> m_Metadata;
	// ADDED
	private Metadata m_Metadata;
	
	private String m_VarName = null;
	
	protected AbstractFinalData(int cols, int rows, DataType type, String outputName, Metadata metadata){
		m_Cols = cols;
		m_Rows = rows;
		m_Type = type;
		m_OutputName = outputName;
		m_Metadata = metadata;
	}
	
	//readObject method to init the transient listeners collection after deserialization
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException{
		in.defaultReadObject();
		m_Listeners = new HashSet<TableModelListener>();
	}

	@Override
	public int getColumnCount() {
		return m_Cols;
	}

	@Override
	public int getRowCount() {
		return m_Rows;
	}

	public void setDataType(DataType type) {
		m_Type = type;
	}

	public DataType getDataType() {
		return m_Type;
	}
	
	public void setInputName(String name) {
		m_InputName = name;
	}
	
	public String getInputName() {
		return m_InputName;
	}
	
	public void setOutputName(String name) {
		m_OutputName = name;
	}
	
	public String getOutputName() {
		return m_OutputName;
	}
	
	public void setVarName(String name) {
		m_VarName = name;
	}
	
	public String getVarName() {
		return m_VarName;
	}

	@Override
	public void setImportMetadata(String dataType) {
		if(m_Type==DataType.INPUT && m_Metadata==null) {
			Metadata inputMD= new Metadata();
			inputMD.put("Data", m_VarName);
			inputMD.put("Type", dataType+" (imported)");
			
//			Map<String, String> inputMetadata = new LinkedHashMap<>() {{
//				put("Data", m_VarName);
//				put("Type", dataType+" (imported)"); 
//			}};
			m_Metadata = inputMD;
		}
	}

	public Metadata getMetadata(){
		return m_Metadata;
	}
	
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		//do nothing
	}

	@Override
	public JTable createTable() {
		JTable table = new JTable(this);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		try{
			((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		}catch(Exception e){
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
		table.setFont(new Font(Font.MONOSPACED, table.getFont().getStyle(), table.getFont().getSize()));
		TableFormatter.formatTable(table, Constants.DATA_TABLE_MAX_COL_WIDTH, Constants.DATA_TABLE_ROW_SAMPLE);
		table.setShowGrid(true);
		return table;
	}
	
	
	protected void notifyListeners(TableModelEvent e){
		for(TableModelListener l : m_Listeners){
			l.tableChanged(e);
		}
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		m_Listeners.add(arg0);
	}
	

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		m_Listeners.remove(arg0);
	}

}
