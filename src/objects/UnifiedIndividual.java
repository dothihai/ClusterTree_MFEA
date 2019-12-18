package objects;

public class UnifiedIndividual {
	public int nTasks;
	public double[] constraintViolation;
	public double[] factorialCost;
	public int[] factorialRank;
	public double scalarFitness;
	public int skillFactor;
	public UnifiedIndividual(int nTasks){
		this.nTasks = nTasks;
		this.factorialRank = new int[nTasks];
		this.factorialCost = new double[nTasks];
	}

	public double[] getFactorialCost() {
		return factorialCost;
	}

	public void setFactorialCost(double[] factorialCost) {
		this.factorialCost = factorialCost;
	}

	public int getnTasks() {
		return nTasks;
	}

	public void setnTasks(int nTasks) {
		this.nTasks = nTasks;
	}

	public double[] getConstraintViolation() {
		return constraintViolation;
	}

	public void setConstraintViolation(double[] constraintViolation) {
		this.constraintViolation = constraintViolation;
	}

	public int[] getFactorialRank() {
		return factorialRank;
	}

	public void setFactorialRank(int factorialRank, int task) {
		this.factorialRank[task] = factorialRank;
	}

	public double getScalarFitness() {
		return scalarFitness;
	}

	public void setScalarFitness(double scalarFitness) {
		this.scalarFitness = scalarFitness;
	}

	public int getSkillFactor() {
		return skillFactor;
	}

	public void setSkillFactor(int skillFactor) {
		this.skillFactor = skillFactor;
	}

}
