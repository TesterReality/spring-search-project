package com.example.Viazmus.controller.searchLogic.indexing;

import java.io.*;
import java.util.*;

public class frequencyCounter {
    HashMap<String,List<String>> MainMap = new HashMap<>();
    HashMap<String,String> docID;


    public frequencyCounter(HashMap<String, String> docID) {
        this.docID = docID;
    }

    public frequencyCounter() {
    }

    public void startIndexind() throws IOException{

        frequencyCounter index = new frequencyCounter(docID);
        File directory = new File("upload/Lemma/");
        File[] files = directory.listFiles();
        try {
            for (File Index_file : files) {
                String fileExtension = getExtension(Index_file);
                if (!(fileExtension.toLowerCase().equals("ds_store")))
                    index.createIndex(Index_file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Writing...");
        index.Sort();
    }

    public static String getExtension(File file){
        String fileName = file.getName();
        if(fileName.lastIndexOf('.')>fileName.lastIndexOf('/'))
            return fileName.substring((fileName.lastIndexOf('.'))+1);
        else
            return "";
    }

    // create inverted index of all files
    public void createIndex(File file) throws Exception{

        //For each file, we create a submap
        HashMap<String, List<String>> SubMap = new HashMap<>();
        String fileName = file.getName().substring(0,(file.getName().lastIndexOf('.')));
        System.out.println("File name:"+ fileName +" docID: "+ docID.get(file.getName()));
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter(" ");

        int position = 0;

        while (scanner.hasNext()) {
            String text = scanner.next();
            position++;
            if(SubMap.containsKey(text)){
                SubMap.get(text).add(Integer.toString(position));
            }
            else{
                List<String> position_list = new ArrayList<>();
                position_list.add(Integer.toString(position));
                SubMap.put(text,position_list);
            }
        }

        System.out.println("End");

        for (String text : SubMap.keySet()){
            //For a particular word, we retrieve the positions
            List<String> positionList = SubMap.get(text);

            //Make posting for each word, market 1:4:3,11,15,25 (Document)
            StringBuilder posting = new StringBuilder("["+docID.get(file.getName())+"]"+ ":" + positionList.size() + ":");
            int i=1;
            for(String pos:positionList){
                //posting = posting + pos;
                posting.append(pos);
                if(i != positionList.size()){
                   // posting = posting + ',';
                    posting.append(",");
                }
                i++;
            }

            //Make posting for each word, market 1:4:3,11,15,25;2:1:4 (Documents)
            List<String> Documents_posting;
            if(MainMap.containsKey(text)){
                Documents_posting = MainMap.get(text);
                Documents_posting.add(posting.toString());
                MainMap.put(text,Documents_posting);
            }
            else{
                Documents_posting = new ArrayList<>();
                Documents_posting.add(posting.toString());
                MainMap.put(text,Documents_posting);
            }
        }
    }

    public void Sort() throws IOException{
        Map<String, List<String>> treeMainMap = new TreeMap<String, List<String>>(MainMap);
        /*agricultur 1:1:7
        agrochem 1:1:17
        carri 1:1:5
        chemic 1:2:8,13*/

        char ch = 'a';
        for(String str : treeMainMap.keySet()){
            if(!str.equals("")) {
                ch = str.charAt(0);
                File dirOfIndex = new File("upload/indexFile");
                if (!dirOfIndex.exists()) {
                    dirOfIndex.mkdir();
                    System.out.println("Создан" + dirOfIndex.getCanonicalPath());
                }

                File file = new File("upload/indexFile/" + ch + ".txt");
                try {
                    boolean created = file.createNewFile();
                    if (created)
                        System.out.println(file.getName() + " has been created");
                } catch (IOException ex) {

                    System.out.println(ex.getMessage());
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.write(str + " " + String.join(";", treeMainMap.get(str)) + "\n");
                writer.close();
            }
        }
    }


}
