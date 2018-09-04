package chiptools.jprobe.function.peakfinder;

import java.util.ArrayList;
import java.util.Collection;

import util.genome.peak.PeakGroup;
import util.genome.peak.PeakSequenceGroup;
import util.genome.reader.GenomeReader;
import util.genome.reader.GenomeReaderFactory;
import util.progress.ProgressListener;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.GenomeArgument;
import chiptools.jprobe.function.args.OutputNameArgument;
import chiptools.jprobe.function.args.PeaksArgument;
import chiptools.jprobe.function.args.SummitArgument;
import jprobe.services.data.Data;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.function.Argument;

public class PeakFinder extends AbstractChiptoolsFunction<PeakFinderParams>{

	public PeakFinder() {
		super(PeakFinderParams.class);
	}

	@Override
	public Collection<Argument<? super PeakFinderParams>> getArguments() {
		Collection<Argument<? super PeakFinderParams>> args = new ArrayList<Argument<? super PeakFinderParams>>();
		args.add(new PeaksArgument(this, false));
		args.add(new GenomeArgument(this, false));
		args.add(new SummitArgument(this, true));
//		args.add(new OutputNameArgument(this, false));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, PeakFinderParams params) throws Exception {
		PeakGroup peaks = params.getPeaks().getPeaks();
		Collection<ProgressListener> listeners = new ArrayList<ProgressListener>();
		listeners.add(l);
		GenomeReader genomeReader = GenomeReaderFactory.createGenomeReader(params.getGenomeFile(), listeners);
		int summit = params.getSummit();
		String outputName = params.getOutputName();
		if(summit >= 0){
			return new PeakSequences(PeakSequenceGroup.readFromGenome(genomeReader, peaks, summit), DataType.OUTPUT, outputName);
		}else{
			return new PeakSequences(PeakSequenceGroup.readFromGenome(genomeReader, peaks), DataType.OUTPUT, outputName);
		}
	}

}
