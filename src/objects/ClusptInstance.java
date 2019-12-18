package objects;

import java.util.ArrayList;

import objects.Cluster;
import objects.Vertex;

public class ClusptInstance {
	public ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	public ArrayList<Vertex> vertices = new ArrayList<>();
	public int nVertices;
	public double weightMatrix[][];
	public int nClusters;
	public int root;

	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}

	public int getnVertices() {
		return nVertices;
	}

	public void setnVertices(int nVertices) {
		this.nVertices = nVertices;
	}

	public double[][] getWeightMatrix() {
		return weightMatrix;
	}

	public void setWeightMatrix(double[][] weightMatrix) {
		this.weightMatrix = weightMatrix;
	}

	public int getnClusters() {
		return nClusters;
	}

	public void setnClusters(int nClusters) {
		this.nClusters = nClusters;
	}

	public int getRoot() {
		return root;
	}

	public void setRoot(int root) {
		this.root = root;
	}

}