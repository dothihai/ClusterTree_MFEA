package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import objects.UnifiedIndividual;
import utils.ObjComparator;

public class CayleyMFEA{
	
	public ArrayList<UnifiedIndividual> updateSkillFactor(ArrayList<UnifiedIndividual> pop){
		for (UnifiedIndividual indiv : pop) {
			if (indiv.getFactorialRank()[0] < indiv.getFactorialRank()[1]) {
				indiv.setSkillFactor(0);
				indiv.setScalarFitness(1.0 / (indiv.getFactorialRank()[0]) + 1);
			} else {
				indiv.setSkillFactor(1);
				indiv.setScalarFitness(1.0 / (indiv.getFactorialRank()[1]) + 1);
			}
		}
		return pop;
	}

	public ArrayList<UnifiedIndividual> updateFactorialCost(ArrayList<UnifiedIndividual> pop){
		return pop;
	}

	public ArrayList<UnifiedIndividual> applyGeneticOperators(ArrayList<UnifiedIndividual> pop, double rmp,
			double muRate, Random rnd){
		return pop;
	}

}
