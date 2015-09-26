package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.TreeMap;

import datas.Ahenk;
import datas.Makam;
import datas.Note;
public class Test8
{
   public static void main(String [] args)
   {
      TreeMap<String,Note> e1 = null;
      try {
         FileInputStream fileIn = new FileInputStream("notesMap.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         e1 = extractedNote(in);
         in.close();
         fileIn.close();
      } catch(IOException i) {
         i.printStackTrace();
         return;
      } catch(ClassNotFoundException c) {
         System.out.println("Employee class not found");
         c.printStackTrace();
         return;
      }
      System.out.println("Deserialized Employee...");
      System.out.println("Name: " + e1.get("n5").name);
      
      
      TreeMap<String,Ahenk> e2 = null;
      try{
          FileInputStream fileIn = new FileInputStream("ahenksMap.ser");
          ObjectInputStream in = new ObjectInputStream(fileIn);
          e2 = extractedAhenk(in);
          in.close();
          fileIn.close();
       }catch(IOException i)
       {
          i.printStackTrace();
          return;
       }catch(ClassNotFoundException c)
       {
          System.out.println("Employee class not found");
          c.printStackTrace();
          return;
       }
       System.out.println("Deserialized Employee...");
       System.out.println("Name: " + e2.get("a1").name);
      
       TreeMap<String,Makam> e3 = null;

       try{
           FileInputStream fileIn = new FileInputStream("makamsMap.ser");
           ObjectInputStream in = new ObjectInputStream(fileIn);
           e3 = extractedMakam(in);
           in.close();
           fileIn.close();
        }catch(IOException i)
        {
           i.printStackTrace();
           return;
        }catch(ClassNotFoundException c)
        {
           System.out.println("Employee class not found");
           c.printStackTrace();
           return;
        }
        System.out.println("Deserialized Employee...");
        System.out.println("Name: " + e3.get("m3").getName());    
    }

@SuppressWarnings("unchecked")
private static TreeMap<String, Makam> extractedMakam(ObjectInputStream in)
		throws IOException, ClassNotFoundException {
	return (TreeMap<String,Makam>) in.readObject();
}
@SuppressWarnings("unchecked")
private static TreeMap<String, Ahenk> extractedAhenk(ObjectInputStream in)
		throws IOException, ClassNotFoundException {
	return (TreeMap<String,Ahenk>) in.readObject();
}
@SuppressWarnings("unchecked")
private static TreeMap<String, Note> extractedNote(ObjectInputStream in)
		throws IOException, ClassNotFoundException {
	return (TreeMap<String,Note>) in.readObject();
}
}