package com.example.Viazmus.controller;

import com.example.Viazmus.controller.searchLogic.EnLemmatizer;
import com.example.Viazmus.controller.searchLogic.Lemmatizer;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class ResultSearch {

    private String filePath;
    private String[] searchWord;

    private String img;//ссылка на картинку
    private String link;//ссылка на вью файл
    private String firstWord;//большая надпись (главная)
    private String secondWord;//надпись снизу
    private String fileName;//название файла
    private String size;//размер файла
    private String relevantStr;//размер файла

    private Double relevant;//пока не знаю, будет значение релевантности навреное(цифры) или хорошо плохо

    public ResultSearch(String fileName, Double relevant) {
        this.fileName = fileName;
        this.relevant = relevant;
    }


    public void createInformation()
    {
        filePath="upload/"+fileName;
        img = "img/TXT.png";
        link="/view?file="+fileName;
        firstWord=serchWordInFile(3);
        secondWord = serchWordInFile(6);
        secondWord = secondWord +" ...";
        size = getSizeFile();
        relevantStr=relevantToStr(relevant);
    }

    private String getSizeFile()
    {
        File file = new File(filePath);
        return getFileSizeKiloBytes(file);
    }

    private static String relevantToStr(Double rel) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String test = df.format(rel);
        return test;
    }

    // метод возвращает размер файла в килобайтах
    // длину файла делим на 1 килобайт (1024 байт) и узнаем количество килобайт
    private static String getFileSizeKiloBytes(File file) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        double kbSize = (double) file.length()/1024;
       String test = df.format(kbSize);
        return test+ " KB";
    }
    private String serchWordInFile(int lenght)
    {
        String resultBigName ="";
        String content = null;
        try {
            content = Files.lines(Paths.get(filePath)).reduce("", String::concat);
            content = content.toLowerCase();
            String[] words = content.split("\\s");
            boolean ready = false;
            for(int i=0;i<searchWord.length;i++)
            {
                for(int j=0;j<words.length;j++)
                {
                    if(searchWord[i].equals(words[j]))
                    {
                        ready=true;
                        int howLenght =0;
                        do {
                            try {
                                resultBigName = resultBigName + words[j] + " ";
                            }catch (Exception e){}
                            howLenght++;
                            j++;
                        }while(howLenght<lenght);
                        if(ready) break;
                    }
                }
                if(ready) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(resultBigName.equals(""))
        {

            Lemmatizer lemmatizer = null;
            String[] words = content.split("\\s");

            try {
                lemmatizer = new EnLemmatizer();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }


            boolean ready = false;
            for(int i=0;i<searchWord.length;i++)
            {
                for(int j=0;j<words.length;j++)
                {
                    if(words[j].isEmpty()) continue;
                    /*
                    if(searchWord[i].equals(lemmatizer.getLemma(words[j])))
                    {
                        ready=true;
                        int howLenght =0;
                        do {
                            resultBigName = resultBigName+words[j] +" ";
                            howLenght++;
                            j++;
                        }while(howLenght<lenght);
                        if(ready) break;
                    }*/
                if( words[j].contains(searchWord[i]))
                {
                    ready=true;
                    int howLenght =0;
                    do {
                        try {
                            resultBigName = resultBigName + words[j] + " ";
                        }catch (Exception e){}
                        howLenght++;
                        j++;
                    }while(howLenght<lenght);
                    if(ready) break;
                }

                }
                if(ready) break;
            }
        }


        return resultBigName;
    }

    public String[] getSearchWord() {
        return searchWord;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Double getRelevant() {
        return relevant;
    }

    public void setRelevant(Double relevant) {
        this.relevant = relevant;
    }

    public void setSearchWord(String[] searchWord) {
        this.searchWord = searchWord;
    }

    public String getImg() {
        return img;
    }

    public String getRelevantStr() {
        return relevantStr;
    }

    public void setRelevantStr(String relevantStr) {
        this.relevantStr = relevantStr;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public void setSecondWord(String secondWord) {
        this.secondWord = secondWord;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
