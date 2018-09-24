package plugins.dataviewer.gui.datalist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import plugins.dataviewer.gui.Constants;
import plugins.dataviewer.gui.DataUtils;
import plugins.dataviewer.gui.DataviewerActivator;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.data.Data;

public class DataListModel extends DefaultTableModel implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	private Map<Data, String> m_Data = new HashMap<Data, String>();
	private DataType m_Type;
	private int[] m_SelectedCell = new int[2];;
	
	public DataListModel(JProbeCore core, DataType type){
		
		super(new String[][]{}, Constants.DATALIST_COL_HEADERS);
		m_Core = core;
		m_Core.addCoreListener(this);
		m_Type = type;
		
		
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				initData();
			}
			
		});
		
	}
	private DataType getType() {
		return this.m_Type;
	}
	
	private void initData(){
		List<Data> data = null;
		switch(m_Type) {
		case INPUT:
			data = m_Core.getDataManager().getInputData();
			break;
		case OUTPUT:
			data = m_Core.getDataManager().getOutputData();
			break;
		}
		for(Data d : data){
			this.add(d);
		}
	}
	
	public Data getData(int row){
		String name = (String) this.getValueAt(row, 0);
		return m_Core.getDataManager().getData(name);
	}
	
	public List<Data> getData(int[] rows){
		List<Data> filled = new ArrayList<>();
		String name;
		for(int i = 0; i < rows.length; i++) {
			name = (String) this.getValueAt(rows[i], 0);
			filled.add(m_Core.getDataManager().getData(name));
		}
		return filled;
	}
	
	public void setSelectedCell(int row, int col) {
		m_SelectedCell[0] = row;
		m_SelectedCell[1] = col;
	}
	public int[] getSelectedCell() {
		return m_SelectedCell;
	}

	public void cleanup(){
		m_Core.removeCoreListener(this);
	}
	
	@Override
	public boolean isCellEditable(int row, int col){
//		return col == 0;
		return false;
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int col){
		if(row < 0 || row >= this.getRowCount() || col < 0 || col >= this.getColumnCount()){
			return;
		}
		try{
			String newName = (String) aValue;
			String oldName = (String) this.getValueAt(row, col);
			if(newName.equals(oldName)){
				return;
			}
			if(m_Core.getDataManager().contains(oldName)){
				//This means that the name change needs to be push to the core
				Data change = m_Core.getDataManager().getData(oldName);
				DataUtils.rename(change, newName, m_Core, DataviewerActivator.getGUIFrame());
				super.setValueAt(newName, row, col); //added
			}else{
				//This means that the name change is received from the core and the field should be updated accordingly
				Data changed = m_Core.getDataManager().getData(newName);
				m_Data.put(changed, newName);
				super.setValueAt(newName, row, col);
			}
		} catch (Exception e){
			ErrorHandler.getInstance().handleException(e, DataviewerActivator.getBundle());
		}
	}
	
	private void clear(){
		for(int i=this.getRowCount() - 1; i>=0; --i){
			this.removeRow(i);
		}
		m_Data.clear();
	}

	private void add(Data data){
		String name = m_Core.getDataManager().getDataName(data);
		m_Data.put(data, name);
		if(getType() == data.getDataType()) {
			this.addRow(new String[]{name, data.getClass().getSimpleName()});
		}
	}
	
	private void remove(Data data){
		String name = m_Data.get(data);
		for(int i=0; i<this.getRowCount(); i++){
			if(this.getValueAt(i, 0).equals(name)){
				this.removeRow(i);
				m_Data.remove(data);
				break;
			}
		}
	}
	
//	private void rename(Data data, String oldName, String newName){
//		System.out.println("In DataListModel rename");
//		if(oldName.equals(newName)){
//			return;
//		}
//		for(int i=0; i<this.getRowCount(); i++){
//			if(this.getValueAt(i, 0).equals(oldName)){
//				this.setValueAt(newName, i, 0);
//				break;
//			}
//		}
//	}
	
	private void process(CoreEvent event){
		switch(event.type()){
		case DATA_ADDED:
			this.add(event.getData());
			break;
		case DATA_REMOVED:
			this.remove(event.getData());
			break;
		// remove this
//		case DATA_NAME_CHANGE:
//			this.rename(event.getData(), event.getOldName(), event.getNewName());
//			break;
	
		case WORKSPACE_CLEARED:
			this.clear();
			break;
		case WORKSPACE_LOADED:
			this.initData();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void update(final CoreEvent event) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				process(event);
			}
			
		});
	}
	
}
