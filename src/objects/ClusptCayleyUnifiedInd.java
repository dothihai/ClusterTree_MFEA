package objects;


public class ClusptCayleyUnifiedInd extends UnifiedIndividual {
	public int[] chromosome;
	public ClusptCayleyUnifiedInd(int nTasks){
		super(nTasks);
	}
	public int[] getChromosome() {
		return chromosome;
	}

	public void setChromosome(int[] chromosome) {
		this.chromosome = chromosome;
	}
	
}
