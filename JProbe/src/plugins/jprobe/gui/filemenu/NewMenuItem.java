package plugins.jprobe.gui.filemenu;

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.SaveLoadUtil;
import jprobe.services.JProbeCore;

public class NewMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;

	private JProbeCore m_Core;
	
	public NewMenuItem(JProbeCore core){
		super("New Workspace");
		m_Core = core;
		this.addActionListener(this);
		//this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_DOWN_MASK));
	
		/*GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		  gbc.gridheight = 1;
		  gbc.gridwidth  = 1;
		  gbc.gridy = 0;
		  gbc.gridx = 0;
		  gbc.insets = new Insets(0, 0, 0, 4);
		
		  gbc.weightx = 1d;
		  gbc.fill = GridBagConstraints.HORIZONTAL;
		  this.add(Box.createHorizontalGlue(), gbc);
		  gbc.gridx = 1;
		  gbc.fill = GridBagConstraints.NONE;
		  gbc.weightx = 0d;
		  gbc.anchor = GridBagConstraints.EAST;
		  */
		  
		  
		  this.setMnemonic(KeyEvent.VK_N);
		  this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		SaveLoadUtil.newWorkspace(m_Core, GUIActivator.getFrame());
	}

}
