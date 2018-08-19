package plugins.dataviewer.gui;

import java.awt.AWTKeyStroke;
import java.awt.GridBagConstraints;
import java.awt.KeyboardFocusManager;
import java.util.HashSet;
import java.util.Set;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import plugins.dataviewer.gui.datalist.DataListPanel;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;

public class DataViewerSplitPane extends JSplitPane{
	private static final long serialVersionUID = 1L;
	
	private final DataTabPane m_DataTab;
	private final DataListPanel m_DataList;
	
	public DataViewerSplitPane(JProbeCore core, JProbeGUI gui){
		super(JSplitPane.HORIZONTAL_SPLIT);
		m_DataTab = new DataTabPane(core.getDataManager());
		
		//m_DataTab.addChangeListener((javax.swing.event.ChangeListener) changeListener);
		
		m_DataList = new DataListPanel(core, gui, m_DataTab);
		this.setOneTouchExpandable(true);
		this.setContinuousLayout(true);
		this.setLeftComponent(m_DataTab);
		this.setRightComponent(m_DataList);
		this.setResizeWeight(1.0);
		
		setupTabTraversalKeys(m_DataTab);
	}
	
	public void cleanup(){
		m_DataTab.cleanup();
		m_DataList.cleanup();
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
