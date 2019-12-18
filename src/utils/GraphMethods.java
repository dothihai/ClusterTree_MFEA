package utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import objects.Edge;

public class GraphMethods {

	// nguyen duc nghia
	public int[] dijkstra(int[][] weightMatrix, int n_vertices, int startVertex) {

		int[] truoc = new int[n_vertices]; // vertex before
		double[] d = new double[n_vertices]; // the list of vertex consider
		// the weightMatrix = distances between startVertex and the others
		// vertex in consider vertex list
		ArrayList<Integer> Tree = new ArrayList<Integer>();
		int u = 0;
		double maxp = 10000000000000000f;
		// initialize label
		for (int v = 0; v < n_vertices; v++) {
			if ((weightMatrix[startVertex - 1][v] <= 0) || (weightMatrix[startVertex - 1][v]) == maxp) {
				d[v] = maxp;
			} else {
				d[v] = weightMatrix[startVertex - 1][v];
			}
			truoc[v] = startVertex - 1;
			Tree.add(v);

		}

		truoc[startVertex - 1] = -1;
		d[startVertex - 1] = 0;
		Tree.remove(startVertex - 1);
		// buocs lap
		while (Tree.size() > 0) {
			// Tim u la dinh co nhan tam thoi nho nhat
			double minp = 10000000000000000f;
			u = -1;

			for (int v : Tree) {
				if (minp > d[v]) {
					u = v;
					minp = d[v];
				}
			}
			Tree.remove(u);
			for (int v : Tree) {

				if ((d[u] == maxp) || (weightMatrix[u][v] == maxp)) {
					break;
				} else {
					if (((d[u] + weightMatrix[u][v]) < d[v]) && (weightMatrix[u][v] > 0)) {
						d[v] = d[u] + weightMatrix[u][v];
						truoc[v] = u;
					}
				}
			}
		}
		return truoc;
	}

	public int[] getVertexInEachSubGraph(int[][] weightMatrix, int n_Vertices) {
		int[] chua_Xet = new int[n_Vertices];
		int solt = 0;
		for (int i = 0; i < n_Vertices; i++) {
			chua_Xet[i] = -1;
		}
		for (int i = 0; i < n_Vertices; i++) {
			if (chua_Xet[i] == -1) {

				BFS(weightMatrix, n_Vertices, chua_Xet, solt, i);
				solt++;
			}
		}

		int[] vertexInSubGraph = new int[solt];
		for (int i = 0; i < solt; i++) {
			for (int j = 0; j < n_Vertices; j++) {
				if (chua_Xet[j] == i) {
					vertexInSubGraph[i] = j;
				}
			}
		}

		return vertexInSubGraph;
	}

	public int[] BFS(int[][] weightMatrix, int n_Vertices, int[] chua_Xet, int solt, int startVertex) {

		int[] queue = new int[n_Vertices];
		int[] truoc = new int[n_Vertices];
		int u, dauQ, cuoiQ;

		for (int i = 0; i < n_Vertices; i++) {
			truoc[i] = startVertex;
			queue[i] = -1;
		}
		truoc[startVertex] = -1;
		dauQ = 0;
		cuoiQ = 0;
		queue[cuoiQ] = startVertex;
		chua_Xet[startVertex] = solt;

		while (dauQ <= cuoiQ) {
			u = queue[dauQ];
			dauQ++;
			for (int i = 0; i < n_Vertices; i++) {

				if (weightMatrix[u][i] == Double.MAX_VALUE) {
					continue;
				} else {
					if ((weightMatrix[u][i] > 0) && (chua_Xet[i] == -1)) {
						cuoiQ++;
						queue[cuoiQ] = i;
						chua_Xet[i] = solt;
						truoc[i] = u;
					}
				}
			}
		}
		return truoc;
	}

	public ArrayList<Integer> findCycleDFS(int[][] graph, int nVertices, int startVertex) {

		Stack<Integer> stack = new Stack<Integer>();
		int[] preVertex = new int[nVertices];
		ArrayList<Integer> path = new ArrayList<Integer>();
		boolean[] isVisited = new boolean[nVertices];

		int curVertex = startVertex;
		isVisited[startVertex] = true;
		stack.push(startVertex);

		// initialize the previous vertex for each vertex in graph
		for (int k = 0; k < nVertices; k++) {
			preVertex[k] = -1;
		}
		preVertex[startVertex] = startVertex;

		// DFS to traverse tree by using stack structure.
		while (!stack.isEmpty()) {
			curVertex = stack.peek();
			int i = 0;
			while (i < nVertices) {
				if (graph[curVertex][i] > 0 && !isVisited[i]) {
					preVertex[i] = curVertex;
					stack.push(i);
					isVisited[i] = true;
					curVertex = i;
					// detect the back edge then return the path.
					path = detectBackEdge(graph, isVisited, preVertex, nVertices, curVertex);
					if (path.size() > 0) {
						return path;
					}
					i = 0;
				} else {
					i++;
				}
			}
			stack.pop();
		}
		return path;
	}

	public ArrayList<Integer> detectBackEdge(int[][] graph, boolean[] isVisited, int[] preVertex, int nVertices,
			int curVertex) {

		ArrayList<Integer> path = new ArrayList<Integer>();

		for (int j = 0; j < nVertices; j++) {
			if (graph[curVertex][j] > 0 && (isVisited[j]) && j != preVertex[curVertex]) {

				path.add(curVertex);
				while (preVertex[curVertex] != j) {
					path.add(preVertex[curVertex]);
					curVertex = preVertex[curVertex];
				}
				path.add(j);
				return path;
			}
		}
		return path;
	}

	public ArrayList<Integer> findPathBetweenTwoVerticesDFS(int startVertex, int endVertex, int[][] graph,
			int nVertices) {

		Stack<Integer> stack = new Stack<Integer>();
		int[] preVertex = new int[nVertices];
		ArrayList<Integer> path = new ArrayList<Integer>();
		boolean[] isVisited = new boolean[nVertices];

		int curVertex = startVertex;
		isVisited[startVertex] = true;
		stack.push(startVertex);
		// initialize the previous vertex for each vertex in graph
		for (int k = 0; k < nVertices; k++) {
			preVertex[k] = -1;
		}
		preVertex[startVertex] = startVertex;

		// DFS to traverse tree by using stack structure.
		while (!stack.isEmpty()) {
			curVertex = stack.peek();
			int i = 0;
			while (i < nVertices) {
				if (graph[curVertex][i] > 0 && !isVisited[i]) {
					preVertex[i] = curVertex;
					stack.push(i);
					isVisited[i] = true;
					curVertex = i;
					if (curVertex == endVertex) {
						return getPath(startVertex, endVertex, preVertex);
					}
					i = 0;
				} else {
					i++;
				}
			}
			stack.pop();
		}
		return path;
	}

	public ArrayList<Integer> getPath(int startVertex, int endVertex, int[] preVertex) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(endVertex);
		while (preVertex[endVertex] != startVertex) {
			path.add(preVertex[endVertex]);
			endVertex = preVertex[endVertex];
		}
		path.add(startVertex);
		return path;
	}

	public int[][] primRST(int[][] weightMatrix, int num_vertices, Random rnd) {
		int randomVertice = -1, indexRandomEdge = -1;
		ArrayList<Integer> contain = new ArrayList<Integer>();
		ArrayList<Edge> edgeListAdjacence = new ArrayList<Edge>();
		int[][] tempTable = new int[num_vertices][num_vertices];

		randomVertice = rnd.nextInt(num_vertices);
		contain.add(randomVertice);
		edgeListAdjacence.addAll(findEdge(num_vertices, randomVertice, contain, weightMatrix));

		while (contain.size() < num_vertices) {

			if (contain.size() == 0) {
				return null;
			}

			indexRandomEdge = rnd.nextInt(edgeListAdjacence.size());
			Edge tempEdge = new Edge(edgeListAdjacence.get(indexRandomEdge).startVertex,
					edgeListAdjacence.get(indexRandomEdge).endVertex);
			edgeListAdjacence.remove(indexRandomEdge);

			if (!contain.contains(tempEdge.endVertex)) {
				//
				tempTable[tempEdge.startVertex][tempEdge.endVertex] = 1;
				tempTable[tempEdge.endVertex][tempEdge.startVertex] = 1;
				contain.add(tempEdge.endVertex);
				edgeListAdjacence.addAll(findEdge(num_vertices, tempEdge.endVertex, contain, weightMatrix));
			}
		}
		return tempTable;
	}

	public ArrayList<Edge> findEdge(int num_vertices, int vertice, ArrayList<Integer> contain, int[][] weightMatrix) {
		ArrayList<Edge> listEdge = new ArrayList<Edge>();
		for (int i = 0; i < num_vertices; i++) {
			if (i != vertice) {
				if (!contain.contains(i)) {
					if (weightMatrix[vertice][i] > 0) {
						Edge tempEdge = new Edge(vertice, i);
						listEdge.add(tempEdge);

					}
				}
			}
		}
		return listEdge;
	}


}
