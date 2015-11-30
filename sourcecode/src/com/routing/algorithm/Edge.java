package com.routing.algorithm;

/***
 * This class is used to define an edge (link) between two vertices (nodes).
 * 
 * @author Manas
 *
 */
public class Edge {
	// v1 and v2 are the name of the end nodes of the edge.
	public final String v1, v2;
	
	//This variable stores the link weight of the link i.e. distance between the two end nodes.
	public int distance;

	/***
	 * This constructor is used to initialize an Edge for a given network.
	 * @param v1	Name of Vertex 1 (First node) of the edge.
	 * @param v2	Name of Vertex 2 (Second node) of the edge.
	 * @param distance Link Weight of the Edge i.e. distance between Node 1 and Node 2
	 */
	public Edge(String v1, String v2, int distance) {
		this.v1 = v1;
		this.v2 = v2;
		this.distance = distance;
	}

	/***
	 * This method overrides the toString() method and displays the end nodes of the edge with the link weight.
	 */
	@Override
	public String toString() {
		return "Edge [" + v1 + " -- " + v2 + " = " + distance + "]";
	}
}
