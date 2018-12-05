package plugins.dataviewer.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import jprobe.services.JProbeCore;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import jprobe.services.data.Metadata.Field;
import jprobe.services.data.Metadata.FieldType;
import jprobe.services.data.MetadataListener;
//implemented MetadataListener here but didn't work
public class MetadataPane extends JPanel { 
	public static Metadata m_Metadata = null;
	private static JTextArea textArea = new JTextArea(9, 18);
	private static JProbeCore m_Core;
	
	public MetadataPane(JProbeCore core) {
		m_Core = core;
		this.setBorder(new TitledBorder(new EtchedBorder(), "Metadata"));
	    textArea.setEditable(false); 
	    textArea.setFont(new Font("Arial", Font.PLAIN, 13));
	    JScrollPane scroll = new JScrollPane(textArea);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    this.add(scroll);
	}
	
	public static void displayMetadata(Metadata metadata) { // can prob remove m_Core
		m_Metadata = metadata;
		textArea.setText(""); // clear text area
		for (Entry<Field, MetaObject> entry: m_Metadata.entrySet()) {
			Field f = entry.getKey();
			FieldType ft = m_Metadata.getFieldType(f);
			MetaObject metaObj = entry.getValue();
			String val;
			
			switch(ft) {  
			case DATA: val = metaObj.getDataName(); break;
			case DATA_SET: val = metaObj.getDataList(); break;
			case STRING: val = metaObj.getString(); break;
			case INT: val = metaObj.getInt(); break;
			case DOUBLE: val = metaObj.getDouble(); break;
			case BOOLEAN: val = metaObj.getBool(); break;
			default: val = "";
			}
					
			if(val.isEmpty()) {
				textArea.append(f.toString()+"\n"); // prob can remove
			}else {
				textArea.append(f.toString()+":   "+val+"\n");
			}
		}
	}
	public static void clearMetadataPane() {
		textArea.setText("");
	}
	public static Metadata getCurrentlyDisplayed() {
		return m_Metadata;
	}

}
