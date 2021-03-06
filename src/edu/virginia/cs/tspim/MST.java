package edu.virginia.cs.tspim;


import java.util.*;

import edu.virginia.cs.tspim.util.Config;
import edu.virginia.cs.tspim.util.Util;

import java.lang.*;
import java.io.*;

public class MST {
	

	// A class to represent a graph edge
	public static class Edge implements Comparable<Edge> {
		int src, dest;
		public int getSrc() {
			return src;
		}

		public void setSrc(int src) {
			this.src = src;
		}

		public int getDest() {
			return dest;
		}

		public void setDest(int dest) {
			this.dest = dest;
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		double weight;
		
		// Comparator function used for sorting edges
		// based on their weight
		public int compareTo(Edge compareEdge) {
			if (this.weight < compareEdge.weight)
				return -1;

			if (this.weight > compareEdge.weight)
				return 1;

			return 0;

		}
		
	};

	// A class to represent a subset for union-find
	class subset {
		int parent, rank;
	};

	int V, E; // V-> no. of vertices & E->no.of edges
	public Edge edge[]; // collection of all edges
	ArrayList<Node> nodeList; 
	ArrayList<TreeEdges> tree = new ArrayList<TreeEdges>();
	int [][]edgeList;
	int check_all_nodes[];
	
	// Creates a graph with V vertices and E edges
	public MST(int v, int e, List<Node> nodeList) {
		V = v;
		E = e;
		Util.logln(V);
		Util.logln(E);
		this.nodeList= (ArrayList<Node>) nodeList;
		check_all_nodes= new int[nodeList.size()];
		edge = new Edge[E];
		for (int i = 0; i < e; ++i)
			edge[i] = new Edge();
	}

	
	public Edge getEdge(int i){
		return this.edge[i];
	}
	
	// A utility function to find set of an element i
	// (uses path compression technique)
	int find(subset subsets[], int i) {
		// find root and make root as parent of i (path compression)
		if (subsets[i].parent != i)
			subsets[i].parent = find(subsets, subsets[i].parent);

		return subsets[i].parent;
	}

	// A function that does union of two sets of x and y
	// (uses union by rank)
	void Union(subset subsets[], int x, int y) {
		int xroot = find(subsets, x);
		int yroot = find(subsets, y);

		// Attach smaller rank tree under root of high rank tree
		// (Union by Rank)
		if (subsets[xroot].rank < subsets[yroot].rank)
			subsets[xroot].parent = yroot;
		else if (subsets[xroot].rank > subsets[yroot].rank)
			subsets[yroot].parent = xroot;

		// If ranks are same, then make one as root and increment
		// its rank by one
		else {
			subsets[yroot].parent = xroot;
			subsets[xroot].rank++;
		}
	}

	// The main function to construct MST using Kruskal's algorithm
	public ArrayList<TreeEdges> KruskalMST() throws IOException {
		//Util.logln("Inside Kruskal MST");
		Edge result[] = new Edge[V]; // Tnis will store the resultant MST
		int e = 0; // An index variable, used for result[]
		int i = 0; // An index variable, used for sorted edges
		for (i = 0; i < V; ++i)
			result[i] = new Edge();

		Arrays.sort(edge);

		// Allocate memory for creating V subsets
		subset subsets[] = new subset[V];
		for (i = 0; i < V; ++i)
			subsets[i] = new subset();

		// Create V subsets with single elements
		for (int v = 0; v < V; ++v) {
			subsets[v].parent = v;
			subsets[v].rank = 0;
		}

		i = 0; // Index used to pick next edge

		// Number of edges to be taken is equal to V-1
		while (e < V-1) {
			Edge next_edge = new Edge();
			next_edge = edge[i++];
			int x = find(subsets, next_edge.src);
			int y = find(subsets, next_edge.dest);

			// If including this edge does't cause cycle,
			// include it in result and increment the index
			// of result for next edge
			if (x != y) {
				result[e++] = next_edge;
				Union(subsets, x, y);
			}
			// Else discard the next_edge
		}
		int [][]edgeList=new int[nodeList.size()][2];
		for (i = 0; i < e; ++i){
			
			TreeEdges ed = new TreeEdges();
			ed.set_s(nodeList.get(result[i].src));
			ed.set_d(nodeList.get(result[i].dest));
			ed.set_weight(result[i].weight);
			edgeList[i][0] = result[i].src;
			edgeList[i][1] = result[i].dest;
			tree.add(ed);
			//System.out.println(result[i].src + " -- " + result[i].dest + " == " + result[i].weight);
		}		
		for(i=0;i<result.length;i++)
		{
			++check_all_nodes[result[i].src];
			++check_all_nodes[result[i].dest];
		}
		
		ConnectedComponents ob = new ConnectedComponents();
		Set<Node> n = new HashSet<Node>();
		for(TreeEdges ef: tree)
		{
			n.add(ef.s);
			n.add(ef.d);
			
		}
		
		Util.logln("Nodes Size " + n.size()); 
		Util.logln("Number of Edges : " + tree.size());
		Collection<TreeEdges> edgesSet = new HashSet<>();
		//edgesSet.addAll(tree);
		for(TreeEdges edge : tree){
			if(!edgesSet.contains(edge)){
				edgesSet.add(edge);
			}
			else{
				Util.logln(edge);
			}
		}
		Util.logln("Number of Edges in EdgeSet : " + edgesSet.size());
		return tree;
	}
}
