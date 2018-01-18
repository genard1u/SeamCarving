package modelisation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class SeamCarving
{

   public static int[][] readpgm(String fn)
	 {		
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();
            String line = d.readLine();
		   while (line.startsWith("#")) {
			  line = d.readLine();
		   }
		   Scanner s = new Scanner(line);
		   int width = s.nextInt();
		   int height = s.nextInt();		   
		   line = d.readLine();
		   s = new Scanner(line);
		   int maxVal = s.nextInt();
		   int[][] im = new int[height][width];
		   s = new Scanner(d);
		   int count = 0;
		   while (count < height * width) {
			  im[count / width][count % width] = s.nextInt();
			  count++;
		   }
		   return im;
        }
		
        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            return null;
        }
    }

   public static void writepgm(int[][] image, String filename) {
	   try {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		writer.println("P2");
		
		//Facultatif
		writer.println("#Ce fichier a été écrit par SeamCarving");
		
		int height=image.length;
		int width=image[0].length;
		
		writer.println(width+" "+height);

		writer.println("255");
		
		int ligne=0;
		
		for(int i=0;i<height;i++) {
			for (int j=0;j<width;j++) {
				writer.print(image[i][j]+" ");
				ligne++;
				if (ligne > 25) {
					writer.println();
					ligne=0;
				}
			}
		}
		writer.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
            	   /* pas de pixel droit */
            	   moyenne = image[y][x - 1];
               }
		   }
		   else {
			   if (x < largeur - 1) {
            	   /* pas de pixel gauche */	
				   moyenne = image[y][x + 1];
			   }
		   }
		   
		   int facteur = Math.abs(valeur - moyenne);
		   
		   interet[y][x] = facteur;
	   }
	   
	   return interet;
   }
   
}
