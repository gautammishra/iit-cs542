package com.routing.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

/***
 * This class is core of this Link State Routing project. It maintains a graph object which has all the nodes (vertices) 
 * of the network along with the neighbor nodes information. It has methods which can be used to perform routing and
 * getting the shortest path(s) between two nodes as well as to get all the paths between two nodes.
 * 
 * @author Gautam, Sekkhar
 */
public class Dijkstras {

	// This MAP data structure stores all the vertices in the graph in <Key, Value> pairs where Key is the name of the vertex and Value is the Vertex Object.
	// This is built using the list of Edges in the network.
	private volatile Map<String, Vertex> graph; 

	// This structure is used to for storing all the shortest paths available in the network.
	private Set<List<Vertex>> allShortestPaths;
	
	// This structure is used to store all the paths between two nodes.
	private Set<List<Vertex>> allPaths;

	/***
	 * This constructor builds a graph from a set of edges.
	 * @param edges	List of edges which we get after parsing the text file.
	 */
	public Dijkstras(ArrayList<Edge> edges) {
		graph = new HashMap<>(edges.size());

		// This block of code detects all the vertices from the set of edges.
		for (Edge e : edges) {
			if (!graph.containsKey(e.v1))
				graph.put(e.v1, new Vertex(e.v1));
			if (!graph.containsKey(e.v2))
				graph.put(e.v2, new Vertex(e.v2));
		}

		// This block of code detects the neighboring vertices.
		for (Edge e : edges) {
			// Adding neighboring vertices in both the directions i.e. if there is an edge 
			// between Node 1 and Node 2, then Node 1 is neighbor of Node 2 and Node 2 is the neighbor if Node 1.
			graph.get(e.v1).neighbours.put(graph.get(e.v2), e.distance);
			graph.get(e.v2).neighbours.put(graph.get(e.v1), e.distance);
		}
	}

	/***
	 * This method performs Dijkstras algorithm on the graph and builds the shortest path(s) from the source node to all other nodes in the graph.
	 * 
	 * @param sourceId	Name of the Node from which the shortest path(s) has to found
	 */
	public void route(String sourceId) {
		// Retrieve the Vertex object for the source node having name = sourceId.
		//System.out.println("Route == " + graph);
		Vertex source = graph.get(sourceId);
		source.dist = 0;

		// Maintain a Priority Queue for graph traversal. Basically, it stores the list of nodes which have been visited.
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);
		List<Vertex> prev = null;

		while (!vertexQueue.isEmpty()) {
			// Get the first node in the queue
			Vertex u = vertexQueue.poll();

			// Iterate through the neighbor nodes of the first node
			for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {

				// Get the Node (Vertex) from the <Vertex, Distance> pair
				Vertex nv = a.getKey();
				prev = nv.prev;
				
				// Get the distance of the vertex. This distance is the total distance of the path up to this current Vertex (Node)
				int weight = a.getValue();
				
				// Add the new node's distance and distance upto previous node
				int distanceThroughU = u.dist + weight;

				if (distanceThroughU < nv.dist) {
					// If the new distance is less than the previous distance (taken from neighboring node) update the distance of this 
					// vertex with the new distance (which is also the shortest distance) and add this vertex to the list of the shortest paths
					vertexQueue.remove(nv);
					nv.dist = distanceThroughU;
					nv.previous = u;
					vertexQueue.add(nv);
					prev = new ArrayList<Vertex>();
					prev.add(u);
					nv.prev = prev;
				} else if (distanceThroughU == nv.dist) {
					// If the new distance is equal to the previous distance (taken from neighboring node) add this vertex to the list of the shortest paths
					if (prev != null)
						prev.add(u);
					else {
						prev = new ArrayList<Vertex>();
						prev.add(u);
						nv.prev = prev;
					}
				}
			}
		}
	}

	/***
	 * This method returns a list of of nodes (vertex) in a single path from the source node to the target node.
	 * Remember that source node is specified when calling the route(source) method.
	 * route(source) method must be called before calling this method.
	 * 
	 * @param target 	Name of the target node to which the path from the source node has to be returned
	 * @return			Returns a List of Vertex (node) in the path from source to target.
	 * 					The nodes in the list are in the order in which they are in the path.
	 */
	public List<Vertex> getShortestPathTo(String target) {
		// Initializing the path with an empty list
		List<Vertex> path = new ArrayList<Vertex>();
		// Iterate through the previous node of the target node
		for (Vertex vertex = graph.get(target); vertex != null; vertex = vertex.previous)
			path.add(vertex);
		
		// Reverse the List so that it appears from source to target and return back to the caller
		Collections.reverse(path);
		return path;
	}

	/***
	 * This method returns a Set containing all the shortest paths from the source node to the target node.
	 * Each path is defined as a List of Vertex (Node) in this Set. 
	 * Remember that source node is specified when calling the route(source) method.
	 * route(source) method must be called before calling this method.
	 * 
	 * @param target 	Name of the target node to which the path from the source node has to be returned
	 * @return			Returns a Set containing a List of Vertex (node) in the path from source to target. (Each List represents a path)
	 * 					The nodes in the list are in the order in which they are in the path.
	 */
	public Set<List<Vertex>> getAllShortestPathsTo(String target) {
		// Initialize the set with an empty set with no list (path)
		allShortestPaths = new HashSet<List<Vertex>>();
		Vertex v = graph.get(target);
		
		// Call the method which gives us all the shortest paths.
		getShortestPath(new ArrayList<Vertex>(), v);
		
		// Return the Set
		return allShortestPaths;
	}

	/***
	 * This method recursively adds all the shortest paths available from the source node to the target node.
	 * This is an internal (private) method defined to make use of recursion and is used by getAllShortestPathsTo(...) method.
	 * 
	 * @param shortestPath	A List defining a single shortest path from the source node to the target node.
	 * @param target		A target node (Vertex object) which specifies the paths to be evaluated from source node to this target node.
	 * @return				Returns a List containing nodes (Vertex object) in a single shortest path from source to target.
	 */
	private List<Vertex> getShortestPath(List<Vertex> shortestPath, Vertex target) {
		// Retrieve the target node's previous node
		List<Vertex> prev = target.prev;
		//System.out.println(target);
		
		if (prev == null) {
			// If there is no previous node, it means we have reached end of path. So we reverse the list so that nodes are in from source to target order.
			// We then add this list to the <allShortestPaths> Set structure.
			shortestPath.add(target);
			Collections.reverse(shortestPath);
			allShortestPaths.add(shortestPath);
		} else {
			// If previous node exists, the we add this node to the single shortest path list
			List<Vertex> updatedPath = new ArrayList<Vertex>(shortestPath);
			updatedPath.add(target);
			
			// We then iterate through all the previous nodes (It will have multiple previous nodes if there are multiple shortest paths in the network)
			for (Iterator<Vertex> iterator = prev.iterator(); iterator.hasNext();) {
				Vertex vertex = (Vertex) iterator.next();
				// We call this method again with the path generated till now and current node as target node so that next time we check previous of this node.
				// Here we are doing recursion.
				getShortestPath(updatedPath, vertex);
			}
		}
		// If we have evaluated a single shorted path, we return that. In the updated code, at this given moment we dont make use of this returned value.
		return shortestPath;
	}

	/***
	 * This method returns a Set containing all the paths from the source node to the destination node.
	 * Each path is defined as a List of Vertex (Node) in this Set. 
	 * It is not required that you call route(source) method before calling this method.
	 * 
	 * @param source		Name of the source node whose paths you want to retrieve.
	 * @param destination	Name of the destination node whose paths you want to retrieve.
	 * @return				Returns a Set containing a List of Vertex (node) in the path from source to target. (Each List represents a path)
	 * 						The nodes in the list are in the order in which they are in the path.
	 */
	public Set<List<Vertex>> getAllPaths(String source, String destination) {
		allPaths = new HashSet<List<Vertex>>();
		LinkedList<Vertex> visited = new LinkedList<Vertex>();
		
		visited.add(graph.get(source));
		breadthFirst(visited, graph.get(destination));

		return allPaths;
	}

	/***
	 * This method performs a Breadth First Search algorithm on the graph to get all the paths from the source node to the destination node.
	 *
	 * @param visited	A LinkedList structure which stores a list of all the visited nodes.
	 * @param end		Vertex object of the destination node whose path you want to get.
	 *					Remember that source node will be already there in the <visited> list when this method is called for the first time.
	 */
	private void breadthFirst(LinkedList<Vertex> visited, Vertex end) {
		// Retrieve all the neighboring nodes of the last node in the visited nodes list.
		Map<Vertex, Integer> neighbours = visited.getLast().neighbours;

		// Examine all the adjacent (neighbor) nodes
		for (Map.Entry<Vertex, Integer> neighbor : neighbours.entrySet()) {
			Vertex node = (Vertex) neighbor.getKey();
			
			// If this node is already visited, then skip the remaining part of the block.
			if (visited.contains(node)) {
				continue;
			}
			
			if (node.equals(end)) {
				visited.add(node);
				// printPath(visited);
				// System.out.println(visited);
				allPaths.add(new LinkedList<Vertex>(visited));
				// System.out.println("\n\n" + allPaths);
				visited.removeLast();
				break;
			}
		}
		
		// After we have visited the adjacent nodes, we do a recursion and call this (breadthFirst(...)) method again.
		for (Map.Entry<Vertex, Integer> neighbor : neighbours.entrySet()) {
			Vertex node = (Vertex) neighbor.getKey();
			if (visited.contains(node) || node.equals(end)) {
				continue;
			}
			visited.addLast(node);
			breadthFirst(visited, end);
			visited.removeLast();
		}
	}

	/*private void printPath(LinkedList<Vertex> visited) {
		for (Vertex node : visited) {
			System.out.print(node.name);
			System.out.print(" ");
		}
		System.out.println();
	}*/

	/*public void getNeighbours() {
		for (Vertex v : graph.values()) {
			System.out.printf("\n\n%s:", v.name);
			for (Map.Entry<Vertex, Integer> n : v.neighbours.entrySet()) {
				System.out.printf(" %s,", ((Vertex) n.getKey()).name);
			}
		}
	}*/
	
	/***
	 * This method prints all the Paths from one node to other node specified by the structure passed in the parameter.
	 * The evaluation is already done and this method just prints the content in a proper format.
	 * @param paths	A Set containing a List of Vertices indicating all the paths between two nodes. Each list defines a single path.
	 * @return		Returns the formatted String which can be printed to display all paths between two nodes.
	 */
	public String printPaths(Set<List<Vertex>> paths) {
		StringBuffer text = new StringBuffer();
		
		text.append("PATHS AVAILABLE = ").append(paths.size()).append("\n\n");
		int i = 1;
		
		for (Iterator<List<Vertex>> iter = paths.iterator(); iter.hasNext(); i++) {
			List<Vertex> p = (List<Vertex>) iter.next();
			text.append("Path ").append(i).append(" : ");
			
			String path = "";
			for (Vertex v : p) {
				path += " -->  " + v.name;
			}
			path = path.replaceFirst(" --> ", "");
			text.append(path).append("\n\n");
		}
		
		return text.toString();
	}
	
	/***
	 * This method returns the distance value from the shortest paths from source node to destination node.
	 * @param paths	A Set containing shortest paths from source node to destination node.
	 * @return		Returns an integer value specifying the Shortest Path's Distance from source node to destination node.
	 */
	public int getDistance(Set<List<Vertex>> paths) {
		int distance = -1;
		for (Iterator<List<Vertex>> iter = paths.iterator(); iter.hasNext();) {
			List<Vertex> p = (List<Vertex>) iter.next();
			distance = p.get(p.size() - 1).dist;
			return distance;
		}
		return distance;
	}
	
	/***
	 * This method returns the total number of nodes in the network.
	 * @return	Returns an integer value which specifies the number of nodes in the network.
	 */
	public int getNodesCount() {
		// Return the graph size because graph stores all the vertices (nodes) in the network.
		return graph.size();
	}
	
	/***
	 * This method checks if a node with the specified name exists in the network.
	 * @param node	Name of the node in String format whose existence in the network has to be checked.
	 * @return		Returns true if a node with the specified name exists in the network else returns false.
	 */
	public boolean nodeExists(String node) {
		return graph.get(node) != null;
	}

	/***
	 * This method returns a Map structure which contains <Destination, Next Hop> from the source node to all the other nodes in the network.
	 * Remember that source node is specified when calling the route(source) method.
	 * @return	A TreeMap structure having <Destination, Next_Hop> pairs from the source node all other other nodes in the network.
	 */
	public TreeMap<String, String> getPaths() {
		TreeMap<String, String> routingTable = new TreeMap<>();
		
		for (Vertex v : graph.values()) {
			List<Vertex> list = new LinkedList<>();
			v.printPath(list);
			Collections.reverse(list);
			if (list.size() > 0) {
				routingTable.put(list.get(0).name, list.get(list.size() - 1).name);
			} else {
				routingTable.put(v.name, "-");
			}
		}
		return routingTable;
	}
	
	/***
	 * This map returns a TreeMap structure which has the routing table (also called as connection table) of all the nodes.
	 * @param edges	A List of all the Edges in the network.
	 * @return		Returns a TreeMap structure which has the routing table of all the nodes.
	 */
	public TreeMap<String, TreeMap<String, String>> getRoutingTable(ArrayList<Edge> edges) {
		TreeMap<String, TreeMap<String, String>> routingTable = new TreeMap<>();
		for (Vertex v : graph.values()) {
			Dijkstras d = new Dijkstras(edges);
			d.route(v.name);
			TreeMap<String, String> localTable = d.getPaths();
			routingTable.put(v.name, localTable);
		}
		return routingTable;
	}
	
	/***
	 * This method returns a string which has routing table of all the nodes in the network in a formatted manner for printing purpose.
	 * @param edges	A List of all the Edges in the network.
	 * @return		Returns the string which has the routing tables in formatted manner.
	 */
	public String printRoutingTable(ArrayList<Edge> edges) {
		TreeMap<String, TreeMap<String, String>> routingTable = getRoutingTable(edges);
		StringBuffer connectionTable = new StringBuffer();
		
		for (Map.Entry<String, TreeMap<String, String>> map : routingTable.entrySet()) {
			connectionTable.append(" ROUTER ").append(map.getKey()).append(" CONNECTION TABLE\n\n");
			connectionTable.append("     Destination    Next Hop \n");
			connectionTable.append("--------------------------------------\n");
			
			TreeMap<String, String> rt = (TreeMap<String, String>) map.getValue();
			for (Map.Entry<String, String> entry : rt.entrySet()) {
				String line = String.format("%13s %19s", entry.getKey() , entry.getValue());
				connectionTable.append(line).append("\n");
			}
			connectionTable.append("--------------------------------------\n\n");
		}
		return connectionTable.toString();
	}
	
	/***
	 * This method removes a node (Vertex object) from the graph. It also removes that node from neighbors of other nodes.
	 * @param node	The name of the node in String format which has to be removed.
	 */
	public void removeNode(String node) {
		Vertex delete = graph.get(node);
		for (Vertex v : graph.values()) {
			v.neighbours.remove(delete);
		}
		graph.remove(node);
	}
	
	/***
	 * This method returns a Set which contains name of all the nodes in the graph.
	 * @return	Returns Set of String containing names of nodes.
	 */
	public Set<String> getNodes() {
		return graph.keySet();
	}
}