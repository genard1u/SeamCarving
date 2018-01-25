package modelisation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Lecture {

	@SuppressWarnings("unused")
    public static int[][] readpgm(String fn) {	
		int[][] im = null;
		
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
		    s.close();
		    s = new Scanner(line);
		   
		    int maxVal = s.nextInt();
		    
		    im = new int[height][width];		   
		    s.close();
		    s = new Scanner(d);
		   
		    int count = 0;
		   
		    while (count < height * width) {
			    im[count / width][count % width] = s.nextInt();
			    count++;
		    }
		   
		    s.close();
        } 
		     
        catch (Throwable t) {
            t.printStackTrace(System.err);
            System.exit(1);
        }
        
        return im;
    }        
	
	public static int[][] readppm(String fn) {
        int[][] im = null;
		
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
			String magic = d.readLine();
            String line = d.readLine();
        }
        catch (Throwable t) {
            t.printStackTrace(System.err);
            System.exit(1);
        }
        
		return im;
	}
	
}
