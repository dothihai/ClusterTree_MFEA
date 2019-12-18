package visualizations;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import objects.Cluster;
import objects.Vertex;

public class ClusptVisualize extends JPanel {
	private static final long serialVersionUID = 1L;
	public int n_vertices;
	private ArrayList<Vertex> vertices;
	private ArrayList<Cluster> clusters;
	private int root;
	private int[][] tree;
	private double fitness;
	private int marginX = 0;
	private int marginY = 0;
	private double scale;
	
	
	public void setPaint(int[][] tree, ArrayList<Vertex> vertices, ArrayList<Cluster> clusters, int n_vertices,
			int root, double fitness, int marginX, int marginY, double scale) {
		this.vertices = vertices;
		this.clusters = clusters;
		this.n_vertices = n_vertices;
		this.root = root;
		this.tree = tree;
		this.fitness = fitness;
		this.marginX = marginX;
		this.marginY = marginY;
		this.scale = scale;
	

	}
	
	public void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		
		
		for (Cluster cluster : clusters) {
			int nClusterVertices = cluster.getCluster().size();
			Random r = new Random();
			g2d.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
			int sizeVertex = 10;
			for (int j = 0; j < nClusterVertices; j++) {
				int x = (int) (vertices.get(cluster.getCluster().get(j)).getX() * scale - sizeVertex/2 + marginX);
				int y = (int) (vertices.get(cluster.getCluster().get(j)).getY() * scale - sizeVertex/2 + marginY);
				g2d.fillOval(x, y, sizeVertex, sizeVertex);
			}
		}
		
		
		for (int i = 0; i < n_vertices; i++) {
			for (int j = 0; j < n_vertices; j++) {
				int x1 = (int) (vertices.get(i).getX() * scale + marginX);
				int y1 = (int) (vertices.get(i).getY() * scale + marginY);
				int x2 = (int) (vertices.get(j).getX() * scale + marginX);
				int y2 = (int) (vertices.get(j).getY() * scale + marginY);
				g2d.setColor(Color.RED);
				g2d.drawString("" + (i), x1 + 10, y1 + 10);
				if (tree[i][j] > 0) {
					g2d.setColor(Color.BLUE);
					g2d.drawLine(x1, y1, x2, y2);
				}
			}
		}
		
		
		int sizeRootVertex = 20; 
		g2d.setColor(Color.BLACK);
		g2d.fillOval((int) (vertices.get(root).getX() * scale - sizeRootVertex/2 + marginX),
				(int) (vertices.get(root).getY() * scale - 10) + marginY, sizeRootVertex, sizeRootVertex);
		
		
		g2d.drawString("Cluspt cost:" + fitness, 50 + marginX + 200, 20);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

}