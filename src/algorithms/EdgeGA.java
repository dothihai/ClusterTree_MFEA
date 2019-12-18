package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fileinout.ClusptReadData;
import fileinout.LoadConfig;
import objects.ClusptEdgesUnifiedInd;
import objects.ClusptInstance;
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

public class EdgeGA {
	public Crossovers crossovers = new Crossovers();
	public Mutations mutations = new Mutations();
	public Decodings decodings = new Decodings();
	public Encodings encodings = new Encodings();
	public Evaluation evaluation = new Evaluation();
	public InitializationPopulation initPop = new InitializationPopulation();

	public static void main(String[] args) {
		Parameters parameters = LoadConfig.loadFromFile("config.json");
		Results results = new Results();
		EdgeGA ga = new EdgeGA();
		Random rnd = new Random();

		for (int i = 0; i < parameters.fileNames.length; i++) {
			String fileName = "test/" + parameters.fileNames[i] + ".clt";
			results.name = parameters.fileNames[i];
			results.dirName = "Results/" + parameters.fileNames[i];
			ga.runExperiment(fileName, parameters, results, rnd);
		}

	}

	public void runExperiment(String clusptfiles, Parameters parameters, Results results, Random rnd) {
		ClusptInstance clusptIns = new ClusptInstance();
		ClusptReadData clusptReadData = new ClusptReadData();
		clusptIns = clusptReadData.readData(clusptfiles);
		Collections.sort(clusptIns.clusters, ObjComparator.compareByNumberOfCluster);
		for (int i = 0; i < parameters.nRuns; i++) {
			System.out.println(
					"------------------------------------------------------------------------------------------------------");
			rnd.setSeed(i);
			results.seed = i;
			ga(parameters.popSize, parameters.generations, parameters.rmp, parameters.muRate, clusptIns, results,
					parameters.nTasks, rnd);
		}
	}

	public void ga(int popSize, int generations, double rmp, double muRate, ClusptInstance clusptIns, Results results,
			int nTasks, Random rnd) {

		double[][] bestEachGenerations = new double[generations][nTasks];
		Utils utils = new Utils();
		long start = System.currentTimeMillis();

		ArrayList<ClusptEdgesUnifiedInd> curPop = new ArrayList<>();

		curPop = initPop.initPopulation(popSize, clusptIns.clusters, clusptIns.nVertices, clusptIns.nClusters, nTasks,
				rnd);
		curPop = updateFactorialCost(curPop, clusptIns, rnd);
		curPop = sortPopulationByTask(curPop, 0);
		int[] clustersLength = encodings.getClustersLength(clusptIns.clusters);
		ClusptEdgesUnifiedInd bestIndividuals = curPop.get(0);
		double bestCosts = bestIndividuals.getFactorialCost()[0];

		for (int i = 0; i < generations; i++) {
			ArrayList<ClusptEdgesUnifiedInd> offspringPop = new ArrayList<>();
			ArrayList<ClusptEdgesUnifiedInd> intermiatePop = new ArrayList<>();
			offspringPop = applyGeneticOperators(curPop, popSize, clusptIns.nVertices, clusptIns.clusters,
					clusptIns.nClusters, clustersLength, rmp, muRate, rnd);
			offspringPop = updateFactorialCost(offspringPop, clusptIns, rnd);
			intermiatePop.addAll(offspringPop);
			intermiatePop.addAll(curPop);

			intermiatePop = sortPopulationByTask(intermiatePop, 0);
			for (int j = 0; j < popSize; j++) {
				curPop.set(j, intermiatePop.get(j));
			}
			bestIndividuals = intermiatePop.get(0);
			double newbestCosts = bestIndividuals.getFactorialCost()[0];
			if (newbestCosts < bestCosts) {
				bestCosts = newbestCosts;
			}
			bestEachGenerations[i][0] = bestCosts;
		}

		long end = System.currentTimeMillis();
		results.time = utils.getTimeFromMillis(end - start);
		results.bestCost = bestCosts;
		results.bestCostInEachGeneration = bestEachGenerations;

		System.out.println(
				"|" + results.seed + "\t|" + results.name + "\t|" + results.bestCost + "\t|" + results.time + "|");
		results.storeBestSolution();
		results.bestCostInEachGeneration(results);

	}

	public ArrayList<ClusptEdgesUnifiedInd> sortPopulationByTask(ArrayList<ClusptEdgesUnifiedInd> pop, int task) {
		ClusptComparator clusptComparator = new ClusptComparator(task);
		Collections.sort(pop, clusptComparator);
		return pop;

	}

	public ArrayList<ClusptEdgesUnifiedInd> updateFactorialCost(ArrayList<ClusptEdgesUnifiedInd> pop,
			ClusptInstance clusptIns, Random rnd) {
		int nTasks = 1;
		for (ClusptEdgesUnifiedInd indiv : pop) {
			double[] factorialCost = new double[nTasks];
			factorialCost[0] = evaluation.calClusterTreeCost(indiv.getChromosome(), clusptIns.weightMatrix,
					clusptIns.nVertices, clusptIns.root);

			indiv.setFactorialCost(factorialCost);
		}
		return pop;
	}

	public ArrayList<ClusptEdgesUnifiedInd> applyGeneticOperators(ArrayList<ClusptEdgesUnifiedInd> pop, int popSize,
			int nVertices, ArrayList<Cluster> clusters, int nClusters, int[] clustersLength, double rmp, double muRate,
			Random rnd) {

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
			if (r < rmp) {

				ArrayList<int[][]> childrenChromo = crossovers.gaEdgeCrossover(par1.getChromosome(),
						par2.getChromosome(), nVertices, clusters, nClusters, clustersLength, rnd);

				ClusptEdgesUnifiedInd child = new ClusptEdgesUnifiedInd(1);
				ClusptEdgesUnifiedInd child1 = new ClusptEdgesUnifiedInd(1);
				child.setChromosome(childrenChromo.get(0));
				child1.setChromosome(childrenChromo.get(1));
				offsprings.add(child);
				offsprings.add(child1);

			} else {
				par1.setChromosome(
						mutations.mutationClusterTreeGA(par1.getChromosome(), nVertices, clusters, muRate, rnd));
				par2.setChromosome(
						mutations.mutationClusterTreeGA(par2.getChromosome(), nVertices, clusters, muRate, rnd));
				offsprings.add(par1);
				offsprings.add(par2);
			}
		}
		return offsprings;
	}
}
