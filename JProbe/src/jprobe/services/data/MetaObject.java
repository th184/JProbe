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
	private int m_Int = -1;
	private double m_Double = -1;
	private boolean m_Bool;
	
	public MetaObject() {}
	public MetaObject(Data d) {
		m_Data = d;
	}
	public MetaObject(String s) {
		m_String = s;
	}
	public MetaObject(Integer i) {
		m_Int = i;
	}
	
	public MetaObject(double d) {
		m_Double = d;
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
		return m_Data.getVarName(); 
	}
	
	public String getDataList() {
		StringJoiner joiner = new StringJoiner(", ");
		StringBuilder sb = new StringBuilder();
		if(m_DataList.size()!=0) {
			for(Object d: m_DataList) {
				joiner.add(((Data) d).getVarName());
			}
			
			return joiner.toString();
		}
		return "N/A";
	}
	public List<?> getDataListObject(){
		return m_DataList;
	}
	public String getString() { 
		if(m_String != null && m_String != "") {  
			return m_String;
		}
		return "N/A"; 
	}
	public String getInt() { 
		if(m_Int != -1) {
			return String.format("%,d",m_Int); 
		}
		return "N/A";
	}
	public String getDouble() {
		if(m_Double != -1) {
			return String.format("%.2f", m_Double);
		}
		return "N/A";
	}
	public String getBool() { 
		return String.valueOf(m_Bool); 
	}
	
}
