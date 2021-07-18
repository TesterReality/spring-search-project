package com.example.Viazmus.controller.searchLogic;

import com.example.Viazmus.controller.ResultSearch;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.stream.Collectors;

public class Search {
    //Document xyz,score;
   private static HashMap<String,String> docID;

    public  List<ResultSearch> startSearch (String[] args1) throws IOException{

        File haveFile = new File("upload/docID/docId.txt");
        if (!haveFile.exists()) {
            return null;
        }

        ConverDocIdToFile converDocIdToFile = new ConverDocIdToFile("upload/docID/docId.txt");
           if(converDocIdToFile.parseFile() == false)
           {
               return null;
           }

        docID=converDocIdToFile.getDocID();

        File folder = new File("upload/query");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println(folder.getCanonicalPath());
        }


        HashMap<String,Double> Document_Scorer = new HashMap<>();
        ArrayList<String> Query_list = new ArrayList<>();
       // String[] args1 = new String[]{"child","relaxing","live","work"};

        for (int i=0; i < args1.length; i++) {
            Query_list.add(args1[i].toLowerCase());
            System.out.println(args1[i]);

        }
        File file = null;
        //Removes all stopwords
        Search obj = new Search();
        ArrayList<String> Query_file_Words = new ArrayList<>();
        Query_file_Words=obj.stop_words_remover(Query_list);
        HashMap<String,String> QueryPostingMap = new HashMap<>();

        for(String query:Query_file_Words){
            char file_char = query.charAt(0);
            file = new File("upload/indexFile/"+file_char+".txt");
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String text_line = scanner.next();
                String term_name=text_line.split(" ")[0];
                String posting = text_line.split(" ")[1];
                if((term_name).equals(query)){

                    String[] allFiles = posting.split(";");
                    if(allFiles.length>1)
                    {
                        posting = "";

                        for(int i=0; i<allFiles.length;i++) {


                            String[] word = allFiles[i].split(":");
                            word[0] = word[0].replace("[", "");
                            word[0] = word[0].replace("]", "");

                            word[0] = docID.get(word[0]);
                            for (int j = 0; j < word.length; j++) {
                                if (j == 0 && i==0) {
                                    posting = word[j];
                                } else
                                {
                                    if(j==0)
                                    {
                                        posting = posting + word[j];

                                    }else
                                    {
                                        posting = posting + ":" + word[j];
                                    }
                                }
                            }
                            if(i+1<allFiles.length)
                            {
                                posting=posting+";";
                            }
                        }

                        QueryPostingMap.put(query,posting);



                    }else
                    {
                        String[] word = posting.split(":");
                        word[0]=  word[0].replace("[","");
                        word[0]= word[0].replace("]","");

                        word[0] = docID.get(word[0]);
                        posting="";
                        for (int i=0; i<word.length;i++)
                        {
                            if(i==0)
                            {
                                posting=word[i];
                            }else
                                posting= posting+":"+word[i];
                        }

                        QueryPostingMap.put(query,posting);
                    }



                }
            }
        }

        HashMap<String,List<String>> Query_individualPosting_Map= new HashMap<>();
        //Query and document level posting as array list
        for (String key : QueryPostingMap.keySet()){
            String full_posting = QueryPostingMap.get(key);
            String[] temp= full_posting.split(";");
            List<String> individual_posting = Arrays.asList(temp);
            Query_individualPosting_Map.put(key,individual_posting);
        }

        //Get the documents and add to main Hashmap Document_Scorer
        HashMap<String,List<String>>Document_posting = new HashMap<>();
        List<String> posting_list_doc;
        List<String> individual_posting;
        String document_name,posting;
        int counter;

        for(String query1:Query_individualPosting_Map.keySet()){
            individual_posting = Query_individualPosting_Map.get(query1);
            for(String str:individual_posting){
                document_name = str.split(":")[0];
                counter = Integer.parseInt(str.split(":")[1]);

                //Go through all documents and add score counts
                if(Document_Scorer.containsKey(document_name)){
                    Double score= counter+Document_Scorer.get(document_name);
                    Document_Scorer.put(document_name,score); //Adding frequency to the score
                }else {
                    Double score= (double) counter;
                    Document_Scorer.put(document_name,score);
                }

                //posting is csv of positions
                posting =str.split(":")[2];
                if(Document_posting.containsKey(document_name)){
                    posting_list_doc=Document_posting.get(document_name);
                    posting_list_doc.add(posting);
                    Document_posting.put(document_name,posting_list_doc);
                }else{
                    posting_list_doc = new ArrayList<>();
                    posting_list_doc.add(posting);
                    Document_posting.put(document_name,posting_list_doc);
                }
            }
        }

        List<String> positions;
        List<int[]> integerArray;
        //Go thru entire document set and add score positions
        HashMap<String,List<int[]>>Document_score_positions = new HashMap<>();
        for (String document:Document_posting.keySet()){
            positions = Document_posting.get(document);
            //store in arrays
            for(String str:positions){
                String[] temp= str.split(",");
                int[] position_integer = new int [temp.length];
                for(int i=0;i<temp.length; i++){
                    position_integer[i] =(Integer.parseInt(temp[i]));
                }

                if(Document_score_positions.containsKey(document)){
                    integerArray = Document_score_positions.get(document);
                    integerArray.add(position_integer);
                    Document_score_positions.put(document,integerArray);
                }else{
                    integerArray = new ArrayList<>();
                    integerArray.add(position_integer);
                    Document_score_positions.put(document,integerArray);
                }
            }
        }

        for(String document:Document_score_positions.keySet()){
            for(int i=0;i<(Document_score_positions.get(document).size()-1);i++){
                int A[] = Document_score_positions.get(document).get(i);
                int m = A.length;
                int B[] = Document_score_positions.get(document).get(i+1);
                int n = B.length;
                double shortestDis= Smallestdistance(A,B,m,n);

                Double score= (1/shortestDis)+Document_Scorer.get(document);

                Document_Scorer.put(document,score);
                //System.out.println(score);
            }
        }

        for(String document:Document_score_positions.keySet()) {

            RaitingBoost raitingBoost = new RaitingBoost();
            String str = document;
            int ind = str.lastIndexOf("_lemma");
            if( ind>=0 )
                str = new StringBuilder(str).replace(ind, ind+(str.length()-ind),"").toString();

          //  raitingBoost.boostDoc(str,"0.25");

            double boost =  raitingBoost.getBoostRaiting(str);
            Double score= Document_Scorer.get(document);
            score +=boost;
            Document_Scorer.put(document,score);
        }



        //Select top ten
        Map<String, Double> sortedMap =
                Document_Scorer.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        List<ResultSearch> resultSearches = new ArrayList<>();
        int i=1;
        for(String srt:sortedMap.keySet())
        {
            System.out.println(i+": /input-transform/"+srt+
                    " Score: "+ sortedMap.get(srt));

            double rele = sortedMap.get(srt);
            int ind = srt.lastIndexOf("_lemma");
            if( ind>=0 )
                srt = new StringBuilder(srt).replace(ind, ind+(srt.length()-ind),"").toString();
            System.out.println(srt);

            try {


            ResultSearch rsult = new ResultSearch(srt,rele);
            rsult.setSearchWord(args1);
            rsult.createInformation();

            resultSearches.add(rsult);
            }catch (Exception e)
            {
                System.out.println("Просроченый файл");
            }
           // if (i==10)
              //  break;
            i++;
        }

        return resultSearches;
    }

    static double Smallestdistance(int A[], int B[], int m, int n)
    {
        Arrays.sort(A);
        Arrays.sort(B);
        int a = 0, b = 0;
        double result = Integer.MAX_VALUE;
        while (a < m && b < n)
        {
            if (Math.abs(A[a] - B[b]) < result)
                result = Math.abs(A[a] - B[b]);
            if (A[a] < B[b])
                a++;
            else
                b++;
        }
        return result;
    }

    public ArrayList<String> stop_words_remover(ArrayList<String> Query_list) throws IOException {

        File file = new File("upload/query/Query_file_temp.txt");

        if(!file.exists())
        {
            try
            {
                boolean created = file.createNewFile();
                if(created)
                    System.out.println(file.getName()+ " has been created");
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
        }else
        {
           // clearTheFile(file.getCanonicalPath());
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        ArrayList<String> Query_file_Words = new ArrayList<>();

        Lemmatizer lemmatizer = null;

        for(int i=0;i<Query_list.size();i++)
        {
            String word = Query_list.get(i);
            word=word.replaceAll("[^A-Za-zА-Яа-я0-9 ]", "");// удалится все кроме букв и цифр
            word=word.toLowerCase();
            writer.write(word+" ");

        }

        writer.close();

        String[] file_arg = new String[1];
        file_arg[0] = "Query_file_temp.txt";
        String file_name = "Query_file.txt";

        try {
            lemmatizer = new EnLemmatizer();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        File file_not_temp = new File("upload/query/Query_file.txt");

        if(!file_not_temp.exists())
        {
            try
            {
                boolean created = file_not_temp.createNewFile();
                if(created)
                    System.out.println(file_not_temp.getName()+ " has been created");
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
        }else
        {
           // clearTheFile(file.getCanonicalPath());
        }

        BufferedWriter writerTemp = new BufferedWriter(new FileWriter(file_not_temp.getCanonicalPath()));

        for(int i=0;i<Query_list.size();i++) {
           String result = lemmatizer.getLemma(Query_list.get(i));

            writerTemp.write(result+ " ");
        }
        writerTemp.close();


        File Index_file_temp = new File("upload/query/Query_file_temp.txt");
        Index_file_temp.delete();

        file = new File("upload/query/Query_file.txt");



        Scanner scanner = new Scanner(file);
        scanner.useDelimiter(" ");
        while (scanner.hasNext()) {
            String text = scanner.next();
            Query_file_Words.add(text);
        }
        return (Query_file_Words);
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
}