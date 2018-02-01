package modelisation;

import java.util.ArrayList;

import modelisation.graphe.Edge;
import modelisation.graphe.Graph;

public class SeamCarving {
   
	public final static int LARGEUR_MINI = 3;
	public final static int HAUTEUR_MINI = 3;
	
	
    public SeamCarving(String source, String dest, int reduction) {
	    int[][] img = Lecture.readpgm(source);
	    int largeur = img[0].length;
	    
	    if (largeur - reduction < LARGEUR_MINI) {
		    System.err.println("La largeur de la nouvelle image doit être au moins de " + LARGEUR_MINI);
		    System.exit(1);
	    }
	   
	    for (int i = 0; i < reduction; i ++) {
		    img = reduction(img);   
		    System.out.print(".");
	    } 
	   
	    if (reduction > 0) System.out.println(); 
	    Ecriture.writepgm(img, dest);	    
    }
   
    public static int[][] interest(int[][] img) {
	    int hauteur = img.length;
	    int largeur = img[0].length;
	    
	    assert hauteur >= HAUTEUR_MINI;
	    assert largeur >= LARGEUR_MINI;
	    	    
	    int[][] itr = new int[hauteur][largeur];
	    int moyenne = 0;
	    	    
	    for (int y = 0; y < hauteur; y ++) {
	    	for (int x = 0; x < largeur; x ++) {
	    		if (x == 0) {
			    	moyenne = img[y][x + 1];
			    }
			    else if (x == largeur - 1) {
			    	moyenne = img[y][x - 1];
			    }
			    else {
			    	moyenne = (img[y][x - 1] + img[y][x + 1]) / 2;
			    }
			   
			    itr[y][x] = Math.abs(img[y][x] - moyenne);
	    	}
	    }   
	   
	    return itr;
    }
   
    /**
     * sommet tout en haut = avant-dernier
     * celui tout en bas = dernier
     * @param img
     * @return
     */
    public static Graph energie(int[][] img) {
	    int hauteur = img.length;
	    int largeur = img[0].length;
	    
	    assert hauteur >= HAUTEUR_MINI;
	    assert largeur >= LARGEUR_MINI;
	    
	    int pixels = hauteur * largeur;
	    int source = pixels;
	    int puits = pixels + 1;
	    int derniereRangee = pixels - largeur;
	    Graph g = new Graph(pixels + 2);
	   	
	    for (int i = 0; i < largeur; i ++) {
		    g.addEdge(new Edge(source, i, 0));
	    }
	    
	    int gauche = 0;
	    int milieu = 0;
	    int droite = 0;
	    
	    for (int p = 0; p < derniereRangee; p ++) {
		    int i = p / largeur;
		    int j = p % largeur;
		    		    
            if (j == 0) {
            	milieu = Math.abs(img[i][j + 1]);
            	droite = Math.abs(img[i][j + 1] - img[i + 1][j]);
            	
            	g.addEdge(new Edge(p, p + largeur, milieu));
			    g.addEdge(new Edge(p, p + largeur + 1, droite));
            }
            else if (j == largeur -1) {
            	gauche = Math.abs(img[i][j - 1] - img[i + 1][j]);
            	milieu = Math.abs(img[i][j - 1]);
            	
            	g.addEdge(new Edge(p, p + largeur - 1, gauche));
			    g.addEdge(new Edge(p, p + largeur, milieu));
            }
            else {
            	milieu = Math.abs(img[i][j + 1] - img[i][j - 1]);
            	droite = Math.abs(img[i][j + 1] - img[i + 1][j]);
            	gauche = Math.abs(img[i][j - 1] - img[i + 1][j]);
            	
            	g.addEdge(new Edge(p, p + largeur - 1, gauche));
			    g.addEdge(new Edge(p, p + largeur, milieu));
			    g.addEdge(new Edge(p, p + largeur + 1, droite));
            }  
	    }
	   	
	   	for (int i = derniereRangee; i < pixels; i ++) {
	   		int y = i / largeur;
		    int x = i % largeur;
		    int cout = 0;
		    
		    if (x == 0) {
		    	cout = img[y][x + 1];
		    }
		    else if (x == largeur - 1) {
		    	cout = img[y][x - 1];
		    }
		    else {
		    	cout = img[y][x - 1];
		    	cout -= img[y][x + 1];
		    	cout = Math.abs(cout);
		    }
		    
		    g.addEdge(new Edge(i, puits, cout));
	    }
	   	
	    return g;
    }
   
    /**
     * sommet tout en haut = avant-dernier
     * celui tout en bas = dernier
     * @param itr
     * @return itr en graphe
     */
    public static Graph tograph(int[][] itr) {
	    int hauteur = itr.length;
	    int largeur = itr[0].length;
	    
	    assert hauteur >= HAUTEUR_MINI;
	    assert largeur >= LARGEUR_MINI;
	    
	    int pixels = hauteur * largeur;
	    int source = pixels;
	    int puits = pixels + 1;
	    int derniereRangee = pixels - largeur;
	    Graph g = new Graph(pixels + 2);
	   
	    for (int i = 0; i < largeur; i ++) {
		    g.addEdge(new Edge(source, i, 0));
	    }
	   
	    for (int i = 0; i < derniereRangee; i ++) {
		    int y = i / largeur;
		    int x = i % largeur;
		   
            if (x == 0) {
            	g.addEdge(new Edge(i, i + largeur, itr[y][x]));
			    g.addEdge(new Edge(i, i + largeur + 1, itr[y][x]));
            }
            else if (x == largeur -1) {
            	g.addEdge(new Edge(i, i + largeur - 1, itr[y][x]));
			    g.addEdge(new Edge(i, i + largeur, itr[y][x]));
            }
            else {
            	g.addEdge(new Edge(i, i + largeur - 1, itr[y][x]));
			    g.addEdge(new Edge(i, i + largeur, itr[y][x]));
			    g.addEdge(new Edge(i, i + largeur + 1, itr[y][x]));
            }  
	    }
	   
	    for (int i = derniereRangee; i < pixels; i ++) {
		    int y = i / largeur;
		    int x = i % largeur;
		   
		    g.addEdge(new Edge(i, puits, itr[y][x]));
	    }
	   
	    return g;
    }
    
    /**
     * @param img
     * @return img réduite de 1 colonne
     */
    public static int[][] reduction(int[][] img) {
	    int hauteur = img.length;
	    int largeur = img[0].length;
	    	    
	    assert hauteur >= HAUTEUR_MINI;
	    assert largeur >= LARGEUR_MINI;
	    
	    int source = largeur * hauteur;
	    int puits = source + 1;	    
	   	    	   
	    /* int[][] itr = interest(img);	   
	    Graph g = tograph(itr);  */
	    Graph g = energie(img);
	    ArrayList<Integer> chemin = g.dijkstra(source, puits);
	   
	    for (int i = 0; i < chemin.size(); i ++) {
		    int sommet = chemin.get(i);
		   
	        img[sommet / largeur][sommet % largeur] = -1;
	    }
	   
	    int[][] img2 = new int[hauteur][largeur - 1];
	    int pixel = 0;
	   
	    for (int i = 0; i < hauteur; i ++) {
	        for (int j = 0; j < largeur; j ++) {
	    	    if (img[i][j] != -1) {	    		    
	    		    img2[pixel / (largeur - 1)][pixel % (largeur - 1)] = img[i][j];
	    		    pixel ++;
	    	    }	
	        }
	    }
	   
	    return img2;
    }
   
    public static void aucunArgument() {
	    System.err.println("Nombre incorrect d'arguments");
        System.err.println("\tjava -jar SeamCarving.jar src.pgm [dest.pgm] [réduction]");
        System.exit(1);
    }
   
    public static int reduction(String arg) {
	    int reduction = 50;
	   
	    try {
            reduction = Integer.parseInt(arg);
        }
        catch (NumberFormatException nfe) {
            System.err.println("La réduction n'est pas un nombre valide");
            System.exit(1);
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
