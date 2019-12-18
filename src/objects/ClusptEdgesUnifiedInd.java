package objects;

public class ClusptEdgesUnifiedInd extends UnifiedIndividual {
	int[][] chromosome;

	public ClusptEdgesUnifiedInd(int nTasks) {
		super(nTasks);
	}

	public int[][] getChromosome() {
		return chromosome;
	}

	public void setChromosome(int[][] chromosome) {
		this.chromosome = chromosome;
	}

}
