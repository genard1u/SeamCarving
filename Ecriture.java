package modelisation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Ecriture {

	public static void writepgm(int[][] image, String filename) {
		try {
		    PrintWriter writer = new PrintWriter(filename, "UTF-8");
			writer.println("P2");
			
			/* Facultatif */
			writer.println("#Ce fichier a été écrit par SeamCarving");
			
			int height = image.length;
			int width = image[0].length;
			
			writer.println(width + " " + height);
			writer.println("255");
			
			int ligne = 0;
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					writer.print(image[i][j] + " ");
					ligne++;
					   
					if (ligne > 25) {
					    writer.println();
						ligne = 0;
					}
				}
			}
			   
			writer.close();		
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
	    } 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeppm(int[][] image, String filename) {
		
	}
	
}
