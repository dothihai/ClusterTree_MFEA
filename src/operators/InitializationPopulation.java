package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import objects.Cluster;
import utils.GraphMethods;
import objects.ClusptCayleyUnifiedInd;
import objects.ClusptEdgesUnifiedInd;

public class InitializationPopulation {

	public int[] initClusptCayleyChromosome(int[] clustersLength, Random rnd) {
		ArrayList<Integer> chromosome = new ArrayList<>();
		int nClusters = clustersLength.length;
		ArrayList<Integer> HSegs = initRandomCayleyString(nClusters, rnd);
		ArrayList<Integer> MSegs = new ArrayList<>();
		chromosome.addAll(HSegs);

		for (int lenCluster : clustersLength) {
			ArrayList<Integer> randomCayley = initRandomCayleyString(lenCluster, rnd);
			chromosome.addAll(randomCayley);
			MSegs.add(rnd.nextInt(lenCluster));

		}

		chromosome.addAll(MSegs);
		int[] newChromosome = chromosome.stream().mapToInt(Integer::intValue).toArray();
		return newChromosome;

	}

	public ArrayList<Integer> initRandomCayleyString(int nVertices, Random rnd) {
		ArrayList<Integer> randomCayley = new ArrayList<Integer>();
		int lenCayleyString = nVertices - 2;
		if (nVertices == 1) {
			randomCayley.add(-1);// - 1 the number of cluster = 1;
		} else if (nVertices == 2) {
			randomCayley.add(-2); // -2 the number of cluster = 2;
		} else {
			for (int j = 0; j < nVertices; j++) {
				randomCayley.add(j);
			}
			Collections.shuffle(randomCayley, rnd);
			randomCayley.remove(lenCayleyString - 1);
			randomCayley.remove(lenCayleyString - 2);
		}
		return randomCayley;
	}

	public ArrayList<ClusptCayleyUnifiedInd> initPopulation(int[] clustersLength, int popLength, int nTasks,
			Random rnd) {
		ArrayList<ClusptCayleyUnifiedInd> population = new ArrayList<>();
		for (int m = 0; m < popLength; m++) {
			ClusptCayleyUnifiedInd individual = new ClusptCayleyUnifiedInd(nTasks);
			int[] chromosome = initClusptCayleyChromosome(clustersLength, rnd);
			individual.setChromosome(chromosome);
			individual.setSkillFactor(rnd.nextInt(2));
			population.add(individual);
		}
		return population;
	}

	public ArrayList<ClusptEdgesUnifiedInd> initPopulation(int popLength, ArrayList<Cluster> maxClusters,
			int[] minClustersLength, int[] maxClustersLength, int maxnVertices, int minnClusters, int maxnClusters,
			int nTasks, Random rnd) {
		ArrayList<ClusptEdgesUnifiedInd> population = new ArrayList<>();
		for (int m = 0; m < popLength; m++) {
			ClusptEdgesUnifiedInd individual = new ClusptEdgesUnifiedInd(nTasks);
			int[][] chromosome = initClusptEdgeChromosome(maxClusters, minClustersLength, maxClustersLength,
					maxnVertices, minnClusters, maxnClusters, rnd);
			individual.setChromosome(chromosome);
			individual.setSkillFactor(rnd.nextInt(nTasks));
			population.add(individual);
		}
		return population;
	}

	public ArrayList<ClusptEdgesUnifiedInd> initPopulation(int popLength, ArrayList<Cluster> clusters, int nVertices,
			int nClusters, int nTasks, Random rnd) {
		ArrayList<ClusptEdgesUnifiedInd> population = new ArrayList<>();
		for (int m = 0; m < popLength; m++) {
			ClusptEdgesUnifiedInd individual = new ClusptEdgesUnifiedInd(nTasks);
			int[][] chromosome = initialPrimRST(clusters, nVertices, nClusters, rnd);
			individual.setChromosome(chromosome);
			individual.setSkillFactor(-1);
			population.add(individual);
		}
		return population;
	}

	public int[][] initClusptEdgeChromosome(ArrayList<Cluster> maxClusters, int[] minClustersLength,
			int[] maxClusterVertices, int maxnVertices, int minnClusters, int maxnClusters, Random rnd) {
		GraphMethods graphMethods = new GraphMethods();
		int[][] treeMax = new int[maxnVertices][maxnVertices];
		int[][] weightMatrix = new int[maxnVertices][maxnVertices];
		for (int i = 0; i < maxnVertices; i++) {
			for (int j = 0; j < maxnVertices; j++) {
				weightMatrix[i][j] = 1;
			}
		}

		// initialize gene for each cluster
		for (int i = 0; i < maxnClusters; i++) {

			int[][] bigClusterTree = new int[maxClusterVertices[i]][maxClusterVertices[i]];
			int[][] smallClusterTree = new int[minClustersLength[i]][minClustersLength[i]];

			for (int j = 0; j < minClustersLength[i]; j++) {
				for (int k = 0; k < minClustersLength[i]; k++) {
					smallClusterTree[j][k] = 1;
				}
			}
			// use primRST to generate tree for smallClusterTree
			smallClusterTree = graphMethods.primRST(smallClusterTree, minClustersLength[i], rnd);
			// copy smallClusterTree to bigClusterTree
			for (int j = 0; j < minClustersLength[i]; j++) {
				for (int k = 0; k < minClustersLength[i]; k++) {
					bigClusterTree[j][k] = smallClusterTree[j][k];
				}
			}

			// add edges to bigClusterTree without making cycle randomly.
			int tempNum = minClustersLength[i];
			while (tempNum < maxClusterVertices[i]) {
				int randomIndex = rnd.nextInt(tempNum);
				bigClusterTree[randomIndex][tempNum] = 1;
				bigClusterTree[tempNum][randomIndex] = 1;
				tempNum++;
			}

			// transform to the max tree( Tree of Two instances)
			for (int j = 0; j < maxClusterVertices[i]; j++) {
				for (int k = 0; k < maxClusterVertices[i]; k++) {
					if (bigClusterTree[j][k] > 0) {
						treeMax[maxClusters.get(i).getCluster().get(j)][maxClusters.get(i).getCluster()
								.get(k)] = bigClusterTree[j][k];
					}
				}
			}

		}

		// End initialize gene for each cluster//

		// Start to initialize for presentation cluster//

		// build up the Tree for presentation Cluster in instance which has the
		// number of clusters less than.
		int[][] clusterWeightMatrix;
		int[][] smallClusterSpanningTree;
		// choose randomly the presentation vertex is in instance have less than
		// number of clusters
		Cluster clusterRepresentation = new Cluster();
		for (int i = 0; i < minnClusters; i++) {
			int vertex = rnd.nextInt(minClustersLength[i]);
			clusterRepresentation.getCluster().add(maxClusters.get(i).getCluster().get(vertex));

		}

		clusterWeightMatrix = buildClusterWeightMatrix(weightMatrix, clusterRepresentation.getCluster());
		smallClusterSpanningTree = graphMethods.primRST(clusterWeightMatrix, minnClusters, rnd);

		// choose randomly the presentation vertex is in instance have greater
		// number of clusters
		for (int i = minnClusters; i < maxnClusters; i++) {
			int vertex = rnd.nextInt(minClustersLength[i]);
			clusterRepresentation.getCluster().add(maxClusters.get(i).getCluster().get(vertex));

		}

		// copy
		int[][] bigClusterTree = new int[maxnClusters][maxnClusters];
		for (int j = 0; j < minnClusters; j++) {
			for (int k = 0; k < minnClusters; k++) {
				bigClusterTree[j][k] = smallClusterSpanningTree[j][k];
			}
		}

		// build up the Tree for presentation Cluster in instance which has the
		// number of clusters greater.
		int tempNum = minnClusters;
		while (tempNum < maxnClusters) {
			int randomIndex = rnd.nextInt(tempNum);
			bigClusterTree[randomIndex][tempNum] = 1;
			bigClusterTree[tempNum][randomIndex] = 1;
			tempNum++;
		}

		// Transform presentation cluster to TreeMax
		for (int i = 0; i < maxnClusters; i++) {
			for (int j = 0; j < maxnClusters; j++) {
				if (bigClusterTree[i][j] > 0) {
					treeMax[clusterRepresentation.getCluster().get(i)][clusterRepresentation.getCluster()
							.get(j)] = bigClusterTree[i][j];
				}
			}
		}
		return treeMax;
	}

	public int[][] buildClusterWeightMatrix(int[][] weightMatrix, ArrayList<Integer> cluster) {
		int nClusterVertices = cluster.size();
		int[][] clusterWeightMatrix = new int[nClusterVertices][nClusterVertices];
		for (int i = 0; i < nClusterVertices; i++) {
			for (int j = 0; j < nClusterVertices; j++) {
				clusterWeightMatrix[i][j] = weightMatrix[cluster.get(i)][cluster.get(j)];
			}
		}
		return clusterWeightMatrix;
	}

	public int[][] findSpanningTreeBetweenClusters(int[][] parent, int num_vertex, ArrayList<Cluster> maxClusters,
			ArrayList<Cluster> clusters1) {
		int numberOfCluster = clusters1.size();
		int[][] tree = new int[num_vertex][num_vertex];
		int numberClusterVertex1 = 0;
		int numberClusterVertex2 = 0;
		for (int i = 0; i < numberOfCluster; i++) {
			for (int j = 0; j < numberOfCluster; j++) {
				tree[i][j] = 0;
				tree[j][i] = 0;
			}
		}

		for (int i = 0; i < numberOfCluster; i++) {
			numberClusterVertex1 = clusters1.get(i).getCluster().size();
			for (int j = 0; j < numberClusterVertex1; j++) {
				for (int k = i + 1; k < numberOfCluster; k++) {
					numberClusterVertex2 = clusters1.get(k).getCluster().size();

					for (int t = 0; t < numberClusterVertex2; t++) {
						if (parent[maxClusters.get(i).getCluster().get(j)][maxClusters.get(k).getCluster()
								.get(t)] > 0) {

							tree[clusters1.get(i).getCluster().get(j)][clusters1.get(k).getCluster().get(t)] = 1;
							tree[clusters1.get(k).getCluster().get(t)][clusters1.get(i).getCluster().get(j)] = 1;
						}
					}
				}
			}
		}
		return tree;
	}

	public int[][] initialPrimRST(ArrayList<Cluster> clusters, int nVertices, int nClusters, Random rnd) {
		// int randomVertice = -1, indexRandomVertice = -1;
		GraphMethods graphMethods = new GraphMethods();
		int[][] tree = new int[nVertices][nVertices];
		int[][] clusterWeightMatrix;
		int[][] clusterTree;
		// int[] indexPresentationVertex = new int[clusters.size()];
		// build the spanning for each cluster
		int[][] weightMatrix = new int[nVertices][nVertices];
		for (int i = 0; i < nVertices; i++) {
			for (int j = 0; j < nVertices; j++) {
				weightMatrix[i][j] = 1;
			}
		}
		for (int i = 0; i < nClusters; i++) {

			int nClusterVertices = clusters.get(i).getCluster().size();
			clusterWeightMatrix = buildClusterWeightMatrix(weightMatrix, clusters.get(i).getCluster());
			clusterTree = graphMethods.primRST(clusterWeightMatrix, nClusterVertices, rnd);

			for (int j = 0; j < nClusterVertices; j++) {
				for (int k = 0; k < nClusterVertices; k++) {
					if (clusterTree[j][k] > 0) {
						tree[clusters.get(i).getCluster().get(j)][clusters.get(i).getCluster()
								.get(k)] = clusterTree[j][k];
					}
				}
			}
		}

		// Each cluster is represented by one vertex
		Cluster clusterRepresentation = new Cluster();
		for (int i = 0; i < nClusters; i++) {
			int vertex = rnd.nextInt(clusters.get(i).getCluster().size());
			clusterRepresentation.getCluster().add(clusters.get(i).getCluster().get(vertex));
		}
		clusterWeightMatrix = buildClusterWeightMatrix(weightMatrix, clusterRepresentation.getCluster());
		clusterTree = graphMethods.primRST(clusterWeightMatrix, nClusters, rnd);
		// Transform to spanning tree of G Graph
		for (int i = 0; i < nClusters; i++) {
			for (int j = 0; j < nClusters; j++) {
				if (clusterTree[i][j] > 0) {
					tree[clusterRepresentation.getCluster().get(i)][clusterRepresentation.getCluster()
							.get(j)] = clusterTree[i][j];
				}
			}
		}
		return tree;
	}

}
