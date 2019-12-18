package utils;

import java.util.Comparator;

import objects.Cluster;
import objects.Cycles;

public class ObjComparator{

	public static Comparator<Cycles> compareByMaxElement = new Comparator<Cycles>() {
		public int compare(Cycles cc1, Cycles cc2) {
			return Integer.compare(cc2.getMaxElement(), cc1.getMaxElement());
		}
	};
	public static Comparator<Cluster> compareByNumberOfCluster = new Comparator<Cluster>() {
		public int compare(Cluster cluster1, Cluster cluster2) {
			return Integer.compare(cluster1.getCluster().size(), cluster2.getCluster().size());
		}
	};
	

}
