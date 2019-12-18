package operators;

import java.util.ArrayList;
import java.util.Random;

import objects.Cluster;
import utils.BlobCode;
import utils.DandelionCode;
import utils.PruferCode;

public class Decodings {

	public int[][] decodeTree(int[] chromosome, int sgmOffsets[], int n_vertices, ArrayList<Cluster> clusters,
			String cLCode, Random rnd) {
		int[][] tree = new int[n_vertices][n_vertices];
		int nClusters = clusters.size();

		int startMSegs = sgmOffsets[sgmOffsets.length - 1];
		int[] mSegs = getSegmentGenes(chromosome, startMSegs, nClusters);
		int[] decodedMSegs = decodeMSegs(mSegs, clusters);

		int[] hSeg = getSegmentGenes(chromosome, 0, nClusters - 2);
		int[] cayLeyString = getCayleyString(hSeg, nClusters);
		int[][] hTree = decodeCayley(cLCode, cayLeyString, nClusters);
		tree = mapToClusterTree(decodedMSegs, tree, hTree, nClusters);

		for (int i = 1; i < nClusters + 1; i++) {
			Cluster curCluster = clusters.get(i - 1);
			int nClusterVertices = curCluster.getCluster().size();
			int[] clstSegs = getSegmentGenes(chromosome, sgmOffsets[i], nClusterVertices);
			int[] clusterCayleyString = getCayleyString(clstSegs, nClusterVertices);
			int[][] clusterTree = decodeCayley(cLCode, clusterCayleyString, nClusterVertices);
			tree = mapToClusterTree(curCluster, tree, clusterTree, nClusterVertices);
		}

		return tree;
	}

	public int[][] mapToClusterTree(Cluster cluster, int[][] tree, int[][] clusterTree, int nVertices) {
		for (int j = 0; j < nVertices; j++) {
			for (int k = 0; k < nVertices; k++) {
				tree[cluster.getCluster().get(j)][cluster.getCluster().get(k)] = clusterTree[j][k];
			}
		}
		return tree;
	}

	public int[] getSegmentGenes(int[] chromosome, int start, int offset) {
		int end = start + offset;
		int[] segmentGenes = new int[offset];
		for (int i = start; i < end; i++) {
			segmentGenes[i - start] = chromosome[i];
		}
		return segmentGenes;
	}

	public int[][] mapToClusterTree(int[] mSegs, int[][] tree, int[][] clusterTree, int nVertices) {
		for (int i = 0; i < nVertices; i++) {
			for (int j = 0; j < nVertices; j++) {
				tree[mSegs[i]][mSegs[j]] = clusterTree[i][j];
			}
		}
		return tree;
	}

	public int[] decodeMSegs(int[] mSegs, ArrayList<Cluster> clusters) {
		int nClusters = clusters.size();
		int[] decodedMSegs = new int[nClusters];
		for (int i = 0; i < nClusters; i++) {
			int nclusterVertices = clusters.get(i).getCluster().size();
			if (mSegs[i] < nclusterVertices) {
				decodedMSegs[i] = clusters.get(i).getCluster().get(mSegs[i]);
			} else {
				int indexLocalRoot = mSegs[i] % nclusterVertices;
				decodedMSegs[i] = clusters.get(i).getCluster().get(indexLocalRoot);
			}
		}
		return decodedMSegs;
	}

	public int[] getCayleyString(int[] segmentGenes, int n_vertices) {
		int[] cayleyString = new int[n_vertices - 2];
		ArrayList<Integer> validGens = new ArrayList<>();
		ArrayList<Integer> additionGens = new ArrayList<>();
		for (int i = 0; i < segmentGenes.length; i++) {
			if (segmentGenes[i] < n_vertices) {
				validGens.add(segmentGenes[i]);
			} else {
				additionGens.add(segmentGenes[i] % n_vertices);
			}
		}
		validGens.addAll(additionGens);
		for (int i = 0; i < n_vertices - 2; i++) {
			cayleyString[i] = validGens.get(i);
		}
		return cayleyString;
	}

	public int[][] decodeCayley(String CLCode, int[] cayLeyString, int nVertices) {

		int[][] tree = new int[nVertices][nVertices];
		if (nVertices == 1) {
			// do nothing
		} else if (nVertices == 2) {
			tree[0][1] = 1;
			tree[1][0] = 1;
		} else {
			switch (CLCode) {
			case "Blob":
				BlobCode blobCode = new BlobCode();
				tree = blobCode.decode(cayLeyString);
				break;
			case "Dandel":
				DandelionCode dandelCode = new DandelionCode();
				tree = dandelCode.decode(cayLeyString);
				break;
			case "Prufer":
				PruferCode pruferCode = new PruferCode();
				tree = pruferCode.decode(cayLeyString);
				break;
			}
		}
		return tree;
	}

	public int[][] decodingCayleyTest(int[] Chromosome, int[] sgmOffsets, ArrayList<Cluster> clusters, int n_vertices,
			String CLCode, Random rnd) {
		int numberOfCluster = clusters.size();
		int maxNumberOfCluster = sgmOffsets.length - 2;

		int[][] tree = new int[n_vertices][n_vertices];
		int[] indexCluster = new int[numberOfCluster];
		int count1 = 0;
		int count4 = 0;
		int[] presentationClusterPrufer = new int[numberOfCluster - 2];
		int[] auxilityArray1 = new int[numberOfCluster - 2];
		for (int i = 0; i < numberOfCluster; i++) {
			// lay cho cluster dai dien
			if (i < numberOfCluster - 2) {
				if (Chromosome[sgmOffsets[maxNumberOfCluster - 1] + i] < numberOfCluster) {
					presentationClusterPrufer[count1] = Chromosome[sgmOffsets[maxNumberOfCluster - 1] + i];
					count1++;
				} else {
					auxilityArray1[count4] = Chromosome[sgmOffsets[maxNumberOfCluster - 1] + i] % numberOfCluster;
					count4++;
				}
			}
			int numberClusterVertices = clusters.get(i).getCluster().size();
			int[][] clusterTree = new int[numberClusterVertices][numberClusterVertices];
			// check whether number elements of each cluster greater 2 or not?
			if (numberClusterVertices == 1) {
				// do nothing
			} else if (numberClusterVertices == 2) {
				clusterTree[0][1] = 1;
				clusterTree[1][0] = 1;

			} else {

				int[] pruferNumberCodeCluster = new int[numberClusterVertices - 2];
				int[] auxilityArray = new int[numberClusterVertices - 2];
				// get Cayley code for each cluster
				int count = 0;
				int count2 = 0;
				for (int j = 0; j < numberClusterVertices - 2; j++) {
					if (i == 0) {

						if (Chromosome[j] < numberClusterVertices) {
							pruferNumberCodeCluster[count] = Chromosome[j];
							count++;
						} else {
							auxilityArray[count2] = Chromosome[j] % numberClusterVertices;
							count2++;
						}

					} else {
						if (Chromosome[sgmOffsets[i - 1] + j] < numberClusterVertices) {
							pruferNumberCodeCluster[count] = Chromosome[sgmOffsets[i - 1] + j];
							count++;
						} else {
							auxilityArray[count2] = Chromosome[sgmOffsets[i - 1] + j] % numberClusterVertices;
							count2++;
						}
					}
				}
				int temp1 = (numberClusterVertices - count - 2);
				for (int k = 0; k < temp1; k++) {
					pruferNumberCodeCluster[k + count] = auxilityArray[k];
				}
				// build tree from prufer number code
				switch (CLCode) {
				case "Blob":
					BlobCode blobCode = new BlobCode();
					clusterTree = blobCode.decode(pruferNumberCodeCluster);
					break;
				case "Dandel":
					DandelionCode dandelCode = new DandelionCode();
					clusterTree = dandelCode.decode(pruferNumberCodeCluster);
					break;
				case "Prufer":
					PruferCode pruferCode = new PruferCode();
					clusterTree = pruferCode.decode(pruferNumberCodeCluster);
					break;

				}

			}
			for (int j = 0; j < numberClusterVertices; j++) {
				for (int k = 0; k < numberClusterVertices; k++) {
					tree[clusters.get(i).getCluster().get(j)][clusters.get(i).getCluster().get(k)] = clusterTree[j][k];
				}
			}
			int position = Chromosome[sgmOffsets[maxNumberOfCluster - 1] + maxNumberOfCluster - 2 + i];
			if (position >= numberClusterVertices) {
				position = position % numberClusterVertices;
			}
			indexCluster[i] = clusters.get(i).getCluster().get(position);

		}
		int temp2 = (numberOfCluster - count1 - 2);
		for (int k = 0; k < temp2; k++) {
			// int temp = rnd.nextInt(numberOfCluster);
			presentationClusterPrufer[k + count1] = auxilityArray1[k];
		}
		int[][] spanningTreeOfCluster = new int[numberOfCluster][numberOfCluster];
		switch (CLCode) {
		case "Blob":
			BlobCode blobCode = new BlobCode();
			spanningTreeOfCluster = blobCode.decode(presentationClusterPrufer);
			break;
		case "Dandel":
			DandelionCode dandelCode = new DandelionCode();
			spanningTreeOfCluster = dandelCode.decode(presentationClusterPrufer);
			break;
		case "Prufer":
			PruferCode pruferCode = new PruferCode();
			spanningTreeOfCluster = pruferCode.decode(presentationClusterPrufer);
			break;

		}
		for (int i = 0; i < numberOfCluster; i++) {
			for (int j = 0; j < numberOfCluster; j++) {
				tree[indexCluster[i]][indexCluster[j]] = spanningTreeOfCluster[i][j];
			}
		}
		return tree;
	}

	public ArrayList<int[][]> decodingTwoTree(int[][] maxTree, ArrayList<Cluster> clusters1,
			ArrayList<Cluster> clusters2, ArrayList<Cluster> maxClusters, int num_vertices1, int num_vertices2,
			Random r) {
		ArrayList<int[][]> list = new ArrayList<int[][]>();
		InitializationPopulation initPop = new InitializationPopulation();
		int[][] weightTree1 = new int[num_vertices1][num_vertices1];
		int[][] weightTree2 = new int[num_vertices2][num_vertices2];
		int[][] TreeInst1 = new int[num_vertices1][num_vertices1];
		int[][] TreeInst2 = new int[num_vertices2][num_vertices2];
		int numberOfcluster1 = clusters1.size();
		int numberOfcluster2 = clusters2.size();

		for (int i = 0; i < numberOfcluster1; i++) {
			int[][] TreeOfEachCluster = initPop.buildClusterWeightMatrix(maxTree, maxClusters.get(i).getCluster());

			// copy the tree in Each cluster in for Tree in Instance1.
			for (int j = 0; j < clusters1.get(i).getCluster().size(); j++) {
				for (int k = 0; k < clusters1.get(i).getCluster().size(); k++) {
					if (TreeOfEachCluster[j][k] > 0) {
						TreeInst1[clusters1.get(i).getCluster().get(j)][clusters1.get(i).getCluster()
								.get(k)] = TreeInst1[clusters1.get(i).getCluster().get(k)][clusters1.get(i).getCluster()
										.get(j)] = 1;
					}
				}
			}
		}

		weightTree1 = initPop.findSpanningTreeBetweenClusters(maxTree, num_vertices1, maxClusters, clusters1);
		for (int j = 0; j < num_vertices1; j++) {
			for (int k = 0; k < num_vertices1; k++) {
				if (weightTree1[j][k] > 0) {
					TreeInst1[j][k] = weightTree1[j][k];
				}
			}
		}
		list.add(TreeInst1);

		for (int i = 0; i < numberOfcluster2; i++) {
			int[][] TreeOfEachCluster = initPop.buildClusterWeightMatrix(maxTree, maxClusters.get(i).getCluster());
			// copy the tree in Each cluster for Tree in Instance2.
			for (int j = 0; j < clusters2.get(i).getCluster().size(); j++) {
				for (int k = 0; k < clusters2.get(i).getCluster().size(); k++) {
					if (TreeOfEachCluster[j][k] > 0) {
						TreeInst2[clusters2.get(i).getCluster().get(j)][clusters2.get(i).getCluster()
								.get(k)] = TreeInst2[clusters2.get(i).getCluster().get(k)][clusters2.get(i).getCluster()
										.get(j)] = 1;
					}
				}
			}
		}

		weightTree2 = initPop.findSpanningTreeBetweenClusters(maxTree, num_vertices2, maxClusters, clusters2);
		for (int j = 0; j < num_vertices2; j++) {
			for (int k = 0; k < num_vertices2; k++) {
				if (weightTree2[j][k] > 0) {
					TreeInst2[j][k] = weightTree2[j][k];
				}
			}
		}
		list.add(TreeInst2);

		return list;
	}

}
