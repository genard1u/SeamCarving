package modelisation;

import java.util.ArrayList;

import modelisation.graphe.Edge;
import modelisation.graphe.Graph;

public class SeamCarving {
   
   public SeamCarving(String source, String dest, int reduction) {
	   int[][] img = Lecture.readpgm(source);
	
	   for (int i = 0; i < reduction; i++) {
		   img = reduction(img);
		   
		   if (img == null) {
			   if (i > 0) System.out.println();
			   throw new ReductionException(i, reduction);
		   }
		   
		   System.out.print(".");
	   } 
	   
	   if (reduction > 0) System.out.println(); 
	   Ecriture.writepgm(img, dest);
   }
   
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
   
   public static int[][] energie (int[][] image) {
	   int hauteur = image.length;
	   int largeur = image[0].length;
	   int pixels = hauteur * largeur;
	   int[][] itr = new int[hauteur][largeur];
	   
	   for (int indice = 0; indice < pixels; indice ++) {
		   int y = indice / largeur;
		   int x = indice % largeur;		
		   int val = image[y][x];	
		   
	   }
	   
	   return itr;
   }
   
   /**
    * le sommet tout en haut est l'avant-dernier
    * celui tout en bas est le dernier
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
   
   /**
    * @param img
    * @return img réduite de 1 colonne, null si la largeur est nulle
    */
   public static int[][] reduction(int[][] img) {
	   int height = img.length;
	   int width = img[0].length;
	   int s = width * height;
	   
	   if (width < 1) return null;
	   	 
	   /* déclaration de la nouvelle image */
	   int[][] img2 = new int[height][width - 1];
	   
	   int[][] itr = interest(img);	   
	   Graph g = tograph(itr);
	   ArrayList<Integer> chemin = g.dijkstra(s, s + 1);
	   
	   for (int i = 0; i < chemin.size(); i++) {
		   int sommet = chemin.get(i);
		   
	       img[sommet / width][sommet % width] = -1;
	   }
	   
	   int pixel = 0;
	   
	   /* remplissage de la nouvelle image */
	   for (int i = 0; i < height; i++) {
	       for (int j = 0; j < width ; j++) {
	    	   if (img[i][j] != -1) {	    		    
	    		   img2[pixel / (width - 1)][pixel % (width - 1)] = img[i][j];
	    		   pixel++;
	    	   }	
	       }
	   }
	   
	   return img2;
   }
   
   public static void aucunArgument() {
	   System.err.println("Nombre incorrect d'arguments");
       System.err.println("\tjava -jar SeamCarving.jar <source.pgm> <nouveau.pgm> <réduction>");
       System.exit(1);
   }
   
   public static int reduction(String arg) {
	   int reduction = 50;
	   
	   try {
           reduction = Integer.parseInt(arg);
       }
       catch (NumberFormatException nfe) {
           nfe.printStackTrace();
       }
	   
	   return reduction;
   }
   
   public static void main(String[] args) {
	   switch (args.length) {
	       case 0:
	    	   aucunArgument();
	       case 1: 
		       new SeamCarving(args[0], args[0], 50);
		       break;
	       case 2:
	    	   new SeamCarving(args[0], args[1], 50);
		       break;
		   default:
			   new SeamCarving(args[0], args[1], reduction(args[2]));
	   } 
   }
   
}
