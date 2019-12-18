package fileinout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import objects.ClusptInstance;
import objects.Cluster;
import objects.Vertex;

public class ClusptReadData {
	public ClusptInstance readData(String fileName) {
		ClusptInstance clusptInst = new ClusptInstance();
		BufferedReader br = null; // string to store data from file
		try {
			String sCurrentLine = null;
			br = new BufferedReader(new FileReader(fileName));
			// read lines 1..4
			for (int j = 0; j < 3; j++) {
				sCurrentLine = br.readLine();
			}

			String[] str = sCurrentLine.split(": ");
			clusptInst.nVertices = Integer.parseInt(str[1]);
			int n_vertices = clusptInst.nVertices;

			clusptInst.weightMatrix = new double[n_vertices][n_vertices];
			sCurrentLine = br.readLine();
			str = sCurrentLine.split(": ");
			clusptInst.nClusters = Integer.parseInt(str[1]);
			sCurrentLine = br.readLine();
			sCurrentLine = br.readLine();

			// read the coordinates of the vertices
			for (int j = 0; j < n_vertices; j++) {
				sCurrentLine = br.readLine();
				str = sCurrentLine.split("\\s+");
				Vertex vertex = new Vertex();
				// set coordinates to each vertex
				vertex.setX(Double.parseDouble(str[1]));
				vertex.setY(Double.parseDouble(str[2]));
				// calculate distances
				clusptInst.vertices.add(vertex);
				for (int i = 0; i <= j; i++) {
					if (i == j) {
						clusptInst.weightMatrix[j][i] = 0;
					} else {
						clusptInst.weightMatrix[j][i] = clusptInst.weightMatrix[i][j] = Math.sqrt(Math
								.pow((clusptInst.vertices.get(j).getX() - clusptInst.vertices.get(i).getX()), 2)
								+ Math.pow((clusptInst.vertices.get(j).getY() - clusptInst.vertices.get(i).getY()), 2));
					}
				}

			}

			sCurrentLine = br.readLine();
			sCurrentLine = br.readLine();
			str = sCurrentLine.split(": ");
			clusptInst.root = Integer.parseInt(str[1]);
			for (int i = 0; i < clusptInst.nClusters; i++) {
				int numberClusterVertex;
				int arrayCluster;
				sCurrentLine = br.readLine();
				str = sCurrentLine.split(" ");
				numberClusterVertex = str.length;
				Cluster cluster = new Cluster();
				for (int j = 0; j < numberClusterVertex - 2; ++j) {
					arrayCluster = Integer.parseInt(str[j + 1]);
					cluster.addElement(j, arrayCluster);
				}
				clusptInst.clusters.add(cluster);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return clusptInst; // return number of vertex
	}

	public ClusptInstance readWeightedData(String filename) {
		ClusptInstance clusptInst = new ClusptInstance();
		Scanner sc;
		String temp;
		try {
			sc = new Scanner(new File(filename));
			sc.nextLine();
			sc.nextLine();
			temp = sc.nextLine();
			String[] str = temp.split(": ");
			clusptInst.nVertices = Integer.parseInt(str[1]);
			int n_vertices = clusptInst.nVertices;

			temp = sc.nextLine();
			str = temp.split(": ");
			clusptInst.nClusters = Integer.parseInt(str[1]);
			sc.nextLine();
			sc.nextLine();
			// optimalCost = sc.nextDouble();
			clusptInst.weightMatrix = new double[n_vertices][n_vertices];
			for (int i = 0; i < n_vertices; i++) {
				for (int j = 0; j < n_vertices; j++) {
					clusptInst.weightMatrix[i][j] = sc.nextDouble();
				}
				sc.nextLine();
			}
			sc.nextLine();
			temp = sc.nextLine();
			str = temp.split(": ");
			clusptInst.root = Integer.parseInt(str[1]);
			for (int i = 0; i < clusptInst.nClusters; i++) {
				int numberClusterVertex;
				int arrayCluster;
				temp = sc.nextLine();
				str = temp.split(" ");
				numberClusterVertex = str.length;
				Cluster cluster = new Cluster();
				for (int j = 0; j < numberClusterVertex - 2; ++j) {
					arrayCluster = Integer.parseInt(str[j + 1]);
					cluster.addElement(j, arrayCluster);
				}
				clusptInst.clusters.add(cluster);
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return clusptInst;

	}


}
