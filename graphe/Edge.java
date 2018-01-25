package modelisation.graphe;

public class Edge {
	
   public int from;
   public int to;
   public int cost;
   
   
   public Edge(int x, int y, int cost) {
		this.from = x;
		this.to = y;
		this.cost = cost;
   }
   
}
