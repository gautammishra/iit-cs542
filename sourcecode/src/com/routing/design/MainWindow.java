package com.routing.design;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Font;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.routing.algorithm.Dijkstras;
import com.routing.algorithm.Edge;
import com.routing.algorithm.Graph;
import com.routing.algorithm.Vertex;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Component;

/***
 * Design of Link State Routing simulator.
 * @author Gautam, Sekkhar, Ravali
 */
public class MainWindow {

	private JFrame frame;
	private JTextField txtSource;
	private JTextField txtDestination;
	private JDialog newNodeWindow;
	
	private String filePath = null;
	private String source = null;
	private String destination = null;
	
	private final String sourcePromptText = "Enter Source Node";
	private final String destinPromptText = "Enter Destination Node";
	private JTextPane textPane;
	private JTextPane txtConnection;
	private JButton btnNewButton;
	private ArrayList<JTextField> txtFieldsList = null;
	
	private static Dijkstras graph;
	private static ArrayList<Edge> edges = null;
	
	private JPanel modifyLinkPanel;
	private JTextField txtFromNode;
	private JTextField txtToNode;
	private JLabel lblFromNode;
	private JLabel lblToNode;
	private JLabel lblWeight;
	private JTextField txtLinkWeight;
	private JButton btnModify;
	private JTextField txtShortestDistance;
	private JTextPane txtShortestPaths;
	private JPanel addNodePanel;
	private JPanel evaluatePanel;
	private JPanel allPathsPanel;
	private JButton btnFindAll;
	private JScrollPane scrollPane;
	private JTextPane allPaths;
	private JLabel lblFrom;
	private JTextField txtFrom;
	private JLabel lblTo;
	private JTextField txtTo;
	private JButton btnShowGraph;
	
	// This variable store the shortest path(s) between two nodes.
	Set<List<Vertex>> shortestPaths = null;
	
	// This variable stores all the paths between two nodes.
	Set<List<Vertex>> paths = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1548, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("CS542 Project - Link State Routing");
		
		Font textFont = new Font("Tahoma", Font.PLAIN, 14);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(textFont);
		//textPane.setBounds(15, 25, 270, 170);
		textPane.setOpaque(false);
		
		JScrollPane graphPane = new JScrollPane(textPane);
		graphPane.setBounds(361, 48, 450, 230);
		graphPane.setBorder(BorderFactory.createTitledBorder("Network Graph Matrix"));
		frame.getContentPane().add(graphPane);
		
		JButton btnOpenGraph = new JButton("Open Graph File");
		btnOpenGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// Load the file loader.
				loadFile(frame);
				if (filePath != null) {
					// If the user has selected the file, read the file and build the adjacency matrix and graph object required for routing.
					edges = Graph.getGraphEdges(filePath);
					graph = new Dijkstras(edges);
					textPane.setText(Graph.printAdjacencyMatrix().replaceAll("-1", "--"));
					txtConnection.setText(graph.printRoutingTable(edges));
	
					// Enable all the components since the graph has been loaded.
					setEnabled(addNodePanel.getComponents(), true);
					setEnabled(modifyLinkPanel.getComponents(), true);
					setEnabled(evaluatePanel.getComponents(), true);
					setEnabled(allPathsPanel.getComponents(), true);
					btnShowGraph.setEnabled(false);
				}
			}
		});
		btnOpenGraph.setBounds(43, 48, 140, 35);
		frame.getContentPane().add(btnOpenGraph);
		
		JPanel routePanel = new JPanel();
		routePanel.setLayout(null);
		routePanel.setBorder(BorderFactory.createTitledBorder("Shortest Route Information"));
		routePanel.setBounds(361, 306, 292, 268);
		frame.getContentPane().add(routePanel);
		
		txtShortestDistance = new JTextField();
		txtShortestDistance.setColumns(10);
		txtShortestDistance.setBounds(200, 32, 75, 25);
		txtShortestDistance.setForeground(new Color(0, 128, 0));
		txtShortestDistance.setFont(textFont);
		txtShortestDistance.setOpaque(false);
		txtShortestDistance.setEditable(false);
		routePanel.add(txtShortestDistance);
		
		JLabel lblShortestDistance = new JLabel("Shortest Distance from A to B :");
		lblShortestDistance.setBounds(15, 35, 180, 16);
		routePanel.add(lblShortestDistance);
		
		txtShortestPaths = new JTextPane();
		//txtShortestPaths.setBounds(20, 70, 250, 130);
		//routePanel.add(txtShortestPaths);
		txtShortestPaths.setEditable(false);
		txtShortestPaths.setOpaque(false);
		txtShortestPaths.setForeground(new Color(0, 128, 0));
		txtShortestPaths.setFont(textFont);
		
		JScrollPane pathsPane = new JScrollPane(txtShortestPaths);
		pathsPane.setBounds(15, 70, 265, 185);
		pathsPane.setBorder(BorderFactory.createTitledBorder("Shortest Path(s)"));
		routePanel.add(pathsPane);
		
		txtConnection = new JTextPane();
		txtConnection.setEditable(false);
		txtConnection.setFont(textFont);
		//txtConnection.setBounds(20, 30, 240, 170);
		txtConnection.setOpaque(false);
		
		JScrollPane connectionPane = new JScrollPane(txtConnection);
		connectionPane.setBounds(840, 48, 270, 526);
		connectionPane.setBorder(BorderFactory.createTitledBorder("Connection Table"));
		frame.getContentPane().add(connectionPane);
		
		modifyLinkPanel = new JPanel();
		modifyLinkPanel.setBounds(43, 404, 292, 170);
		modifyLinkPanel.setBorder(BorderFactory.createTitledBorder("Modify link weight of an edge"));
		frame.getContentPane().add(modifyLinkPanel);
		modifyLinkPanel.setLayout(null);
		
		txtFromNode = new JTextField();
		txtFromNode.setBounds(95, 32, 55, 25);
		modifyLinkPanel.add(txtFromNode);
		txtFromNode.setColumns(10);
		
		txtToNode = new JTextField();
		txtToNode.setColumns(10);
		txtToNode.setBounds(225, 32, 55, 25);
		modifyLinkPanel.add(txtToNode);
		
		lblFromNode = new JLabel("From Node :");
		lblFromNode.setBounds(15, 35, 75, 16);
		modifyLinkPanel.add(lblFromNode);
		
		lblToNode = new JLabel("To Node :");
		lblToNode.setBounds(162, 36, 65, 16);
		modifyLinkPanel.add(lblToNode);
		
		lblWeight = new JLabel("Enter new link weight :");
		lblWeight.setBounds(15, 78, 140, 16);
		modifyLinkPanel.add(lblWeight);
		
		txtLinkWeight = new JTextField();
		txtLinkWeight.setColumns(10);
		txtLinkWeight.setBounds(154, 75, 80, 25);
		modifyLinkPanel.add(txtLinkWeight);
		
		btnModify = new JButton("Modify");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Check if From & To node and Link Weight has been entered
				if (txtFromNode.getText().isEmpty() || txtToNode.getText().isEmpty() || txtLinkWeight.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Please enter From Node, To Node and Link Weight.", "Empty Data", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						// If From & To node and Link Weight has been entered by the user.
						String from = txtFromNode.getText();
						String to = txtToNode.getText();
						int distance = Integer.parseInt(txtLinkWeight.getText());
						
						//  Validate From & To node and Link Weight
						if (!graph.nodeExists(from)) {
							String message = String.format("Node %s does not exist in the network. Please add the node if you want.", from);
							JOptionPane.showMessageDialog(frame, message, "Add Link Error", JOptionPane.ERROR_MESSAGE);
						} else if (!graph.nodeExists(to)) {
							String message = String.format("Node %s does not exist in the network. Please add the node if you want.", to);
							JOptionPane.showMessageDialog(frame, message, "Add Link Error", JOptionPane.ERROR_MESSAGE);
						} else if (checkLink(from, to)) {
							// If the link is present, modify it and re-calculate everything.
							modifyLink(from, to, distance);
							JOptionPane.showMessageDialog(frame, "Link/Edge modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
							evaluate();
						} else {
							String message = String.format("There is no edge/link between Node %s and Node %s. Would you like to add one?", from, to);
							int result = JOptionPane.showConfirmDialog(frame, message, "Add Link Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
							// If the link is not present but user has agreed to add the link, add it and re-calculate everything.
							if (result == JOptionPane.YES_OPTION) {
								addLink(from, to, distance);
								JOptionPane.showMessageDialog(frame, "Link/Edge added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
								evaluate();
							}
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(frame, "Error in modifying link weight of the edge.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnModify.setBounds(99, 120, 100, 30);
		modifyLinkPanel.add(btnModify);
		
		addNodePanel = new JPanel();
		addNodePanel.setLayout(null);
		addNodePanel.setBorder(BorderFactory.createTitledBorder("Modify Network Topology"));
		addNodePanel.setBounds(43, 294, 292, 80);
		frame.getContentPane().add(addNodePanel);
		
		btnNewButton = new JButton("Add New Node");
		btnNewButton.setBounds(18, 27, 124, 35);
		addNodePanel.add(btnNewButton);
		
		JButton btnRemoveNode = new JButton("Remove Node");
		btnRemoveNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String node = JOptionPane.showInputDialog(frame, "Enter the node which you want to remove.");
				// Check if valid node has been entered by the user.
				if (node == null || node.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Please enter a node.", "Data Required", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!graph.nodeExists(node)) {
					JOptionPane.showMessageDialog(frame, "Node does not exist. Plese enter a valid Node.", "Data Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				boolean delete = true;
				// If the node is valid call removeNode() method to remove the node from the network.
				if (removeNode(node)) {
					JOptionPane.showMessageDialog(frame, "Node successfully removed from the network topology.", "Node Removed", JOptionPane.INFORMATION_MESSAGE);
					
					// Update the result text boxes and print updated adjacency matrix.
					if (txtSource.getText().equalsIgnoreCase(node) || txtDestination.getText().equalsIgnoreCase(node)) {
						txtShortestDistance.setText("Node Deleted");
						txtShortestPaths.setText("Source or Destination Node has been deleted.");
						textPane.setText(Graph.printAdjacencyMatrix().replaceAll("-1", "--"));
						delete = false;
					} 
					
					if (txtFrom.getText().equalsIgnoreCase(node) || txtTo.getText().equalsIgnoreCase(node)) {
						allPaths.setText("Source or Destination Node has been deleted.");
						txtFrom.setText("");
						txtTo.setText("");
						delete = false;
					}
					
					// Call evaluate() method to perform routing and getting all paths again.
					if (delete) {
						evaluate();
					}
				}
			}
		});
		btnRemoveNode.setBounds(156, 27, 124, 35);
		addNodePanel.add(btnRemoveNode);
		
		evaluatePanel = new JPanel();
		evaluatePanel.setLayout(null);
		evaluatePanel.setBorder(BorderFactory.createTitledBorder("Peform Routing"));
		evaluatePanel.setBounds(43, 98, 292, 170);
		frame.getContentPane().add(evaluatePanel);
		
		txtSource = new JTextField();
		txtSource.setBounds(20, 28, 200, 30);
		evaluatePanel.add(txtSource);
		txtSource.setFont(textFont);
		txtSource.setText("Enter Source Node");
		txtSource.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent fe) {
				if (txtSource.getText().equalsIgnoreCase(sourcePromptText)) {
					txtSource.setText("");
				}
			}
			
			@Override
			public void focusLost(FocusEvent fe) {
				if (txtSource.getText().equalsIgnoreCase("")) {
					txtSource.setText(sourcePromptText);
				}
			}
		});
		txtSource.setColumns(10);
		
		txtDestination = new JTextField();
		txtDestination.setBounds(20, 75, 200, 30);
		evaluatePanel.add(txtDestination);
		txtDestination.setFont(textFont);
		txtDestination.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtDestination.getText().equalsIgnoreCase(destinPromptText)) {
					txtDestination.setText("");
				}	
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (txtDestination.getText().equalsIgnoreCase("")) {
					txtDestination.setText(destinPromptText);
				}
			}
		});
		txtDestination.setText("Enter Destination Node");
		txtDestination.setColumns(10);
		
		JButton txtRoute = new JButton("Route It");
		txtRoute.setBounds(20, 118, 125, 35);
		evaluatePanel.add(txtRoute);
		
		btnShowGraph = new JButton("Display Graph");
		btnShowGraph.setEnabled(false);
		btnShowGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// Display Graph with shortest path.
				(new GraphStream(Graph.getAdjacencyMatrix(), shortestPaths)).run();
			}
		});
		btnShowGraph.setBounds(155, 118, 125, 35);
		evaluatePanel.add(btnShowGraph);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(215, 48, 120, 35);
		frame.getContentPane().add(btnExit);
		
		allPathsPanel = new JPanel();
		allPathsPanel.setLayout(null);
		allPathsPanel.setBorder(BorderFactory.createTitledBorder("Get all Paths between two Nodes"));
		allPathsPanel.setBounds(1150, 48, 350, 526);
		frame.getContentPane().add(allPathsPanel);
		
		btnFindAll = new JButton("Get All Paths");
		btnFindAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Validate the From and To nodes. 
				if (txtFrom.getText().isEmpty() || txtTo.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Please enter FROM node and TO node.", "Data Required", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (!graph.nodeExists(txtFrom.getText().trim())) {
					JOptionPane.showMessageDialog(frame, "From Node does not exist. Plese enter a valid From Node.", "Source Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (!graph.nodeExists(txtTo.getText().trim())) {
					JOptionPane.showMessageDialog(frame, "To Node does not exist. Plese enter a valid To Node.", "Destination Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// If From and To node is valid, then get all the paths between the two nodes and display the result in appropriate text boxes.
				scrollPane.setBorder(BorderFactory.createTitledBorder(String.format("All Paths between Node %s and Node %s", txtFrom.getText(), txtTo.getText())));
				System.out.println(edges.size());
				graph = new Dijkstras(edges);
				
				paths = graph.getAllPaths(txtFrom.getText(), txtTo.getText());
				allPaths.setText(graph.printPaths(paths));
			}
		});
		btnFindAll.setBounds(12, 68, 125, 35);
		allPathsPanel.add(btnFindAll);
		
		allPaths = new JTextPane();
		allPaths.setEditable(false);
		allPaths.setFont(textFont);
		//textPane.setBounds(15, 25, 270, 170);
		allPaths.setOpaque(false);
		
		scrollPane = new JScrollPane(allPaths);
		scrollPane.setBorder(BorderFactory.createTitledBorder("All Paths"));
		scrollPane.setBounds(12, 116, 325, 397);
		allPathsPanel.add(scrollPane);
		
		lblFrom = new JLabel("From Node :");
		lblFrom.setBounds(12, 33, 75, 16);
		allPathsPanel.add(lblFrom);
		
		txtFrom = new JTextField();
		txtFrom.setColumns(10);
		txtFrom.setBounds(92, 30, 55, 25);
		allPathsPanel.add(txtFrom);
		
		lblTo = new JLabel("To Node :");
		lblTo.setBounds(159, 34, 65, 16);
		allPathsPanel.add(lblTo);
		
		txtTo = new JTextField();
		txtTo.setColumns(10);
		txtTo.setBounds(222, 30, 55, 25);
		allPathsPanel.add(txtTo);
		
		txtRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ea) {
				// Validate the Source and Destination 
				if (filePath == null) {
					JOptionPane.showMessageDialog(frame, "Graph (Matrix) file not selected.", "Select Graph File", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (txtSource.getText().equalsIgnoreCase("") || txtSource.getText().equalsIgnoreCase(sourcePromptText)) {
					JOptionPane.showMessageDialog(frame, "Source is empty.", "Source Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (txtDestination.getText().equalsIgnoreCase("") || txtDestination.getText().equalsIgnoreCase(destinPromptText)) {
					JOptionPane.showMessageDialog(frame, "Destination is empty.", "Destination Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (!graph.nodeExists(txtSource.getText().trim())) {
					JOptionPane.showMessageDialog(frame, "Source Node does not exist. Plese enter a valid Source Node.", "Source Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (!graph.nodeExists(txtDestination.getText().trim())) {
					JOptionPane.showMessageDialog(frame, "Destination Node does not exist. Plese enter a valid Destination Node.", "Destination Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// If the Source and Destination is not empty and valid calling evaluate() method to perform routing and display results.
				btnShowGraph.setEnabled(true);
				evaluate();
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				newNodeWindow = new JDialog(frame, "Enter new node data", Dialog.ModalityType.DOCUMENT_MODAL);
				JPanel dialogWindow = newNodeWindow();
				
				newNodeWindow.getContentPane().add(dialogWindow);
				newNodeWindow.setBounds(50, 50, dialogWindow.getWidth(), dialogWindow.getHeight());
				newNodeWindow.setVisible(true);
			}
		});
		
		setEnabled(addNodePanel.getComponents(), false);
		setEnabled(modifyLinkPanel.getComponents(), false);
		setEnabled(evaluatePanel.getComponents(), false);
		setEnabled(allPathsPanel.getComponents(), false);
	}
	
	/***
	 * This method displays a File Choose window to the user so that the user can select the text file which contains the network topology matrix.
	 * @param parent	Parent frame window which will be calling the File Choose window.
	 */
	private void loadFile(JFrame parent) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		fileChooser.setAcceptAllFileFilterUsed(true);
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    filePath = selectedFile.getAbsolutePath();
		}
	}
	
	/**
	 * This method builds a panel which displays a new node window. The window is used to accept the link weights of the new node to other nodes.
	 * @return	Returns a JPanel reference which contains the window to be used to ad  a new window.
	 */
	private JPanel newNodeWindow() {
		txtFieldsList = new ArrayList<JTextField>();
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		int x = 30, y = 20, height = 32, labelWidth = 190, textWidth = 60;
		
		Font font = new Font("Tahoma", Font.PLAIN, 14);
		
		JLabel label = new JLabel();
		label.setText("ENTER LINK WEIGHT FROM THE NEW NODE TO OTHER NODES");
		label.setForeground(Color.red);
		label.setBounds(x, y, 450, height);
		label.setFont(font);
		panel.add(label);
		
		label = new JLabel();
		label.setForeground(Color.red);
		label.setBounds(x, y + y, 450, height);
		label.setFont(font);
		label.setText("LEAVE BLANK IF THERE IS NO CONNECTION TO THE NODE");
		panel.add(label);
		
		int nodes = graph.getNodesCount();
		y += 15;
		
		Set<String> nodeNames = graph.getNodes();
		for (String string : nodeNames) {
			y += 50;
			
			JLabel promptLabel = new JLabel();
			promptLabel.setText("Enter Link Weight to Node " + string + ": ");
			promptLabel.setBounds(x, y, labelWidth, height);
			promptLabel.setFont(font);
			panel.add(promptLabel);
			
			JTextField textField = new JTextField();
			textField.setFont(font);
			//textField.setText("0");
			textField.setBounds(x + labelWidth, y, textWidth, height);
			textField.setToolTipText("Distance to Node " + string);
			txtFieldsList.add(textField);
			panel.add(textField);
		}
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(x, y + 50, 100, 35);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (addNewNode()) {
					graph = new Dijkstras(edges);
					evaluate();
					JOptionPane.showMessageDialog(frame, "New node added successfully.", "New Node Added", JOptionPane.INFORMATION_MESSAGE);
					newNodeWindow.dispose();
				} else {
					JOptionPane.showMessageDialog(frame, "New node could not be added. Please check is you have mentioned atleast one link weight.", "New Node Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(btnSubmit);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(x + 120, y + 50, 100, 35);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				newNodeWindow.dispose();
			}
		});
		panel.add(btnCancel);
		
		panel.setBounds(20, 20, x + labelWidth + textWidth + 190, y + 150);
		return panel;
	}
	
	/**
	 * This method is used to extract the data from the New Node window and call the addNode() method which basically adds a new node to the network topology.
	 * @return	Returns true if the node is added successfully to the network topology.
	 */
	private boolean addNewNode() {
		//int[] linkWeights = new int[graph.getNodesCount()];
		Map<Integer, Integer> linkWeights = new HashMap<>();
		
		int i = 0, noLink = 0;
		
		try {
			// Read link weights to all other existing nodes from the new nodes.
			for (JTextField textField : txtFieldsList) {
				String tooltip = textField.getToolTipText();
				String node = tooltip.substring(tooltip.lastIndexOf(" ") + 1, tooltip.length());

				if(textField.getText().isEmpty()) {
					noLink++;
					linkWeights.put(Integer.parseInt(node), -1);
				} else {
					linkWeights.put(Integer.parseInt(node), Integer.parseInt(textField.getText().trim()));
				}
			}
			// If no link weight is enetered return false indicating node could not be added.
			if (noLink == graph.getNodesCount()) {
				return false;
			}
			return addNode(linkWeights);
		} catch (NumberFormatException e) {
		
		}
		return false;
	}
	
	/***
	 * This method does multiple things and is called for routing as well as any change is done to the network.
	 * It calculates the shortest path(s) between two nodes and sets the data in the appropriate text fields.
	 * It calculates all the paths between two nodes and sets the data in the appropriate text fields.
	 */
	private void evaluate() {
		textPane.setText(Graph.printAdjacencyMatrix().replaceAll("-1", "--"));
		
		source = txtSource.getText().trim();
		destination = txtDestination.getText().trim();
		
		// Reset the result text fields.
		txtShortestDistance.setText("-");
		txtShortestPaths.setText("No Path exists between source node and destination node.");
		allPaths.setText("No Path exists between source node and destination node.");
		
		txtConnection.setText(graph.printRoutingTable(edges));
		
		if (!source.isEmpty() && !source.equals(sourcePromptText) && !destination.isEmpty() && !destination.equals(destinPromptText)) {		
			// Get the shortest path(s) from source and destination nodes (if specified) and set the data to appropriate text fields.
			graph = new Dijkstras(edges);
			graph.route(source);
			shortestPaths = graph.getAllShortestPathsTo(destination);
			txtShortestPaths.setText("SHORTEST " + graph.printPaths(shortestPaths));	
			txtShortestDistance.setText(Integer.toString(graph.getDistance(shortestPaths)));
			//txtConnection.setText(graph.printAllConnectionTables());
			
			// Get all the paths from source and destination nodes (if specified) and set the data to appropriate text fields.
			if (!txtFrom.getText().isEmpty() && !txtTo.getText().isEmpty()) {
				paths = graph.getAllPaths(txtFrom.getText(), txtTo.getText());
				allPaths.setText(graph.printPaths(paths));
			} else {
				paths = graph.getAllPaths(source, destination);
				allPaths.setText(graph.printPaths(paths));
				txtFrom.setText(source);
				txtTo.setText(destination);
				scrollPane.setBorder(BorderFactory.createTitledBorder(String.format("All Paths between Node %s and Node %s", source, destination)));
			}
		}
	}
	
	/***
	 * This method checks if a link/edge exists between the two nodes specified in the parameter.
	 * @param from	Name of the first node of the link/edge.
	 * @param to	Name of the second node of the link/edge.
	 * @return		Returns true if there is a link/edge else returns false.	
	 */
	private boolean checkLink(String from, String to) {
		for (Edge edge : edges) {
			if (edge.v1.equalsIgnoreCase(from) && edge.v2.equalsIgnoreCase(to)) {
				return true;
			}
		}
		return false;
	}
	
	/***
	 * This method modifies the distance or link weight of an link/edge to the specified new distance.
	 * @param from		Name of the first node of the link/edge.
	 * @param to		Name of the second node of the link/edge.
	 * @param distance	New Distance or link weight between the two nodes.
	 */
	private void modifyLink(String from, String to, int distance) {
		for (Edge edge : edges) {
			if ((edge.v1.equalsIgnoreCase(from) && edge.v2.equalsIgnoreCase(to)) || (edge.v1.equalsIgnoreCase(to) && edge.v2.equalsIgnoreCase(from))) {
				edge.distance = distance;
			}
		}
		Graph.modifyLink(from, to, distance);
	}
	
	/**
	 * This method adds a link/edge from one node to other node.
	 * @param from		Name of the first node of the link/edge.
	 * @param to		Name of the second node of the link/edge.
	 * @param distance	Distance or link weight between the two nodes.
	 */
	private void addLink(String from, String to, int distance) {
		edges.add(new Edge(from, to, distance));
		edges.add(new Edge(to, from, distance));
		Graph.modifyLink(from, to, distance);
	}
	
	/***
	 * This method adds a node to the network topology. It adds the edges from the new node to other nodes to our EDGES ArrayList
	 * @param distances	A Map containing Edge's To node as Key and Distance to that node as Value.
	 * @return			Returns true if the node is added successfully.
	 */
	private boolean addNode(Map<Integer, Integer> distances) {
		int nodesCount = graph.getNodesCount();
		
		for (Map.Entry link : distances.entrySet()) {
			if ((int) link.getValue() != -1) {
				edges.add(new Edge(Integer.toString(nodesCount + 1), Integer.toString((int) link.getKey()), (int) link.getValue()));
				edges.add(new Edge(Integer.toString((int) link.getKey()), Integer.toString(nodesCount + 1), (int) link.getValue()));
			}
		}
		//System.out.println("Success addNode");
		return Graph.addNode(distances);
	}
	
	/***
	 * This method is used to remove a node from the network topology. It removes all the edges from the node to be deleted to other node.
	 * @param node	Name of the node in String format which has to be removed from the network,
	 * @return		Returns true if the node was successfully removed else returns false.
	 */
	private boolean removeNode(String node) {
		ArrayList<Edge> delete = new ArrayList<>();
		
		// Removing edges linked from the node to be deleted to all other nodes.
		for (Edge edge : edges) {
			if (edge.v1.equalsIgnoreCase(node) || edge.v2.equalsIgnoreCase(node)) {
				delete.add(edge);
			}
		}

		edges.removeAll(delete);	
		graph.removeNode(node);
		return Graph.removeNode(node);
	}
	
	/***
	 * This method enables/disables all the components in the array.
	 * @param components	Array of Component object containing the Component which has be enabled/disabled.
	 * @param enable		Set it to true to enable or false to disable.
	 */
	private void setEnabled(Component[] components, boolean enable) {
		for (Component component : components) {
			component.setEnabled(enable);
		}
	}
}
