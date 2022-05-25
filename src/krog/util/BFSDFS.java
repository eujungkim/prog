package krog.util;

import java.util.LinkedList;
import java.util.Queue;

public class BFSDFS {
	// Breadth First Search - sibling first
	public void bfs(int n) {
		boolean[] visited = new boolean[n + 1];
		boolean[][] edge = new boolean[n + 1][n + 1];
		//TODO edge[i][j] = true;

		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(1);

		while (!queue.isEmpty()) {
			int data = queue.poll();
			System.out.println(">>" +  data);
			for (int i = 0; i <= n; i++) {
				if (!visited[i] && edge[data][i]) {
					visited[i] = true;
					queue.add(i);
				}
			}
		}
	}
	
	// Depth First Search - child first
	int n;
	boolean[] visited;
	boolean[][] edge;
	public void runDFS(int n) {
		visited = new boolean[n + 1];
		edge = new boolean[n + 1][n + 1];
		//TODO edge[i][j] = true;

		dfs(1);
	}
	
	public void dfs(int cur) {
		visited[cur] = true;
		System.out.println(">>" + cur);
		for (int i = 0; i <= n; i++) {
			if (!visited[i] && edge[cur][i]) {
				dfs(i);
			}
		}
	}
}
