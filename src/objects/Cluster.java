package objects;

import java.util.ArrayList;

public class Cluster {
	private ArrayList<Integer> cluster = new ArrayList< Integer>();
	
	
	public Cluster(){
		
	}
	public void addElement( int index , int value){
		cluster.add( index , value);
		
	}
	// setter
	public void setSluster( ArrayList<Integer> cluster){
		this.cluster = cluster;
	}
	// getter
	public  ArrayList<Integer>  getCluster(){
		return cluster;
	}
	

}
