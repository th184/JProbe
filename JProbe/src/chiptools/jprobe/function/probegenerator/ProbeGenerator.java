package chiptools.jprobe.function.probegenerator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import jprobe.services.data.Data;
import jprobe.services.data.MetaObject;
import jprobe.services.data.Metadata;
import jprobe.services.JProbeCore;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.function.Argument;
import util.genome.peak.PeakSequence;
import util.genome.peak.PeakSequenceGroup;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.*;

public class ProbeGenerator extends AbstractChiptoolsFunction<ProbeGeneratorParams>{

	public ProbeGenerator() {
		super(ProbeGeneratorParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeGeneratorParams>> getArguments() {
		Collection<Argument<? super ProbeGeneratorParams>> args = new ArrayList<Argument<? super ProbeGeneratorParams>>();
		
		args.add(new PeakSeqsArgument(this, false));
		args.add(new KmerArgument(this, false));
		args.add(new PWMArgument(this, false));
		args.add(new ProbeLengthArgument(this, true));
		args.add(new BindingSiteArgument(this, true));
		args.add(new WindowSizeArgument(this, true));
		args.add(new EscoreArgument(this, true, 0.4));
		
		return args;
	}

	protected int fireProgressEvent(ProgressListener l, int progress, int maxProgress, int prevPercent){
		int percent = progress*100/maxProgress;
		if(percent != prevPercent){
			l.update(new ProgressEvent(this, Type.UPDATE, progress, maxProgress, "Generating probes..."));
		}
		return percent;
	}
	
	@Override
	public Data execute(ProgressListener l, ProbeGeneratorParams params) throws Exception {
		//check whether bindingSite + 2x window exceeds PWM length and fire an error
		if(params.BINDINGSITE + 2 * params.WINDOWSIZE < params.getPWM().getPWM().length()){
			throw new RuntimeException("PWM length must be <= (binding site + 2*window).");
		}
		
		PeakSequenceGroup peakSeqs = params.getPeakSeqs().getPeakSeqs();
		int count = 0;
		int prevPercent = this.fireProgressEvent(l, count, peakSeqs.size(), -1);
		Queue<Probe> probes = new PriorityQueue<Probe>();
		int num_probes_created = 0;
		for(PeakSequence peakSeq : peakSeqs){
			try{
				List<Probe> probesCreated = ProbeUtils.extractFrom(
						peakSeq.getGenomicSequence(),
						peakSeq.getStrand(),
						peakSeq.getName(),
						params.getKmers().getKmer(),
						params.getPWM().getPWM(),
						params.getProbeLength(),
						params.BINDINGSITE,
						params.WINDOWSIZE,
						params.getEscore()
						);
				probes.addAll(probesCreated);
				num_probes_created += probesCreated.size();
			} catch (Exception e){
				throw e;
			}
			  
			prevPercent = this.fireProgressEvent(l, ++count, peakSeqs.size(), prevPercent);
		}
		
		ProbeGroup group = new ProbeGroup(probes);
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done generating probes."));
		
		params.createMetadata();
		double avg_probe_per_peak = num_probes_created/peakSeqs.size();
//		NumberFormat defaultFormat = NumberFormat.getPercentInstance();
//		defaultFormat.setMinimumFractionDigits(2);
//		String percent = defaultFormat.format(avg_probe_per_peak);
		String avg = String.format("%.2f (%,d / %,d)", avg_probe_per_peak, num_probes_created, peakSeqs.size());
		params.addMetadata(Metadata.Field.NUM_PROBE_GEN, new MetaObject((Integer)group.size()));
		params.addMetadata(Metadata.Field.AVG_PROBE_PER_PEAK, new MetaObject(avg));
		
		return new Probes(group, DataType.OUTPUT, null, params.getMetadata());
	}

}
