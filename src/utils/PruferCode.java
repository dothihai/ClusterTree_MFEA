package utils;

public class PruferCode implements ICayleyCode {

	public int[][] decode(int[] pruferString) {
		int nVertices = pruferString.length + 2;
		int[][] tree = new int[nVertices][nVertices];
		int[] degree = new int[nVertices]; // store the degree of vertices

		for (int i = 0; i < nVertices - 2; i++) {
			int vertex = pruferString[i];
			degree[vertex]++;
		}

		// build the tree from prufer number code
		for (int i = 0; i < nVertices - 2; i++) {
			int curVertex = pruferString[i];
			for (int j = 0; j < nVertices; j++) {
				if (degree[j] == 0) {
					tree[curVertex][j] = tree[j][curVertex] = 1;
					degree[curVertex]--;
					degree[j]--;
					break;
				}
			}
		}
		// if Prufer number code have no element left
		int nLeftVertices = 2;
		int[] twoLeftVertices = new int[nLeftVertices];
		for (int i = 0; i < nVertices; i++) {
			if (degree[i] == 0) {
				twoLeftVertices[nLeftVertices - 1] = i;
				nLeftVertices--;
			}
			if (nLeftVertices < 0) {
				break;
			}
		}
		tree[twoLeftVertices[0]][twoLeftVertices[1]] = tree[twoLeftVertices[1]][twoLeftVertices[0]] = 1;

		return tree;
	}

	public int[] encode(int[][] tree) {
		int n_vertices = tree[0].length;
		int[] pruferString = new int[n_vertices];
		return pruferString;
	}

}
