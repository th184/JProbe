package plugins.dataviewer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DataTabLabel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private DataTabPane m_TabbedPane;
	private DataTab m_Tab;
	private JLabel m_Label;	
	
	public DataTabLabel(DataTabPane tabbedPane, DataTab tab, String title){
		super();
		this.setOpaque(false);
		m_TabbedPane = tabbedPane;
		m_Tab = tab;
		m_Label = new JLabel(title);
		m_Label.setOpaque(false);
		this.add(m_Label);
		//test
//		this.addKeyListener(new CustomKeyListener());
		
		JButton close = new IconButton(Constants.getXIcon(), Constants.getXHighlightedIcon(), Constants.getXClickedIcon());
		close.addActionListener(this);
		this.add(close);
	}
	
	public void setTitle(String title){
		m_Label.setText(title);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		m_TabbedPane.closeTab(m_Tab);
		
	}
	
//	class CustomKeyListener implements KeyListener{
//		@Override
//	      public void keyTyped(KeyEvent e) {
//	      }
//		@Override
//	      public void keyPressed(KeyEvent e) {
//	         if(e.getKeyCode() == KeyEvent.VK_ENTER){
//	        	 System.out.println("closing the tab");
//	         }
//	      }
//	      public void keyReleased(KeyEvent e) {
//	      }   
//	   }
//
//	@Override
//	public void keyPressed(KeyEvent arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void keyReleased(KeyEvent arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void keyTyped(KeyEvent arg0) {
//		// TODO Auto-generated method stub
//		
//	}
	
}
