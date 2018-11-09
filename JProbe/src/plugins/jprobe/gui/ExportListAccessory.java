package plugins.jprobe.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import jprobe.services.data.Data;

public class ExportListAccessory extends JComponent{
    private DefaultListModel m_Model;
    private List<Data> m_toExport;
    private JList m_List;
    public ExportListAccessory(List<Data> dataList) {
    	m_toExport = dataList;
        m_Model = new DefaultListModel();
        m_List = new JList(m_Model);
        fillList();
        m_List.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane pane = new JScrollPane(m_List);
        pane.setPreferredSize(new Dimension(200, 250));
        
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(pane);
        
    }
    private void fillList() {
    	for(Data d: m_toExport) {
//    		m_Model.addElement(d.getVarName());
    		m_Model.addElement(new File(d.getVarName()+".txt"));
    	}
    }
    
    public DefaultListModel getModel() {
        return m_Model;
    }
    public int[] getSelectedIndices() {
    	return m_List.getSelectedIndices();
    }
	

}
