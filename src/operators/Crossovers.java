package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import objects.Edge;
import objects.Cluster;
import utils.GraphMethods;
import utils.visualizeTest;
import visualizations.ClusptVisualize;
import visualizations.Windows;

public class Crossovers {

	public ArrayList<int[]> twoPointCrossover(int[] father, int[] mother, int genLength, Random rnd) {
		int point = rnd.nextInt(genLength);
		int point2 = rnd.nextInt(genLength);
		while (point == point2) {
			point2 = rnd.nextInt(genLength);
		}
		int[] offspring1 = new int[genLength];
		int[] offspring2 = new int[genLength];
		ArrayList<int[]> offsprings = new ArrayList<int[]>();
		for (int i = 0; i < genLength; i++) {
			if (i < point) {
				offspring1[i] = father[i];
				offspring2[i] = mother[i];
			} else if (i <= point2) {
				offspring1[i] = mother[i];
				offspring2[i] = father[i];
			} else {
				offspring1[i] = father[i];
				offspring2[i] = mother[i];
			}
		}

		offsprings.add(offspring1);
		offsprings.add(offspring2);
		return offsprings;
	}

	public ArrayList<int[]> onePointCrossover(int[] father, int[] mother, int genLength, Random rnd) {
		int point = rnd.nextInt(genLength);
		while (point == 0 || point == genLength - 1) {
			point = rnd.nextInt(genLength);
		}
		int[] offspring1 = new int[genLength];
		int[] offspring2 = new int[genLength];
		ArrayList<int[]> offsprings = new ArrayList<int[]>();
		for (int i = 0; i < genLength; i++) {
			if (i < point) {
				offspring1[i] = father[i];
				offspring2[i] = mother[i];
			} else {
				offspring1[i] = mother[i];
				offspring2[i] = father[i];
			}
		}

		offsprings.add(offspring1);
		offsprings.add(offspring2);
		return offsprings;
	}

	public ArrayList<int[][]> edgeCrossover(int[][] father, int[][] mother, int maxnVertices,
			ArrayList<Cluster> maxClusters, int nClusters1, int nClusters2, int[] minClustersLength,
			int[] maxClustersLength, Random rnd) {
		ArrayList<int[][]> offsprings = new ArrayList<>();
		int[][] tree = new int[maxnVertices][maxnVertices];
		int[][] tree1 = new int[maxnVertices][maxnVertices];
		int[][] clusterTree, clusterTree1;
		int nClusters = maxClusters.size();

		for (int i = 0; i < nClusters; i++) {
			int nClusterVertices = maxClustersLength[i];
			clusterTree = getAClusterTree(father, maxClusters.get(i).getCluster());
			clusterTree1 = getAClusterTree(mother, maxClusters.get(i).getCluster());

			ArrayList<int[][]> offsClusterTrees = twoLevelCrossover(clusterTree, clusterTree1, minClustersLength[i],
					maxClustersLength[i], rnd);

			tree = mapClusterToTree(tree, offsClusterTrees.get(0), nClusterVertices, maxClusters.get(i));
			tree1 = mapClusterToTree(tree1, offsClusterTrees.get(1), nClusterVertices, maxClusters.get(i));

		}

		clusterTree = findTreeBetweenClusters(father, nClusters, maxClusters);
		clusterTree1 = findTreeBetweenClusters(mother, nClusters, maxClusters);
		int[] localRoot = getLocalRootClusters(father, nClusters, maxClusters);
		int[] localRoot1 = getLocalRootClusters(mother, nClusters, maxClusters);

		ArrayList<int[][]> offsClusterTrees = twoLevelCrossover(clusterTree, clusterTree1, nClusters1, nClusters2, rnd);

		tree = mapClusterToTree(tree, offsClusterTrees.get(0), nClusters, localRoot, maxClusters, rnd);
		tree1 = mapClusterToTree(tree1, offsClusterTrees.get(1), nClusters, localRoot1, maxClusters, rnd);
		offsprings.add(tree);
		offsprings.add(tree1);
		return offsprings;
	}
	public ArrayList<int[][]> gaEdgeCrossover(int[][] father, int[][] mother, int nVertices,
			ArrayList<Cluster> clusters, int nClusters, int[] clustersLength, Random rnd) {
		ArrayList<int[][]> offsprings = new ArrayList<>();
		int[][] tree = new int[nVertices][nVertices];
		int[][] tree1 = new int[nVertices][nVertices];
		int[][] clusterTree, clusterTree1;

		for (int i = 0; i < nClusters; i++) {
			int nClusterVertices = clustersLength[i];
			clusterTree = getAClusterTree(father, clusters.get(i).getCluster());
			clusterTree1 = getAClusterTree(mother, clusters.get(i).getCluster());

			ArrayList<int[][]> offsClusterTrees = crossoverTree(clusterTree, clusterTree1,0,
					clustersLength[i], rnd);

			tree = mapClusterToTree(tree, offsClusterTrees.get(0), nClusterVertices, clusters.get(i));
			tree1 = mapClusterToTree(tree1, offsClusterTrees.get(1), nClusterVertices, clusters.get(i));

		}

		clusterTree = findTreeBetweenClusters(father, nClusters, clusters);
		clusterTree1 = findTreeBetweenClusters(mother, nClusters, clusters);
		int[] localRoot = getLocalRootClusters(father, nClusters, clusters);
		int[] localRoot1 = getLocalRootClusters(mother, nClusters, clusters);

		ArrayList<int[][]> offsClusterTrees = crossoverTree(clusterTree, clusterTree1, 0, nClusters, rnd);

		tree = mapClusterToTree(tree, offsClusterTrees.get(0), nClusters, localRoot, clusters, rnd);
		tree1 = mapClusterToTree(tree1, offsClusterTrees.get(1), nClusters, localRoot1, clusters, rnd);
		offsprings.add(tree);
		offsprings.add(tree1);
		return offsprings;
	}


	public ArrayList<int[][]> edgeCrossover1(int[][] father, int[][] mother, int maxnVertices,
			ArrayList<Cluster> maxClusters, int nClusters1, int nClusters2, int[] minClustersLength,
			int[] maxClustersLength, Random rnd) {
		ArrayList<int[][]> offsprings = new ArrayList<>();
		int[][] tree = new int[maxnVertices][maxnVertices];
		int[][] tree1 = new int[maxnVertices][maxnVertices];
		int nClusters = maxClusters.size();

		for (int i = 0; i < nClusters; i++) {
			int nClusterVertices = maxClustersLength[i];
			int[][] clusterTree = getAClusterTree(father, maxClusters.get(i).getCluster());
			int[][] clusterTree1 = getAClusterTree(mother, maxClusters.get(i).getCluster());

			ArrayList<int[][]> offsClusterTrees = twoLevelCrossover(clusterTree, clusterTree1, minClustersLength[i],
					maxClustersLength[i], rnd);

			tree = mapClusterToTree(tree, offsClusterTrees.get(0), nClusterVertices, maxClusters.get(i));
			tree1 = mapClusterToTree(tree1, offsClusterTrees.get(1), nClusterVertices, maxClusters.get(i));

		}

		int[][] clusterTree = findTreeBetweenClusters1(father, nClusters, maxClusters);
		int[][] clusterTree1 = findTreeBetweenClusters1(mother, nClusters, maxClusters);
		ArrayList<int[][]> offsClusterTrees = twoLevelCrossover(clusterTree, clusterTree1, nClusters1, nClusters2, rnd);

		tree = selectVertexAndMapClusterToTree(tree, offsClusterTrees.get(0), nClusters, clusterTree, clusterTree1,
				rnd);
		tree1 = selectVertexAndMapClusterToTree(tree1, offsClusterTrees.get(1), nClusters, clusterTree, clusterTree1,
				rnd);
		offsprings.add(tree);
		offsprings.add(tree1);
		return offsprings;
	}

	public int[][] mapClusterToTree(int[][] Tree, int[][] clusterTree, int nClusterVertices, Cluster cluster) {
		for (int j = 0; j < nClusterVertices; j++) {
			for (int k = 0; k < nClusterVertices; k++) {
				Tree[cluster.getCluster().get(j)][cluster.getCluster().get(k)] = clusterTree[j][k];
			}
		}
		return Tree;
	}

	public int[][] mapClusterToTree(int[][] tree, int[][] clusterTree, int nClusters, int[] localRoot,
			ArrayList<Cluster> clusters, Random rnd) {
		for (int j = 0; j < nClusters; j++) {
			for (int k = 0; k < nClusters; k++) {
				if (clusterTree[j][k] > 0) {
					tree[localRoot[j]][localRoot[k]] = clusterTree[j][k];
				}
			}
		}
		return tree;
	}

	public int[][] selectVertexAndMapClusterToTree(int[][] tree, int[][] clusterTree, int nClusters, int[][] father,
			int[][] mother, Random rnd) {
		// debug
//		Windows windows = new Windows();
//		windows.runWindow("tree");
		for (int i = 0; i < nClusters; i++) {
			for (int j = 0; j < nClusters; j++) {
				if (clusterTree[i][j] > 0) {
					int vertex1 = father[i][j] - 1;
					int vertex2 = father[j][i] - 1;
					int vertex3 = mother[i][j] - 1;
					int vertex4 = mother[j][i] - 1;
					if (father[i][j] > 0 && mother[i][j] > 0) {
						if (rnd.nextDouble() < 0.5) {
							tree[vertex1][vertex2] = 1;
						} else {
							tree[vertex3][vertex4] = 1;
						}
					}
					if (father[i][j] > 0 && mother[i][j] == 0) {
						tree[vertex1][vertex2] = 1;
					}
					if (father[i][j] == 0 && mother[i][j] > 0) {
						tree[vertex3][vertex4] = 1;
					}

				}
			}
		}
		Decodings decode = new Decodings();

		ArrayList<int[][]> solutions = decode.decodingTwoTree(tree, visualizeTest.ClusptIns.clusters,
				visualizeTest.ClusptIns1.clusters, visualizeTest.clInst.clusters, visualizeTest.ClusptIns.nVertices,
				visualizeTest.ClusptIns1.nVertices, rnd);

//		ClusptVisualize clusptVis = new ClusptVisualize();
//		clusptVis.setPaint(solutions.get(0), visualizeTest.ClusptIns.vertices, visualizeTest.ClusptIns.clusters,
//				visualizeTest.ClusptIns.nVertices, visualizeTest.ClusptIns.root, 0, 0, 0, 10);
//		windows.addClusptVis(clusptVis);
		return tree;
	}

	public int[][] getAClusterTree(int[][] cluterTree, ArrayList<Integer> cluster) {
		int nClusterVertices = cluster.size();
		int[][] aClusterTree = new int[nClusterVertices][nClusterVertices];
		for (int i = 0; i < nClusterVertices; i++) {
			for (int j = 0; j < nClusterVertices; j++) {
				aClusterTree[i][j] = cluterTree[cluster.get(i)][cluster.get(j)];
			}
		}
		return aClusterTree;
	}

	public ArrayList<int[][]> twoLevelCrossover(int[][] tree1, int[][] tree2, int minNumVertices, int maxNumVertices,
			Random rnd) {
		ArrayList<int[][]> smallTrees = crossoverTree(tree1, tree2, 0, minNumVertices, rnd);
		ArrayList<int[][]> bigTrees = crossoverTree(tree1, tree2, minNumVertices, maxNumVertices, rnd);
		ArrayList<int[][]> offspringTrees = new ArrayList<>();
		int nOffsprings = 2;
		for (int k = 0; k < nOffsprings; k++) {
			int[][] tree = new int[maxNumVertices][maxNumVertices];
			for (int i = 0; i < maxNumVertices; i++) {
				for (int j = 0; j < maxNumVertices; j++) {
					if (i < minNumVertices && j < minNumVertices) {
						tree[i][j] = smallTrees.get(k)[i][j];
					} else {
						tree[i][j] = bigTrees.get(k)[i][j];
					}
				}
			}
			offspringTrees.add(tree);
		}

		return offspringTrees;
	}


	public ArrayList<int[][]> crossoverTree(int[][] tree1, int[][] tree2, int minNumVertices, int maxNumVertices,
			Random rnd) {
		ArrayList<Edge> edgesTree1Only = new ArrayList<>();
		ArrayList<Edge> edgesTree2Only = new ArrayList<>();
		ArrayList<int[][]> offsprings = new ArrayList<>();

		for (int i = 0; i < maxNumVertices; i++) {
			for (int j = 0; j < maxNumVertices; j++) {
				if (tree1[i][j] > 0 && tree2[i][j] == 0) {
					edgesTree1Only.add(new Edge(i, j));
				}
				if (tree2[i][j] > 0 && tree1[i][j] == 0) {
					edgesTree2Only.add(new Edge(i, j));
				}
			}
		}

		if (minNumVertices > 0) {
			edgesTree1Only = getEdgeFromBigTree(edgesTree1Only, minNumVertices);
			edgesTree2Only = getEdgeFromBigTree(edgesTree2Only, minNumVertices);

		}

		if (edgesTree2Only.size() == 0) {
			offsprings.add(tree1);
			offsprings.add(tree2);
			return offsprings;
		}

		int[][] offspring1 = crosoverInter(tree1, edgesTree2Only, minNumVertices, maxNumVertices, rnd);
		int[][] offspring2 = crosoverInter(tree2, edgesTree1Only, minNumVertices, maxNumVertices, rnd);
		offsprings.add(offspring1);
		offsprings.add(offspring2);
		return offsprings;

	}

	public ArrayList<Edge> getEdgeFromBigTree(ArrayList<Edge> edges, int minNumVertices) {
		ArrayList<Edge> bigTreeEdge = new ArrayList<>();
		for (Edge e : edges) {
			if (e.endVertex > (minNumVertices - 1) || e.startVertex > (minNumVertices - 1)) {
				bigTreeEdge.add(e);
			}
		}
		return bigTreeEdge;

	}

	public int[][] crosoverInter(int[][] tree, ArrayList<Edge> addEdges, int minNumVertices, int maxNumVertices,
			Random rnd) {

		int nAddEdges = 1 + rnd.nextInt(addEdges.size() - 1);
		Collections.shuffle(addEdges, rnd);
		for (int i = 0; i < nAddEdges; i++) {
			int startVertex = addEdges.get(i).getStartVertex();
			int endVertex = addEdges.get(i).getEndVertex();
			tree = deleteTwoVerticesFromCycle(startVertex, endVertex, tree, minNumVertices, maxNumVertices, rnd);
			tree[startVertex][endVertex] = 1;
			tree[endVertex][startVertex] = 1;
		}
		return tree;
	}

	public int[][] deleteTwoVerticesFromCycle(int startVertex, int endVertex, int[][] tree, int nVertices, Random rnd) {
		GraphMethods graphMethods = new GraphMethods();
		ArrayList<Integer> path = graphMethods.findPathBetweenTwoVerticesDFS(startVertex, endVertex, tree, nVertices);
		int selectedVertex = 1 + rnd.nextInt(path.size() - 1);
		int firstVertex = path.get(selectedVertex - 1);
		int secondVertex = path.get(selectedVertex);
		tree[firstVertex][secondVertex] = 0;
		tree[secondVertex][firstVertex] = 0;
		return tree;
	}

	public int[][] deleteTwoVerticesFromCycle(int startVertex, int endVertex, int[][] tree, int minNumVertices,
			int maxNumVertices, Random rnd) {
		GraphMethods graphMethods = new GraphMethods();
		ArrayList<Integer> path = graphMethods.findPathBetweenTwoVerticesDFS(startVertex, endVertex, tree,
				maxNumVertices);
		int selectedVertex = 1 + rnd.nextInt(path.size() - 1);
		int firstVertex = path.get(selectedVertex - 1);
		int secondVertex = path.get(selectedVertex);
		while (firstVertex < minNumVertices && secondVertex < minNumVertices) {
			selectedVertex = 1 + rnd.nextInt(path.size() - 1);
			firstVertex = path.get(selectedVertex - 1);
			secondVertex = path.get(selectedVertex);
		}
		tree[firstVertex][secondVertex] = 0;
		tree[secondVertex][firstVertex] = 0;
		return tree;
	}

	public int[][] findTreeBetweenClusters(int[][] tree, int nClusters, ArrayList<Cluster> clusters) {
		int[][] clusterTree = new int[nClusters][nClusters];
		int nClusterVertices1 = 0;
		int nClusterVertices2 = 0;

		for (int i = 0; i < nClusters; i++) {
			nClusterVertices1 = clusters.get(i).getCluster().size();
			for (int j = 0; j < nClusterVertices1; j++) {
				for (int k = i + 1; k < nClusters; k++) {
					nClusterVertices2 = clusters.get(k).getCluster().size();

					for (int t = 0; t < nClusterVertices2; t++) {
						if (tree[clusters.get(i).getCluster().get(j)][clusters.get(k).getCluster().get(t)] > 0) {
							clusterTree[i][k] = tree[clusters.get(i).getCluster().get(j)][clusters.get(k).getCluster()
									.get(t)];
							clusterTree[k][i] = clusterTree[i][k];
						}
					}
				}
			}
		}
		return clusterTree;
	}

	public int[] getLocalRootClusters(int[][] tree, int nClusters, ArrayList<Cluster> clusters) {
		int[] localRoot = new int[nClusters];
		for (int i = 0; i < nClusters; i++) {
			int nClusterVertices1 = clusters.get(i).getCluster().size();
			for (int j = 0; j < nClusterVertices1; j++) {
				for (int k = i + 1; k < nClusters; k++) {
					int nClusterVertices2 = clusters.get(k).getCluster().size();
					for (int t = 0; t < nClusterVertices2; t++) {
						if (tree[clusters.get(i).getCluster().get(j)][clusters.get(k).getCluster().get(t)] > 0) {
							localRoot[i] = clusters.get(i).getCluster().get(j);
							localRoot[k] = clusters.get(k).getCluster().get(t);
						}
					}
				}
			}
		}
		return localRoot;
	}

	public int[][] findTreeBetweenClusters1(int[][] tree, int nClusters, ArrayList<Cluster> clusters) {
		int[][] clusterTree = new int[nClusters][nClusters];
		for (int i = 0; i < nClusters; i++) {
			int nClusterVertices1 = clusters.get(i).getCluster().size();
			for (int j = 0; j < nClusterVertices1; j++) {
				for (int k = i + 1; k < nClusters; k++) {
					int nClusterVertices2 = clusters.get(k).getCluster().size();
					for (int t = 0; t < nClusterVertices2; t++) {
						if (tree[clusters.get(i).getCluster().get(j)][clusters.get(k).getCluster().get(t)] > 0) {
							clusterTree[i][k] = clusters.get(i).getCluster().get(j) + 1;
							clusterTree[k][i] = clusters.get(k).getCluster().get(t) + 1;
						}
					}
				}
			}
		}
		for (int i = 0; i < nClusters; i++) {
			System.out.print(i + "\t ");
		}
		System.out.println();
		for (int i = 0; i < nClusters; i++) {
			System.out.print(i + ":");
			for (int j = 0; j < nClusters; j++) {
				System.out.print(clusterTree[i][j] + "\t ");
			}
			System.out.println();
		}
		return clusterTree;

	}

}
