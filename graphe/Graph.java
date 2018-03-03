package modelisation.graphe;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import modelisation.Heap;

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
   
   public Iterable<Edge> prev(int v) {
	   ArrayList<Edge> n = new ArrayList<Edge>();
		
	   for (Edge e : adj(v)) {
		  if (e.from != v)
			n.add(e);
	   }
	   
	   return n;
   }
   
   public Iterable<Edge> next(int v) {
	   ArrayList<Edge> n = new ArrayList<Edge>();
		
	   for (Edge e : adj(v)) {
		  if (e.to != v)
			n.add(e);
	   }
	   
	   return n;
   }      
   
   public Edge edge(int from, int to) {
	   for (Edge e : next(from)) {
		   if (e.to == to) {
			   return e;
		   }
	   }
	   
	   return null;
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
		   System.err.println("Impossible d'écrire le fichier");
		   System.exit(1);
	   }						
   }
   
   public void writeFile(String s, int[] d) {
	   try {			 
			 PrintWriter writer = new PrintWriter(s, "UTF-8");
			 
			 writer.println("digraph G{");
			 
			 for (int i=0;i<vertices();i++) {
				 writer.println(i + " [label=\""+d[i]+"\"];"); 
			 }
			 
			 for (Edge e : edges()) {
			   writer.println(e.from + "->" + e.to + "[label=\"" + e.cost + "\"];");
			 }
			 
			 writer.println("}");
			 writer.close();
	   }
	   catch (IOException e) {
		   System.err.println("Impossible d'écrire le fichier");
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
   	   
   	   /* nb valeurs empilées = nb de sommets */
   	   for (int i = 0; i < vertices() ; i++) {
   		   ordre[i] = pile.pop();
   	   }
   	   
   	   return ordre;
   }
   
   /**
    * algorithme de Bellman-Ford
    * les sommets sont traités dans l'ordre du tri topologique
    * @param sommet pour lequel on veut les CCM
    * @return parents, null si présence d'un cycle de coût négatif
    */
   public int[] bellman(int s) {
	   assert s >= 0;
	   assert s < vertices();
	   
	   int[] d = new int[vertices()];
	   int[] p = new int[vertices()];
	   
	   for (int i = 0; i < vertices(); i++) {
		   d[i] = Integer.MAX_VALUE;
		   p[i] = -1;
	   }

	   d[s] = 0;
	   
	   int modifie = 0;
	   int i = 0;
	   
	   while (i < vertices() && modifie != -1) {
		   modifie = -1;
		   
		   for (Edge e : edges(topo())) {
			   long x = d[e.to];
			   long y = ((long) d[e.from]) + ((long) e.cost);
			   
			   if (x > y) {
				   d[e.to] = d[e.from] + e.cost;
				   p[e.to] = e.from;
				   modifie = e.from;
			   }
		   }
		   
		   i ++;
	   }
	   
	   if (modifie != -1) {
		   /* cycle négatif */
		   return null;
	   }
	   else {
		   /* d s'est stabilisé */
		   return p;
	   }
   }
   
   /**
    * algorithme de Bellman-Ford
    * les sommets sont traités dans l'ordre du tri topologique
    * @param sommet pour lequel on veut les CCM
    * @return parents, null si présence d'un cycle de coût négatif
    */
   public int[] bellmanCCM(int s) {
	   assert s >= 0;
	   assert s < vertices();
	   
	   int[] d = new int[vertices()];
	   int[] p = new int[vertices()];
	   
	   for (int i = 0; i < vertices(); i++) {
		   d[i] = Integer.MAX_VALUE;
		   p[i] = -1;
	   }

	   d[s] = 0;
	   
	   int modifie = 0;
	   int i = 0;
	   
	   while (i < vertices() && modifie != -1) {
		   modifie = -1;
		   
		   for (Edge e : edges(topo())) {
			   long x = d[e.to];
			   long y = ((long) d[e.from]) + ((long) e.cost);
			   
			   if (x > y) {
				   d[e.to] = d[e.from] + e.cost;
				   p[e.to] = e.from;
				   modifie = e.from;
			   }
		   }
		   
		   i ++;
	   }
	   
	   if (modifie != -1) {
		   /* cycle négatif */
		   return null;
	   }
	   else {
		   /* d s'est stabilisé */
		   return d;
	   }
   }
   
   /**
    * @param s
    * @param t
    * @return sommets du chemin de coût minimal de s à t dans l'ordre inverse (s et t exclus)
    */
   public ArrayList<Integer> bellman(int s, int t) {
	   ArrayList<Integer> chemin = new ArrayList<Integer>();
	   int[] p = bellman(s);
       int emprunte = t;
	   
       /* pour l'instant, on suppose qu'il y a bel et bien un chemin de s à t
          on suppose que le graphe est un DAG */
       while (p[emprunte] != s) {
		   emprunte = p[emprunte];
		   chemin.add(emprunte);
       }
       
	   return chemin;
   }
   
   /**
    * algorithme de Dijkstra
    * @param source
    * @return tableau des parents
    */
   public int[] dijkstra(int s) {
	   Heap H = new Heap(vertices());
	   boolean[] v = new boolean[vertices()];
	   int[] p = new int[vertices()];
	   	   
	   for (int i = 0; i < vertices(); i++) {
		   v[i] = false;
		   p[i] = -1;
	   }
	   
	   H.decreaseKey(s, 0);
	   
	   for (int i = 0; i < vertices(); i++) {
		   int min = H.pop();
		   
		   v[min] = true;
		   
		   for (Edge e : next(min)) {
			   if (!v[e.to]) {
				   long x = H.priority(e.to);
				   long y = ((long) e.cost) + ((long) H.priority(e.from));
				   
				   if (x > y) {
					   H.decreaseKey(e.to, e.cost + H.priority(e.from));	
					   p[e.to] = e.from;
				   }
			   }
		   }
	   }
	   
	   return p;
   }
   
   /**
    * @param s
    * @param t
    * @return sommets du chemin de coût minimal de s à t dans l'ordre inverse (s et t exclus)
    */
   public ArrayList<Integer> dijkstra(int s, int t) {
	   ArrayList<Integer> chemin = new ArrayList<Integer>();
	   int[] p = dijkstra(s);
       int emprunte = t;
	   
       while (p[emprunte] != s) {
    	   assert p[emprunte] != - 1;
		   emprunte = p[emprunte];
		   chemin.add(emprunte);
       }
       
	   return chemin;
   }
   
   /**
    * pour la gestion de pixels à garder
    * coût des arêtes vers les sommets de la zone = +INFINI
    * @param zone
    */
   public void garder(int[] zone, int largeur) {
	   for (int y = zone[1]; y < zone[3]; y ++) {
		   for (int x = zone[0]; x < zone[2]; x ++) {
			   for (Edge e : prev(y * largeur + x)) {
				   e.cost = Integer.MAX_VALUE;
			   }
		   }
	   }
   }
   
   /**
    * pour la gestion de pixels à supprimer
    * coût des arêtes vers les sommets de la zone = - INFINI
    * @param zone
    */
   public void supprimer(int[] zone, int largeur) {
	   for (int y = zone[1]; y < zone[3]; y ++) {
		   for (int x = zone[0]; x < zone[2]; x ++) {
			   for (Edge e : prev(y * largeur + x)) {
				   e.cost = Integer.MIN_VALUE;
			   }
		   }
	   }
   }
   
   /**
    * fonction auxiliaire utilisée dans les tograph 
    * (pour la suppression de colonnes)
    * @param itr
    * @param base
    * @param bout
    * @param y
    * @param x
    */
   public void addEdges(int[][] itr, int base, int bout, int x, int y) {
	   if (x == 0) {
           addEdge(new Edge(base, base + bout, itr[y][x]));
		   addEdge(new Edge(base, base + bout + 1, itr[y][x]));
       }
       else if (x == bout -1) {
       	   addEdge(new Edge(base, base + bout - 1, itr[y][x]));
		   addEdge(new Edge(base, base + bout, itr[y][x]));
       }
       else {
       	   addEdge(new Edge(base, base + bout - 1, itr[y][x]));
		   addEdge(new Edge(base, base + bout, itr[y][x]));
		   addEdge(new Edge(base, base + bout + 1, itr[y][x]));
       }
   }
   
   /**
    * mettre les arêtes du chemin à +INFINI
    * @param chemin
    */
   public void garder(ArrayList<Integer> chemin) {
	   for (int i = chemin.size() - 1; i > 0; i --) {
		   int from = chemin.get(i);
		   int to = chemin.get(i - 1);
		   
		   Edge e = edge(from, to);
		   
		   assert e != null;
		   
		   e.cost = Integer.MAX_VALUE;
	   }
   }
   
   /**
    * algorithme de Bellman
    * @param s sommet de depart pour l'obtention des ccm
    * @param d tableau des ccm 
    * @return parents, null si présence d'un cycle de coût négatif
    */
   public int[] bellman(int s, int[] d) {
	   assert s >= 0;
	   assert s < vertices();
	   assert d.length == vertices();
	   
	   int[] p = new int[vertices()];
	   
	   for (int i = 0; i < vertices(); i++) {
		   d[i] = Integer.MAX_VALUE;
		   p[i] = -1;
	   }

	   d[s] = 0;
	   
	   int modifie = 0;
	   int i = 0;
	   
	   while (i < vertices() && modifie != -1) {
		   modifie = -1;
		   
		   for (Edge e : edges(topo())) {
			   long x = d[e.to];
			   long y = ((long) d[e.from]) + ((long) e.cost);
			   
			   if (x > y) {
				   d[e.to] = d[e.from] + e.cost;
				   p[e.to] = e.from;
				   modifie = e.from;
			   }
		   }
		   
		   i ++;
	   }
	   
	   if (modifie != -1) {
		   /* cycle négatif */
		   return null;
	   }
	   else {
		   /* d s'est stabilisé */
		   return p;
	   }
   }
   
   public void modifiePoids(int[] d) {
	   for (Edge e : edges()) {
		   e.cost += d[e.from] - d[e.to];
	   }
   }
   
   public void inverseCCM(ArrayList<Integer> ccm) {
	   for (int i=ccm.size()-2;i>-1;i--) {
		   int sommet1Arrete=ccm.get(i+1);
		   int sommet2Arrete=ccm.get(i);
		   
		   ArrayList<Edge> sommet1arretes=adj[sommet1Arrete];
		   ArrayList<Edge> sommet2arretes=adj[sommet2Arrete];
		   
		   for (int j=0;j<sommet1arretes.size();j++) {
			   Edge e=sommet1arretes.get(j);
			   if (e.from == sommet1Arrete && e.to == sommet2Arrete) {
				   int temp=e.from;
				   e.from=e.to;
				   e.to=temp;
			   }
		   }
		   
		   for (int j=0;j<sommet2arretes.size();j++) {
			   Edge e=sommet2arretes.get(j);
			   if (e.from == sommet1Arrete && e.to == sommet2Arrete) {
				   int temp=e.from;
				   e.from=e.to;
				   e.to=temp;
			   }
		   }
	   }
   } 
   
   public ArrayList<Integer>[] twopath(int s, int t) {
	   /* nos deux ccm qui seront à retirer */
	   ArrayList<Integer>[] ccm = (ArrayList<Integer>[]) new ArrayList[2];
	   
	   /* contient les ccm entre le sommet de départ et les autres */
	   int[] d = new int[vertices()]; 
	   
	   
	   /* contient les sommets d'un premier ccm entre s et t */
	   /* remplit aussi d avec les ccm pour chaque sommet */
	   int[] p = bellman(s, d);
	   
	   assert p != null;
	   
	   for (int c = 0; c < 2; c ++) {
		   ccm[c] = new ArrayList<Integer>();
	   }
	   
       int emprunte = t;
	   
       while (p[emprunte] != s) {
    	   assert p[emprunte] != - 1;
		   emprunte = p[emprunte];
		   ccm[0].add(emprunte);
       }
       
       /* il faut ensuite modifier le poids des arêtes du graphe */
       modifiePoids(d);
       
       /* on inverse les arêtes du ccm */
       inverseCCM(ccm[0]);
       
       /* on cherche un second ccm dans le nouveau graphe */
       p = dijkstra(s);
       
       /* on cherche les sommets en commun */
       for (int i=0;i<p.length;i++) {
    	   
       }
       
       
	   return ccm;
   }
   
}
