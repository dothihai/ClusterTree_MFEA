package operators;

import java.util.ArrayList;

import objects.Cluster;

public class Encodings {

	public int[] getSegOffsets(int[] clustersLength) {
		int nClusters = clustersLength.length;
		int[] segOffsets = new int[nClusters + 2];
		segOffsets[0] = 0; // start Point for H-graph
		// start point for Cayley string of the first cluster
		segOffsets[1] = nClusters - 2;
		for (int i = 2; i < segOffsets.length; i++) {
			// cluster have less than 3 vertices
			if (clustersLength[i - 2] < 3) {
				segOffsets[i - 1] += 1;
			} else {
				segOffsets[i - 1] += clustersLength[i - 2] - 2;
			}
		}
		return segOffsets;
	}

	public int[] getMaxClustersLength(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {

		int[] clustersLength1 = getClustersLength(clusters1);
		int[] clustersLength2 = getClustersLength(clusters2);
		int[] maxClustersLength = getTwoArrayMaxElements(clustersLength1, clustersLength2);

		return maxClustersLength;

	}

	public int[] getClustersLength(ArrayList<Cluster> clusters) {
		int nClusters = clusters.size();
		int[] clustersLength = new int[clusters.size()];

		for (int i = 0; i < nClusters; i++) {
			clustersLength[i] = clusters.get(i).getCluster().size();
		}

		return clustersLength;
	}

	public int[] getTwoArrayMaxElements(int[] array1, int[] array2) {
		int lenArray1 = array1.length;
		int lenArray2 = array2.length;
		int maxLen = Math.max(lenArray1, lenArray2);
		int[] maxElements = new int[maxLen];

		for (int i = 0; i < maxLen; i++) {
			if (i < lenArray1 && i < lenArray2) {
				maxElements[i] = Math.max(array1[i], array2[i]);
			} else if (i >= lenArray1) {
				maxElements[i] = array2[i];
			} else {
				maxElements[i] = array1[i];
			}
		}

		return maxElements;
	}

}
