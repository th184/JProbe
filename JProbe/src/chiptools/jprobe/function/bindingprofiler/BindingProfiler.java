package chiptools.jprobe.function.bindingprofiler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import jprobe.services.data.Data;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.function.Argument;
import util.genome.Sequences;
import util.genome.Sequences.Profile;
import util.genome.kmer.Kmer;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.pwm.PWM;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import chiptools.jprobe.data.BindingProfile;
//import chiptools.jprobe.data.BindingProfilePlot;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.PrimerArgument;
import chiptools.jprobe.function.args.ProbesArgument;
import chiptools.jprobe.function.bindingprofiler.BindingProfiler.Plot.PlotType;

public class BindingProfiler extends AbstractChiptoolsFunction<BindingProfileParams>{

	public BindingProfiler() {
		super(BindingProfileParams.class);
	}

	@Override
	public Collection<Argument<? super BindingProfileParams>> getArguments() {
		Collection<Argument<? super BindingProfileParams>> args = new ArrayList<Argument<? super BindingProfileParams>>();
		args.add(new ProbesArgument(this, false));
		args.add(new PrimerArgument(this, true));
		args.add(new BindingKmerArgument(this, true));
		args.add(new BindingPWMArgument(this, true));
		//args.add(new OutputNameArgument(this, false));
		return args;
	}
	
	protected int fireProgressUpdate(ProgressListener l, int progress, int maxProgress, int prevPercent){
		int percent = progress*100/maxProgress;
		if(percent != prevPercent){
			l.update(new ProgressEvent(this, Type.UPDATE, progress, maxProgress, "Profiling binding..."));
		}
		return percent;
	}
	private int kmer_len = -1; // should be a list (could have multiple kmers)
	private int pwm_len = -1; // should be a list
	private int probe_len = -1;
	private static int[] probe_end = new int[2]; // kmer, pwm
	private static boolean m_PrimerAdded = false;
	@Override
	public Data execute(ProgressListener l, BindingProfileParams params) throws Exception {
		util.genome.kmer.Kmer[] kmers = new util.genome.kmer.Kmer[params.getKmers().size()];
		if(params.getKmers().size()!=0) {
			kmer_len = params.getKmers().get(0).getKmer().getWordLengths()[0];}
		if(params.getPWMs().size()!=0) {
			pwm_len = params.getPWMs().get(0).getPWM().length();}
		
		for(int i=0; i<kmers.length; i++){
			kmers[i] = params.getKmers().get(i).getKmer();
		}
		
		String[] kmerNames = params.KMER_NAMES.toArray(new String[params.KMER_NAMES.size()]);
		util.genome.pwm.PWM[] pwms = new util.genome.pwm.PWM[params.getPWMs().size()];
		for(int i=0; i<pwms.length; i++){
			pwms[i] = params.getPWMs().get(i).getPWM();
		}
		String[] pwmNames = params.PWM_NAMES.toArray(new String[params.PWM_NAMES.size()]);
		
		//REMOVE LATER
		List<Profile> bindingProfiles = new ArrayList<Profile>();
		
		List<Profile> bindingProfilesKmer = new ArrayList<Profile>();
		List<Profile> bindingProfilesPWM = new ArrayList<Profile>();
		
		// key:val = metric: scores (mimic 2D array, a list element is a row)
		Map<String, ArrayList<double[]>> bp_kmer_score = new HashMap<>();
		Map<String, ArrayList<double[]>> bp_pwm_score = new HashMap<>();
		
		String primer="";
		if(params.getPrimer()!=null) {
			m_PrimerAdded = true;
			primer=params.getPrimer();
		}
		
		ProbeGroup group = params.getProbes().getProbeGroup();
		probe_len = group.getProbe(0).getSequence().length();
		if(kmer_len!=-1) {probe_end[0] = probe_len - kmer_len + 1;}
		if(pwm_len!=-1)  { probe_end[1] = probe_len - pwm_len + 1;}
		
		int percentComplete = this.fireProgressUpdate(l, 0, group.size(), -1);
		for(int i=0; i<group.size(); i++){
			Probe p = group.getProbe(i);
			//REMOVE LATER
//			bindingProfiles.add(Sequences.profile(p.getSequence()+primer, p.getName(i+1), kmers, kmerNames, pwms, pwmNames));
			
			String seq = p.getSequence()+primer;
			String seqName = p.getName(i+1);
			
			for(int j=0; j<kmers.length;j++) {
				Profile profile = new Profile();
				Kmer kmer = kmers[j];
				String kmerName = kmerNames[j];
				
				if(kmer == null) continue; //
				String name = seqName + "_"; //
				name += j < kmerNames.length ? kmerNames[j] : "Kmer"+(j+1); //
				
				for(int wordLen : kmer.getWordLengths()){
					if(seq.length() < wordLen) continue;
					double[] kmerScore = Sequences.getKmerScore(p.getSequence()+primer, kmer);
					if(!bp_kmer_score.containsKey(kmerName)) {
						bp_kmer_score.put(kmerName, new ArrayList<double[]>());
					}
					bp_kmer_score.get(kmerName).add(kmerScore);
					profile.put(name, "Kmer", wordLen, kmerScore);
					
				}
				bindingProfilesKmer.add(profile);
			}
			for(int k=0; k<pwms.length; k++) {
				Profile profile = new Profile();
				PWM pwm = pwms[k];
				String pwmName = pwmNames[k];
				
				if(pwm == null) continue; //
				String name = seqName + "_";
				name += k < pwmNames.length ? pwmNames[k] : "PWM"+(k+1);
				if(seq.length() < pwm.length()) continue;
				
				double[] pwmScore = Sequences.getPWMScore(p.getSequence()+primer, pwm);
				if(!bp_pwm_score.containsKey(pwmName)) {
					bp_pwm_score.put(pwmName, new ArrayList<double[]>());
				}
				bp_pwm_score.get(pwmName).add(pwmScore);
				
				profile.put(name, "PWM",pwm.length(), pwmScore); //
				bindingProfilesPWM.add(profile);
			}
			
			percentComplete = this.fireProgressUpdate(l, i+1, group.size(), percentComplete);
		}
		bindingProfiles.addAll(bindingProfilesKmer);
		bindingProfiles.addAll(bindingProfilesPWM);
		
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done profiling binding."));
		String outputName = params.getOutputName();

		// key:val = metric: avg (1D array)
		Map<String, double[][]> bp_kmer_stats = computeStatistics(bp_kmer_score);
		Map<String, double[][]> bp_pwm_stats = computeStatistics(bp_pwm_score);
		
		JFrame f = new JFrame("Binding Profiles of "+params.getProbes().getVarName());
		int num_panel = 0;
		if(bp_kmer_stats.size()!=0) {
			for(String metric: bp_kmer_stats.keySet()) {
				f.add(new Plot(bp_kmer_stats.get(metric), metric, PlotType.KMER));
				num_panel++;
			}
		}
		if(bp_pwm_stats.size()!=0) {
			for(String metric: bp_pwm_stats.keySet()) {
				f.add(new Plot(bp_pwm_stats.get(metric), metric, PlotType.PWM));
				num_panel++;
			}
		}
		
        f.setLayout(new GridLayout(0,num_panel));
        f.pack();
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f.setVisible(true);
		f.setLocationRelativeTo(null);

        
		return new BindingProfile(bindingProfiles, DataType.OUTPUT, outputName, params.getMetadata());
	}
//	public int getProbeEndPos() { // get probe end position for Kmer
//		return probe_end[0];
//	}
	private final static int AVG = 0;
	private final static int SD_ABOVE = 1;
	private final static int MAX = 2;
	
	private final static String[] STAT_NAME = { "Mean", "1 SD above mean", "Max" };
	private final static int NUM_STATS = STAT_NAME.length;
	
	private Map<String, double[][]> computeStatistics(Map<String, ArrayList<double[]>> bp_score) {
		Map<String, double[][]> metric_stats = new HashMap<String, double[][]>();
		for(String metric: bp_score.keySet()) {
			ArrayList<double[]> entries = bp_score.get(metric);
			int num_pos = entries.get(0).length;
			
			double[] avg = new double[num_pos];
			double[] max = new double[num_pos]; Arrays.fill(max, -1000);
			double[] sd = new double[num_pos]; 
			double[] sd_above_avg = new double[num_pos];
			
			for(int profile=0;profile<entries.size();profile++) { // a profile = a row
				for(int pos=0;pos<num_pos;pos++) { 				  // a pos = a col
					double score = entries.get(profile)[pos];
					avg[pos] += score;
					if(score>max[pos]) max[pos] = score;
					if(profile == entries.size()-1) { 
						avg[pos]=avg[pos]/entries.size();
					}
				}
			}
			
			// calulate sd at each position
			int N = entries.size();
			for(int pos=0;pos<num_pos;pos++) {
				double mean = avg[pos];
				double sum = 0;
				for(int profile=0; profile<N; profile++) {
					double i = entries.get(profile)[pos];
					sum += Math.pow((i - mean), 2);
				}
				sd[pos] = Math.sqrt(sum/(N-1)); 
				sd_above_avg[pos] = avg[pos] + sd[pos];
			}
			// a set of stats for each matric
			double[][] stats = new double[NUM_STATS][]; 
			stats[AVG] = avg;
			stats[SD_ABOVE] = sd_above_avg;
			stats[MAX] = max;
			
			metric_stats.put(metric, stats);
		}
		return metric_stats;
	}
	
	public static class Plot extends JPanel{
		private static final long serialVersionUID = 1L;
		
	    enum PlotType{ KMER, PWM }
	    
		private static final int H = 200;
	    private static final int W = 2*H;
	    	    
	    public Plot(double[][] stats, String metric, PlotType type) {
	    	this.setLayout(new GridLayout());
	    	XYDataset dataset=createDataset(stats);
	    	String plot_name="";
	    	Color[] color = null;
	    	// dark to light 
	    	Color[] blue = {new Color(0, 0, 205), new Color(30, 144, 255), new Color(0, 191, 255)};
	    	Color[] red = {new Color(255,0,0), new Color(255,127,80), new Color(255,165,0)};
	    	Color[] green = {new Color(34,139,34), new Color(50,205,50), new Color(102,205,170)};
	    	int dashline_pos = 0;
	    	switch(type) {
	    	case KMER:
	    		plot_name = "E-scores using "+metric; 
	    		color = blue;
	    		dashline_pos = probe_end[0];
	    		break;
	    	case PWM:
	    		plot_name = "PWM scores using "+metric; 
	    		color = green;
	    		dashline_pos = probe_end[1];
	    		break;
	    	}
	    
		    JFreeChart chart = ChartFactory.createScatterPlot(
		        plot_name, 
		        "Position", "Score", dataset,  PlotOrientation.VERTICAL,
		        true,            // include legend
		        true,            // tooltips
		        false             // urls
		        );
		    XYPlot plot = (XYPlot)chart.getPlot();
		    plot.setBackgroundPaint(new Color(255,228,196));
		    StandardXYItemRenderer renderer = new StandardXYItemRenderer();
	        renderer.setSeriesPaint(0, color[0]); 
	        renderer.setSeriesPaint(1, color[1]); 
	        renderer.setSeriesPaint(2, color[2]); 
	        
	        renderer.setDrawSeriesLineAsPath(true);
	        renderer.setStroke(new BasicStroke(3));
	        plot.setRenderer(renderer);
	        if(dashline_pos!=0 && m_PrimerAdded) {
	        	ValueMarker marker = new ValueMarker(dashline_pos);  // position is the value on the axis
	 	        marker.setPaint(Color.black);
	 	        marker.setStroke(new BasicStroke( // dashed line
	 	                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
	 	                1.0f, new float[] {6.0f, 6.0f}, 0.0f
	 	            ));
	 	        plot.addDomainMarker(marker);
	        }
	        if(type==PlotType.KMER) {
	        	ValueMarker binding_cutoff = new ValueMarker(0.4);
	 	        binding_cutoff.setPaint(Color.red);
	 	        binding_cutoff.setStroke(new BasicStroke( // dashed line
	 	                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
	 	                1.0f, new float[] {6.0f, 6.0f}, 0.0f
	 	            ));
	 	        ValueMarker nobinding_cutoff = new ValueMarker(0.35);
	 	        nobinding_cutoff.setPaint(Color.red);
	 	        nobinding_cutoff.setStroke(new BasicStroke( // dashed line
	 	                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
	 	                1.0f, new float[] {6.0f, 6.0f}, 0.0f
	 	            ));
	 	        plot.addRangeMarker(binding_cutoff);
	 	        plot.addRangeMarker(nobinding_cutoff);
	        }
	        
	        this.add(new ChartPanel(chart, W, H, W, H, W, H,
	             false, true, true, true, true, true));
	        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
	     }
	    private XYDataset createDataset(double[][] stats) {
	    	XYSeriesCollection dataset = new XYSeriesCollection();
	    	System.out.println("num of stats: "+stats.length);
	    	for(int s=0; s<stats.length;s++) {
	    		XYSeries series = new XYSeries(STAT_NAME[s]);
	    		double[] stat = stats[s];
	    		for(int pos=0; pos<stat.length; pos++) {
	    			series.add(pos, stat[pos]);
	    		}
	    		dataset.addSeries(series);
	    	}
	    	return dataset;
	    }
	    
	 }
}


