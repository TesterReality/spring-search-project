package com.example.Viazmus.controller.searchLogic;

import javax.print.Doc;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class RaitingBoost {


    private String BoostDir = "upload/boost";
    private String BoostFile = "boost.txt";
    HashMap<String, Double> Document_Boost = new HashMap<>();

    public double getBoostRaiting(String docName) {
        try {
            checkLoading();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(BoostDir + "/" + BoostFile);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String text_line = scanner.next();
                String[] valueDoc = text_line.split(":");
                Document_Boost.put(valueDoc[0], Double.parseDouble(valueDoc[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double res = 0;

        boolean hasInFile = false;

        if (Document_Boost.get(docName) != null) {
            hasInFile = true;
        }

        if (hasInFile == false) {
            boostDoc(docName, "0");
        } else
            res = Document_Boost.get(docName);

        return res;

    }

    public void boostDoc(String fileName, String value) {
        boolean isCreate = false;
        try {
            checkLoading();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(BoostDir + "/" + BoostFile);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            // scanner.useDelimiter("\r");
            while (scanner.hasNext()) {
                String text_line = scanner.next();
                text_line = text_line.replace("\n", "");
                text_line = text_line.replace("\r", "");

                String[] valueDoc = text_line.split(":");

                if (valueDoc[0].equals(fileName)) {//нужный нам файл

                    isCreate = true;
                    double valueBoost = Double.parseDouble(valueDoc[1]);
                    double newValue = Double.parseDouble(value);
                    newValue = valueBoost + newValue;
                    replaceStr(file.getCanonicalPath(), text_line, valueDoc[0] + ":" + String.valueOf(newValue));
                    break;
                }

            }
            if (!isCreate)//если в файле прежде не было такого документа(документ:оценка)
            {

                try {
                    FileWriter writer = new FileWriter(file.getCanonicalPath(), true);
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    bufferWriter.write(fileName + ":" + value + "\n");
                    bufferWriter.close();

                } catch (IOException e) {
                    System.out.println(e);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void replaceStr(String filePath, String old, String newStr) throws IOException {
        List<String> fileContent = null;
        fileContent = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));

        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals(old)) {
                fileContent.set(i, newStr);
                break;
            }
        }

        Files.write(Paths.get(filePath), fileContent);
    }

    //Есть ли такая директория с тсаким файлом? true - да
    public boolean checkLoading() throws IOException {

        File folder = new File(BoostDir);
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Директория boost создана");
        }
        File fileOut = null;
        fileOut = new File(folder.getCanonicalPath() + "/" + BoostFile);
        if (!fileOut.exists()) {
            boolean created = fileOut.createNewFile();
            if (created) {
                System.out.println(fileOut.getName() + " has been created");
                return false;
            }
        }
        return true;
    }

    public void refresh() {
        try {
            FileWriter fstream1 = new FileWriter(BoostDir + "/" + BoostFile);// конструктор с одним параметром - для перезаписи
            BufferedWriter out1 = new BufferedWriter(fstream1); //  создаём буферезированный поток
            out1.write(""); // очищаем, перезаписав поверх пустую строку
            out1.close(); // закрываем
        } catch (Exception e) {
            System.err.println("Error in file cleaning: " + e.getMessage());
        }
    }
}
