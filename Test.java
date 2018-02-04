package modelisation;

import java.util.ArrayList;
import java.util.Random;

import modelisation.graphe.Edge;
import modelisation.graphe.Graph;

public class Test {
	
   static boolean visite[];
   
   
   public static void dfs(Graph g, int u) {
		visite[u] = true;
		System.out.println("Je visite " + u);
		
		for (Edge e: g.next(u)) {
		    if (!visite[e.to]) {
			    dfs(g,e.to);
		    }
		}
   }

   public static void testHeap() {
		// Crée une file de priorité contenant les entiers de 0 à 9, tous avec priorité +infty
		Heap h = new Heap(10);
		h.decreaseKey(3,1664);
		h.decreaseKey(4,5);
		h.decreaseKey(3,8);
		h.decreaseKey(2,3);
		// A ce moment, la priorité des différents éléments est:
		// 2 -> 3
		// 3 -> 8
		// 4 -> 5
		// tout le reste -> +infini
		int x=  h.pop();
		System.out.println("On a enlevé "+x+" de la file, dont la priorité était " + h.priority(x));
		x=  h.pop();
		System.out.println("On a enlevé "+x+" de la file, dont la priorité était " + h.priority(x));
		x=  h.pop();
		System.out.println("On a enlevé "+x+" de la file, dont la priorité était " + h.priority(x));
		// La file contient maintenant uniquement les éléments 0,1,5,6,7,8,9 avec priorité +infini
   }
   
   public static void testGraph() {
		int n = 5;
		int i,j;
		Graph g = new Graph(n * n + 2);
		
		for (i = 0; i < n - 1; i++)
		  for (j = 0; j < n ; j++)
			g.addEdge(new Edge(n * i + j, n * (i + 1) + j, 1664 - (i + j)));

		for (j = 0; j < n ; j++)		  
		  g.addEdge(new Edge(n * (n - 1) + j, n * n, 666));
		
		for (j = 0; j < n ; j++)					
		  g.addEdge(new Edge(n * n + 1, j, 0));
		
		g.addEdge(new Edge(13, 17, 1337));
		g.writeFile("test.dot");
		
		// dfs à partir du sommet 3
		visite = new boolean[n * n + 2];
		dfs(g, 3);
   }
   
   public static void testWritePgm() {
	   int[][] image = Lecture.readpgm("modelisation/image/ex1.pgm");
	   
	   Ecriture.writepgm(image, "ex1-m.pgm");
   }
   
   public static void compare(int[][] interet, int[][] retourInterest) {
	   int hauteur = retourInterest.length;  
	   int largeur = retourInterest[0].length;
	   int pixels = hauteur * largeur;
	   
	   assert hauteur == interet.length;
	   assert largeur == interet[0].length;
	   
	   for (int indice = 0; indice < pixels; indice ++) {
		   int y = indice / largeur;
		   int x = indice % largeur;
		   
		   //assert interet[y][x] == retourInterest[y][x];
		   if (interet[y][x] != retourInterest[y][x]) {
			   System.out.println("interet["+y+"]["+x+"] : "+interet[y][x]);
			   System.out.println("retourInterest["+y+"]["+x+"] : "+retourInterest[y][x]);
		   }
	   }
	   System.out.println("Tout est bon");
   }
   
   public static void testInterestLigne() {
	   int image[][] = { {1,2,3,7},
			             {4,5,6,5},
			             {7,8,9,5}
			           };
	   
	   int interet[][] = { {3,3,3,2}, 
			               {0,0,0,1},
			               {3,3,3,0}
	                     };
	   
	   int retourInterest[][] = SeamCarving.interestLigne(image);
	   
	   compare(interet, retourInterest);
	   Graph g=SeamCarving.tographLigne(retourInterest);
	   g.writeFile("test.dot");
   }
   
   public static void testInterestRemplissage() {
	   int image[][] = { {7,1,3,5,-1},
			   			 {3,-1,-1,-1,2},
			             {-1,5,2,2,2},
			             {2,4,1,3,5}
			           };
	   
	   /*int interet[][] = { {3,3,3,2}, 
			               {0,0,0,1},
			               {3,3,3,0}
	                     };
	   
	   int retourInterest[][] = SeamCarving.interestLigne(image);
	   
	   compare(interet, retourInterest);
	   Graph g=SeamCarving.tographLigne(retourInterest);
	   g.writeFile("test.dot");*/
   }
   
   public static void testCompletLigne(String source, String dest, int reduction) {
	   int[][] img = Lecture.readpgm(source);
	    int hauteur = img.length;
	    
	    if (hauteur - reduction < SeamCarving.LARGEUR_MINI) {
		    System.err.println("La largeur de la nouvelle image doit être au moins de " + SeamCarving.LARGEUR_MINI);
		    System.exit(1);
	    }
	   
	    for (int i = 0; i < reduction; i ++) {
		    img = SeamCarving.reductionLigne(img);   
		    System.out.print(".");
	    } 
	   
	    if (reduction > 0) System.out.println(); 
	    Ecriture.writepgm(img, dest);	
   }
   
   public static void testInterest() {
	   int image[][] = { {3, 11, 24, 39},
			             {8, 21, 29, 39},
			             {200, 60, 25, 0}
			           };
	   
	   int interet[][] = { {8, 2, 1, 15}, 
			               {13, 3, 1, 10},
			               {140, 52, 5, 25}
	                     };
	   
	   int retourInterest[][] = SeamCarving.interest(image);
	   
	   compare(interet, retourInterest);
   }
   
   public static void testTograph() {
	   int image[][] = { {3, 11, 24, 39},
	                     {8, 21, 29, 39},
	                     {200, 60, 25, 0}
	                   };
	   
	   int[][] itr = SeamCarving.interest(image);
	   Graph g = SeamCarving.tograph(itr);
	   
	   g.writeFile("test.dot");
   }
   
   public static Graph graph(int l, int c) {
	   int[][] image = new int[l][c];	   
	   Random r = new Random();
	   
	   for (int i = 0; i < l; i++) {
		   for (int j = 0; j < c; j++) {
			   image[i][j] = r.nextInt(200);
		   }
	   }
	   
	   int[][] itr = SeamCarving.interest(image);
	   Graph g = SeamCarving.tograph(itr);
	   
	   return g;
   }
   
   public static void testDijkstra() {
	   int l = 4;
	   int c = 4;
	   int source = l * c;
	   
	   Graph g = graph(l, c);
	   
	   g.writeFile("test.dot");
	   System.out.println("Dijkstra :");
	   
	   int[] p = g.dijkstra(source);
	   
	   System.out.print("\tparents : ");
	   
	   for (int i = 0; i < g.vertices(); i++) {
		   System.out.print(p[i] + ", ");
	   }

	   System.out.println();	   
	   System.out.print("\tchemin : ");
	   
	   int emprunte = source + 1;
	   
	   while (emprunte != source) {
		   System.out.print(emprunte + ", ");
		   emprunte = p[emprunte];
	   }
	   
	   System.out.print(emprunte);
   }
   
   public static void testTopo() {
       Graph g = graph(4, 4);
	   
	   g.writeFile("test.dot");
	   System.out.print("Ordre Topologique : ");
	   
	   int[] ordre = g.topo();
	   
	   for (int i = 0; i < g.vertices(); i++) {
		   System.out.print(ordre[i] + ", ");
	   }
	   
	   System.out.println();
   }
   
   public static void testBellman() {
	   int l = 4;
	   int c = 4;
	   int source = l * c;
	   
	   Graph g = graph(l, c);
	   
	   g.writeFile("test.dot");
	   System.out.println("Bellman :");
	   
	   int[] p = g.bellman(source);
	   
	   System.out.print("\tparents : ");
	   
	   for (int i = 0; i < p.length; i++) {
		   System.out.print(p[i] + ", ");
	   }

	   System.out.println();	   
	   System.out.print("\tchemin : ");
	   
	   int emprunte = source + 1;
	   
	   while (emprunte != source) {
		   System.out.print(emprunte + ", ");
		   emprunte = p[emprunte];
	   }
	   
	   System.out.print(emprunte);
	   
	   System.out.println();	   
	   System.out.print("\tchemin2 : ");
	   
	   ArrayList<Integer> chemin = g.dijkstra(source, source + 1);
	   for (int i = 0; i < chemin.size(); i++) {
		   System.out.print(chemin.get(i) + ", ");
	   }
	   
	   System.out.println();
   }
   
   public static void testPPM(String source, String dest) {
	   SeamCarving.couleur(source, dest, 50);
   }
   
   public static void testZone(String source, String dest) {
	   int[] zone = {100, 80, 460, 300};
	   
	   SeamCarving.zone(source, dest, 320, zone, true);
   }
   

   public static void main(String[] args) {	
	   // testInterestLigne();
	   // testCompletLigne(args[0],args[1],100);
	   testBellman();
   }

}
