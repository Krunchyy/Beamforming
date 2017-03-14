package ntr.utils;

import java.io.File;
import java.io.FileWriter;

import ntr.simulation.Coordonnee;

public class BuildCSV {

	public static void main(String[] args)
	{
		buildCSV("test", new Coordonnee[]{new Coordonnee(0,0), new Coordonnee(10,10)}, new String[]{"Col1", "Col2"});
	}
	
	public static void buildCSV(String fileName, Coordonnee[] coord, String[] header)
	{
       final String chemin = "./"+fileName+".csv";
       final File fichier =new File(chemin); 
       try {
           // Creation du fichier
           fichier .createNewFile();
           // creation d'un writer (un écrivain)
           final FileWriter writer = new FileWriter(fichier);
           try {
        	   for(int i = 0 ; i < header.length ; i++)
        	   {
        		   writer.write(header[i]);
        		   if(i < header.length-1)
        			   writer.write(";");
        	   }
        	   writer.write("\n");
        	   for(Coordonnee co : coord)
        	   {
        		   writer.write(co._x+";"+co._y+"\n");
        	   }
           } finally {
               // quoiqu'il arrive, on ferme le fichier
               writer.close();
           }
       } catch (Exception e) {
           System.out.println("Impossible de creer le fichier");
       }
	}
	
	public static void buildCSV(String fileName, long[] coord, String[] header)
	{
       final String chemin = "./"+fileName+".csv";
       final File fichier =new File(chemin); 
       try {
           // Creation du fichier
           fichier .createNewFile();
           // creation d'un writer (un écrivain)
           final FileWriter writer = new FileWriter(fichier);
           try {
        	   for(int i = 0 ; i < header.length ; i++)
        	   {
        		   writer.write(header[i]);
        		   if(i < header.length-1)
        			   writer.write(";");
        	   }
        	   writer.write("\n");
        	   for(int i = 0 ; i < coord.length ; i++)
        	   {
        		   writer.write(i+";"+coord[i]+"\n");
        	   }
           } finally {
               // quoiqu'il arrive, on ferme le fichier
               writer.close();
           }
       } catch (Exception e) {
           System.out.println("Impossible de creer le fichier");
       }
	}
}
