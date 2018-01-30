package modelisation.graphe;

import java.util.ArrayList;
import java.util.Stack;

import modelisation.Heap;

import java.io.*;

public class Graph {
	
   private ArrayList<Edge>[] adj; /* tableau d'ArrayList de Edge */  
   private final int V; /* nombre de sommets */
   int E; /* nombre d'arêtes */
   
   
   @SuppressWarnings("unchecked")
   public Graph(int N) {
	   this.V = N;
	   this.E = 0;
	   
	   /* tableau d'ArrayList de Edge */
	   adj = (ArrayList<Edge>[]) new ArrayList[N];
	   
	   for (int v = 0; v < N; v++) {
		   adj[v] = new ArrayList<Edge>();
	   }
   }

   public int vertices() {
	   return V;
   }
   
   public void addEdge(Edge e) {
		int v = e.from;
		int w = e.to;
		
		adj[v].add(e);
		adj[w].add(e);
   }
   
   public Iterable<Edge> adj(int v) {
       return adj[v];
   }      

   public Iterable<Edge> next(int v) {
	   ArrayList<Edge> n = new ArrayList<Edge>();
		
	   for (Edge e : adj(v)) {
		  if (e.to != v)
			n.add(e);
	   }
	   
	   return n;
   }      
   
   public Iterable<Edge> edges() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		
        for (int v = 0; v < V; v++) {
            for (Edge e : adj(v)) {
                if (e.to != v) {
                    list.add(e);
                }
            }
        }
        
        return list;
   }
   
   public Iterable<Edge> edges(int[] ordre) {
	   ArrayList<Edge> list = new ArrayList<Edge>();
	   
	   for (int v = 0; v < V; v++) {
		   for (Edge e : adj(ordre[v])) {
               if (e.to != v) {
                   list.add(e);
               }
           }
	   }
	   
	   return list;
   }
   
   public void writeFile(String s) {
	   try {			 
			 PrintWriter writer = new PrintWriter(s, "UTF-8");
			 
			 writer.println("digraph G{");
			 
			 for (Edge e : edges()) {
			   writer.println(e.from + "->" + e.to + "[label=\"" + e.cost + "\"];");
			 }
			 
			 writer.println("}");
			 writer.close();
	   }
	   catch (IOException e) {
		   System.err.println("impossible d'écrire le fichier");
		   System.exit(1);
	   }						
   }
   
   /**
    * dfs pour le tri troplogique
    * @param u
    * @param pile
    * @param v
    */
   public void dfs(int u, Stack<Integer> pile, boolean[] v) {
	   v[u] = true;
	   
	   for (Edge e : next(u)) {
		   if (!v[e.to]) {
			    dfs(e.to, pile, v);
		    }
	   }
	   
	   pile.push(u);
   }
   
   /**
    * tri topologique
    * @return sommets dans l'ordre topologique
    */
   public int[] topo() {
	   Stack<Integer> pile = new Stack<Integer>();
	   boolean[] v = new boolean[vertices()];
	   int[] ordre = new int[vertices()];
   	      	   
   	   for (int i = 0; i < vertices(); i++) {
   		   v[i] = false;
   		   ordre[i] = -1;
   	   }
   	   
   	   for (int i = 0; i < vertices(); i++) {
   		   if (!v[i]) {
			   dfs(i, pile, v);
		   }
   	   }
   	   
   	   /* On sait que l'on a bien V valeurs empilées */
   	   for (int i = 0; i < vertices() ; i++) {
   		   ordre[i] = pile.pop();
   	   }
   	   
   	   return ordre;
   }
   
   /**
    * algorithme de Bellman-Ford
    * les sommets sont traités dans l'ordre du tri topologique
    * @param sommet source
    * @return tableau des coûts minimaux, null si présence d'un cycle de coût négatif
    */
   public int[] bellman(int s) {
	   int[] d = new int[vertices()];
	   
	   /* On initialise le tableau des distances
	      d[i] = +infinity */
	   for (int i = 0; i < d.length; i++) {
		   d[i] = Integer.MAX_VALUE;
	   }

	   d[s] = 0;
	   
	   boolean changement;
	   
	   for (int i = 0; i < vertices(); i++) {
		   changement = false;
		   
		   /* On parcourt chaque arête */
		   for (Edge e : edges(topo())) {
			   if (d[e.to] > (d[e.from] + e.cost)) {
				   d[e.from] = d[e.to] + e.cost;
				   changement=true;
			   }
		   }
		   
		   /* Le tableau des distances s'est stabilisé */
		   if (changement == false) {
			   return d;
		   }
	   }
	   
	   /* Au moins une valeur du tableau a changé lors de la dernière itération = cycle négatif */
	   return null;
   }
   
   /**
    * algorithme de Dijkstra
    * @param s
    * @return tableau des plus courts chemins
    */
   public int[] dijkstra(int s) {
	   Heap H = new Heap(vertices());
	   boolean[] v = new boolean[vertices()];
	   int[] d = new int[vertices()];
	   	   
	   for (int i = 0; i < vertices(); i++) {
		   v[i] = false;
		   d[i] = Integer.MAX_VALUE;
	   }
	   
	   H.decreaseKey(s, 0);
	   d[s] = 0;
	   
	   for (int i = 0; i < vertices(); i++) {
		   int min = H.pop();
		   
		   v[min] = true;
		   
		   for (Edge e : next(min)) {
			   if (!v[e.to]) {
				   if (d[e.to] > e.cost + d[e.from]) {
					   H.decreaseKey(e.to, e.cost + d[e.from]);	
					   d[e.to] = e.cost = d[e.from];
				   }
			   }
		   }
	   }
	   
	   return d;
   }
   
}