package plugins.dataviewer.gui.datalist;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import jprobe.services.data.Data;
import plugins.dataviewer.gui.DataTabPane;

public class RenameDataMenuItem extends AbstractDataMenuItem {
	private DataListModel m_Model;
	
	public RenameDataMenuItem(DataListModel model) {
		super("Rename");
		m_Model = model;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int[] selectedCell = m_Model.getSelectedCell();
		int row = selectedCell[0];
		int col = selectedCell[1];
		String oldName = (String) m_Model.getValueAt(row, col);
		String newName = (String)JOptionPane.showInputDialog(
                null,
                "New name:",
                "Rename data object",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                oldName); // default text in textfield
		if(newName != null) { //newName is null when user click "cancel"
			
			m_Model.setValueAt(newName, row, col);
		}
	}


}
