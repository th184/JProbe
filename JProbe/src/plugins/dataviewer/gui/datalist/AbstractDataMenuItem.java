package plugins.dataviewer.gui.datalist;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;

import jprobe.services.data.Data;

public abstract class AbstractDataMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private List<Data> m_Data;
	
	protected AbstractDataMenuItem(String title, List<Data> data){
		super(title);
		m_Data = data;
		this.addActionListener(this);
	}
	
	protected AbstractDataMenuItem(String title){
		this(title, null);
	}
	
	public void setData(List<Data> data){
		m_Data = data;
	}
	
	protected List<Data> getData(){
		return m_Data;
	}
	
	
}
