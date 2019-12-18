package objects;

import java.util.ArrayList;

import operators.Encodings;

public class ClusptUnifiedInstance extends ClusptInstance {
	public int[] minClustersLength;
	public int[] maxClustersLength;
	public int minnClusters;
	public int maxnClusters;
	public Encodings encoding = new Encodings();

	public ClusptUnifiedInstance(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		setminClustersLength(clusters1, clusters2);
		setmaxClustersLength(clusters1, clusters2);
		setMinnClusters(clusters1, clusters2);
		setMaxnClusters(clusters1, clusters2);
		setCluster(clusters1, clusters2);
		setnVertices(maxClustersLength);
	}

	public void setCluster(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		super.clusters = encoding.buildMaxCluster(clusters1, clusters2);
	}
	public void setMinnClusters(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2){
		this.minnClusters = Math.min(clusters1.size(), clusters2.size());
	}
	public void setMaxnClusters(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2){
		this.maxnClusters = Math.max(clusters1.size(), clusters2.size());
	}

	public void setnVertices(int[] maxClustersLength) {
		super.nVertices = encoding.getMaxNumVertices(maxClustersLength);
	}

	public void setmaxClustersLength(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		int[] clustersLength1 = encoding.getClustersLength(clusters1);
		int[] clustersLength2 = encoding.getClustersLength(clusters2);
		int[] maxClustersLength = encoding.getTwoArrayMaxElements(clustersLength1, clustersLength2);
		this.maxClustersLength = maxClustersLength;

	}

	public void setminClustersLength(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		int[] clustersLength1 = encoding.getClustersLength(clusters1);
		int[] clustersLength2 = encoding.getClustersLength(clusters2);
		int[] minClustersLength = encoding.getTwoArrayMinElements(clustersLength1, clustersLength2);
		this.minClustersLength = minClustersLength;
	}

}
