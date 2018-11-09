package chiptools.jprobe.function.agilentformatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jprobe.services.data.Data;
import jprobe.services.data.Metadata;
import jprobe.services.data.AbstractFinalData.DataType;
import jprobe.services.function.Argument;
import util.DNAUtils;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import chiptools.jprobe.data.AgilentArray;
import chiptools.jprobe.data.AgilentProbe;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.OutputNameArgument;
import chiptools.jprobe.function.args.PrimerArgument;

public class AgilentFormatter extends AbstractChiptoolsFunction<AgilentFormatterParams>{

	public AgilentFormatter() {
		super(AgilentFormatterParams.class);
	}

	@Override
	public Collection<Argument<? super AgilentFormatterParams>> getArguments() {
		Collection<Argument<? super AgilentFormatterParams>> args = new ArrayList<Argument<? super AgilentFormatterParams>>();
		args.add(new ProbeCategoriesArgument(this.getClass(), false));
		args.add(new ArrayNameArgument(this, false));
		//args.add(new OutputNameArgument(this, false));
		args.add(new PrimerArgument(this, true));
		args.add(new ForwardReplicatesArgument(this, true));
		args.add(new ReverseReplicatesArgument(this, true));
		
		return args;
	}
	
	protected int fireProgressUpdate(ProgressListener l, int progress, int maxProgress, int prevPercent, String category){
		int percent = progress*100 / maxProgress;
		if(percent != prevPercent){
			l.update(new ProgressEvent(this, Type.UPDATE, progress, maxProgress, "Converting "+category+" to Agilent format..."));
		}
		return percent;
	}
	
	@Override
	public Data execute(ProgressListener l, AgilentFormatterParams params) throws Exception {
		int maxProgress = 0;
		int counter = 1;
		
		for(DataCategory<Probes> cat : params.PROBE_CATEGORIES){
//			System.out.println("data: "+cat.DATA.getVarName());
			maxProgress += cat.DATA.getProbeGroup().size();
		}
		
		int percent = -1;
		int count = 1;
		
		int fwdReps = params.FWD_REPS;
		int rvsReps = params.RVS_REPS;
		String primer = params.getPrimer() != null ? params.getPrimer() : "";
		List<AgilentProbe> agilentProbes = new ArrayList<AgilentProbe>();
		StringBuilder agilentMetadata = new StringBuilder();
		agilentMetadata.append("[Number of probe sets formatted: "+params.PROBE_CATEGORIES.size()+"] \n");
		for(DataCategory<Probes> cat : params.PROBE_CATEGORIES){
			String category = cat.CATEGORY;
			ProbeGroup probes = cat.DATA.getProbeGroup();
			int categoryIndex = 1;
			AgilentMetadata metaAgilent = new AgilentMetadata(cat.DATA);
			agilentMetadata = metaAgilent.buildString(agilentMetadata);
			for(Probe p : probes){
				percent = this.fireProgressUpdate(l, count, maxProgress, percent, category);
				String type = p.getType();
				int fwdReplicate = 1;
				int revReplicate = 1;
				for(int i=0; i<fwdReps; ++i){
					String info = createInfo("o1", fwdReplicate, type);
//					String info = createInfo("fwd", replicate, type);
					AgilentProbe fwd = createForwardProbe(p, primer, info, category, categoryIndex);
					agilentProbes.add(fwd);
					++fwdReplicate;
				}
				for(int i=0; i<rvsReps; ++i){
					String info = createInfo("o2", revReplicate, type);
//					String info = createInfo("rvs", replicate, type);
					AgilentProbe rvs = createReverseProbe(p, primer, info, category, categoryIndex);
					agilentProbes.add(rvs);
					++revReplicate;
				}
				++categoryIndex;
				++count;
			}
		}
		
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done converting probes to Agilent format."));
		
		return new AgilentArray(params.ARRAY_NAME, agilentProbes, DataType.EXPORT, agilentMetadata);
	}
	
	protected static String createInfo(String direction, int replicate, String type){
		StringBuilder infoBuilder = new StringBuilder();
		if(type != null) infoBuilder.append(type).append("_");
		infoBuilder.append(direction).append("_r").append(replicate);
		return infoBuilder.toString();
	}
	
	protected static AgilentProbe createForwardProbe(Probe p, String primer, String info, String category, int categoryIndex){
		return new AgilentProbe(p.getSequence() + primer, category, categoryIndex, info, p.getRegion());
	}
	
	protected static AgilentProbe createReverseProbe(Probe p, String primer, String info, String category, int categoryIndex){
		return new AgilentProbe(DNAUtils.reverseCompliment(p.getSequence()) + primer, category, categoryIndex, info, p.getRegion());
	}
	
	public class Pair {
		  private final String key;
		  private final String val;

		  public Pair(String key, String val) {
		    this.key = key;
		    this.val = val;
		  }
		  public String getKey() { return key; }
		  public String getValue() {return val; }
	}
		  
		  
}
