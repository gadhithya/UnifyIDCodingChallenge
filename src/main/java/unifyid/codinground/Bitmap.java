package unifyid.codinground;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;


public class Bitmap 
{
	final static int width=128,height=128;
	static BufferedImage img = null;
	static File f = null;

	//Boilerplate code to send HTTP request
	public static String executePost(String targetURL, String urlParameters) 
		{
	  	HttpURLConnection connection = null;

	  	try {
	    //Create connection
	    URL url = new URL(targetURL);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type", 
	        "application/x-www-form-urlencoded");

	    connection.setRequestProperty("Content-Length", 
	        Integer.toString(urlParameters.getBytes().length));
	    connection.setRequestProperty("Content-Language", "en-US");  

	    connection.setUseCaches(false);
	    connection.setDoOutput(true);

	    //Send request
	    DataOutputStream wr = new DataOutputStream (
	        connection.getOutputStream());
	    wr.writeBytes(urlParameters);
	    wr.close();

	    //Get Response  
	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
	    String line;
	    while ((line = rd.readLine()) != null) {
	       //line=line.trim().replaceAll("\n"," ");
	      response.append(line);
	      response.append(' ');
	    }
	    rd.close();
	    return response.toString();
	  	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	  	} finally {
	    if (connection != null) {
	      connection.disconnect();
	    	}
	  		}
		}


	//Test array after I got banned :-/
	public static String generateRandomArray(int n){
	    Random random = new Random();
	    String s="";
	    for (int i = 0; i < n; i++)
	    {
	        s+=random.nextInt(256)+" ";
	    }
	   return s;
	}


    public static void main( String[] args )
    	{
    	img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

    	int[][] r= new int[128][128];
    	int[][] g= new int[128][128];
    	int[][] b= new int[128][128];
    	int[][] p= new int[128][128];
     	for(int i=0;i<128;i++)
    		{

    		String targetURL="https://www.random.org/integers/?num=384&min=0&max=255&col=1&base=10&format=plain&rnd=new";
    		String response = executePost(targetURL,new String());
    		int[] random = Arrays.stream(response.split(" ")).mapToInt(Integer::parseInt).toArray();
    		for(int j=0;j<128;j++)
    			{
    			r[i][j]=random[j];
    			g[i][j]=random[j+128];
    			b[i][j]=random[j+256];
    			}
    		}
        for(int i=0;i<128;i++)
        {
        	for(int j=0;j<128;j++)
        	{
        		p[i][j]= p[i][j] | (r[i][j]<<16);
        		p[i][j]= p[i][j] | (g[i][j]<<8);
        		p[i][j]= p[i][j] | b[i][j];	
        		img.setRGB(i,j,p[i][j]);
        	}
        }
        try
          {
	      f = new File("Output.jpg");
	      ImageIO.write(img, "jpg", f);
	      }
	      catch(IOException e)
	      	{
	      	System.out.println(e);
	    	}

    	}
	}
