package jprobe.services.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MetaObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Data m_Data = null;
	private List<?> m_DataList = new ArrayList<>();
	private String m_String = null;
	private double m_Num = -1;
	private boolean m_Bool;
	
	
	public MetaObject() {}
	public MetaObject(Data d) {
		m_Data = d;
	}
	public MetaObject(String s) {
		m_String = s;
	}
	public MetaObject(double i) {
		m_Num = i;
	}
	public MetaObject(List<?> l) { 
		m_DataList = l;  
	}
	public MetaObject(boolean b) {
		m_Bool = b;
	}
	public Data getData() {
		return m_Data;
	}
	public String getDataName() { 
		return m_Data.getVarName(); }
	
	public String getDataList() {
		
		StringJoiner joiner = new StringJoiner(", ");
		if(m_DataList.size()!=0) {
			for(Object d: m_DataList) {
				joiner.add(((Data) d).getVarName());
			}
			return joiner.toString();
		}
		return "N/A";
	}
	
	public String getString() { 
		if(m_String != null) {
			return m_String;
		}
		return "N/A"; 
	}
	public String getInt() { 
		if(m_Num != -1) {
			return String.valueOf(m_Num); 
		}
		return "N/A";
	}
	public String getBool() { 
		return String.valueOf(m_Bool); 
	}
	
}
