package utils;

import java.util.ArrayList;
import java.util.Collections;

public class BlobCode implements ICayleyCode {

	public int[][] decode(int[] blobString) {
		int blobStringLength = blobString.length;
		int nVertices = blobStringLength + 2;
		int[][] tree = new int[nVertices][nVertices];
		int[] color = new int[nVertices];// values meant 0: uncolored,
		// step 2 + 3: Let the cycles associated with the function D.
		// determine cycles;
		int k = 0;
		for (int i = 0; i < blobStringLength; i++) {
			// tim dinh trang:
			k = i;
			boolean[] mask = new boolean[nVertices];
			mask[i + 1] = true;
			boolean flag = false;
			if (blobString[i] == 0) {
				color[i + 1] = 1;
				continue;
			}
			int vertex = i + 1;
			while ((vertex) >= blobString[k]) {
				mask[blobString[k]] = true;
				if (blobString[k] == 0) {
					flag = true;
					color[i + 1] = 1;
					break;
				}
				k = blobString[k] - 1;
				if (mask[blobString[k]]) {
					color[i + 1] = 1;
					flag = true;
					break;
				}
			}
			if (!flag) {
				color[i + 1] = 2;
			}
		}
		color[0] = 1;
		color[blobStringLength + 1] = 1;

		// Step 5: Suppose the black vertices are x1 < x2 < . . . < xt,
		// where t ∈ [2, n] is the total number of black vertices; observe
		// that x1 = 1 and xt = n. To construct the tree T ∈ Tn
		// corresponding to B, take a set of n isolated vertices (labelled
		// with the integers from 1 to n), create the edge (i, bi) for each
		// white vertex i ∈ [2, n − 1], create the edge (xi, bxi−1) for
		// each i ∈ [3, t], and finally create the edge (x2, 1)
		ArrayList<Integer> blackVertices = new ArrayList<>();
		for (int i = 0; i < nVertices; i++) {
			if (color[i] == 1) {
				blackVertices.add(i);
			}
		}
		// blackVertices.add(0);
		Collections.sort(blackVertices);
		// create the edge (i, bi) for each white vertex i ∈ [2, n − 1]
		for (int i = 1; i < blobStringLength + 1; i++) {
			if (color[i] == 2) {
				tree[i][blobString[i - 1]] = 1;
				tree[blobString[i - 1]][i] = 1;

			}
		}

		// create the edge (xi, bxi−1) for each i ∈ [3, t]
		int t = blackVertices.size();
		int xi = 0;
		int xi_1 = 0;
		for (int i = 2; i < t; i++) {
			xi = blackVertices.get(i);
			xi_1 = blackVertices.get(i - 1);
			tree[xi][blobString[xi_1 - 1]] = 1;
			tree[blobString[xi_1 - 1]][xi] = 1;
		}
		// and finally create the edge (x2, 1)
		tree[blackVertices.get(1)][0] = 1;
		tree[0][blackVertices.get(1)] = 1;
		return tree;
	}

	public int[] encode(int[][] tree) {
		int n_vertices = tree[0].length;
		int[] blobString = new int[n_vertices];
		return blobString;
	}

}
