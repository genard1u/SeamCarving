package modelisation;
import java.util.ArrayList;
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
		
	   for (Edge e: adj(v)) {
		  if (e.to != v)
			n.add(e);
	   }
	   
	   return n;
   }      
   
   public Iterable<Edge> edges()
	 {
		ArrayList<Edge> list = new ArrayList<Edge>();
        for (int v = 0; v < V; v++)
            for (Edge e : adj(v)) {
                if (e.to != v)
                    list.add(e);
            }
        return list;
    }
   
   
   public void writeFile(String s) {
	   try {			 
			 PrintWriter writer = new PrintWriter(s, "UTF-8");
			 writer.println("digraph G{");
			 for (Edge e: edges())
			   writer.println(e.from + "->" + e.to + "[label=\"" + e.cost + "\"];");
			 writer.println("}");
			 writer.close();
	   }
	   catch (IOException e){
		   System.err.println("impossible d'écrire le fichier");
		   System.exit(1);
	   }						
   }
   
   /**
    * Méthode appliquant l'algorithme de Bellman Ford
    * @param s Le sommet source
    * @return null si il y a un cycle négatif, sinon le tableau des coûts minimaux
    */
   public int[] BellmanFord(int s) {
	   int[] d=new int[this.vertices()];
	   
	   //On initialise le tableau des distances
	   //d[i]=+infinity
	   for (int i=0;i<d.length;i++) {
		   d[i]=Integer.MAX_VALUE;
	   }
	   //d[s]=0;
	   d[s]=0;
	   
	   boolean changement;
	   for (int i=0;i<this.vertices();i++) {
		   changement=false;
		   //On parcourt chaque arrête
		   for (Edge e: edges()) {
			   if (d[e.to] > (d[e.from] + e.cost)) {
				   d[e.from] = d[e.to] + e.cost;
				   changement=true;
			   }
		   }
		   if (changement == false) {
			   return d;
		   }
	   }
	   //Si le dernier changement est à true alors il y a un cycle négatif (on retourne null)
	   return null;
   }
   
}
