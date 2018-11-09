package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import util.ByteCounterOutputStream;
import util.ClassLoaderObjectInputStream;
import util.OSGIUtils;
import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import plugins.dataviewer.gui.DataViewerSplitPane;
import jprobe.services.AbstractServiceListener;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

public class CoreDataManager implements DataManager{
	
	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();

	private Map<Class<? extends Data>, List<Data>> m_Data = new HashMap<Class<? extends Data>, List<Data>>();
	private Map<String, Data> m_NameToData = new HashMap<String, Data>();
	private Map<Data, String> m_DataToName = new LinkedHashMap<Data, String>();
	
	private final int OCCUPANCY_ARRAY_SIZE = 50; // change this later!!!
	
	public Map<String, boolean[]> m_Index = new HashMap<>()
	{{	// boolean array = "occupied?"; initialize to all false
		put("JoinedProbes", new boolean[OCCUPANCY_ARRAY_SIZE]);
		put("GenProbes", new boolean[OCCUPANCY_ARRAY_SIZE]);
		put("NegCtrl", new boolean[OCCUPANCY_ARRAY_SIZE]);
	}};	
//	private Set<String> m_Standard = new HashSet<>()
//	{{
//		add("JoinedProbes"); add("GenProbes"); add("NegCtrl");
//		add("PeakSeqs"); add("BindingProfile"); 
//		add("mut"); add("GRunMut"); add("filtered");
//	}};
	
	private Map<String, Boolean> m_Standard = new HashMap<>()
	{{ //boolean: indicate whether in group_num
		put("JoinedProbes",true); put("GenProbes", true); put("NegCtrl",true);
		put("PeakSeqs", false); put("BindingProfile", false); 
		put("mut", false); put("GRunMut", false); put("filtered", false);
	}};
	
	public static Map<String, String> m_Standard_Num = new HashMap<String, String>()
	{{	// prefix_n 
		put("ProbeJoiner", "JoinedProbes");
		put("ProbeGenerator", "GenProbes");
		put("NegativeControlGenerator", "NegCtrl");
	}};
	public static Map<String, String> m_Standard_Prefix = new HashMap<String, String>()
	{{
		// prefix_fileName   
		put("PeakFinder", "PeakSeqs");
		put("BindingProfiler", "BindingProfile");

	}};
	public static Map<String, String> m_Standard_Suffix = new HashMap<String, String>()
	{{
		// fileName_suffix 
		put("ProbeMutator", "mut");
		put("GCRunMutator", "GRunMut");  
		put("PeakFilter", "filtered");
		put("ProbeFilter", "filtered");	
	}};
	
//	private Map<Class<? extends Data>, List<Data>> m_Data = null;
//	private Map<String, Data> m_NameToData = null;
//	private Map<Data, String> m_DataToName = null;
	
	private final Map<Class<? extends Data>, List<Data>> m_InputData = new HashMap<Class<? extends Data>, List<Data>>();
	private final Map<Class<? extends Data>, List<Data>> m_OutputData = new HashMap<Class<? extends Data>, List<Data>>();
	private final Map<Class<? extends Data>, String> m_DataProviders = new HashMap<Class<? extends Data>, String>();
	private final Map<String, Data> m_InputNameToData = new HashMap<String, Data>();
	private final Map<String, Data> m_OutputNameToData = new HashMap<String, Data>();
	private final Map<Data, String> m_InputDataToName = new LinkedHashMap<Data, String>();
	private final Map<Data, String> m_OutputDataToName = new LinkedHashMap<Data, String>();
	
//	private final Map<Class<? extends Data>, Integer> m_Counts = new HashMap<Class<? extends Data>, Integer>();
	private final Map<Class<? extends Data>, DataReader> m_TypeToReader = new HashMap<Class<? extends Data>, DataReader>();
	private final Map<DataReader, Class<? extends Data>> m_ReaderToType = new HashMap<DataReader, Class<? extends Data>>();
	private final Map<Class<? extends Data>, DataWriter> m_TypeToWriter = new HashMap<Class<? extends Data>, DataWriter>();
	private final Map<DataWriter, Class<? extends Data>> m_WriterToType = new HashMap<DataWriter, Class<? extends Data>>();
	

	private final JProbeCore m_Core;

	private final AbstractServiceListener<DataReader> m_ReaderListener;
	private final AbstractServiceListener<DataWriter> m_WriterListener;
	
	private boolean m_ChangesSinceLastSave = false;
	
	private BundleContext m_Context;
	
	public CoreDataManager(JProbeCore core, BundleContext context){
		m_Core = core;
		m_Context = context;
		m_ReaderListener = new AbstractServiceListener<DataReader>(DataReader.class, context){
			@Override
			public void register(DataReader service, Bundle provider) {
				addDataReader(service, provider);
			}

			@Override
			public void unregister(DataReader service, Bundle provider) {
				removeDataReader(service, provider);
			}
			
		};
		m_WriterListener = new AbstractServiceListener<DataWriter>(DataWriter.class, context){

			@Override
			public void register(DataWriter service, Bundle provider) {
				addDataWriter(service, provider);
			}

			@Override
			public void unregister(DataWriter service, Bundle provider) {
				removeDataWriter(service, provider);
			}
			
		};
	}
	
	public void setBundleContext(BundleContext context){
		m_Context = context;
	}
	
	@Override
	public synchronized void addListener(CoreListener listener){
		m_Listeners.add(listener);
	}
	
	@Override
	public synchronized void removeListener(CoreListener listener){
		m_Listeners.remove(listener);
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : m_Listeners){
			m_ChangesSinceLastSave = event.type() == CoreEvent.Type.DATA_ADDED
					|| event.type() == CoreEvent.Type.DATA_NAME_CHANGE 
					|| event.type() == CoreEvent.Type.DATA_REMOVED;
			l.update(event);
		}
	}
	
	private void determineDataType(Data d) {
		switch(d.getDataType()) {
		case INPUT:
			m_Data = m_InputData;
			m_NameToData = m_InputNameToData;
			m_DataToName = m_InputDataToName;
			break;
		case OUTPUT:
			m_Data = m_OutputData;
			m_NameToData = m_OutputNameToData;
			m_DataToName = m_OutputDataToName;
			break;
		}
	}
	private void combineData() {
		
		m_Data = new HashMap<Class<? extends Data>, List<Data>>();
		m_Data.putAll(m_InputData);
		m_Data.putAll(m_OutputData);
	}
	private void combineNameToData() {
		m_NameToData = new HashMap<String, Data>();
		m_NameToData.putAll(m_InputNameToData);
		m_NameToData.putAll(m_OutputNameToData);
	}
	private void combineDataToName() {
		m_DataToName = new LinkedHashMap<Data, String>();
		m_DataToName.putAll(m_InputDataToName);
		m_DataToName.putAll(m_OutputDataToName);
	}
		
	private synchronized void addData(Data d, String name, Bundle responsible, boolean notify){
		Class<? extends Data> clazz = d.getClass();
		determineDataType(d);
		
		if(!m_Data.containsKey(clazz)){
			List<Data> list = new ArrayList<Data>();
			list.add(d);
			m_Data.put(clazz, list);
//			m_Counts.put(clazz, 1);
			m_DataProviders.put(clazz, OSGIUtils.getProvider(clazz, m_Context).getSymbolicName());
		}else{
			List<Data> list = m_Data.get(clazz);
			if(!list.contains(d)){
				list.add(d);
//				m_Counts.put(clazz, m_Counts.get(clazz)+1);
			}
		}
		if(m_DataToName.containsKey(d)){
			this.rename(d, name, responsible);
		}else{
			m_NameToData.put(name, d);
			m_DataToName.put(d, name);
			if(notify){
				notifyListeners(new CoreEvent(m_Core, Type.DATA_ADDED, responsible, d));
			}
		}
	}
	@Override
	public synchronized void addData(Data d, String filename, String func, Bundle responsible){
		String varName = filename;
			if(filename == null) {
				varName = assignName(d, func);
			}else {
				varName = assignName(d, func, filename);
			}

			d.setVarName(varName);
			d.getMetadata().put(Metadata.Field.DATA, new MetaObject(d)); 
			d.getMetadata().put(Metadata.Field.DATA_TYPE, new MetaObject(d.getClass().getSimpleName()+" (generated)"));
			this.addData(d, varName, responsible, true);
	}
	@Override
	public synchronized void addData(Data d, String filename, Bundle responsible){
		this.addData(d, filename, responsible, true);
	}
	
	/*
	 * return the next available index from m_Index
	 */
	private int nextIndex(String name, boolean group_num) { 
		boolean[] avail = m_Index.get(name);
		for(int i = 0; i<avail.length; i++) {
			if(!avail[i]) {
				avail[i] = true;
				if(group_num) return i+1;
				return i; 
			}
		}
		return -1; // array all full - need to handle this later (get a bigger array)
	}
	private void updateArray(String name, int num, boolean group_num) {
//		System.out.println("NAME: "+name);
//		System.out.println("group_num: "+group_num);
		int i;
		if(group_num) {
			i = num-1;
		}else {
			i = num;
		}
		if(!m_Index.containsKey(name)) {
//			System.out.println("adding name to m_Index");
			m_Index.put(name, new boolean[OCCUPANCY_ARRAY_SIZE]);
		}
//		System.out.println("updating index: "+i);
		boolean[] array = m_Index.get(name);
		array[i] = !array[i];
//		System.out.println(Arrays.toString(array));
	}
	
	private String assignName(Data d, String func) {
		String name = null;
		if(m_Standard_Num.containsKey(func)) {
			String standardName = m_Standard_Num.get(func);
			name = standardName +"_"+ nextIndex(standardName, true);
								
//			name = standardName +"_"+ m_Counts.get(standardName);
//			m_Counts.put(standardName, m_Counts.get(standardName)+1); 
		}
		return name;
	}
	private String assignName(Data d, String func, String name) {
		if(m_Standard_Prefix.containsKey(func)) {
			name = m_Standard_Prefix.get(func) +"_"+ name;
		}else {
			name = name +"_"+ m_Standard_Suffix.get(func);
		}
		
		if(!m_Index.containsKey(name)) {
			
			m_Index.put(name, new boolean[OCCUPANCY_ARRAY_SIZE]);
//			System.out.println("putting "+name+" in m_Index");
		}else {
//			System.out.println("m_Index contains name: "+name);
			int nextInd = nextIndex(name, false);
			if(nextInd==0) {
				name = name;
			}else {
				name = name +"_"+ nextInd;
			}
		}
		
//		if(!m_Counts.containsKey(name)){
//			m_Counts.put(name, 1);  // start from 1
//			
//		}else{
//			String fullName = name +"_"+ m_Counts.get(name);
//			m_Counts.put(name, m_Counts.get(name)+1);
//			name = fullName;
//		}
		return name;
	}
	private boolean getTag(String pre, String suff) {
		
		if(m_Standard.get(suff) != null) {
			return m_Standard.get(suff);
		}
		return m_Standard.get(pre);
	}
	@Override
	public void checkIfDefaultName(String label) {
		List<String> list = new LinkedList<String>(Arrays.asList(label.split("_",-1)));
		
		String pre = list.get(0);
		String suff;
		String name;
		if(isNumeric(list.get(list.size()-1))) {
			int num = Integer.parseInt(list.get(list.size()-1));
			suff = list.get(list.size()-2);
			if(m_Standard.containsKey(pre)||m_Standard.containsKey(suff)) {
				list.remove(list.size()-1);
				name = String.join("_", list);
//				System.out.println("num = "+ num);
				updateArray(name, num, getTag(pre, suff));
//				m_Counts.put(name, num+1);
			}
			
		}else {
			suff = list.get(list.size()-1);
			if(m_Standard.containsKey(pre)||m_Standard.containsKey(suff)) {
				
				name = String.join("_", list);
				updateArray(name, 0, getTag(pre, suff));
//				m_Counts.put(name, 1);
			}
		}
	}
	
	private boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	private void removeData(String name, Data d, Bundle responsible){
		determineDataType(d);
		checkIfDefaultName(name);
		m_Data.get(d.getClass()).remove(d);
//		System.out.println("name: "+name);
//		System.out.println("m_NameToData contains name: "+m_NameToData.containsKey(name));
		m_NameToData.remove(name);
		m_DataToName.remove(d);
//		System.out.println("m_NameToData contains name: "+m_NameToData.containsKey(name)); 
		d.dispose();
		notifyListeners(new CoreEvent(m_Core, Type.DATA_REMOVED, responsible, d));
	}
	
	
	@Override
	public synchronized void removeData(String name, Bundle responsible){
		combineNameToData();
		removeData(name, m_NameToData.get(name), responsible);
	}
	
	@Override
	public synchronized void removeData(Data d, Bundle responsible){
		combineDataToName();
		removeData(m_DataToName.get(d), d, responsible);
	}
	@Override
	public void removeAllData(List<Data> data, Bundle responsible) {
		combineDataToName();
		for(int i=data.size()-1;i>=0;i--) {
			Data d = data.get(i);
			removeData(m_DataToName.get(d), d, responsible);
		}
	}
	
	@Override
	public synchronized List<Data> getAllData(){
		List<Data> full = new ArrayList<Data>();
		combineDataToName();
		for(Data d : m_DataToName.keySet()){
			full.add(d);
		}
		return full;
	}
	
	@Override
	public List<Data> getInputData() {
		List<Data> full = new ArrayList<Data>();
		for(Data d : m_InputDataToName.keySet()){
			full.add(d);
		}
		return full;
	}

	@Override
	public List<Data> getOutputData() {
		List<Data> full = new ArrayList<Data>();
		combineDataToName();
		for(Data d : m_OutputDataToName.keySet()){
			full.add(d);
		}
		return full;
	}
	
	@Override
	public synchronized List<Data> getData(Class<? extends Data> type){
		combineData();
		return Collections.unmodifiableList(m_Data.get(type));
	}
	
	@Override
	public synchronized Data getData(String name){
		combineNameToData();
		return m_NameToData.get(name);
	}
	
	@Override
	public synchronized String getDataName(Data d){
		combineDataToName();
		return m_DataToName.get(d);
	}
	
	@Override
	public synchronized String[] getDataNames(){
		combineNameToData();
		return m_NameToData.keySet().toArray(new String[m_NameToData.size()]);
	}
	
	@Override
	public boolean varExists(String variable) {
		// used in JProbeGUIFrame to check if a to-be-import variable is in the system already
		return m_NameToData.containsKey(variable);
	}
	
	
	@Override
	public synchronized void rename(Data d, String name, Bundle responsible){
		determineDataType(d);
		String old = m_DataToName.get(d);
		if(m_NameToData.containsKey(name)){
			this.removeData(name, responsible);
		}
		m_NameToData.remove(old);
		m_NameToData.put(name, d);
		m_DataToName.put(d, name);
		d.setVarName(name); // so varName reflects the name-change
		// update default name
		checkIfDefaultName(old);
		checkIfDefaultName(name);
				
		notifyListeners(new CoreEvent(m_Core, Type.DATA_NAME_CHANGE, responsible, d, old, name));
	}
	
	@Override
	public synchronized boolean isReadable(Class<? extends Data> type){
		return m_TypeToReader.containsKey(type);
	}
	
	@Override
	public synchronized boolean isWritable(Class<? extends Data> type){
		return m_TypeToWriter.containsKey(type);
	}
	
	@Override
	public synchronized DataReader getDataReader(Class<? extends Data> type){
		return m_TypeToReader.get(type);
	}
	
	@Override
	public synchronized DataWriter getDataWriter(Class<? extends Data> type){
		return m_TypeToWriter.get(type);
	}
	
	@Override
	public synchronized void addDataReader(DataReader reader, Bundle responsible){
		m_TypeToReader.put(reader.getReadClass(), reader);
		m_ReaderToType.put(reader, reader.getReadClass());
		notifyListeners(new CoreEvent(m_Core, Type.DATAREADER_ADDED, responsible, reader.getReadClass()));
	}
	
	@Override
	public synchronized void addDataWriter(DataWriter writer, Bundle responsible){
		m_TypeToWriter.put(writer.getWriteClass(), writer);
		m_WriterToType.put(writer, writer.getWriteClass());
		notifyListeners(new CoreEvent(m_Core, Type.DATAWRITER_ADDED, responsible, writer.getWriteClass()));
	}
	
	private void removeDataReader(Class<? extends Data> type, DataReader reader, Bundle responsible){
		m_ReaderToType.remove(reader);
		m_TypeToReader.remove(type);
		notifyListeners(new CoreEvent(m_Core, Type.DATAREADER_REMOVED, responsible, type));
	}
	
	private void removeDataWriter(Class<? extends Data> type, DataWriter writer, Bundle responsible){
		m_WriterToType.remove(writer);
		m_TypeToWriter.remove(type);
		notifyListeners(new CoreEvent(m_Core, Type.DATAWRITER_REMOVED, responsible, type));
	}
	
	@Override
	public synchronized void removeDataReader(Class<? extends Data> type, Bundle responsible){
		removeDataReader(type, m_TypeToReader.get(type), responsible);
	}
	
	@Override
	public synchronized void removeDataWriter(Class<? extends Data> type, Bundle responsible){
		removeDataWriter(type, m_TypeToWriter.get(type), responsible);
	}
	
	@Override
	public synchronized void removeDataReader(DataReader reader, Bundle responsible){
		removeDataReader(m_ReaderToType.get(reader), reader, responsible);
	}
	
	@Override
	public synchronized void removeDataWriter(DataWriter writer, Bundle responsible){
		removeDataWriter(m_WriterToType.get(writer), writer, responsible);
	}
	
	@Override
	public synchronized Collection<Class<? extends Data>> getReadableDataTypes(){
		return m_TypeToReader.keySet();
	}
	
	@Override
	public synchronized Collection<Class<? extends Data>> getWritableDataTypes(){
		return m_TypeToWriter.keySet();
	}
	
	public synchronized DataReader getReader(Class<? extends Data> type){
		return m_TypeToReader.get(type);
	}
	
	public synchronized DataWriter getWriter(Class<? extends Data> type){
		return m_TypeToWriter.get(type);
	}
	
	public synchronized Class<? extends Data> getReadType(DataReader reader){
		return m_ReaderToType.get(reader);
	}
	
	public synchronized Class<? extends Data> getWriteType(DataWriter writer){
		return m_WriterToType.get(writer);
	}

	@Override
	public synchronized FileFilter[] getValidReadFormats(Class<? extends Data> type) {
		DataReader reader = m_TypeToReader.get(type);
		if(reader == null){
			return new FileFilter[]{};
		}
		return reader.getValidReadFormats();
	}

	@Override
	public synchronized FileNameExtensionFilter[] getValidWriteFormats(Class<? extends Data> type) {
		DataWriter writer = m_TypeToWriter.get(type);
		if(writer == null){
			return new FileNameExtensionFilter[]{};
		}
		return writer.getValidWriteFormats();
	}

	@Override
	public synchronized Data readData(File file, Class<? extends Data> type, FileFilter format, Bundle responsible) throws Exception {
		if(!this.isReadable(type)){
			throw new Exception(type+" not readable");
		}
		DataReader reader = m_TypeToReader.get(type);
		if(reader == null){
			throw new Exception(type+" reader is null");
		}
		try{
			String fileName = file.getName();
			FileInputStream in = new FileInputStream(file);
			Data read = reader.read(format, in);
			this.addData(read, fileName, "", responsible);
			in.close();
			return read;
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public synchronized void writeData(File file, Data data, FileNameExtensionFilter format) throws Exception {
		if(!this.isWritable(data.getClass())){
			throw new Exception(data.getClass()+" not writable");
		}
		DataWriter writer = m_TypeToWriter.get(data.getClass());
		if(writer == null){
			throw new Exception(data.getClass()+" writer is null");
		}
		try{
			BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
			writer.write(data, format, buffer);
			buffer.close();
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public synchronized boolean contains(String name) {
		combineNameToData();
		return m_NameToData.containsKey(name);
	}
	
	
	@Override
	public synchronized boolean contains(Data data) {
		combineDataToName();
		return m_DataToName.containsKey(data);
	}
	@Override
	public synchronized boolean containsAll(List<Data> data) {
		combineDataToName();
		for(int i=0; i<data.size();i++) {
			if(!m_DataToName.containsKey(data.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	public synchronized void clearData(){
		m_InputData.clear();
		m_OutputData.clear();
		m_InputNameToData.clear();
		m_OutputNameToData.clear();
		m_InputDataToName.clear();
		m_OutputDataToName.clear();
		
//		m_Data.clear();
//		m_NameToData.clear();
//		m_DataToName.clear();
//		m_Counts.clear();
		this.notifyListeners(new CoreEvent(m_Core, Type.WORKSPACE_CLEARED, JProbeActivator.getBundle()));
		this.m_ChangesSinceLastSave = false;
	}
	
	@Override
	public synchronized boolean changedSinceSave(){
		return m_ChangesSinceLastSave;
	}

	@Override
	public synchronized long save(OutputStream out) {
		ByteCounterOutputStream counter = new ByteCounterOutputStream(out);
		try {
			ObjectOutputStream oout = new ObjectOutputStream(counter);
			combineDataToName();
			for(Data stored : m_DataToName.keySet()){
				String name = this.getDataName(stored);
				String bundle = m_DataProviders.get(stored.getClass());
				oout.writeObject(name);
				oout.writeObject(bundle);
				oout.writeObject(stored);
			}
			m_ChangesSinceLastSave = false;
			oout.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());	
		}
		return counter.bytesWritten();
	}

	@Override
	public synchronized void load(InputStream in) {
		try {
			this.clearData();
			ClassLoaderObjectInputStream oin = new ClassLoaderObjectInputStream(in, this.getClass().getClassLoader());
			boolean finished = false;
			while(!finished){
				try {
					oin.setClassLoader(this.getClass().getClassLoader());
					String name = (String) oin.readObject();
					String bundleName = (String) oin.readObject();
					Bundle bundle = OSGIUtils.getBundleWithName(bundleName, m_Context);
					if(bundle!=null){
						oin.setClassLoader(OSGIUtils.getBundleClassLoader(bundle));
					}
					Data data = (Data) oin.readObject();
					this.addData(data, name, JProbeActivator.getBundle(), false);
					
				} catch (ClassNotFoundException e) {
					//do nothing, this means the plugin that provides the data type is not loaded so simply proceed
					continue;
				} catch (Exception e){
					finished = true;
				}
			}
			m_ChangesSinceLastSave = false;
			this.notifyListeners(new CoreEvent(m_Core, Type.WORKSPACE_LOADED, JProbeActivator.getBundle()));
			oin.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}

	public void load(){
		m_ReaderListener.load();
		m_WriterListener.load();
	}
	
	public void unload(){
		m_ReaderListener.unload();
		m_WriterListener.unload();
	}

	

	
	
	
}
