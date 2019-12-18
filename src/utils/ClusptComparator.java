package utils;

import java.util.Comparator;

import objects.ClusptEdgesUnifiedInd;


public class ClusptComparator extends ObjComparator implements Comparator<ClusptEdgesUnifiedInd>{
	public int task = 0;
	public ClusptComparator(int task){
		this.task = task;
	}

	@Override
	public int compare(ClusptEdgesUnifiedInd ind1, ClusptEdgesUnifiedInd ind2) {
		return (ind1.getFactorialCost()[task] < ind2.getFactorialCost()[task] ? -1
				: ind1.getFactorialCost()[task] > ind2.getFactorialCost()[task] ? 1 : 0);
	}

	public static Comparator<ClusptEdgesUnifiedInd> compareByScalarFitness = new Comparator<ClusptEdgesUnifiedInd>() {
		public int compare(ClusptEdgesUnifiedInd one, ClusptEdgesUnifiedInd other) {
			return Double.compare(other.getScalarFitness(), one.getScalarFitness());
		}
	};
}
