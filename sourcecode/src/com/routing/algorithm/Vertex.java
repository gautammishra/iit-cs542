package com.routing.algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * One vertex (node) of the graph with mappings to neighboring vertices.
 * @author Sekkhar
 */
public class Vertex implements Comparable<Vertex> {
	// This variable stores the name of the node.
	public final String name;
	
	// This value stores the distance which is used when finding a path. By default it is assumed to be infinity (MAX. value of Integer in JAVA)
	public int dist = Integer.MAX_VALUE;
	
	// This variable stores a link to the previous node.
	public Vertex previous = null;
	
	// This MAP data structure stores the neighbor nodes of this (current) node.
	public final Map<Vertex, Integer> neighbours = new HashMap<>();
	
	// This LIST data structure stores a list of all the previous nodes which have been tracked while finding the shortest path.
	// It is used for finding multiple shortest path (if exists) in a network.
	public List<Vertex> prev;

	/***
	 * Constructor of Vertex class which initializes the node. It accepts the name of the node in String format.
	 * @param name	Name of the node in String format.
	 */
	public Vertex(String name) {
		this.name = name;
	}

	/***
	 * This method implements the Comparable interface's compareTo() method.
	 * It compares the current node with the node passed in the parameter with respect to the distance.
	 */
	public int compareTo(Vertex other) {
		return Integer.compare(dist, other.dist);
	}
	
	/***
	 * This method is used to print the path of a given node to all other other nodes in the network.
	 * Basically, it does a recursion and tracks the previous nodes of the current node which stored in the "prev" member variable.
	 * @param list	List of vertex objects (nodes) in a single path i.e. from one node to another node.
	 */
	public void printPath(List<Vertex> list) {
		if (this == this.previous || this.previous == null) {
			//System.out.printf("%s", this.name);
			//list.add(this);
		} else {
			this.previous.printPath(list);
			list.add(this);
			//System.out.printf(" -> %s(%d)", this.name, this.dist);
		}
	}

	/***
	 * This method overrides the toString() method and displays the name of the vertex (node) if printed.
	 */
	@Override
	public String toString() {
		return "Vertex [" + name + "]";
	}
}