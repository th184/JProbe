package plugins.dataviewer.gui;

import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import plugins.dataviewer.gui.services.DataViewer;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.data.Data;
import jprobe.services.data.Metadata;

//import org.fife.ui.TabbedPaneTransferHandler;

public class DataTabPane extends JTabbedPane implements CoreListener, DataViewer, KeyListener, MouseListener{
	private static final long serialVersionUID = 1L;
	
	private DataManager m_DataManager;
	private Map<Data, DataTab> m_DataToTabs;
	private Map<Data, DataTabLabel> m_TabLables;
	private GridBagConstraints m_Constraints;
	
	private Map<Integer, Data> m_IndexToData = new HashMap<>();
	private Map<Integer, DataTab> m_IndexToTabs = new HashMap<>();
	private Map<Data, Integer> m_DataToIndex = new HashMap<>();
	private Map<DataTab, Integer> m_TabsToIndex = new HashMap<>();
	private Data m_dataDisplayed;
	public static boolean last_action_closed_tab = false;
	
	public DataTabPane(DataManager dataManager){
		super();
		
		m_DataManager = dataManager;
		m_DataManager.addListener(this);  
		m_Constraints = new GridBagConstraints();
		m_Constraints.fill = GridBagConstraints.BOTH;
		m_Constraints.weightx = 0.7;
		m_Constraints.weighty = 0.7;
		m_Constraints.gridheight = GridBagConstraints.REMAINDER;
		m_Constraints.gridwidth = 3;
		m_DataToTabs = new HashMap<Data, DataTab>();
		m_TabLables = new HashMap<Data, DataTabLabel>();
		
		this.addMouseListener(this); 
		this.addKeyListener(this);
		this.setFocusable(true);
		 
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				initTabs();
			}
		});
	}
	
	public Data getIndexData(int index) {
		return m_IndexToData.get(index);
	}
	
	
	private void updateIndex() {
		int totalTabs = this.getTabCount();
		for(int i = 0; i < totalTabs; i++) {
		   DataTab tab = (DataTab) this.getComponentAt(i);
		   Data data = tab.getData();
		   int old_i = m_TabsToIndex.get(tab);
		   m_TabsToIndex.put(tab, i);
		   m_IndexToTabs.remove(old_i);
		   m_IndexToTabs.put(i, tab);
		   m_DataToIndex.put(data, i);
		   m_IndexToData.remove(old_i);
		   m_IndexToData.put(i, data);
		   
		}
	}
	private void updateMetadataPane() {
		if(this.getTabCount()==0) {
			MetadataPane.clearMetadataPane();
		}else {
			int j = this.getSelectedIndex();// index of the tab that's in focus
			MetadataPane.displayMetadata(this.getIndexData(j).getMetadata());
		}
		// these cases need the same result
//		if(focused_data==closed_data) { 
//		System.out.println("closed data == focused data");
////		if(tab_count-1==i) {// right-most tab
//			// next focused tab is on the left
////		}else {
////			// next focused tab is on the right
////		}
//	}else {
//		System.out.println("not equal");
//	}
	}
	public void initTabs(){
		for(Data d : m_DataManager.getAllData()){
			DataTab tab = new DataTab(d);
			String label = m_DataManager.getDataName(d);
			m_DataToTabs.put(d, tab);
			this.addTab("", null, tab, label); // tooltip on tab label
			int index = this.indexOfComponent(tab);
			DataTabLabel tabLabel = new DataTabLabel(this, tab, label);
			m_DataManager.checkIfDefaultName(m_DataManager.getDataName(d));
			m_TabLables.put(d, tabLabel);
			this.setTabComponentAt(index, tabLabel);
			// need to add data to m_IndexData
			m_IndexToData.put(index, d);
			m_IndexToTabs.put(index, tab);
			m_DataToIndex.put(d, index);
			m_TabsToIndex.put(tab, index);
			
		}
		m_dataDisplayed = this.getIndexData(0); // the default displayed data is the first tab
		if(m_dataDisplayed !=null)
		MetadataPane.displayMetadata(m_dataDisplayed.getMetadata());
	}
	public GridBagConstraints getGridBagConstraints(){
		return m_Constraints;
	}
	
	@Override
	public void selectData(Data data){
		if(!m_DataToTabs.containsKey(data)){
			this.displayData(data);
		}
		final DataTab select = m_DataToTabs.get(data);
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				try{
					DataTabPane.this.setSelectedComponent(select);
				} catch (Exception e){
					//do nothing
				}
			}
		});
	}
	
	@Override
	public void displayData(Data data) {
		// create new tab for imported data
		if(!m_DataToTabs.containsKey(data)){
			m_dataDisplayed = data;
			DataTab tab = new DataTab(data);
			m_DataToTabs.put(data, tab);
			this.addTab("", tab);
			int index = this.indexOfComponent(tab);
			DataTabLabel lable = new DataTabLabel(this, tab, m_DataManager.getDataName(data));
			m_TabLables.put(data, lable);
			this.setTabComponentAt(index, lable);
			this.setSelectedIndex(index);    
			m_IndexToData.put(index, data);
			m_IndexToTabs.put(index, tab);
			m_DataToIndex.put(data, index);
			m_TabsToIndex.put(tab, index);
			if(data.getMetadata() != null) {
				MetadataPane.displayMetadata(data.getMetadata());
			}
		}
	}

	@Override
	public void closeData(Data data) { // called when deleting data
		
		this.remove(m_DataToTabs.get(data));
		m_DataToTabs.remove(data);
		m_TabLables.remove(data);
		updateIndex();
		updateMetadataPane();
		
	}
	
	public void closeTab(DataTab tab){
		last_action_closed_tab = true;
		
		this.remove(tab);
		m_DataToTabs.remove(tab.getData());
		m_TabLables.remove(tab.getData());	
		updateIndex();
		updateMetadataPane();
		
	}
	
	public void clear(){
		this.removeAll();
		m_DataToTabs.clear();
		m_TabLables.clear();
	}
	
	void cleanup(){
		m_DataManager.removeListener(this);
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
	
	private void process(CoreEvent event){
		switch(event.type()){
		case DATA_ADDED:
			displayData(event.getData());
			break;
		case DATA_NAME_CHANGE:
			Metadata currentMeta = MetadataPane.getCurrentlyDisplayed();
			m_TabLables.get(event.getData()).setTitle(m_DataManager.getDataName(event.getData()));
			MetadataPane.displayMetadata(currentMeta);
			break;
		case DATA_REMOVED:
			closeData(event.getData());
			break;
		case WORKSPACE_CLEARED:
			this.clear();
			break;
		case WORKSPACE_LOADED:
			this.initTabs();
			break;
		default:
			break;
		}	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W) {
			DataTab tab = m_IndexToTabs.get(this.getSelectedIndex());
			this.closeTab(tab);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		this.requestFocusInWindow();
	}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
