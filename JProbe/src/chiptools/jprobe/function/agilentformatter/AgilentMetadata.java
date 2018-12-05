package chiptools.jprobe.function.agilentformatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;

import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.data.Data;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import jprobe.services.data.Metadata.Field;
import jprobe.services.data.Metadata.FieldType;

public class AgilentMetadata {
	Map<Field, Object> metaAgilent = new LinkedHashMap<Field, Object>();
	Metadata m_Metadata;
	public AgilentMetadata(Metadata m) { 
		m_Metadata = m;
		for(Entry<Field, MetaObject> entry: m_Metadata.entrySet()) {
			LinkedHashMap<Field, Object> dataName = new LinkedHashMap<>();
			dataName.put(entry.getKey(), entry.getValue().getString()); // metadata of the array only contain string
			metaAgilent.put(entry.getKey(), dataName);
		}
	}
	public AgilentMetadata(Data d) {
		// metadata of the other data objects
		m_Metadata = d.getMetadata();
		for (Entry<Field, MetaObject> entry: m_Metadata.entrySet()) {
			if(m_Metadata.getFieldType(entry.getKey())==FieldType.DATA && 
					entry.getValue().getDataName()==d.getVarName()) {
				
				LinkedHashMap<Field, Object> dataName = new LinkedHashMap<>();
				dataName.put(entry.getKey(), entry.getValue().getDataName());
				metaAgilent.put(entry.getKey(), dataName);
			}else {
				metaAgilent.put(entry.getKey(), expand(entry.getKey(), entry.getValue()));
			}
		}
		
//		for (Entry<Field, Object> entry : metaAgilent.entrySet()) {
//		    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
//		    
//		}
	}
	
	private Object expand(Field field, MetaObject metaObj) {
		FieldType ft = m_Metadata.getFieldType(field);
		if(ft==FieldType.DATA && 
				metaObj.getData().getDataType()==DataType.OUTPUT ) {
			Map<Field, Object> nested = new LinkedHashMap<>();
			Metadata meta = metaObj.getData().getMetadata();
			for (Entry<Field, MetaObject> entry: meta.entrySet()) {
				Field f = entry.getKey();
				MetaObject mo = entry.getValue();
				if(f== Field.DATA && 
						mo.getDataName()==metaObj.getDataName()) {
					LinkedHashMap<Field, Object> dataName = new LinkedHashMap<>();
					dataName.put(entry.getKey(), entry.getValue().getDataName());
					nested.put(f, dataName);
				}else {
					nested.put(f, expand(f, mo));
				}
			}
			return nested;
		
		} else if(ft==FieldType.DATA_SET){
			List<Map<Field, Object>> masterList = new ArrayList<>();
			List<?> dataList = metaObj.getDataListObject();
			for(Object o: dataList) {
				if(o instanceof Data) {
					Data d = (Data)o;
					Metadata meta = d.getMetadata();
					Map<Field, Object> dataBlock = new LinkedHashMap<>();
					for (Entry<Field, MetaObject> entry: meta.entrySet()) {
						Field f = entry.getKey();
						MetaObject mo = entry.getValue();
						if(f== Field.DATA && 
								mo.getDataName()==d.getVarName()) {
							LinkedHashMap<Field, Object> dataName = new LinkedHashMap<>();
							dataName.put(entry.getKey(), entry.getValue().getDataName());
							dataBlock.put(f,dataName);
						} else {
							LinkedHashMap<Field, Object> expanded = (LinkedHashMap<Field, Object>) expand(f, mo);
							dataBlock.put(f, expanded);
						}
					}
					masterList.add(dataBlock);
				}
			}
			return masterList;
		}
		else {
			Map<Field, Object> base = new LinkedHashMap<>();
			String val;
			switch(ft) {  
			case DATA: val = metaObj.getDataName(); break;
//			case DATA_SET: val = metaObj.getDataList(); break;
			case STRING: val = metaObj.getString(); break;
			case INT: val = metaObj.getInt(); break;
			case DOUBLE: val = metaObj.getDouble(); break;
			case BOOLEAN: val = metaObj.getBool(); break;
			default: val = "";
			}
			base.put(field, val);
			return base;
		}
	}
	public StringBuilder buildArray(StringBuilder sb) {
		// metadata of the array contains only string
		for(Entry<Field, Object> e: metaAgilent.entrySet()) {
			Map<Field, Object> m = (Map<Field, Object>)e.getValue();
			String line = e.getKey().toString()+": "+m.get(e.getKey());
			sb.append(line+"\n");
		}
		sb.append("\n");
		return sb;
	}
	
	public StringBuilder buildString(StringBuilder sb) {
		for(Entry<Field, Object> e: metaAgilent.entrySet()) {
			if(e.getValue() instanceof Map) {
				Map<Field, Object> m = (Map<Field, Object>)e.getValue();
				if(m.size()==1) {
					String line = e.getKey().toString()+": "+m.get(e.getKey());
					sb.append(line+"\n");
				}else {
					printBlock(e, m, 1, sb);
				}
			}else if(e.getValue() instanceof List) {
				unwrapList(e, 0, sb);
//				List<Map<Field, Object>> lst = (List<Map<Field, Object>>) e.getValue(); //
//				String data_list = getDataList(lst); 
//				String line = e.getKey().toString()+": "+data_list;
//				sb.append(line+"\n");
//				int counter = 0;
//				for(Map<Field, Object> map: lst) {
//					counter++;
//					printDataListBlock(map, 1, sb);
//					if(counter<lst.size()) {
//						sb.append("\n");
//					}
//				}
			}
		}
		sb.append("\n"); // separation for the next probe set
		return sb;
	}
	private void unwrapList(Entry<Field, Object> e, int numTabs, StringBuilder sb) {
		List<Map<Field, Object>> lst = (List<Map<Field, Object>>) e.getValue(); //
		String data_list = getDataList(lst); 
		String indent = repeat("\t", numTabs);
		String line = indent + e.getKey().toString()+": "+data_list;
		sb.append(line+"\n");
		int counter = 0;
		for(Map<Field, Object> map: lst) {
			counter++;
			printDataListBlock(map, numTabs+1, sb);
			if(counter<lst.size()) {
				sb.append("\n");
			}
		}
		
	}
	private String getDataList(List<Map<Field, Object>>lst) {
		StringJoiner data_list = new StringJoiner(", ");
		for(Map<Field,Object> m: lst) {
			String name = (String)((Map<Field, Object>)m.get(Field.DATA)).get(Field.DATA);
			data_list.add(name);
		}
		return data_list.toString();
	}
	
	private void printDataListBlock(Map<Field, Object> m, int numTabs, StringBuilder sb) {
		for(Entry<Field, Object> e2: m.entrySet()) {
			String indent = "";
			String line = "";
			if(e2.getValue() instanceof Map) {
				Map<Field, Object> m2 = (Map<Field, Object>)e2.getValue();
				if(m2.size()==1) { 
					indent = repeat("\t", numTabs);
					line = indent +e2.getKey().toString()+": "+m2.get(e2.getKey());
					sb.append(line+"\n");
				} else {
					printBlock(e2, m2, numTabs+1, sb);
				}
			}else {
				unwrapList(e2, numTabs, sb);
//				List<Map<Field, Object>> lst = (List<Map<Field, Object>>) e2.getValue(); //
//				String data_list = getDataList(lst); 
//				indent = repeat("\t", numTabs);
//				line = indent + e2.getKey().toString()+": "+data_list; 
//				sb.append(line+"\n");
//				int counter = 0;
//				for(Map<Field, Object> map: lst) {
//					counter++;
//					printDataListBlock(map, numTabs+1, sb);
//					if(counter<lst.size()) {
//						sb.append("\n");
//					}
//				}
			}
		}
	}  
	
	private void printBlock(Entry<Field, Object> e, Map<Field, Object> m, int numTabs, StringBuilder sb) {
		for(Entry<Field, Object> e2: m.entrySet()) {
			String indent = "";
			String line = "";
			if(e2.getValue() instanceof Map) {
				Map<Field, Object> m2 = (Map<Field, Object>)e2.getValue();
				if(m2.size()==1 && e2.getKey()==Field.DATA) {
					indent = repeat("\t", numTabs-1);
					line = indent+ e.getKey().toString()+": "+m2.get(e2.getKey());
					sb.append(line+"\n");
				} else if( m2.size()==1) { 
					indent = repeat("\t", numTabs);
					line = indent +e2.getKey().toString()+": "+m2.get(e2.getKey());
					sb.append(line+"\n"); 
				} else {
					printBlock(e2, m2, numTabs+1, sb);
				}
			}else {
				unwrapList(e2, numTabs, sb);
			}
		}
	}
	
	private String repeat(String s, int n) {
		    if(s == null) {
		        return null;
		    }
		    final StringBuilder sb = new StringBuilder(s.length() * n);
		    for(int i = 0; i < n; i++) {
		        sb.append(s);
		    }
		    return sb.toString();
	}

}
