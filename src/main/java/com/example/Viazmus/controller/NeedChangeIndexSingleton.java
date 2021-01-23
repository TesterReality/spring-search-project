package com.example.Viazmus.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NeedChangeIndexSingleton {

    private static final NeedChangeIndexSingleton INSTANCE = new NeedChangeIndexSingleton();
    private static boolean isChanged = false;
    private static boolean isFirst = true;

    public NeedChangeIndexSingleton(){}

    public void someMethod() {

    }

    public static NeedChangeIndexSingleton getInstance(){
        if(isFirst)
        {
            isFirst=false;
            try {
                first();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }
    private static void first() throws IOException {

        File folder = new File("upload/changes");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Директория статуса создана");
        }
        File fileOut =null;
        fileOut = new File(folder.getCanonicalPath()+"/status.txt");
        if(!fileOut.exists()) {
            boolean created = fileOut.createNewFile();
            if (created)
                System.out.println(fileOut.getName() + " has been created");
            setChanged(false);
        }else
        {
           String res= readUsingFiles(fileOut.getCanonicalPath());

           if(res.equals("true"))
           {
               setChanged(true);
           }else setChanged(false);
        }
    }
    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
    private static void writeStatus(boolean stat) throws IOException {
        File folder = new File("upload/changes");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Директория статуса создана");
        }

        File fileOut =null;
            fileOut = new File(folder.getCanonicalPath()+"/status.txt");
            if(!fileOut.exists()) {
                boolean created = fileOut.createNewFile();
                if (created)
                    System.out.println(fileOut.getName() + " has been created");
            }
        clearTheFile(fileOut);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileOut,true));
        writer.write(Boolean.toString(stat));
        writer.close();
    }
    public boolean isChanged() {
        return isChanged;
    }
    public static void clearTheFile(File file) throws IOException {
        FileWriter fwOb = new FileWriter(file.getCanonicalPath(), false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }
    public static void setChanged(boolean changed) {
        isChanged = changed;
        try {

            writeStatus(changed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
