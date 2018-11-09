package plugins.jprobe.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ImportListAccessory extends JComponent implements PropertyChangeListener {

    private File file = null;
    private DefaultListModel model;
    private JList list;
    private JButton removeItem;
    private int[] m_selectedIndices =null;
    private boolean removeButtonPressed = false;
    
    public ImportListAccessory(JFileChooser chooser) {
        chooser.addPropertyChangeListener(this);

        model = new DefaultListModel();
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        list.addFocusListener(new FocusAdapter() {
//        	  public void focusLost(FocusEvent e) {
//        	    JList list = (JList) e.getComponent();
//        	    if(!removeButtonPressed) {
//        	    	list.clearSelection();
//        	    }
//        	  }
//        	});
        
        JScrollPane pane = new JScrollPane(list);
        pane.setPreferredSize(new Dimension(200, 250));

        removeItem = createRemoveItemButton();

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(pane);
        add(removeItem, BorderLayout.SOUTH);

    }

    public DefaultListModel getModel() {
        return model;
    }

    private void addFileToList() {
    	if(!model.contains(file)) {
    		model.addElement(file);
    	}
        
    }

    private void removeFileFromList() {
    	int[] selectedIndices = list.getSelectedIndices();
		for (int i= selectedIndices.length-1; i >=0; i--) {
			if(selectedIndices[i] != -1) {
				model.removeElementAt(selectedIndices[i]);
			}
		}
		
		 
    }

    private JButton createRemoveItemButton() {
        JButton button = new JButton("Remove");
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	removeButtonPressed = true;
                removeFileFromList();
            }
        });
        return button;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();

        //If the directory changed, don't do anything
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            update = true;
            //If a file became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            update = true;
        }

        if (update && file != null) {
        	
            addFileToList();
        }
    }
}