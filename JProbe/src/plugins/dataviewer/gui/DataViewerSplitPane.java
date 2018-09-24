package plugins.dataviewer.gui;

import java.awt.AWTKeyStroke;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;
//import com.sun.java.swing.plaf.windows.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

import javafx.beans.value.ObservableValue;
import plugins.dataviewer.gui.datalist.DataListPanel;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataViewerSplitPane extends JSplitPane{
	private static final long serialVersionUID = 1L;
	
	private final DataTabPane m_DataTab;
//	private final DataListPanel m_DataList;
	private final ViewTabPane m_ViewTab;
	private final MetadataPane m_MetadataPane;
	private final JSplitPane m_splitPane;
	
	public DataViewerSplitPane(JProbeCore core, JProbeGUI gui){
		super(JSplitPane.HORIZONTAL_SPLIT);
		
	
		BasicTabbedPaneUI jtpui = new BasicTabbedPaneUI() {
		    @Override 
		    protected boolean shouldRotateTabRuns(int i) {
		        return false;
		    }
		};
		m_DataTab = new DataTabPane(core.getDataManager());
		m_DataTab.setUI(jtpui);
//		UIManager.getDefaults().put("TabbedPane.tabRunOverlay", 0);  
//		m_DataTab.setUI(new MetalTabbedPaneUI()
//	    {
//	      @Override
//	      protected int calculateTabWidth(int tabPlacement, int tabIndex,
//	                                      FontMetrics metrics)
//	      {
//	        int width = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
//	        int extra = tabIndex * 50;
//	        return width + extra;
//	      }
//	    });
		
		
//		m_DataList = new DataListPanel(core, gui, m_DataTab);
		m_ViewTab = new ViewTabPane(core, gui, m_DataTab); 
		m_MetadataPane = new MetadataPane();
		m_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_ViewTab, m_MetadataPane);
		m_splitPane.setOneTouchExpandable(true);
		m_splitPane.setContinuousLayout(true);
		m_splitPane.setResizeWeight(1.0);
		
		this.setOneTouchExpandable(true);
		this.setContinuousLayout(true);
		this.setLeftComponent(m_DataTab);
//		this.setRightComponent(m_DataList);
		this.setRightComponent(m_splitPane);
		this.setResizeWeight(1.0);
		
		setupTabTraversalKeys(m_DataTab);
		
		m_DataTab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Data selectedData = m_DataTab.getIndexData(m_DataTab.getSelectedIndex());
				if(selectedData != null && selectedData.getMetadata() != null) {
					MetadataPane.displayMetadata(selectedData.getMetadata());
				}
			}
		});
		
	}
	
	public void cleanup(){
		m_DataTab.cleanup();
//		m_DataList.cleanup();
		m_ViewTab.cleanup();
		
	}
	
	public GridBagConstraints getGridBagConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		return gbc;
	}
	
	private static void setupTabTraversalKeys(DataTabPane tabbedPane)
	  {
	    KeyStroke ctrlTab = KeyStroke.getKeyStroke("ctrl TAB");
	    KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke("ctrl shift TAB");
	 
	    // Remove ctrl-tab from normal focus traversal
	    Set<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
	    forwardKeys.remove(ctrlTab);
	    tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
	 
	    // Remove ctrl-shift-tab from normal focus traversal
	    Set<AWTKeyStroke> backwardKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
	    backwardKeys.remove(ctrlShiftTab);
	    tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
	
	    // Add keys to the tab's input map
	    InputMap inputMap = tabbedPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	    inputMap.put(ctrlTab, "navigateNext");
	    inputMap.put(ctrlShiftTab, "navigatePrevious");
	  }
	
	
}
