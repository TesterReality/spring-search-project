package com.example.Viazmus.controller.searchLogic;


import com.example.Viazmus.controller.NeedChangeIndexSingleton;
import com.example.Viazmus.controller.searchLogic.indexing.frequencyCounter;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ChangeIndex {
    public void reindex() {
        String result;
        try {


            File folder = new File("upload/docID");
            if (!folder.exists()) {
                folder.mkdir();
                System.out.println(folder.getCanonicalPath());
            }


            folder = new File("upload/Lemma");
            if (!folder.exists()) {
                folder.mkdir();
                System.out.println(folder.getCanonicalPath());
            }
            deleteAllFilesFolder(folder.getCanonicalPath());

            folder = new File("upload/indexFile");
            if (!folder.exists()) {
                folder.mkdir();
                System.out.println(folder.getCanonicalPath());
            }
            deleteAllFilesFolder(folder.getCanonicalPath());


            folder = new File("upload/");
            File[] listOfFiles = folder.listFiles();
            System.out.println(folder.getAbsolutePath());
            Lemmatizer lemmatizer = new EnLemmatizer();

            for (File file : listOfFiles) {
                if (file.isFile()) {

                    String content = Files.lines(Paths.get(file.getCanonicalPath())).reduce("", String::concat);
                    content = content.replaceAll("[^A-Za-zА-Яа-я0-9 ]", ""); // удалится все кроме букв и цифр
                    content = content.toLowerCase();
                    String[] words = content.split("\\s");
                    File fileOut = new File(folder.getCanonicalPath()+"/Lemma/"+file.getName()+"_lemma"+".txt");
                    try
                    {
                        boolean created = fileOut.createNewFile();
                        if(created)
                            System.out.println(fileOut.getName()+ " has been created");
                    }
                    catch(IOException ex){

                        System.out.println(ex.getMessage());
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileOut,true));

                    StringBuilder resultLemms = new StringBuilder();
                    for(int i=0; i <words.length; i++)
                    {
                        if(!words[i].isEmpty())
                        {
                           // result = lemmatizer.getLemma(words[i]);
                            resultLemms.append(lemmatizer.getLemma(words[i]) +" ");
                        }
                    }
                    writer.write(resultLemms.toString()+" ");

                    writer.close();
                }
            }

            folder = new File("upload/docID");
            File fileDocId = new File(folder.getCanonicalPath()+"/docID"+".txt");
            clearTheFile(fileDocId.getCanonicalPath());
            try
            {
                boolean created = fileDocId.createNewFile();
                if(created)
                    System.out.println(fileDocId.getName()+ " has been created");
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }


            File folderLemma = new File("upload/Lemma");
            File[] listOflems = folderLemma.listFiles();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileDocId,true));
            HashMap<String,String> docID = new HashMap<>();

            for(int i=0;i<listOflems.length;i++)
            {
                docID.put(listOflems[i].getName(),String.valueOf(i));//ключ - имя файла, значение - docID
                writer.write(i+":"+listOflems[i].getName()+"\n");
            }
            writer.close();




          frequencyCounter indexing = new frequencyCounter(docID);
          indexing.startIndexind();//построили инвертированный индекс и словарь
/*
            ConverDocIdToFile converDocIdToFile = new ConverDocIdToFile("src/upload/docID/docId.txt");
            converDocIdToFile.parseFile();

            docID.clear();
            docID = converDocIdToFile.getDocID();*/

            new NeedChangeIndexSingleton().getInstance().setChanged(false);
           new RaitingBoost().refresh();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static void clearTheFile(String FileName) {
        FileWriter fwOb = null;
        try {
            fwOb = new FileWriter(FileName, false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllFilesFolder(String path) {
        for (File myFile : new File(path).listFiles())
            if (myFile.isFile()) myFile.delete();
    }

}
