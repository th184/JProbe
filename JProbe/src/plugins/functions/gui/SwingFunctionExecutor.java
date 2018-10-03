package plugins.functions.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;

import util.gui.OnPress;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.CoreDataManager;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;
import plugins.jprobe.gui.ExportImportUtil;
import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.JProbeGUIFrame;

public class SwingFunctionExecutor<T> extends FunctionExecutor<T>{
	
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
				if(d == null) {
					// func can return null when there's no output (e.g. input files don't match) 
					m_Monitor.dispose();
					JOptionPane.showConfirmDialog(null,
			    			"No peak could be found in the genome.\nPlease ensure the coordinates are within the genome.",
			    			"Input files mismatch",
			    			JOptionPane.DEFAULT_OPTION,  // with one OK button
			    			JOptionPane.WARNING_MESSAGE);
//				}else if(d.getRowCount()==0) {
//					m_Monitor.dispose();
//					JOptionPane.showMessageDialog(null,
//						    "No output was generated.",
//						    "Empty output object",
//						    JOptionPane.PLAIN_MESSAGE);
				}else {
					done(d, func);
				}
				
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, m_Bundle);
				e.printStackTrace();
//				done(null);
			} catch (Throwable t){
				t.printStackTrace();
				//don't report this event, as canceling the thread will cause this to notify the user
				//with java.lang.ThreadDeath
				//ErrorHandler.getInstance().handleException(new RuntimeException(t), m_Bundle);
//				done(null);
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
	private JProbeCore m_Core; // added
	private DataManager m_DataManager;
	private FunctionThread m_Thread;
	private ProgressWindow m_Monitor;
	
	public SwingFunctionExecutor(Function<T> function, T params, JProbeCore core, Bundle bundle) {
//	public SwingFunctionExecutor(Function<T> function, T params, DataManager dataManager, Bundle bundle) {
		super(function);
		m_Core = core;
		m_DataManager = core.getDataManager();
		m_Bundle = bundle;
		m_Thread = new FunctionThread(this.getFunction(), params);
	}
	// add output name as a parameter
	private void done(final Data d, String func){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				if(d.getDataType()==DataType.EXPORT) {
					ExportAgilent.exportData(d, m_Core);
				}else {
					String name = d.getOutputName();
					m_DataManager.addData(d, name, func, m_Bundle);
				}
					
				if(m_Monitor != null){
					m_Monitor.dispose();
					m_Monitor = null;
				}
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
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void cancel() {}

	@Override
	public Data getResults() {
		return null;
	}
	
	
	
}
