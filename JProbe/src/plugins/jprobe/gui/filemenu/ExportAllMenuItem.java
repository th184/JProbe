package plugins.jprobe.gui.filemenu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import plugins.jprobe.gui.ExportImportUtil;
import plugins.jprobe.gui.GUIActivator;

public class ExportAllMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	private List<Data> m_toExport;
	private Data m_Data;
	private JFileChooser m_FileChooser;
	private JProbeCore m_Core;
	
	public ExportAllMenuItem(List<Data> toExport, JProbeCore core, JFileChooser fileChooser){
		super("Export multiple files ...");
		m_toExport = toExport;
		m_Core = core;
		m_FileChooser = fileChooser;
		this.addActionListener(this);
		this.setOpaque(true);
	}
	
	@Override
    public void paintComponent(java.awt.Graphics g){
      super.paintComponent(g);
      this.setBackground(Color.LIGHT_GRAY);
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ExportImportUtil.exportAllData(m_toExport, m_Core, m_FileChooser, GUIActivator.getFrame());
	}
	
}