package com.routing.algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/***
 * This class reads the text file having the network graph in the form of matrix.
 * It reads the file and populates the data in a 2-D matrix. It also returns a list of Edges which is later used by our algorithm.
 * @author Manas, Ravali
 */
public class Graph {

	// This 2-D matrix called the adjacency matrix stores the network information.
	static private int[][] adjacencyMatrix;
	
	// This LIST data structure stores a List of Edges as specified in the network file.
	static private ArrayList<Edge> edges = new ArrayList<>();
	
	// This LIST data structure stores the list of deleted nodes.
	static private ArrayList<Integer> deletedNodes = new ArrayList<>();
	
	/***
	 * This function reads the input network file and populates the data in our 2-D matrix.
	 * @param fileName	Full path along with the name of the text file having the network information in a matrix format.
	 * @return			Returns a List of Edges defined in the network text file.
	 */
	public static ArrayList<Edge> getGraphEdges(String fileName) {
		String line = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		int rows = 0, columns = 0;
		
		try {
			// Initializing objects which are used to read the file.
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Reading the file line by line. The block below is used to count the number of lines which tells us number of nodes in the network.
			while ((line = bufferedReader.readLine()) != null) {
				// Splitting line using SPACE as delimiter
				String tokens[] = line.split(" ");
				columns = tokens.length;
				rows++;
			}
			
			// Checking if number or ROWS = number of COLUMNS. If that is not the case then the file content is not proper and we cannot proceed.
			if (rows == columns) {
				// Initializing objects which are used to read the file.
				fileReader = new FileReader(fileName);
				bufferedReader = new BufferedReader(fileReader);
				
				// Initializing our 2-D matrix.
				// If there are 5 nodes, we create 6x6 matrix because we don't store anything at the 0th row/column.
				adjacencyMatrix = new int[rows + 1][columns + 1];
				rows = 1;
				
				// Reading the file line by line.
				while ((line = bufferedReader.readLine()) != null) {
					String tokens[] = line.split(" ");
					
					// Reading the values after splitting each line using SPACE as delimiter
					for (columns = 1; columns <= tokens.length; columns++) {
						// Storing the values in our adjacency matrix
						int value = Integer.parseInt(tokens[columns - 1]);
						adjacencyMatrix[rows][columns] = value;
						
						// If the value is not 0 or -1, we create an Edge object and add it to the Edges list.
						// 0 means it is directing to itself and -1 means there is no link between the nodes.
						if (value != 0 && value != -1) {
							edges.add(new Edge(Integer.toString(rows), Integer.toString(columns), value));
						}
					}
					rows++;
				}
			}

		} catch (FileNotFoundException ex) {
			// Error Handling
			System.out.println("Unable to open file '" + fileName + "'");
			ex.printStackTrace();
		} catch (IOException ex) {
			// Error Handling
			System.out.println("Error reading file '" + fileName + "'");
			ex.printStackTrace();
		} finally {
			try {
				// Closing all initialized streams
				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
				
				if (fileReader != null) {
					fileReader.close();
					fileReader = null;
				}
			} catch (Exception e) {
				// Error Handling
				e.printStackTrace();
			}
		}
		
		// Returning the final list which contains all the edges in the network.
		return edges;
	}
	
	/***
	 * This method properly prints the adjacency matrix in a formatted manner.
	 * @return	Returns the string containing formatted matrix data in a tabular format.
	 */
	public static String printAdjacencyMatrix() {
		StringBuffer matrixString = new StringBuffer();
		
		matrixString.append(String.format("%7s   !", "~"));
		for (int i = 1; i < adjacencyMatrix.length; i++) {
			if (!deletedNodes.contains(i)) {
				matrixString.append(String.format("%6d  ", i));
				//matrixString.append(String.format("%7c", 64 + i));
			}
		}
		
		matrixString.append("\n");
		for (int i = 1; i <= adjacencyMatrix.length * 9; i++) {
			matrixString.append("-");
		}
		
		matrixString.append("\n");
		for (int i = 1; i < adjacencyMatrix.length; i++) {
			if (!deletedNodes.contains(i)) {
				matrixString.append(String.format("%6d    !", i));
				//matrixString.append(String.format("%7c   ", 64 + i));
				for (int j = 1; j < adjacencyMatrix.length; j++) {
					if (!deletedNodes.contains(j)) {
						matrixString.append(String.format("%6d   ", adjacencyMatrix[i][j]));
					}
				}
				matrixString.append("\n");
			}
		}
		return matrixString.toString();
	}
	
	/***
	 * This method modifies the link weight (distance) of an edge between the specified two nodes.
	 * @param from		Name of the first end node of the edge.
	 * @param to		Name of the second end node of the edge.
	 * @param distance	New link weight of the edge.
	 */
	public static void modifyLink(String from, String to, int distance) {
		adjacencyMatrix[Integer.parseInt(from)][Integer.parseInt(to)] = distance;
		adjacencyMatrix[Integer.parseInt(to)][Integer.parseInt(from)] = distance;
	}
	
	/***
	 * This method adds a node to the adjacency matrix. Basically it adds a new row and column with appropriate link weights.
	 * @param linkWeights	An array containing link weights from the new node to the existing nodes.
	 * 						Link Weight at 0th position defines the link weight of the new node to the First Node.
	 * @return				Returns true if the node is added successfully to the 2-D matrix else returns false.
	 */
	public static boolean addNode(Map<Integer, Integer> linkWeights) {
		int nodesCount = adjacencyMatrix.length - 1;
			
		int[][] tempMatrix = new int[nodesCount + 1][nodesCount + 1];
		
		// Copying existing data to a temporary matrix.
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix.length; j++) {
				tempMatrix[i][j] = adjacencyMatrix[i][j];
			}
		}
		
		// Re-initializing adjacency matrix with a new row and new column
		nodesCount++;
		adjacencyMatrix = new int[nodesCount + 1][nodesCount + 1];
		
		// Adding the data back from the temporary matrix to the adjacency matrix
		for (int i = 0; i < adjacencyMatrix.length - 1; i++) {
			for (int j = 0; j < adjacencyMatrix.length - 1; j++) {
				 adjacencyMatrix[i][j] = tempMatrix[i][j];
			}
		}
		
		// Adding the new data i.e. in the new row and column
		for (Map.Entry link : linkWeights.entrySet()) {
			adjacencyMatrix[nodesCount][(int) link.getKey()] = (int) link.getValue();
			adjacencyMatrix[(int) link.getKey()][nodesCount] = (int) link.getValue();
		}
		
		// Return true (SUCCESS) if we reached till this point.
		//System.out.println("Success addNode Matrix");
		return true;
	}
	
	/***
	 * This method removes a node from the adjacency matrix. If the node is the last node, it removes it otherwise it updates the distance to -1.
	 * @param node	Name of the node which has to be removed.
	 * @return		Returns true if the node has been removed successfully.
	 */
	public static boolean removeNode(String node) {
		int n = Integer.parseInt(node);
		
		if (n + 1 == adjacencyMatrix.length) {
			int[][] tempMatrix = new int[adjacencyMatrix.length - 1][adjacencyMatrix.length - 1];
			
			// Copying existing data to a temporary matrix.
			for (int i = 0; i < adjacencyMatrix.length - 1; i++) {
				for (int j = 0; j < adjacencyMatrix.length - 1; j++) {
					tempMatrix[i][j] = adjacencyMatrix[i][j];
				}
			}

			adjacencyMatrix = new int[tempMatrix.length][tempMatrix.length];
			
			// Adding the data back from the temporary matrix to the adjacency matrix
			for (int i = 0; i < adjacencyMatrix.length; i++) {
				for (int j = 0; j < adjacencyMatrix.length; j++) {
					 adjacencyMatrix[i][j] = tempMatrix[i][j];
				}
			}
		} else {
			for (int i = 1; i < adjacencyMatrix.length; i++) {
				adjacencyMatrix[n][i] = -1;
				adjacencyMatrix[i][n] = -1;
			}
			adjacencyMatrix[n][n] = 0;
			deletedNodes.add(n);
		}
		return true;
	}
	
	/***
	 * This method returns the adjacency matrix.
	 * @return	Returns the adjacency matrix.
	 */
	public static int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
}
