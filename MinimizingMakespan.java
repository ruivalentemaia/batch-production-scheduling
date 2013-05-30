import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

public class MinimizingMakespan extends FitnessFunction{
	
	private final double fitness;

	public MinimizingMakespan(double f) {
		fitness = f/100 + 0.75;
	}

	@Override
	protected double evaluate(IChromosome arg0) {
		return 0;
	}

}
