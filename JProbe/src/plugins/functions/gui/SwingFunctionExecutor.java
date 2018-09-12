package plugins.functions.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;

import util.gui.OnPress;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.CoreDataManager;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;

public class SwingFunctionExecutor<T> extends FunctionExecutor<T>{
	public static Map<String, Integer> m_Counts = new HashMap<String, Integer>()
	{{	
		put("ProbeJoiner", 1);
		put("ProbeGenerator", 1);
		put("NegativeControlGenerator", 1);
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
		put("GCRunMutator", "GRun_mut");  
		put("PeakFilter", "filtered");
		put("ProbeFilter", "filtered");	
	}};
	
	private class FunctionThread extends Thread implements ProgressListener{
		
		private final Function<T> m_Function;
		private final T m_Params;
				
		private FunctionThread(Function<T> func, T params){
			m_Function = func;
			m_Params = params;
		}
		
		@Override
		public void run(){
			try {
				final Data d = m_Function.execute(this, m_Params);
				String func = m_Function.getClass().getSimpleName();
				done(d, func);
			} catch (Exception e) {
				System.out.println("in catch exception ");
				ErrorHandler.getInstance().handleException(e, m_Bundle);
				e.printStackTrace();
				done(null);
			} catch (Throwable t){
				System.out.println("in throwable t...");
				t.printStackTrace();
				//don't report this event, as canceling the thread will cause this to notify the user
				//with java.lang.ThreadDeath
				//ErrorHandler.getInstance().handleException(new RuntimeException(t), m_Bundle);
				done(null);
			}
		}

		@Override
		public void update(final ProgressEvent event) {
			switch(event.getType()){
			case UPDATE:
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						m_Monitor.setIndeterminate(event.isIndeterminant());
						m_Monitor.setMaxValue(event.getMaxProgress());
						m_Monitor.setText(event.getMessage());
						m_Monitor.setValue(event.getProgress());
					}
					
				});
				break;
			default:
				break;
			
			}
		}
		
	}
	
	private Bundle m_Bundle;
	private DataManager m_DataManager;
	private FunctionThread m_Thread;
	private ProgressWindow m_Monitor;
	

	public SwingFunctionExecutor(Function<T> function, T params, DataManager dataManager, Bundle bundle) {
		super(function);
		m_DataManager = dataManager;
		m_Bundle = bundle;
		m_Thread = new FunctionThread(this.getFunction(), params);
	}
	// add output name as a parameter
	private void done(final Data d, String func){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				if(d != null){
					String name = d.getOutputName();
					
					if(name == null) {
						name = assignName(d, func);
					}else {
						name = assignName(d, func, name);
					}
					m_DataManager.addData(d, name, m_Bundle);
					
				}
				if(m_Monitor != null){
					m_Monitor.dispose();
					m_Monitor = null;
				}
			}

			private String assignName(Data d, String func) {
				String name = null;
				if(m_Counts.containsKey(func)) {
					name = m_Standard_Num.get(func) +"_"+ m_Counts.get(func);
					m_Counts.put(func, m_Counts.get(func)+1);
				}
				return name;
			}
			private String assignName(Data d, String func, String name) {
				if(m_Standard_Prefix.containsKey(func)) {
					name = m_Standard_Prefix.get(func) +"_"+ name;
				}else {
					name = name +"_"+ m_Standard_Suffix.get(func);
				}
				return name;
			}
			
		
		});
	}
	
	private void done(final Data d) {
		done(d, "");
	}
	@Override
	public void execute() {
		//this.monitor = new ProgressMonitor(null, thread.function.getName(), null, 0, PROGRESS_BOUND);
		m_Monitor = new ProgressWindow(m_Thread.m_Function.getName(), 0, 0, false, new OnPress(){

			@SuppressWarnings("deprecation")
			@Override
			public void act() {
				m_Thread.stop();
				done(null);
			}
			
		});
		m_Thread.start();
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data getResults() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
