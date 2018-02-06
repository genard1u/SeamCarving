package modelisation;

import java.util.ArrayList;

import modelisation.graphe.Edge;
import modelisation.graphe.Graph;

public class SeamCarving {
   
	public final static int LARGEUR_MINI = 3;
	public final static int HAUTEUR_MINI = 3;
	
	
    public static void defaut(String source, String dest, int reduction) {
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
   
    /**
     * gestion de la couleur, reduction à utiliser avec interest
     * @param source
     * @param dest
     * @param reduction
     * @param couleur
     */
    public static void couleur(String source, String dest, int reduction) {
    	int[][][] img = Lecture.readppm(source);
    	int hauteur = img.length;
	    int largeur = img[0].length;
	    int pixels = hauteur * largeur;
	    
	    if (largeur - reduction < LARGEUR_MINI) {
		    System.err.println("La largeur de la nouvelle image doit être au moins de " + LARGEUR_MINI);
		    System.exit(1);
	    }
	    
	    int[][] r = new int[hauteur][largeur];
	    int[][] g = new int[hauteur][largeur];
	    int[][] b = new int[hauteur][largeur];
	    
	    for (int i = 0; i < pixels; i ++) {
	    	int y = i / largeur;
	    	int x = i % largeur;
	    	
	    	r[y][x] = img[y][x][0];
	    	g[y][x] = img[y][x][1];
	    	b[y][x] = img[y][x][2];
	    }
	    
	    for (int i = 0; i < reduction; i ++) {
		    r = reduction(r);
		    g = reduction(g); 
		    b = reduction(b); 
		    System.out.print(".");
	    } 
	    
	    largeur -= reduction;
	    pixels = hauteur * largeur;
	    img = new int[hauteur][largeur][3];
	    	    
	    for (int i = 0; i < pixels; i ++) {
	    	int y = i / largeur;
	    	int x = i % largeur;
	    	
	    	img[y][x][0] = r[y][x];
	    	img[y][x][1] = g[y][x];
	    	img[y][x][2] = b[y][x];
	    }
	    
	    if (reduction > 0) System.out.println(); 
	    Ecriture.writeppm(img, dest);
    }
    
    /**
     * pour la gestion de pixels à garder ou à supprimer
     * @param source
     * @param dest
     * @param reduction
     * @param zone (coin haut gauche, coin bas droit)
     */
    public static void zone(String source, String dest, int reduction, int[] zone, boolean garde) {
    	int[][] img = Lecture.readpgm(source);
    	int hauteur = img.length;
	    int largeur = img[0].length;
	    int hauteurZone = zone[3] - zone[1];
	    int largeurZone = zone[2] - zone[0];
	    
	    if (largeur - largeurZone - reduction < LARGEUR_MINI) {
	    	System.err.println("La largeur de la nouvelle image doit être au moins de " + Math.max(LARGEUR_MINI, largeurZone));
		    System.exit(1);
	    }
	    
	    if (hauteur - hauteurZone < HAUTEUR_MINI) {
	    	System.err.println("La hauteur de la nouvelle image doit être au moins de " + hauteurZone);
		    System.exit(1);
	    }
	    
	    for (int i = 0; i < reduction; i ++) {
		    img = reduction(img, zone, garde);   
		    System.out.print(".");
	    } 
	   
	    if (reduction > 0) System.out.println(); 
	    Ecriture.writepgm(img, dest);
    }
    
    public static int[][] interest(int[][] img) {
	    int hauteur = img.length;
	    int largeur = img[0].length;
	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	    	    
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
    
    public static int[][] interestLigne(int[][] img) {
	    int hauteur = img.length;
	    int largeur = img[0].length;
	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	    	    
	    int[][] itr = new int[hauteur][largeur];
	    int moyenne = 0;
	    	    
	    for (int y = 0; y < hauteur; y ++) {
	    	for (int x = 0; x < largeur; x ++) {
	    		if (y == 0) {
			    	moyenne = img[y+1][x];
			    }
			    else if (y == hauteur-1) {
			    	moyenne = img[y-1][x];
			    }
			    else {
			    	moyenne = (img[y-1][x] + img[y+1][x]) / 2;
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
	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	    
	    int taille = hauteur * largeur;
	    int source = taille;
	    int puits = taille + 1;
	    int derniere = taille - largeur;
	    Graph g = new Graph(taille + 2);
	   	
	    for (int i = 0; i < largeur; i ++) {
		    g.addEdge(new Edge(source, i, 0));
	    }
	    
	    int gauche = 0;
	    int milieu = 0;
	    int droite = 0;
	    
	    for (int p = 0; p < derniere; p ++) {
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
	   	
	   	for (int i = derniere; i < taille; i ++) {
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
	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	    
	    int taille = hauteur * largeur;
	    int source = taille;
	    int puits = taille + 1;
	    int derniere = taille - largeur;
	    Graph g = new Graph(taille + 2);
	   
	    for (int i = 0; i < largeur; i ++) {
		    g.addEdge(new Edge(source, i, 0));
	    }
	   
	    for (int i = 0; i < derniere; i ++) {
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
	   
	    for (int i = derniere; i < taille; i ++) {
		    int y = i / largeur;
		    int x = i % largeur;
		   
		    g.addEdge(new Edge(i, puits, itr[y][x]));
	    }
	   
	    return g;
    }
    
    /**
     * sommet tout à gauche = avant-dernier
     * celui tout en droite = dernier
     * @param itr
     * @return itr en graphe
     */
    public static Graph tographLigne(int[][] itr) {
	    int hauteur = itr.length;
	    int largeur = itr[0].length;
	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	    
	    int pixels = hauteur * largeur;
	    int source = pixels;
	    int puits = pixels + 1;
	    int derniereRangee = pixels - hauteur;
	    Graph g = new Graph(pixels + 2);
	   
	    for (int i = 0; i < hauteur; i ++) {
		    g.addEdge(new Edge(source, i, 0));
	    }
	   
	    for (int i = 0; i < derniereRangee; i ++) {
	    	int x = i / hauteur;
		    int y = i % hauteur;
		   
            if (y == 0) {
            	g.addEdge(new Edge(i, i + hauteur, itr[y][x]));
			    g.addEdge(new Edge(i, i + hauteur + 1, itr[y][x]));
            }
            else if (y == hauteur -1) {
            	g.addEdge(new Edge(i, i + hauteur - 1, itr[y][x]));
			    g.addEdge(new Edge(i, i + hauteur, itr[y][x]));
            }
            else {
            	g.addEdge(new Edge(i, i + hauteur - 1, itr[y][x]));
			    g.addEdge(new Edge(i, i + hauteur, itr[y][x]));
			    g.addEdge(new Edge(i, i + hauteur + 1, itr[y][x]));
            }  
	    }
	   
	    for (int i = derniereRangee; i < pixels; i ++) {
	    	//System.out.println(i);
	    	int x = i / hauteur;
		    int y = i % hauteur;	
		   
		    g.addEdge(new Edge(i, puits, itr[y][x]));
	    }
	   
	    return g;
    }
    
    /**
     * sommet tout en haut = avant-dernier
     * celui tout en bas = dernier
     * @param itr
     * @return itr en graphe
     */
    public static Graph tograph2(int[][] itr) {
    	int hauteur = itr.length;
	    int largeur = itr[0].length;
	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	       	    	
	    int taille = hauteur * largeur;
	    int derniere = taille - largeur;
	    
	    int sommets = (hauteur - 1) * largeur * 2 + 2;	    	    
	    int source = sommets - 2;
	    int puits = sommets - 1;
	    Graph g = new Graph(sommets);
	    
	    for (int i = 0; i < largeur; i ++) {
		    g.addEdge(new Edge(source, i, 0));
	    }
	    
	    for (int i = 0; i < largeur; i ++) {
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
	    
	    for (int y = 1; y < hauteur -1; y ++) {
	    	int b1 = 2 * y * largeur - largeur; 
	    	int b2 = b1 + largeur; 
	    	
	    	for (int x = 0; x < largeur; x ++, b2 ++) {
	    		g.addEdge(new Edge(b1 + x, b1 + x + largeur, 0));
	    			    		
	    		if (x == 0) {
	            	g.addEdge(new Edge(b2, b2 + largeur, itr[y][x]));
				    g.addEdge(new Edge(b2, b2 + largeur + 1, itr[y][x]));
	            }
	            else if (x == largeur -1) {
	            	g.addEdge(new Edge(b2, b2 + largeur - 1, itr[y][x]));
				    g.addEdge(new Edge(b2, b2 + largeur, itr[y][x]));
	            }
	            else {
	            	g.addEdge(new Edge(b2, b2 + largeur - 1, itr[y][x]));
				    g.addEdge(new Edge(b2, b2 + largeur, itr[y][x]));
				    g.addEdge(new Edge(b2, b2 + largeur + 1, itr[y][x]));
	            }  	    		
	    	}
	    } 
	    
	    int b = sommets - largeur - 2;
	    
	    for (int i = derniere; i < taille; i ++) {
		    int y = i / largeur;
		    int x = i % largeur;
		   
		    g.addEdge(new Edge(b + x, puits, itr[y][x]));
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
	    	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	  
	    int source = largeur * hauteur;
	    int puits = source + 1;	    
	   	    	   
	    int[][] itr = interest(img);	   
	    Graph g = tograph(itr);  
	    /*Graph g = energie(img);*/
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
    
    /**
     * @param img
     * @return img réduite de 1 ligne
     */
    public static int[][] reductionLigne(int[][] img) {
	    int hauteur = img.length;
	    int largeur = img[0].length;
	    	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	  
	    int source = largeur * hauteur;
	    int puits = source + 1;	    
	   	    	   
	    int[][] itr = interestLigne(img);	   
	    Graph g = tographLigne(itr);  
	    /*Graph g = energie(img);*/
	    ArrayList<Integer> chemin = g.dijkstra(source, puits);
	    
	    for (int i = 0; i < chemin.size(); i ++) {
		    int sommet = chemin.get(i);
	        img[sommet % hauteur][sommet / hauteur] = -1;
	    }    
	    
	   
	    int[][] img2 = new int[hauteur-1][largeur];
	    int pixel = 0;

	    for (int j = 0; j < largeur; j ++) {
	    	for (int i = 0; i < hauteur; i ++) {
	    	    if (img[i][j] != -1) {
    	    		img2[pixel % (hauteur - 1)][pixel / (hauteur - 1)] = img[i][j];
    	    		pixel ++;	    	    		
	    	    }
	        }
	    }
	   
	    return img2;
    }
   
    /**
     * pour la gestion de pixels à garder ou à supprimer
     * à terminer
     * @param img
     * @return img réduite de 1 colonne
     */
    public static int[][] reduction(int[][] img, int[] zone, boolean garde) {
	    int hauteur = img.length;
	    int hauteurZone = zone[3] - zone[1];
	    int largeur = img[0].length;
	    int largeurZone = zone[2] - zone[0];
	    
	    assert hauteur > HAUTEUR_MINI;
	    assert largeur > LARGEUR_MINI;
	    assert hauteur > hauteurZone;
	    assert largeur > largeurZone;
	    
	    int source = largeur * hauteur;
	    int puits = source + 1;	    
	   	    	   
	    int[][] itr = interest(img);	   
	    Graph g = tograph(itr); 
	    
	    /* on place les bons coûts sur les arêtes de la zone */
	    if (garde) {
	        g.garder(zone, largeur);
	    }
	    else {
	    	g.supprimer(zone, largeur);
	    }
	    	   
	    ArrayList<Integer> chemin = g.dijkstra(source, puits);	    
	    
	    for (int i = 0; i < chemin.size(); i ++) {
		    int sommet = chemin.get(i);
		    int y = sommet / largeur;
		    int x = sommet % largeur;
		    
	        img[y][x] = -1;
	    }
	    
	    /* la zone est déplacée selon l'endroit où passe le chemin */
	    decalage(img, zone, garde);
	    
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
    
    /**
     * si le chemin passe à gauche de la zone, la zone doit bouger à gauche
     * @param img
     * @param zone
     * @param garde
     */
    public static void decalage(int[][] img, int[] zone, boolean garde) {
    	if (garde) { 
	    	int largeur = img[0].length;
	        int h = zone[1];
	        int x = zone[0];
	        int abs = -1; /* abscisse du sommet sur le chemin à la largeur h */
	        int i = 0; /* compteur */
	        
	        while (i < largeur && abs == -1) {
	        	if (img[h][i] == -1) {
	        		abs = i; 
	        	}
	        	
	        	i++;
	        }
	        
	        if (abs < x) {
	        	/* décalage à gauche de la zone */
	        	zone[0] --;
    		    zone[2] --;	        
    		}	        
	    }
	    else {
	    	zone[2] --;
	    }
    }
    
    public static void nombreIncorrectArguments() {
	    System.err.println("Nombre incorrect d'arguments");
        System.err.println("\tjava -jar SeamCarving.jar src.pgm [dest.pgm] [réduction]");
        System.exit(1);
    }
   
    /**
     * traite l'argument réduction passé dans la ligne de commande
     * @param arg
     * @return réduction (pixels)
     */
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
   
    public static int[] zone(String[] args) {
    	int[] zone = new int[4];
    	
    	for (int i = 4; i < 8; i++) {
    		try {
    			zone[i - 4] = Integer.parseInt(args[i]);
    		}
    		catch (NumberFormatException nfe) {
    			System.err.println("La zone a des coordonnées non valides");
    			System.exit(1);
    		}
    	}
    	
    	return zone;
    }
    
    public static boolean garde(String arg) {
    	boolean garde = true;
    	
    	if (arg.equals("g")) {
    		garde = true;
    	}
    	else if (arg.equals("s")) {
    		garde = false;
    	}
    	else {
    		System.err.println("Il faut passer g ou s avant la zone pour le choix de la garde");
    		System.exit(1);
    	}
    	
    	return garde;
    }
    
    public static void pgm(String[] args) {
    	switch (args.length) {
            case 1: 
                defaut(args[0], args[0], 50);
                break;
            case 2:
            	defaut(args[0], args[1], 50);
                break;
            case 3:
            	defaut(args[0], args[1], reduction(args[2]));
	            break;
            case 4:
            	Test.testCompletLigne(args[0], args[1], reduction(args[2]));
        	    break;
            case 8:
            	zone(args[0], args[1], reduction(args[2]), zone(args), garde(args[3]));
        	    break;
	        default:
		        nombreIncorrectArguments();
        }
    }
    
    public static void ppm(String[] args) {
    	switch (args.length) {
            case 1: 
	            couleur(args[0], args[0], 50);
	            break;
            case 2:
    	        couleur(args[0], args[1], 50);
	            break;
            case 3:
		        couleur(args[0], args[1], reduction(args[2]));
		        break;
		    default:
			    nombreIncorrectArguments();
        } 
    }
    
    public static void source(String[] args) {
    	int i = args[0].lastIndexOf('.');
    	
    	if (i < 1) {
    		System.err.println("Le premier argument n'est pas un nom de fichier source valide");
    		System.exit(1);
    	}
    	
    	String suffixe = args[0].substring(i);
    	
    	if (suffixe.equals(".pgm")) {
    		pgm(args);
    	}
    	else if (suffixe.equals(".ppm")) {
    		ppm(args);
    	}
    	else {
    		System.err.println("Le fichier source doit être un pgm ou un ppm");
    		System.exit(1);
    	}
    }
    
    public static void main(String[] args) {
    	switch (args.length) {
    	    case 0:
    		    nombreIncorrectArguments();
    		    break;
    		default:
    	        source(args);
    	}
    }
   
}
