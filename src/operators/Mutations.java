package operators;

import java.util.ArrayList;
import java.util.Random;

import objects.Cluster;
import utils.GraphMethods;

public class Mutations {
	GraphMethods graphMethods = new GraphMethods();
	Crossovers crossover = new Crossovers();
	InitializationPopulation initializationPop = new InitializationPopulation();

	public int[] swapChangeMutation(ArrayList<Cluster> clusters, int[] chromosome, int[] sgmOffsets, double muRate,
			Random rnd) {

		int[] newChromosome = chromosome;
		int indexCluster = selectACluster(clusters, rnd);
		int nClusterVertices = clusters.get(indexCluster).getCluster().size();
		if (rnd.nextDouble() < muRate) {

			int vertex1 = rnd.nextInt(nClusterVertices - 2);
			int vertex2 = rnd.nextInt(nClusterVertices - 2);
			while (vertex2 == vertex1) {
				vertex2 = rnd.nextInt(nClusterVertices - 2);
			}

			newChromosome = swap((sgmOffsets[indexCluster + 1] + vertex1), (sgmOffsets[indexCluster + 1] + vertex2),
					chromosome);

			newChromosome[sgmOffsets[clusters.size() + 1] + indexCluster] = rnd.nextInt(nClusterVertices);
		}

		return newChromosome;
	}

	public int[] swap(int a, int b, int[] array) {
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
		return array;
	}

	public int selectACluster(ArrayList<Cluster> clusters, Random rnd) {
		int nClusters = clusters.size();
		int indexCluster = rnd.nextInt(nClusters);
		int nClusterVertices = clusters.get(indexCluster).getCluster().size();
		while (nClusterVertices < 3) {
			indexCluster = rnd.nextInt(nClusters);

		}
		return indexCluster;
	}

	public int[][] mutationClusterTree(int[][] parent, int nVertices, ArrayList<Cluster> clusters,
			int[] minClustersLength, int[] maxClustersLength, int nClusters1, int nClusters2, double muRate,
			Random rnd) {

		int[][] offspring = new int[nVertices][nVertices];
		int nClusters = clusters.size();
		int indexCluster = rnd.nextInt(nClusters);
		int[][] presentationTree = new int[nClusters][nClusters];

		for (int i = 0; i < nVertices; i++) {
			for (int j = 0; j < nVertices; j++) {
				offspring[i][j] = parent[i][j];
			}
		}
		int numGene = (int) (1.0 / muRate);
		for (int t = 0; t < numGene; t++) {
			if (rnd.nextDouble() < muRate) {
				// choose the cluster which have more then two vertex
				while (clusters.get(indexCluster).getCluster().size() < 3) {
					indexCluster = rnd.nextInt(nClusters);
				}

				int nClusterVertices = clusters.get(indexCluster).getCluster().size();

				// create the weight matrix, and spanning tree for that cluster
				// then do mutation
				int[][] clusterWeightMatrix = new int[nClusterVertices][nClusterVertices];
				int[][] clusterSpanningTree = new int[nClusterVertices][nClusterVertices];

				clusterWeightMatrix = crossover.getAClusterTree(parent, clusters.get(indexCluster).getCluster());

				clusterSpanningTree = mutationForEachClusters(clusterWeightMatrix, minClustersLength[indexCluster],
						maxClustersLength[indexCluster], rnd);

				for (int i = 0; i < nClusterVertices; i++) {
					for (int j = 0; j < nClusterVertices; j++) {
						offspring[clusters.get(indexCluster).getCluster().get(i)][clusters.get(indexCluster)
								.getCluster().get(j)] = clusterSpanningTree[i][j];
					}
				}

				presentationTree = crossover.findTreeBetweenClusters(parent, nClusters, clusters);

				presentationTree = mutationForEachClusters(presentationTree, nClusters1, nClusters2, rnd);

				int[] indexCluster1 = new int[nClusters];
				indexCluster1 = findPresentationVertex(parent, nVertices, clusters);
				for (int j = 0; j < nClusters; j++) {
					for (int k = 0; k < nClusters; k++) {
						offspring[indexCluster1[j]][indexCluster1[k]] = presentationTree[j][k];

					}
				}
			}
		}
		return offspring;
	}

	public int[][] mutationForEachClusters(int[][] parents, int minNum_vertex, int maxNum_vertex, Random r) {

		int[][] offspring = new int[maxNum_vertex][maxNum_vertex];
		for (int i = 0; i < maxNum_vertex; i++) {
			for (int j = 0; j < maxNum_vertex; j++) {
				offspring[i][j] = parents[i][j];
			}
		}

		int startVertex = r.nextInt(maxNum_vertex);
		int endVertex = r.nextInt(maxNum_vertex);

		while ((startVertex == endVertex) || (offspring[startVertex][endVertex] > 0))
		// two vertices are equal or adjacence > 0 then choose two vertices
		// again
		{
			startVertex = r.nextInt(maxNum_vertex);
			endVertex = r.nextInt(maxNum_vertex);
		}

		// find the path from start_Vertice to end_vertice
		// initialize two matrix visited matrix = false and pre Matrix = -1;

		ArrayList<Integer> path = graphMethods.findPathBetweenTwoVerticesDFS(startVertex, endVertex, parents,
				maxNum_vertex);
		// delete a edge from cycle
		int index1 = 0;
		int index2 = 0;
		if (startVertex < minNum_vertex && endVertex < minNum_vertex) {
			index1 = r.nextInt(path.size() - 1);
			index2 = index1 + 1;

		} else {

			index1 = r.nextInt(path.size());
			while ((path.get(index1) < minNum_vertex)) {
				index1 = r.nextInt(path.size());
			}
			if (index1 == 0) {
				index2 = index1 + 1;
			} else if (index1 == path.size() - 1) {
				index2 = index1 - 1;
			} else {
				index2 = index1 + 1;

			}
		}

		offspring[path.get(index1)][path.get(index2)] = 0;
		offspring[path.get(index2)][path.get(index1)] = 0;

		offspring[startVertex][endVertex] = 1;
		offspring[endVertex][startVertex] = 1;

		return offspring;

	}

	public int[] findPresentationVertex(int[][] parent, int num_vertex, ArrayList<Cluster> clusters) {
		int numberOfCluster = clusters.size();
		int[] presentationvertex = new int[numberOfCluster];
		int numberClusterVertex1 = 0;
		int numberClusterVertex2 = 0;
		for (int i = 0; i < numberOfCluster; i++) {
			numberClusterVertex1 = clusters.get(i).getCluster().size();
			for (int j = 0; j < numberClusterVertex1; j++) {
				for (int k = i + 1; k < numberOfCluster; k++) {
					numberClusterVertex2 = clusters.get(k).getCluster().size();

					for (int t = 0; t < numberClusterVertex2; t++) {
						if (parent[clusters.get(i).getCluster().get(j)][clusters.get(k).getCluster().get(t)] > 0) {
							presentationvertex[i] = clusters.get(i).getCluster().get(j);
							presentationvertex[k] = clusters.get(k).getCluster().get(t);
						}
					}
				}
			}
		}
		return presentationvertex;
	}

	public int[][] mutationClusterTreeGA(int[][] parent, int num_vertex, ArrayList<Cluster> clusters,
			double muRate,  Random rnd) {
		int[][] offspring = new int[num_vertex][num_vertex];
		int numberOfCluster = clusters.size();

		int[][] presentationTree = new int[numberOfCluster][numberOfCluster];

		for (int i = 0; i < num_vertex; i++) {
			for (int j = 0; j < num_vertex; j++) {
				offspring[i][j] = parent[i][j];
			}
		}
		int numGene = (int) (1.0 / muRate);
		for (int t = 0; t < numGene; t++) {
			if (rnd.nextDouble() < muRate) {
				int indexCluster = rnd.nextInt(numberOfCluster);
				// choose the cluster which have more then two vertex
				while (clusters.get(indexCluster).getCluster().size() < 3) {
					indexCluster = rnd.nextInt(numberOfCluster);
				}

				int numberClusterVertex = clusters.get(indexCluster).getCluster().size();

				// create the weight matrix, and spanning tree for that cluster
				// then do mutation
				int[][] clusterWeightMatrix = new int[numberClusterVertex][numberClusterVertex];
				int[][] clusterSpanningTree = new int[numberClusterVertex][numberClusterVertex];

				clusterWeightMatrix = crossover.getAClusterTree(parent, clusters.get(indexCluster).getCluster());
				clusterSpanningTree = mutationForEachClusters(clusterWeightMatrix, numberClusterVertex, rnd);
				//

				for (int i = 0; i < numberClusterVertex; i++) {
					for (int j = 0; j < numberClusterVertex; j++) {
						offspring[clusters.get(indexCluster).getCluster().get(i)][clusters.get(indexCluster)
								.getCluster().get(j)] = clusterSpanningTree[i][j];
					}
				}

				presentationTree = crossover.findTreeBetweenClusters(parent, numberOfCluster, clusters);

				presentationTree = mutationForEachClusters(presentationTree, numberOfCluster, rnd);
				int[] indexCluster1 = new int[numberOfCluster];
				indexCluster1 = findPresentationVertex(parent, num_vertex, clusters);
				for (int j = 0; j < numberOfCluster; j++) {
					for (int k = 0; k < numberOfCluster; k++) {
						offspring[indexCluster1[j]][indexCluster1[k]] = presentationTree[j][k];

					}
				}
			}
		}

		return offspring;
	}

	public int[][] mutationForEachClusters(int[][] parents, int num_vertex, Random r) {
		int[][] offspring = new int[num_vertex][num_vertex];
		for (int i = 0; i < num_vertex; i++) {
			for (int j = 0; j < num_vertex; j++) {
				offspring[i][j] = parents[i][j];
			}
		}

		int startVertex = r.nextInt(num_vertex);
		int endVertex = r.nextInt(num_vertex);

		while ((startVertex == endVertex) || (offspring[startVertex][endVertex] > 0))
		// two vertices are equal or adjacence > 0 then choose two vertices
		// again
		{
			startVertex = r.nextInt(num_vertex);
			endVertex = r.nextInt(num_vertex);
		}
		ArrayList<Integer> path = graphMethods.findPathBetweenTwoVerticesDFS(startVertex, endVertex, parents,
				num_vertex);
		// delete a edge from cycle

		int index1 = r.nextInt(path.size() - 1);
		int index2 = index1 + 1;

		offspring[path.get(index1)][path.get(index2)] = 0;
		offspring[path.get(index2)][path.get(index1)] = 0;

		offspring[startVertex][endVertex] = 1;
		offspring[endVertex][startVertex] = 1;

		return offspring;

	}

}
