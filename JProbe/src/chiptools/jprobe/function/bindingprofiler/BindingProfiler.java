package chiptools.jprobe.function.bindingprofiler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import jprobe.services.data.Data;
import jprobe.services.JProbeCore;
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
import chiptools.jprobe.data.BindingProfilePlot;
//import chiptools.jprobe.data.BindingProfilePlot;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.OutputNameArgument;
import chiptools.jprobe.function.args.ProbesArgument;

public class BindingProfiler extends AbstractChiptoolsFunction<BindingProfileParams>{

	public BindingProfiler() {
		super(BindingProfileParams.class);
	}

	@Override
	public Collection<Argument<? super BindingProfileParams>> getArguments() {
		Collection<Argument<? super BindingProfileParams>> args = new ArrayList<Argument<? super BindingProfileParams>>();
		args.add(new ProbesArgument(this, false));
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

	@Override
	public Data execute(ProgressListener l, BindingProfileParams params) throws Exception {
		util.genome.kmer.Kmer[] kmers = new util.genome.kmer.Kmer[params.getKmers().size()];
		for(int i=0; i<kmers.length; i++){
			kmers[i] = params.getKmers().get(i).getKmer();
		}
		String[] kmerNames = params.KMER_NAMES.toArray(new String[params.KMER_NAMES.size()]);
//		System.out.println("kmerNames = "+Arrays.toString(kmerNames));
		util.genome.pwm.PWM[] pwms = new util.genome.pwm.PWM[params.getPWMs().size()];
		for(int i=0; i<pwms.length; i++){
			pwms[i] = params.getPWMs().get(i).getPWM();
		}
		String[] pwmNames = params.PWM_NAMES.toArray(new String[params.PWM_NAMES.size()]);
//		System.out.println("pwmNames="+ Arrays.toString(pwmNames));
		List<Profile> bindingProfiles = new ArrayList<Profile>();
		
		// key:val = metric: scores (2D array)
		Map<String, ArrayList<double[]>> bp_kmer_score = new HashMap<>();
		Map<String, ArrayList<double[]>> bp_pwm_score = new HashMap<>();
		
		
		ProbeGroup group = params.getProbes().getProbeGroup();
		int percentComplete = this.fireProgressUpdate(l, 0, group.size(), -1);
		for(int i=0; i<group.size(); i++){
			Probe p = group.getProbe(i);
			bindingProfiles.add(Sequences.profile(p.getSequence(), p.getName(i+1), kmers, kmerNames, pwms, pwmNames));
			for(int j=0; j<kmers.length;j++) {
				Kmer kmer = kmers[j];
				String kmerName = kmerNames[j];
				double[] kmerScore = Sequences.getKmerScore(p.getSequence(), kmer);
				if(!bp_kmer_score.containsKey(kmerName)) {
					bp_kmer_score.put(kmerName, new ArrayList<double[]>());
				}
				bp_kmer_score.get(kmerName).add(kmerScore);
			}
//			for(int k=0; k<pwms.length; k++) {
//				PWM pwm = pwms[k];
//				String pwmName = pwmNames[k];
//				double[] pwmScore = Sequences.getPWMScore(p.getSequence(), pwm);
//				if(!bp_pwm_score.containsKey(pwmName)) {
//					bp_pwm_score.put(pwmName, new ArrayList<double[]>());
//				}
//				bp_pwm_score.get(pwmName).add(pwmScore);
//			}
			
			
			percentComplete = this.fireProgressUpdate(l, i+1, group.size(), percentComplete);
		}
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done profiling binding."));
		String outputName = params.getOutputName();

		// temp plot
		// key:val = metric: avg (1D array)
		Map<String, double[]> bp_kmer_avg = computeAverage(bp_kmer_score);
//		Map<String, double[]> bp_pwm_avg = computeAverage(bp_pwm_score);
		Plot plot= new Plot(bp_kmer_avg);
//		BindingProfilePlot plot = new BindingProfilePlot(bp_kmer_avg); 
		
//		System.out.println("calling binding profile plot");
		return new BindingProfile(bindingProfiles, DataType.OUTPUT, outputName, params.getMetadata());
	}

	private Map<String, double[]> computeAverage(Map<String, ArrayList<double[]>> bp_score) {
		// TODO Auto-generated method stub
		Map<String, double[]> bp_avg = new HashMap<String, double[]>();
		for(String metric: bp_score.keySet()) {
			ArrayList<double[]> entries = bp_score.get(metric);
			int num_pos = entries.get(0).length;
			double[] avg = new double[num_pos];
			for(int profile=0;profile<entries.size();profile++) {
				for(int pos=0;pos<num_pos;pos++) {
					avg[pos]+=entries.get(profile)[pos];
					if(profile==entries.size()-1) {
						avg[pos]=avg[pos]/entries.size();
					}
				}
			}
			bp_avg.put(metric, avg);
		}
		return bp_avg;
	}
	
	
	public class Plot extends JFrame{
		Map<String, double[]> m_Avg;
		public Plot(Map<String, double[]> scores) {
			m_Avg = scores;
			XYDataset dataset = createDataset();
		    JFreeChart chart = ChartFactory.createScatterPlot(
		        "Average E-Scores across Positions", 
		        "Position", "E-Score", dataset,  PlotOrientation.VERTICAL,
		        true,            // include legend
		        true,            // tooltips
		        true             // urls
		        );
		    //Changes background color
		    XYPlot plot = (XYPlot)chart.getPlot();
		    plot.setBackgroundPaint(new Color(255,228,196));
		    
		    // Create Panel
		    ChartPanel panel = new ChartPanel(chart);
		    setContentPane(panel);
		    panel.setSize(600, 300);
//		    this.setSize(800, 400);
//		    this.setLocationRelativeTo(null);
//		    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		    this.setVisible(true);
		    
		    
		    JFileChooser exportChooser = new JFileChooser();
		    exportChooser.setSelectedFile(new File("test plot"));
		    FileFilter filter = new FileNameExtensionFilter("JPEG File","jpg");
		    exportChooser.setFileFilter(filter);
			int returnVal = exportChooser.showDialog(null, "Export");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
			    File file = exportChooser.getSelectedFile();
//			    BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
//	            Graphics g = img.getGraphics();
//	            panel.paint(g);
			    
//			    BufferedImage img=chart.createBufferedImage(600,800);
			    ClassLoader cl = ClassLoader.getSystemClassLoader();
		        URL[] urls = ((URLClassLoader)cl).getURLs();
		        System.out.println("IN PROFILER");
		        for(URL url: urls){
		        	System.out.println(url.getFile());
		        }
//			    BufferedImage image = null;
//	            try {
//	            	URL url = new URL("http://www.mkyong.com/image/mypic.jpg");
//		            image = ImageIO.read(url);
//	                ImageIO.write(image, "jpg", file);
////	                JOptionPane.showMessageDialog(null, "Export", "", JOptionPane.INFORMATION_MESSAGE);
//	            }catch (IOException e) {
//			        e.printStackTrace();
//			    } catch (Throwable t){
//			    	t.printStackTrace();
//			    }
		        
		        
		        
		        
//	            BufferedImage objBufferedImage=objJFreechart.createBufferedImage(600,800);
	           
	            
//	            ByteArrayOutputStream bas = new ByteArrayOutputStream();
//	                    try {
//	                        ImageIO.write(img, "png", bas);
//	                    } catch (IOException e) {
//	                        e.printStackTrace();
//	                    }
//
//	            byte[] byteArray=bas.toByteArray();
//	            InputStream in = new ByteArrayInputStream(byteArray);
//	            BufferedImage image;
//				try {
//					image = ImageIO.read(in);
//					File outputfile = new File("image.png");
//		            ImageIO.write(image, "png", outputfile);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
	            
//			    try {
//			    	OutputStream out = new FileOutputStream(file);
//			        ChartUtilities.saveChartAsJPEG(file,
//			                chart,
//			                panel.getWidth(),
//			                panel.getHeight());
//			    }
//			    catch (IOException e) {
//			        e.printStackTrace();
//			    } catch (Throwable t){
//			    	t.printStackTrace();
//			    }
			}

		}
		private XYDataset createDataset() {
			XYSeriesCollection dataset = new XYSeriesCollection();
			for(String metric: m_Avg.keySet()) {
				XYSeries series = new XYSeries(metric);
				double[] entries = m_Avg.get(metric);
				for(int pos=0; pos<entries.length;pos++) {
					series.add(pos, entries[pos]);
				}
				dataset.addSeries(series);
			}
		    return dataset;
		}

	}

}
