package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fileinout.ClusptReadData;
import fileinout.LoadConfig;
import objects.ClusptEdgesUnifiedInd;
import objects.ClusptInstance;
import objects.ClusptUnifiedInstance;
import objects.Cluster;
import objects.Parameters;
import objects.Results;
import operators.Crossovers;
import operators.Decodings;
import operators.Encodings;
import operators.Evaluation;
import operators.InitializationPopulation;
import operators.Mutations;
import utils.ClusptComparator;
import utils.ObjComparator;
import utils.Utils;
import visualizations.ClusptVisualize;
import visualizations.Windows;

public class EdgeMFEA {
	public Crossovers crossovers = new Crossovers();
	public Mutations mutations = new Mutations();
	public Decodings decodings = new Decodings();
	public Encodings encodings = new Encodings();
	public Evaluation evaluation = new Evaluation();
	public InitializationPopulation initPop = new InitializationPopulation();

	public static void main(String[] args) {
		Parameters parameters = LoadConfig.loadFromFile("./config/config.json");
		Results results = new Results();
		Results results1 = new Results();
		EdgeMFEA edgeMFEA = new EdgeMFEA();
		Random rnd = new Random();

		for (int i = 0; i < parameters.fileNames.length; i += 2) {
			String fileName = "test/" + parameters.fileNames[i] + ".clt";
			String fileName1 = "test/" + parameters.fileNames[i + 1] + ".clt";
			results.name = parameters.fileNames[i];
			results1.name = parameters.fileNames[i + 1];
			results.dirName = "Results/" + parameters.fileNames[i] + "_and_" + parameters.fileNames[i + 1];
			results1.dirName = "Results/" + parameters.fileNames[i] + "_and_" + parameters.fileNames[i + 1];
			edgeMFEA.runExperiment(fileName, fileName1, parameters, results, results1, rnd);
		}

	}

	public void runExperiment(String clusptfiles, String clusptfiles1, Parameters parameters, Results results,
			Results results1, Random rnd) {
		ClusptInstance clusptIns = new ClusptInstance();
		ClusptInstance clusptIns1 = new ClusptInstance();
		ClusptReadData clusptReadData = new ClusptReadData();
		clusptIns = clusptReadData.readData(clusptfiles);
		clusptIns1 = clusptReadData.readData(clusptfiles1);
		Collections.sort(clusptIns.clusters, ObjComparator.compareByNumberOfCluster);
		Collections.sort(clusptIns1.clusters, ObjComparator.compareByNumberOfCluster);
		ClusptUnifiedInstance clInst = new ClusptUnifiedInstance(clusptIns.clusters, clusptIns1.clusters);
		for (int i = 0; i < parameters.nRuns; i++) {
			System.out.println(
					"------------------------------------------------------------------------------------------------------");
			rnd.setSeed(i);
			results.seed = i;
			results1.seed = i;
			mfea(parameters.popSize, parameters.generations, parameters.rmp, parameters.muRate, parameters.nTasks,
					clusptIns, clusptIns1, clInst, results, results1, rnd);
		}
	}

	public void mfea(int popSize, int generations, double rmp, double muRate, int nTasks, ClusptInstance clusptIns,
			ClusptInstance clusptIns1, ClusptUnifiedInstance clInst, Results results, Results results1, Random rnd) {

		double[][] bestEachGenerations = new double[generations][nTasks];
		Utils utils = new Utils();
		long start = System.currentTimeMillis();

		ArrayList<ClusptEdgesUnifiedInd> curPop = new ArrayList<>();

		curPop = initPop.initPopulation(popSize, clInst.clusters, clInst.minClustersLength, clInst.maxClustersLength,
				clInst.nVertices, clInst.minnClusters, clInst.maxnClusters, nTasks, rnd);
		curPop = updateFactorialCost(curPop, clusptIns, clusptIns1, clInst, rnd);
		curPop = updateFactorialRank(curPop);
		curPop = updateSkillFactor(curPop);
		Collections.sort(curPop, ClusptComparator.compareByScalarFitness);

		ArrayList<ClusptEdgesUnifiedInd> bestIndividuals = getBestIndividual(curPop, nTasks);
		double[] bestCosts = getBestCosts(bestIndividuals, nTasks);

		Windows windows = new Windows();
		windows.runWindow("Cluster Minimum Routing Cost solution: task 1");

		for (int i = 0; i < generations; i++) {
			ArrayList<ClusptEdgesUnifiedInd> offspringPop = new ArrayList<>();
			ArrayList<ClusptEdgesUnifiedInd> intermiatePop = new ArrayList<>();
			offspringPop = applyGeneticOperators(curPop, popSize, clInst.nVertices, clInst.clusters,
					clusptIns.nClusters, clusptIns1.nClusters, clInst.minClustersLength, clInst.maxClustersLength, rmp,
					muRate, rnd);
			offspringPop = updateFactorialCost(offspringPop, clusptIns, clusptIns1, clInst, rnd);
			intermiatePop.addAll(offspringPop);
			intermiatePop.addAll(curPop);

			intermiatePop = updateFactorialRank(intermiatePop);
			intermiatePop = updateSkillFactor(intermiatePop);

			Collections.sort(intermiatePop, ClusptComparator.compareByScalarFitness);
			for (int j = 0; j < popSize; j++) {
				curPop.set(j, intermiatePop.get(j));
			}
			bestIndividuals = getBestIndividual(curPop, nTasks);
			double[] newbestCosts = getBestCosts(bestIndividuals, nTasks);
			bestCosts = updateBestCosts(bestCosts, newbestCosts, nTasks);
			bestEachGenerations[i] = bestCosts;

			ArrayList<int[][]> solutions = decodings.decodingTwoTree(bestIndividuals.get(1).getChromosome(),
					clusptIns.clusters, clusptIns1.clusters, clInst.clusters, clusptIns.nVertices, clusptIns1.nVertices,
					rnd);
			ClusptVisualize clusptVis = new ClusptVisualize();
			clusptVis.setPaint(solutions.get(0), clusptIns.vertices, clusptIns.clusters, clusptIns.nVertices,
					clusptIns.root, bestCosts[0], 0, 0, 10);
			windows.addClusptVis(clusptVis);

		}

		long end = System.currentTimeMillis();
		results.time = utils.getTimeFromMillis(end - start);
		results1.time = results.time;
		results.bestCost = bestCosts[0];
		results1.bestCost = bestCosts[1];
		results.bestCostInEachGeneration = bestEachGenerations;
		System.out.println("|" + results.seed + "\t|" + results.name + "\t|" + results1.name + "\t|" + results.bestCost
				+ "\t|" + results1.bestCost + "\t|" + results.time + "|");
		results.storeBestSolution();
		results1.storeBestSolution();
		results.bestCostInEachGeneration(results, results1);

	}

	public ArrayList<ClusptEdgesUnifiedInd> updateSkillFactor(ArrayList<ClusptEdgesUnifiedInd> pop) {
		for (ClusptEdgesUnifiedInd indiv : pop) {
			if (indiv.getFactorialRank()[0] <= indiv.getFactorialRank()[1]) {
				indiv.setSkillFactor(0);
				indiv.setScalarFitness(1.0 / (indiv.getFactorialRank()[0]) + 1);
			} else {
				indiv.setSkillFactor(1);
				indiv.setScalarFitness(1.0 / (indiv.getFactorialRank()[1]) + 1);
			}
		}
		return pop;
	}

	public ArrayList<ClusptEdgesUnifiedInd> updateFactorialRank(ArrayList<ClusptEdgesUnifiedInd> pop) {
		int popSize = pop.size();
		sortPopulationByTask(pop, 0);
		for (int i = 0; i < popSize; i++) {
			pop.get(i).setFactorialRank(i + 1, 0);
		}
		sortPopulationByTask(pop, 1);
		for (int i = 0; i < popSize; i++) {
			pop.get(i).setFactorialRank(i + 1, 1);
		}
		return pop;
	}

	public ArrayList<ClusptEdgesUnifiedInd> sortPopulationByTask(ArrayList<ClusptEdgesUnifiedInd> pop, int task) {
		ClusptComparator clusptComparator = new ClusptComparator(task);
		Collections.sort(pop, clusptComparator);
		return pop;

	}

	public ArrayList<ClusptEdgesUnifiedInd> updateFactorialCost(ArrayList<ClusptEdgesUnifiedInd> pop,
			ClusptInstance clusptIns, ClusptInstance clusptIns2, ClusptUnifiedInstance clInst, Random rnd) {
		int nTasks = 2;
		for (ClusptEdgesUnifiedInd indiv : pop) {
			double[] factorialCost = new double[nTasks];
			ArrayList<int[][]> solutions = decodings.decodingTwoTree(indiv.getChromosome(), clusptIns.clusters,
					clusptIns2.clusters, clInst.clusters, clusptIns.nVertices, clusptIns2.nVertices, rnd);
			if (indiv.getSkillFactor() == 0) {
				factorialCost[0] = evaluation.calClusterTreeCost(solutions.get(0), clusptIns.weightMatrix,
						clusptIns.nVertices, clusptIns.root);
				factorialCost[1] = Double.MAX_VALUE;
			} else if (indiv.getSkillFactor() == 1) {
				factorialCost[0] = Double.MAX_VALUE;
				factorialCost[1] = evaluation.calClusterTreeCost(solutions.get(1), clusptIns2.weightMatrix,
						clusptIns2.nVertices, clusptIns2.root);
			} else {
				factorialCost[0] = evaluation.calClusterTreeCost(solutions.get(0), clusptIns.weightMatrix,
						clusptIns.nVertices, clusptIns.root);
				factorialCost[1] = evaluation.calClusterTreeCost(solutions.get(1), clusptIns2.weightMatrix,
						clusptIns2.nVertices, clusptIns2.root);
			}

			indiv.setFactorialCost(factorialCost);
		}
		return pop;
	}

	public ArrayList<ClusptEdgesUnifiedInd> applyGeneticOperators(ArrayList<ClusptEdgesUnifiedInd> pop, int popSize,
			int maxnVertices, ArrayList<Cluster> maxClusters, int nClusters1, int nClusters2, int[] minClustersLength,
			int[] maxClustersLength, double rmp, double muRate, Random rnd) {

		ArrayList<ClusptEdgesUnifiedInd> offsprings = new ArrayList<>();
		while (offsprings.size() < popSize) {
			int pos1 = rnd.nextInt(popSize);
			int pos2 = rnd.nextInt(popSize);
			while (pos1 == pos2) {
				pos2 = rnd.nextInt(popSize);
			}
			ClusptEdgesUnifiedInd par1 = pop.get(pos1);
			ClusptEdgesUnifiedInd par2 = pop.get(pos2);
			double r = rnd.nextDouble();
			if (par1.getSkillFactor() == par2.getSkillFactor() || r < rmp) {

				ArrayList<int[][]> childrenChromo = crossovers.edgeCrossover(par1.getChromosome(), par2.getChromosome(),
						maxnVertices, maxClusters, nClusters1, nClusters2, minClustersLength, maxClustersLength, rnd);

				ClusptEdgesUnifiedInd child = new ClusptEdgesUnifiedInd(2);
				ClusptEdgesUnifiedInd child1 = new ClusptEdgesUnifiedInd(2);
				child.setChromosome(childrenChromo.get(0));
				child1.setChromosome(childrenChromo.get(1));
				if (rnd.nextDouble() < 0.5) {
					child.setSkillFactor(par1.getSkillFactor());
				} else {
					child.setSkillFactor(par2.getSkillFactor());
				}
				if (rnd.nextDouble() < 0.5) {
					child1.setSkillFactor(par1.getSkillFactor());
				} else {
					child1.setSkillFactor(par2.getSkillFactor());
				}

				offsprings.add(child);
				offsprings.add(child1);

			} else {
				par1.setChromosome(mutations.mutationClusterTree(par1.getChromosome(), maxnVertices, maxClusters,
						minClustersLength, maxClustersLength, nClusters1, nClusters2, muRate, rnd));
				par2.setChromosome(mutations.mutationClusterTree(par2.getChromosome(), maxnVertices, maxClusters,
						minClustersLength, maxClustersLength, nClusters1, nClusters2, muRate, rnd));
				offsprings.add(par1);
				offsprings.add(par2);
			}
		}
		return offsprings;
	}

	public ArrayList<ClusptEdgesUnifiedInd> getBestIndividual(ArrayList<ClusptEdgesUnifiedInd> sortedPop, int nTasks) {
		ArrayList<ClusptEdgesUnifiedInd> bestIndividual = new ArrayList<>();
		for (int i = 0; i < nTasks; i++) {
			int firstElement = 0;
			while (sortedPop.get(firstElement).getSkillFactor() != i) {
				firstElement += 1;
			}
			bestIndividual.add(sortedPop.get(firstElement));
		}
		return bestIndividual;
	}

	public double[] getBestCosts(ArrayList<ClusptEdgesUnifiedInd> bestIndividuals, int nTasks) {
		double[] bestCosts = new double[nTasks];
		for (int i = 0; i < nTasks; i++) {
			int skillFactor = bestIndividuals.get(i).getSkillFactor();
			bestCosts[skillFactor] = bestIndividuals.get(i).getFactorialCost()[skillFactor];
		}
		return bestCosts;

	}

	public double[] updateBestCosts(double[] oldbestCosts, double[] newbestCosts, int nTasks) {
		for (int i = 0; i < nTasks; i++) {
			if (oldbestCosts[i] < newbestCosts[i]) {
				newbestCosts[i] = oldbestCosts[i];
			}
		}
		return newbestCosts;
	}

	public void printPop(ArrayList<ClusptEdgesUnifiedInd> pop) {
		System.out.println("batdau");
		for (ClusptEdgesUnifiedInd indiv : pop) {
			System.out.println( indiv.getFactorialRank()[0]);

		}
		System.out.println("---------------------------------");
	}

}
