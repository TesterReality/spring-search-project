package com.example.Viazmus.controller.searchLogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ConverDocIdToFile {

    private String path;
    HashMap<String,String> docID = new HashMap<>();

    public ConverDocIdToFile(String path) {
        this.path = path;
    }


    public void parseFile()
    {
       File doIdFile = new File(path);
       if(doIdFile.exists())
       {
           if (doIdFile.isFile())
           {
               try {
                   String file = readUsingBufferedReader(doIdFile.getCanonicalPath());
                   String[] allStr = file.split("\r\n");

                   for (int i=0;i<allStr.length;i++)
                   {
                       allStr[i]=allStr[i].replace("\r","");
                   }

                   String oneStr="";
                   String[] docIdFileName;
                   for (int i=0;i<allStr.length;i++)
                   {
                       if(!allStr[i].isEmpty())
                       {
                          oneStr = allStr[i];
                          docIdFileName=oneStr.split(":");
                          docID.put(docIdFileName[0],docIdFileName[1]);
                       }
                   }

               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }

    }

    private static String readUsingBufferedReader(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader(fileName));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public HashMap<String, String> getDocID() {
        return docID;
    }

    public void setDocID(HashMap<String, String> docID) {
        this.docID = docID;
    }
}
