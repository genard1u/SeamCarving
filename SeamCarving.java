package modelisation;

import modelisation.graphe.Edge;
import modelisation.graphe.Graph;

public class SeamCarving {
   
   public static int[][] interest (int[][] image) {
	   int hauteur = image.length;
	   int largeur = image[0].length;
	   int pixels = hauteur * largeur;
	   int[][] interet = new int[hauteur][largeur];
	   
	   for (int indice = 0; indice < pixels; indice ++) {
		   int y = indice / largeur;
		   int x = indice % largeur;		
		   int valeur = image[y][x];				   
		   int moyenne = 0;
		   
		   if (x > 0) {
			   if (x < largeur - 1) {
				   /* pixels à droite et à gauche */
				   moyenne = (image[y][x - 1] + image[y][x + 1]) / 2;
			   }
               else {
            	   /* pixel gauche, pas de pixel droit */
            	   moyenne = image[y][x - 1];
               }
		   }
		   else {
			   if (x < largeur - 1) {
            	   /* pixel droit, pas de pixel gauche */	
				   moyenne = image[y][x + 1];
			   }
		   }
		   
		   int facteur = Math.abs(valeur - moyenne);
		   
		   interet[y][x] = facteur;
	   }
	   
	   return interet;
   }
   
   /**
    * Le sommet tout en haut est l'avant dernier.
    * Celui tout en bas est le dernier.
    * @param itr
    * @return tableau itr en graphe
    */
   public static Graph tograph(int[][] itr) {
	   int hauteur = itr.length;
	   int largeur = itr[0].length;
	   int pixels = hauteur * largeur;
	   int avantDernier = pixels;
	   int dernier = pixels + 1;
	   int derniereRangee = pixels - largeur;
	   Graph g = new Graph(pixels + 2);
	   
	   for (int i = 0; i < largeur; i ++) {
		   g.addEdge(new Edge(avantDernier, i, 0));
	   }
	   
	   for (int indice = 0; indice < derniereRangee; indice ++) {
		   int y = indice / largeur;
		   int x = indice % largeur;
		   
		   if (x > 0) {
			   if (x < largeur - 1) {
				   /* pixels à droite et à gauche */
				   g.addEdge(new Edge(indice, indice + largeur - 1, itr[y][x]));
				   g.addEdge(new Edge(indice, indice + largeur, itr[y][x]));
				   g.addEdge(new Edge(indice, indice + largeur + 1, itr[y][x]));
			   }
               else {
            	   /* pixel gauche, pas de pixel droit */
            	   g.addEdge(new Edge(indice, indice + largeur - 1, itr[y][x]));
				   g.addEdge(new Edge(indice, indice + largeur, itr[y][x]));
               }
		   }
		   else {
			   if (x < largeur - 1) {
            	   /* pixel droit, pas de pixel gauche */
				   g.addEdge(new Edge(indice, indice + largeur, itr[y][x]));
				   g.addEdge(new Edge(indice, indice + largeur + 1, itr[y][x]));
			   }
		   }		   
	   }
	   
	   for (int indice = derniereRangee; indice < pixels; indice ++) {
		   int y = indice / largeur;
		   int x = indice % largeur;
		   
		   g.addEdge(new Edge(indice, dernier, itr[y][x]));
	   }
	   
	   return g;
   }
   
}
