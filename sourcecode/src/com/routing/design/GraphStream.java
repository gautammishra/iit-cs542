package com.routing.design;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import com.routing.algorithm.Vertex;

public class GraphStream extends Thread {
	private int[][] adjacencyMatrix;
	private Set<List<Vertex>> shortestPaths;

	public GraphStream(int[][] adjacencyMatrix, Set<List<Vertex>> shortestPaths) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.shortestPaths = shortestPaths;
	}

	public void run() {
		JFrame frame = new JFrame();
		
		Graph g = new MultiGraph("embedded");
		
		
		
		
		//Graph g = new SingleGraph("networkGraph");

		// System.setProperty("org.graphstream.ui.renderer",
		// "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		for (int i = 1; i < adjacencyMatrix.length; i++) {
			g.addNode(Integer.toString(i));
		}

		for (int i = 1; i < adjacencyMatrix.length; i++) {
			for (int j = 1; j < adjacencyMatrix.length; j++) {
				if (i != j && adjacencyMatrix[i][j] != -1) {
					String startNode = Integer.toString(i);
					String endNode = Integer.toString(j);
					String edge = startNode + "_" + endNode;
					String reverseEdge = endNode + "_" + startNode;

					if (g.getEdge(reverseEdge) == null) {
						// System.out.println(edge);
						g.addEdge(edge, startNode, endNode).addAttribute("length", adjacencyMatrix[i][j]);
					}
				}
			}
		}

		for (Node n : g) {
			n.addAttribute("label", "Node " + n.getId());
			n.addAttribute("ui.style", "text-size: 16;");
		}

		for (Edge e : g.getEachEdge()) {
			e.addAttribute("label", "" + (int) e.getNumber("length"));
			e.addAttribute("ui.style", "text-size: 16;");
		}

		String[] colors = { "green;", "blue;", "yellow;" };
		int j = 0;

		for (Iterator<List<Vertex>> iter = shortestPaths.iterator(); iter.hasNext();) {
			List<Vertex> p = (List<Vertex>) iter.next();

			for (int i = 1; i < p.size(); i++) {
				String edge = p.get(i - 1).name + "_" + p.get(i).name;
				// System.out.println(edge);
				if (g.getEdge(edge) != null) {
					g.getEdge(edge).addAttribute("ui.style", "fill-color: " + colors[j]);
				} else {
					String reverseEdge = p.get(i).name + "_" + p.get(i - 1).name;
					g.getEdge(reverseEdge).addAttribute("ui.style", "fill-color: " + colors[j]);
				}
				g.getNode(p.get(i).name).addAttribute("ui.style", "fill-color: blue;");
			}
			j++;
		}
		/*Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		View view = viewer.addDefaultView(true);
		frame.setBounds(100, 100, 500, 500);
		frame.add((JPanel) view);
		frame.setVisible(true);*/
		g.display();
	}
}