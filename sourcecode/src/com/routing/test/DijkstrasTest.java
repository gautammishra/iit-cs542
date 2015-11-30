package com.routing.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.routing.algorithm.Dijkstras;
import com.routing.algorithm.Edge;
import com.routing.algorithm.Graph;
import com.routing.algorithm.Vertex;

public class DijkstrasTest {

	public static void main(String[] args) {

		/*ArrayList<Edge> edges = new ArrayList<>();
		edges.add(new Edge("a", "c", 5));
		edges.add(new Edge("a", "d", 1));
		edges.add(new Edge("b", "d", 7));
		edges.add(new Edge("b", "e", 1));
		edges.add(new Edge("c", "a", 5));
		edges.add(new Edge("c", "e", 2));
		edges.add(new Edge("d", "a", 1));
		edges.add(new Edge("d", "b", 7));
		edges.add(new Edge("d", "e", 6));
		edges.add(new Edge("e", "b", 1));
		edges.add(new Edge("e", "c", 2));
		edges.add(new Edge("e", "d", 6));*/
		
		ArrayList<Edge> edges = Graph.getGraphEdges("D:/graph.txt");

		Dijkstras my = new Dijkstras(edges);
		my.route("1");
		Set<List<Vertex>> x = my.getAllShortestPathsTo("2");

		System.out.println(my.printPaths(x));
		System.out.println(my.getDistance(x));

		System.out.println("\nAll Paths from A to B");

		Set<List<Vertex>> all = my.getAllPaths("1", "2");

		System.out.println(my.printPaths(all));

		System.out.println(Graph.printAdjacencyMatrix());
		
		//my.printAllPaths();
		
		//my.getRoutingTable(edges);
		System.out.println(my.printRoutingTable(edges));
	}
}