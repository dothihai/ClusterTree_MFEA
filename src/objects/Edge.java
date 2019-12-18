package objects;

public class Edge {
	public int startVertex;
	public int endVertex;

	public Edge(int startVertex, int endVertex) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
	}

	public int getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(int startVertex) {
		this.startVertex = startVertex;
	}

	public int getEndVertex() {
		return endVertex;
	}

	public void setEndVertex(int endVertex) {
		this.endVertex = endVertex;
	}

}
