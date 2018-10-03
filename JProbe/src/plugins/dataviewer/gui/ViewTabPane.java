package plugins.dataviewer.gui;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jprobe.services.JProbeCore;
import jprobe.services.data.AbstractFinalData.DataType;
import plugins.dataviewer.gui.datalist.DataListPanel;
import plugins.jprobe.gui.services.JProbeGUI;

public class ViewTabPane extends JTabbedPane{
	private static final long serialVersionUID = 1L;
	private DataListPanel m_inputList;
	private DataListPanel m_outputList;
	
	public ViewTabPane() {}
	
    public ViewTabPane(JProbeCore core, JProbeGUI gui, DataTabPane tabPane){
    	super();
    	
        JPanel panelOne = new JPanel();
        JPanel panelTwo = new JPanel();
        this.add(panelOne,"Imported data");
        this.add(panelTwo,"Generated data");
        
        m_inputList = new DataListPanel(core, gui, tabPane, DataType.INPUT, this);
        JScrollPane scroll_in = new JScrollPane(m_inputList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelOne.add(scroll_in);
        
        m_outputList = new DataListPanel(core, gui, tabPane, DataType.OUTPUT, this);
        JScrollPane scroll_out = new JScrollPane(m_outputList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelTwo.add(scroll_out);
        
        this.setVisible(true);
    }
    
    public void displayList(DataType type) {
    	switch(type) {
    	case INPUT: 
    		this.setSelectedIndex(0); break;
    	case OUTPUT:
    		this.setSelectedIndex(1); break;
    	}
    		
    }
    public void cleanup(){
		m_inputList.cleanup();
		m_outputList.cleanup();
	}
}