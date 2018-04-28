package hw5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A graph used to generate a perfect hash table.
 *
 * @author Stamatios Morellas (morellas@iastate.edu)
 */
public class Graph {
	/**
	 * The vertices of the graph.
	 */
	private final Vertex[] vertices;

	/**
	 * Initializes the graph to have the given number of vertices.
	 *
	 * @param vertexCount
	 *            the number of vertices in the graph
	 *
	 * @throws IllegalArgumentException
	 *             if {@code vertexCount} is negative
	 */
	public Graph(int vertexCount) throws IllegalArgumentException { 
		// TODO – DONE
		
		// If the number of vertices is negative
		if (vertexCount < 0) {
			// Throw an exception
			throw new IllegalArgumentException(String.valueOf(vertexCount));
		}

		// Construct the vertices array
		this.vertices = new Vertex[vertexCount];

		// Step through the vertices array and create a new GraphVertex out of each Vertex in the array
		for (int i = 0; i < vertexCount; i++) {
			this.vertices[i] = new GraphVertex(i);
		}
	}

	/**
	 * Initializes the graph to use the given vertex array. Performs no other
	 * initialization.
	 *
	 * @param vertexArray
	 *            the vertex array to use
	 */
	public Graph(Vertex[] vertexArray) {
		/*
		 * For grading. Do not change this constructor.
		 */
		this.vertices = vertexArray;
	}

	/**
	 * Returns the array of vertices used by this graph.
	 *
	 * @return the array of vertices used by this graph
	 */
	public Vertex[] getVertices() {
		/*
		 * For grading. Do not change this method.
		 */
		return vertices;
	}

	/**
	 * Adds an undirected edge between the two indicated vertices.
	 *
	 * @param fromIdx
	 *            the index of the from vertex
	 * @param toIdx
	 *            the index of the to vertex
	 * @param index
	 *            the index of the data of the edge to add
	 * @param word
	 *            the data of the edge to add
	 *
	 * @throws IndexOutOfBoundsException
	 *             if either of {@code fromIdx} and {@code toIdx} are invalid vertex
	 *             indices
	 * @throws NullPointerException
	 *             if {@code word} is {@code null}
	 */
	public void addEdge(int fromIdx, int toIdx, int index, String word) throws IndexOutOfBoundsException, NullPointerException { // DONE
		// TODO – DONE
		
		// Handle Exceptions
		if (fromIdx < 0 || fromIdx >= vertices.length || toIdx < 0 || toIdx >= vertices.length) { // If fromIdx or toIdx are invalid indices
			throw new IndexOutOfBoundsException("The indices in the addEdge() method is invalid");
		}
		if (word == null) {
			throw new NullPointerException("The data of the edge you wish to add is null");
		}
		
		// Add an edge from "from" to "to"
		vertices[fromIdx].edges().add(new GraphEdge(index, word, vertices[fromIdx], vertices[toIdx]));
		// Add an edge from "to" to "from"
		vertices[toIdx].edges().add(new GraphEdge(index, word, vertices[toIdx], vertices[fromIdx]));
	}

	/**
	 * Marks all vertices and edges within the graph as unvisited.
	 */
	public void unvisitAll() {
		// TODO - DONE

		// Step through the vertices array and mark each vertex as unvisited
		for (Vertex v : vertices) {
			v.unvisit();
		}
	}

	/**
	 * Creates and fills a G array for this graph.
	 *
	 * @param words
	 *            the number of keys in the hash table
	 * @return the populated G array
	 */
	public int[] fillGArray(int words) {
		
		// Mark all vertices and edges within the graph as unvisited
		unvisitAll();

		// The integer array to be returned
		int[] toRet = new int[vertices.length];

		for (Vertex v : vertices) {
			if (!v.isVisited()) {
				toRet[v.index()] = 0;
				v.fillGArray(toRet, words);
			}
		}

		return toRet;
	}

	/**
	 * Returns true if and only if this graph contains a cycle.
	 *
	 * @return whether this graph contains a cycle
	 */
	public boolean hasCycle() {
		// TODO - DONE

		// For each vertex in the vertices array
		for (Vertex v : vertices) {
			// If the vertex is visited and it has a cycle from itself
			if (!v.isVisited() && v.hasCycle(v)) {
				// The graph contains a cycle
				return true;
			}
		}
		// Return false if the graph doesn't contain a cycle
		return false;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();

		build.append("Graph with ").append(vertices.length).append(" vertices:").append(System.lineSeparator());

		for (Vertex v : vertices) {
			build.append("  ").append(v).append(System.lineSeparator());
		}

		if (vertices.length > 0) {
			// remove trailing newline
			build.setLength(build.length() - System.lineSeparator().length());
		}

		return build.toString();
	}

	private class GraphVertex implements Vertex {
		/**
		 * Whether this vertex is marked as visited.
		 */
		private boolean visited;

		/**
		 * The index of this vertex within the vertices array.
		 */
		private final int index;

		/**
		 * Outgoing edges from this vertex.
		 */
		private final Collection<Edge> edges;

		/**
		 * Initializes the vertex.
		 *
		 * @param index
		 *            the index of the vertex within the vertices array
		 */
		public GraphVertex(int index) {
			this.visited = false;
			this.index = index;
			this.edges = new ArrayList<>();
		}

		@Override
		public boolean isVisited() {
			return visited;
		}

		@Override
		public void setVisited(boolean visited) {
			// TODO - DONE
			
			// Set visited to the parameter
			this.visited = visited;
			
			// If the parameter is false
			if (visited == false) {
				// Create a new iterator to step through the edges
				Iterator<Edge> iterator = edges.iterator();
				// While the iterator has a next edge
				while (iterator.hasNext()) {
					// Mark the next edge in the iterator as unvisited
					iterator.next().unvisit();
				}
			}
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public Collection<Edge> edges() {
			return edges;
		}

		@Override
		public void fillGArray(int[] g, int words) throws IndexOutOfBoundsException {
			
			this.visit();

		    for (Edge edge : edges()) {
		        if (!edge.isVisited()) {
		          
		        		edge.visit();
		          
		        		// mark other direction of the edge as visited
		        		for (Edge neighborEdge : edge.getTo().edges()) {
		        			if (this == neighborEdge.getTo()) {
		        				neighborEdge.visit();
		        				break;
		        			}
		        		}

		        		// visit neighbor
		        		if (!edge.getTo().isVisited()) {
		        			g[edge.getTo().index()] = (edge.index() - g[index()] + words) % words;
		        			edge.getTo().fillGArray(g, words);
		        		}
		        	}
		    }  
		}

		@Override
		public boolean hasCycle(Vertex from) {
			// TODO - DONE
			
			// If this GraphVertex is visited
			if (this.isVisited()) {
				return true; // A cycle has been detected
			}
			
			// Otherwise
			else {
				
				// Mark this GraphVertex as visited
				this.setVisited(true);
				
				// Create an iterator to check the neighboring edges (except Vertex from)
				Iterator<Edge> iterator = this.edges.iterator();			
				
				// While the iterator contains another edge
				while (iterator.hasNext()) {
					
					// The current edge
					Edge currentEdge = iterator.next();
					
					// If the currentEdge's "to" vertex is NOT the same as the "from" vertex and 
					// the currentEdge's "to" vertex has a cycle from this GraphVertex
					if (!currentEdge.getTo().equals(from) && currentEdge.getTo().hasCycle(this)) {
						return true; // A cycle has been detected
					}
					
					// Also, if the currentEdge's "to" vertex is the same as this GraphVertex
					if (currentEdge.getTo().equals(this)) {
						return true; // A cycle has been detected
					}
				}
			}
			// Return false if the graph doesn't contain a cycle
			return false;
		}

		@Override
		public String toString() {
			return "v[" + index() + "]: " + edges().toString();
		}
	}

	private class GraphEdge implements Edge {
		/**
		 * Whether this edge is marked as visited.
		 */
		private boolean visited;

		/**
		 * The index of the data of this edge.
		 */
		private final int index;

		/**
		 * The data of this edge.
		 */
		private final String data;

		/**
		 * The vertex from which this edge is outgoing.
		 */
		private final Vertex from;

		/**
		 * The vertex to which this edge is incoming.
		 */
		private final Vertex to;

		/**
		 * Initializes the edge.
		 *
		 * @param index
		 *            the index of the data of the edge
		 * @param data
		 *            the data of the edge
		 * @param from
		 *            the vertex from which the edge is outgoing
		 * @param to
		 *            the vertex to which the edge is incoming
		 */
		public GraphEdge(int index, String data, Vertex from, Vertex to) {
			this.visited = false;
			this.index = index;
			this.data = data;
			this.from = from;
			this.to = to;
		}

		@Override
		public boolean isVisited() {
			return visited;
		}

		@Override
		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public String data() {
			return data;
		}

		@Override
		public Vertex getFrom() {
			return from;
		}

		@Override
		public Vertex getTo() {
			return to;
		}

		@Override
		public String toString() {
			StringBuilder build = new StringBuilder();

			build.append("GraphEdge@").append(hashCode()) // address (unique per instance)
					.append(": ").append(data()).append(" (").append(index()).append("), v[").append(getFrom().index())
					.append("]-v[").append(getTo().index()).append("]");
			return build.toString();
		}
	}
}
