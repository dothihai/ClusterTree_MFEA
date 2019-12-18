package algorithms;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

public abstract class MFEA<T> {
	public abstract ArrayList<T> updateSkillFactor(ArrayList<T> pop);

//	public abstract List updateFactorialRank(List pop);
//
//	public abstract List updateFactorialCost(List pop);
//	
//	public abstract List applyGeneticOperators(List pop, double rmp,
//			double muRate, Random rnd);
}
