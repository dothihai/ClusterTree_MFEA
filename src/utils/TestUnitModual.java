package utils;

import java.util.ArrayList;
import java.util.Random;

import fileinout.ClusptReadData;
import objects.ClusptInstance;
import objects.ClusptUnifiedInstance;
import objects.Cluster;
import operators.Crossovers;
import operators.Decodings;
import operators.Encodings;
import operators.Evaluation;
import operators.InitializationPopulation;
import operators.Mutations;
import utils.PruferCode;
import visualizations.*;
import utils.BlobCode;
import utils.DandelionCode;

public class TestUnitModual {

	public static void main(String[] args) {
		Random rnd = new Random();
		rnd.setSeed(10);
		TestUnitModual testUnit = new TestUnitModual();
		// ClusptInstance ClusptIns = new ClusptInstance();
		// String clusptfiles = "dataSet/5eil76.clt";
		// ClusptReadData clusptReadData = new ClusptReadData();
		// ClusptIns = clusptReadData.readData(clusptfiles);
		//
		// testUnit.testInitPopulation(ClusptIns.clusters, 100, rnd);
		// testUnit.testVisualize(ClusptIns, new
		// int[ClusptIns.n_vertices][ClusptIns.nClusters]);
		// testUnit.testPrufecode();
		// testUnit.testBlobCode();
		// testUnit.testĐandelion();
		// testUnit.testCalEvaluation();
//		testUnit.testClusterEvaluation();
		testUnit.testPipeLineEdge(rnd);
		// testUnit.testFindCycle();

		System.out.println("done");
	}

	public void testVisualize(ClusptInstance ClusptIns, int[][] solution) {
		Windows windows = new Windows();
		windows.runWindow("test_visualization");
		ClusptVisualize clusptVis = new ClusptVisualize();
		clusptVis.setPaint(solution, ClusptIns.vertices, ClusptIns.clusters, ClusptIns.nVertices, 0, 0, 0,
				ClusptIns.root, 10);
		windows.addClusptVis(clusptVis);
	}

	public void testPrufecode() {
		ClusptInstance ClusptIns = new ClusptInstance();
		String clusptfiles = "dataForTest/testPrufer.txt";
		ClusptReadData clusptReadData = new ClusptReadData();
		ClusptIns = clusptReadData.readData(clusptfiles);
		// int[] pruferString = { 16, 4, 6, 2, 12, 0, 7, 0, 19, 3, 5, 17, 3, 16,
		// 6, 12, 11, 9 };
		int[] pruferString = { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
		PruferCode pruferCode = new PruferCode();
		int[][] tree = pruferCode.decode(pruferString);
		testVisualize(ClusptIns, tree);
		System.out.println("done");

	}

	public void testBlobCode() {
		ClusptInstance ClusptIns = new ClusptInstance();
		String clusptfiles = "dataForTest/testPrufer.txt";
		ClusptReadData clusptReadData = new ClusptReadData();
		ClusptIns = clusptReadData.readData(clusptfiles);
		int[] pruferString = { 16, 4, 6, 2, 12, 0, 7, 0, 19, 3, 5, 17, 3, 16, 6, 12, 11, 9 };
		// int[] pruferString = { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
		// 8, 8, 7 };
		BlobCode blobCode = new BlobCode();
		int[][] tree = blobCode.decode(pruferString);
		testVisualize(ClusptIns, tree);
		System.out.println("blobCode");

	}

	public void testĐandelion() {
		ClusptInstance ClusptIns = loadFileTest();
		// int[] pruferString = { 16, 4, 6, 2, 12, 0, 7, 0, 19, 3, 5, 17, 3, 16,
		// 6, 12, 11, 9 };
		int[] pruferString = { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7 };
		DandelionCode dandelionCode = new DandelionCode();
		int[][] tree = dandelionCode.decode(pruferString);
		testVisualize(ClusptIns, tree);
		System.out.println("dandelionCode");

	}

	public ClusptInstance loadFileTest() {
		ClusptInstance ClusptIns = new ClusptInstance();
		String clusptfiles = "dataForTest/testPrufer.txt";
		ClusptReadData clusptReadData = new ClusptReadData();
		ClusptIns = clusptReadData.readData(clusptfiles);
		return ClusptIns;
	}

	public void testCalEvaluation() {
		Evaluation eval = new Evaluation();
		ClusptInstance ClusptIns = loadFileTest();
		int[] pruferString = { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7 };
		DandelionCode dandelionCode = new DandelionCode();
		int[][] tree = dandelionCode.decode(pruferString);
		eval.calClusptCost(tree, ClusptIns.weightMatrix, ClusptIns.nVertices, ClusptIns.root);

	}

	public void testInitPopulation(int[] clusters, int popLength, Random rnd) {
		InitializationPopulation initPop = new InitializationPopulation();
		initPop.initPopulation(clusters, popLength, 2,  rnd);
		System.out.println("Pass");
	}

	public void testPipeLine(Random rnd) {
		InitializationPopulation initPop = new InitializationPopulation();
		Encodings encode = new Encodings();
		Decodings decode = new Decodings();
		Mutations mutations = new Mutations();
		Crossovers crossovers = new Crossovers();
		ClusptInstance ClusptIns = new ClusptInstance();
		ClusptInstance ClusptIns1 = new ClusptInstance();
		String clusptfiles = "dataSet/5eil76.clt";
		ClusptReadData clusptReadData = new ClusptReadData();

		ClusptIns = clusptReadData.readData(clusptfiles);
		ClusptIns1 = clusptReadData.readData("dataSet/10eil76.clt");

		int[] maxClustersLength = encode.getMaxClustersLength(ClusptIns.clusters, ClusptIns1.clusters);
		int[] offSegs = encode.getSegOffsets(maxClustersLength);

		Windows windows = new Windows();
		windows.runWindow("test_visualization");
		for (int i = 0; i < 9000; i++) {
			int[] chromosome = initPop.initClusptCayleyChromosome(maxClustersLength, rnd);
			int[] chromosome1 = initPop.initClusptCayleyChromosome(maxClustersLength, rnd);
			int genLength = chromosome.length;
			ArrayList<int[]> offsprings = crossovers.onePointCrossover(chromosome, chromosome1, genLength, rnd);

			int[] newChromsome = mutations.swapChangeMutation(ClusptIns.clusters, chromosome, offSegs, 1, rnd);
			int[][] tree = decode.decodeTree(offsprings.get(0), offSegs, ClusptIns1.nVertices, ClusptIns1.clusters,
					"Blob", rnd);
			ClusptVisualize clusptVis = new ClusptVisualize();
			clusptVis.setPaint(tree, ClusptIns1.vertices, ClusptIns1.clusters, ClusptIns1.nVertices, ClusptIns1.root, 0,
					0, 0, 10);
			windows.addClusptVis(clusptVis);
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("done");
	}

	public void testFindCycle() {
		GraphMethods graphMethods = new GraphMethods();
		int[][] graph = { { 0, 1, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 1, 1, 0 }, { 0, 0, 1, 0, 1, 1 },
				{ 0, 0, 1, 1, 0, 1 }, { 0, 0, 0, 1, 1, 0 } };
		ArrayList<Integer> path = graphMethods.findCycleDFS(graph, 6, 0);
		ArrayList<Integer> path1 = graphMethods.findPathBetweenTwoVerticesDFS(0, 4, graph, 6);
		System.out.println(path);
		System.out.println(path1);

	}
	public void testClusterEvaluation(){
		Evaluation eval = new Evaluation();
		int[][] graph = { { 0, 1, 1, 1, 0, 0 },
						  { 1, 0, 0, 0, 1, 1 }, 
						  { 1, 0, 0, 0, 0, 0 }, 
						  { 1, 0, 0, 0, 0, 0 },
						  { 0, 1, 0, 0, 0, 0 }, 
						  { 0, 1, 0, 0, 0, 0 } };
		double[][] weightMatrix = { { 0.0, 1.0, 2.0, 3.0, 0.0, 0.0 },
								 	{ 1.0, 0.0, 0.0, 0.0, 4.0, 5.0 }, 
								 	{ 2.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, 
								 	{ 3.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
								 	{ 0.0, 4.0, 0.0, 0.0, 0.0, 0.0 }, 
								 	{ 0.0, 5.0, 0.0, 0.0, 0.0, 0.0 } };
		double cost  = eval.calClusterTreeCost(graph, weightMatrix, 6, 2);
		System.out.println(cost);
	}

	public void testPipeLineEdge(Random rnd) {
		InitializationPopulation initPop = new InitializationPopulation();
		Encodings encode = new Encodings();
		Decodings decode = new Decodings();
		Mutations mutations = new Mutations();
		Crossovers crossovers = new Crossovers();
		ClusptInstance ClusptIns = new ClusptInstance();
		ClusptInstance ClusptIns1 = new ClusptInstance();

		String clusptfiles = "dataSet/5eil76.clt";
		ClusptReadData clusptReadData = new ClusptReadData();

		ClusptIns = clusptReadData.readData(clusptfiles);
		ClusptIns1 = clusptReadData.readData("dataSet/10eil76.clt");
//		 ClusptIns1 = clusptReadData.readData(clusptfiles);
		visualizeTest.ClusptIns = ClusptIns;
		visualizeTest.ClusptIns1 = ClusptIns1;
		ClusptUnifiedInstance clInst = new ClusptUnifiedInstance(ClusptIns.clusters, ClusptIns1.clusters);
		visualizeTest.clInst = clInst;
		Windows windows = new Windows();
		windows.runWindow("test_visualization");
		
		for (int i = 0; i < 9; i++) {
			int[][] father = initPop.initClusptEdgeChromosome(clInst.clusters, clInst.minClustersLength,
					clInst.maxClustersLength, clInst.nVertices, clInst.minnClusters, clInst.maxnClusters, rnd);
			int[][] mother = initPop.initClusptEdgeChromosome(clInst.clusters, clInst.minClustersLength,
					clInst.maxClustersLength, clInst.nVertices, clInst.minnClusters, clInst.maxnClusters, rnd);
			ArrayList<int[][]> offsprings = crossovers.edgeCrossover1(father, mother, clInst.nVertices, clInst.clusters,
					ClusptIns.nClusters, ClusptIns1.nClusters, clInst.minClustersLength, clInst.maxClustersLength, rnd);

			ArrayList<int[][]> solutions = decode.decodingTwoTree(offsprings.get(0), ClusptIns.clusters,
					ClusptIns1.clusters, clInst.clusters, ClusptIns.nVertices, ClusptIns1.nVertices, rnd);

			ClusptVisualize clusptVis = new ClusptVisualize();
			clusptVis.setPaint(solutions.get(1), ClusptIns1.vertices, ClusptIns1.clusters, ClusptIns1.nVertices,
					ClusptIns1.root, 0, 0, 0, 10);
			windows.addClusptVis(clusptVis);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("done");
	}

}
