package chiptools.jprobe.function.agilentformatter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.data.Data;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import jprobe.services.data.Metadata.Field;
import jprobe.services.data.Metadata.FieldType;

public class AgilentMetadata {
	Map<Field, Object> metaAgilent = new LinkedHashMap<Field, Object>();
	Metadata m_Metadata;
	public AgilentMetadata(Data d) {
		m_Metadata = d.getMetadata();
		for (Entry<Field, MetaObject> entry: d.getMetadata().entrySet()) {
			if(m_Metadata.getFieldType(entry.getKey())==FieldType.DATA && 
					entry.getValue().getDataName()==d.getVarName()
					) {
				LinkedHashMap<Field, Object> dataName = new LinkedHashMap<>();
				dataName.put(entry.getKey(), entry.getValue().getDataName());
				metaAgilent.put(entry.getKey(), dataName);
			}else {
				metaAgilent.put(entry.getKey(), expand(entry.getKey(), entry.getValue()));
			}
		}
			
//		StringBuilder formatted = new StringBuilder();
//		for(Entry<Field, Object> entry: metaAgilent.entrySet()) {
//			Object o = entry.getValue();
//			String value="";
//			if(o instanceof String) {
//				value = (String)o;
//			}else if(o instanceof Map) {
//				value = (String) ((Map<Field, Object>)o).get(entry.getKey())+"\n";
//				value = value + formatBlock((Map<Field, Object>)o);
//			}
//			String line = entry.getKey().toString() + ": "+ value +"\n";
//			formatted.append(line);
//		}
//		System.out.print(formatted);
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
		}else {
			
			Map<Field, Object> base = new LinkedHashMap<>();
			String val;
			switch(ft) {  
			case DATA: val = metaObj.getDataName(); break;
			case DATA_SET: val = metaObj.getDataList(); break;
			case STRING: val = metaObj.getString(); break;
			case INT: val = metaObj.getInt(); break;
			case BOOLEAN: val = metaObj.getBool(); break;
			default: val = "";
			}
			base.put(field, val);
			return base;
		}
	}
	
	
	public StringBuilder buildString(StringBuilder sb) {
		for(Entry<Field, Object> e: metaAgilent.entrySet()) {
			Map<Field, Object> m = (Map<Field, Object>)e.getValue();
			if(m.size()==1) {
				String line = e.getKey().toString()+": "+m.get(e.getKey());
				sb.append(line+"\n");
			}else {
				printBlock(e, m, 1, sb);
			}
		}
		sb.append("\n"); // separation for the next probe set
		return sb;
	}
	
	private void printBlock(Entry<Field, Object> e, Map<Field, Object> m, int numTabs, StringBuilder sb) {
		for(Entry<Field, Object> e2: m.entrySet()) {
			Map<Field, Object> m2 = (Map<Field, Object>)e2.getValue();
			String indent = "";
			String line = "";
			if(m2.size()==1 && e2.getKey()==Field.DATA) {
				indent = repeat("\t", numTabs-1);
				line = indent+ e.getKey().toString()+": "+m2.get(e2.getKey());
				sb.append(line+"\n");
			}else if(m2.size()==1) {
				indent = repeat("\t", numTabs);
				line = indent +e2.getKey().toString()+": "+m2.get(e2.getKey());
				sb.append(line+"\n");
			}else{
				printBlock(e2, m2, numTabs+1, sb);
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

//	private String formatBlock(Map<Field, Object> m) {
//		String block ="";
//		String indent = "\t";
//		for(Entry<Field, Object> entry: m.entrySet()) {
//			if(entry.getValue() instanceof String) {
//				Field f = entry.getKey();
//				String line="";
//				if(f != Field.DATA && f != Field.DATA_TYPE) {
//					line = indent + entry.getKey().toString()+ ": "+entry.getValue()+"\n";
//				}
//				block += line;
//			}else if(entry.getValue() instanceof Map){
//				String line = indent + entry.getKey().toString()+ ": "+entry.getKey().toString()+"\n";
//				line = line + formatBlock((Map<Field, Object>)entry.getValue());
//			}
//		}
//		return block;
//	}
	
	
}
