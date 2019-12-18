package utils;

import java.util.ArrayList;
import java.util.Collections;

import objects.Cycles;
import utils.ObjComparator;

public class DandelionCode implements ICayleyCode {
	public int[][] decode(int[] DandelionCode) {
		// define the mapping function
		int DandelionLength = DandelionCode.length;
		int[] tempTable = new int[DandelionLength];
		boolean[] mask = new boolean[DandelionLength + 2];
		int[][] tree = new int[DandelionLength + 2][DandelionLength + 2];

		for (int i = 0; i < DandelionLength; i++) {
			tempTable[i] = i + 1;
			mask[i] = false;
		}
		mask[DandelionLength + 1] = false;
		mask[DandelionLength] = false;
		// step 2 + 3: Let the cycles associated with the function D.
		// determine cycles;

		ArrayList<Cycles> cycle1 = new ArrayList<Cycles>();
		ArrayList<Integer> temp = new ArrayList<>(); 
		for (int i = 0; i < DandelionLength; i++) {
			boolean flag = false;
			int maxElement = tempTable[i];
			// Cycles cc = new Cycles();
			Cycles cycle = new Cycles();
			cycle.getCycle().add(tempTable[i]);

			int k = i;
			if (DandelionCode[k] == 0 || DandelionCode[k] == DandelionLength + 1) {
				mask[tempTable[i]] = true;
				continue;
			}
			while (!mask[DandelionCode[k]] && !temp.contains(DandelionCode[k])) {
				if (DandelionCode[k] == 0 || DandelionCode[k] == DandelionLength + 1) {
					break;
				}
				if (DandelionCode[k] > maxElement) {
					maxElement = DandelionCode[k];
				}

				if (DandelionCode[k] == cycle.getCycle().get(0)) {
					flag = true;
					break;

				} else {
					cycle.getCycle().add(DandelionCode[k]);
					mask[DandelionCode[k]] = true;
					k = DandelionCode[k] - 1;

				}
			}
			mask[tempTable[i]] = true;
			// is this satisfies condition, if it is not satisfy conditions then
			// return to the the first state
			if (!flag) {
				for (int j = 0; j < cycle.getCycle().size(); j++) {
					mask[cycle.getCycle().get(j)] = false;
				}
			} else {
				// sort element from the cycle
				int index = cycle.getCycle().indexOf(maxElement);
				int length = cycle.getCycle().size();

				for (int j = length - 1; j > index; j--) {
					if (cycle.getCycle().get(j) < maxElement) {
						cycle.getCycle().addFirst(cycle.getCycle().get(j));
						cycle.getCycle().remove(length);
					}
				}
				cycle.setMaxElement(maxElement);
				cycle1.add(cycle);
				temp.addAll(cycle.getCycle());

			}
		}
		Collections.sort(cycle1, ObjComparator.compareByMaxElement);
		ArrayList<Integer> path = new ArrayList<>();
		int numberOfCycles = cycle1.size();
		if (numberOfCycles > 0) {
			for (int i = 0; i < numberOfCycles; i++) {
				path.addAll(cycle1.get(i).getCycle());
			}
			int piLength = path.size();
			// step 4: Construct the Tree corresponding to D
			// 4.1: create a path from vertex 1 to vertex n by following the
			// list in pi from left to right
			tree[0][path.get(0)] = tree[path.get(0)][0] = 1;
			tree[DandelionLength + 1][path.get(piLength - 1)] = tree[path.get(piLength - 1)][DandelionLength + 1] = 1;
			for (int i = 0; i < piLength - 1; i++) {
				tree[path.get(i)][path.get(i + 1)] = tree[path.get(i + 1)][path.get(i)] = 1;
			}
		} else {
			tree[0][DandelionLength + 1] = tree[DandelionLength + 1][0] = 1;
		}
		// 4.2 the others vertex are not in pi
		for (int i = 0; i < DandelionLength; i++) {
			if (!path.contains(i + 1)) {
				tree[i + 1][DandelionCode[i]] = tree[DandelionCode[i]][i + 1] = 1;
			}
		}
		return tree;

	}

	public int[] encode(int[][] tree) {
		int n_vertices = tree[0].length;
		int[] dandelionString = new int[n_vertices];
		return dandelionString;
	}

}
