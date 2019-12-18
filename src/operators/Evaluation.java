package operators;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Evaluation {
	public double calClusptCost(int[][] tree, double[][] weightMatrix, int n_vertices, int root) {
		double[] distanceFromRoot = new double[n_vertices];
		double clusptCost = 0;
		distanceFromRoot[root] = 0;
		boolean[] isVisited = new boolean[n_vertices];
		Queue<Integer> queue = new LinkedList<>();
		queue.add(root);

		while (!queue.isEmpty()) {
			int parentVertex = queue.poll();
			isVisited[parentVertex] = true;
			for (int i = 0; i < n_vertices; i++) {
				if (tree[parentVertex][i] > 0 && !isVisited[i]) {
					queue.add(i);
					isVisited[i] = true;
					distanceFromRoot[i] = distanceFromRoot[parentVertex] + weightMatrix[parentVertex][i];
					clusptCost += distanceFromRoot[i];

				}
			}
		}
		return clusptCost;
	};

	public double calClusterTreeCost(int[][] tree, double[][] weightMatrix, int nVertices, int root) {
		double clusterTreeCost = 0;
		boolean[] isVisited = new boolean[nVertices];
		int[] preVertex = new int[nVertices];
		int[] weightVertex = new int[nVertices];
		preVertex[root] = -1;
		Queue<Integer> queue = new LinkedList<>();
		Stack<Integer> stack = new Stack<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			int parentVertex = queue.poll();
			isVisited[parentVertex] = true;
			for (int i = 0; i < nVertices; i++) {
				if (tree[parentVertex][i] > 0 && !isVisited[i]) {
					queue.add(i);
					stack.push(i);
					isVisited[i] = true;
					preVertex[i] = parentVertex;
				}
			}
		}
		while (!stack.isEmpty()) {
			int acentdentVertex = stack.pop();
			weightVertex[acentdentVertex] += 1;
			int parentVertex = preVertex[acentdentVertex];
			weightVertex[parentVertex] += weightVertex[acentdentVertex];
			clusterTreeCost += weightVertex[acentdentVertex] * (nVertices - weightVertex[acentdentVertex])
					* weightMatrix[acentdentVertex][parentVertex];
		}
		return clusterTreeCost;
	}

}
