package chiptools.jprobe.data;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import jprobe.services.data.AbstractFinalData;
import jprobe.services.data.Metadata;

public class BindingProfilePlot extends JFrame{
	Map<String, double[]> m_Avg;
	public BindingProfilePlot(Map<String, double[]> scores) {
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
	    
	    this.setSize(800, 400);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}
//	public BindingProfilePlot(BindingProfile bp) {
//		m_Avg = bp.computeAverage();
//		XYDataset dataset = createDataset();
//	    JFreeChart chart = ChartFactory.createScatterPlot(
//	        "Average E-Scores across Positions", 
//	        "Position", "E-Score", dataset,  PlotOrientation.VERTICAL,
//	        true,            // include legend
//	        true,            // tooltips
//	        true             // urls
//	        );
//	    //Changes background color
//	    XYPlot plot = (XYPlot)chart.getPlot();
//	    plot.setBackgroundPaint(new Color(255,228,196));
//	    
//	    // Create Panel
//	    ChartPanel panel = new ChartPanel(chart);
//	    setContentPane(panel);
//	    
//	    this.setSize(800, 400);
//	    this.setLocationRelativeTo(null);
//	    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//	    this.setVisible(true);
//	}

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
