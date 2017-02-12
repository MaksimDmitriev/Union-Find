package uf;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Graph graph = new Graph();
		graph.addAdge(0, 1);
		graph.addAdge(1, 2);
		graph.addAdge(0, 2);
		graph.addVertex(78);
		System.out.println(graph.connectedComponentsCount());
	}

	private static class Graph {

		private Set<Integer> vertices = new HashSet<>();
		private Set<Edge> edges = new HashSet<>();

		void addVertex(int vertex) {
			if (!vertices.contains(vertex)) {
				vertices.add(vertex);
			}
		}

		void addAdge(int src, int dst) {
			if (!vertices.contains(src)) {
				vertices.add(src);
			}
			if (!vertices.contains(dst)) {
				vertices.add(dst);
			}
			Edge edge = new Edge(src, dst);
			if (!edges.contains(edge)) {
				edges.add(edge);
			}
		}

		int connectedComponentsCount() {
			int components = vertices.size();

			// make sets
			LinkedList[] sets = new LinkedList[vertices.size()];
			Iterator<Integer> verticesIterator = vertices.iterator();
			int i = 0;
			while (verticesIterator.hasNext()) {
				sets[i] = new LinkedList();
				sets[i].append(new ListNode(verticesIterator.next()));
				i++;
			}

			// union
			for (Edge edge : edges) {
				LinkedList srcSet = find(sets, new ListNode(edge.src));
				LinkedList dstSet = find(sets, new ListNode(edge.dst));
				if (!srcSet.head.equals(dstSet.head)) {
					union(srcSet, dstSet);
					components--;
				}
			}
			return components;
		}

		// returns a representative of the set to which the given vertex belongs
		LinkedList find(LinkedList[] sets, ListNode x) {
			for (LinkedList set : sets) {
				Iterator<ListNode> iterator = set.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().equals(x)) {
						return set;
					}
				}
			}
			throw new IllegalStateException("ListNode x = " + x + " wasn't found");
		}

		void union(LinkedList xSet, LinkedList ySet) {
			xSet.append(ySet);
		}
	}

	private static class LinkedList implements Iterable<ListNode> {

		ListNode head;
		ListNode tail;

		void append(ListNode node) {
			if (head == null) {
				head = tail = node;
			} else {
				tail.next = node;
				tail = node;
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder("[");
			Iterator<ListNode> iterator = iterator();
			while (iterator.hasNext()) {
				builder.append(iterator.next());
				if (iterator.hasNext()) {
					builder.append(", ");
				}
			}
			return builder.append("]").toString();

		}

		// As far as union-find is concerned, we don't append anything to an
		// emply list.
		// So we can omit the if(head == null) branch
		void append(LinkedList list) {
			tail.next = list.head;
		}

		@Override
		public Iterator<ListNode> iterator() {
			return new Iterator<ListNode>() {

				ListNode current = head;

				@Override
				public ListNode next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}
					ListNode temp = current;
					current = current.next;
					return temp;
				}

				@Override
				public boolean hasNext() {
					return current != null;
				}
			};
		}
	}

	private static class ListNode {
		ListNode representative;
		ListNode next;
		int key;

		ListNode(int key) {
			this.key = key;
		}

		@Override
		public int hashCode() {
			return key;
		}

		@Override
		public String toString() {
			return Integer.toString(key);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof ListNode)) {
				return false;
			}
			ListNode other = (ListNode) obj;
			return key == other.key;
		}
	}

	private static class Edge {
		int src;
		int dst;

		Edge(int src, int dst) {
			this.src = src;
			this.dst = dst;
		}

		@Override
		public String toString() {
			return "[" + src + " " + dst + "]";
		}

		@Override
		public int hashCode() {
			int res = 17;
			res = 31 * res + src;
			res = 31 * res + dst;
			return res;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Edge)) {
				return false;
			}
			Edge other = (Edge) obj;
			return other.src == src && other.dst == dst;
		}
	}
}
