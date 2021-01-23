package com.example.Viazmus.controller.searchLogic;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Affix> readAffixes(InputStream inputStream) throws XMLStreamException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream must not be null");
        }

        List<Affix> affixes = null;
        Affix curAffix = null;
        String tagContent = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    if ("line".equals(reader.getLocalName())) {
                        curAffix = new Affix();
                    }
                    if ("affixes".equals(reader.getLocalName())) {
                        affixes = new ArrayList<>();
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "line":
                            affixes.add(curAffix);
                            break;
                        case "regex":
                            curAffix.setRegex(tagContent);
                            break;
                        case "base":
                            curAffix.setBase(tagContent);
                            break;
                        case "ending":
                            curAffix.setEnding(tagContent);
                            break;
                    }
                    break;
            }
        }

        // Close the input stream
        inputStream.close();

        return affixes;
    }

    public static List<String> readDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream must not be null");
        }

        List<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("/[a-zA-Z]*", "");
            words.add(line);
        }
        inputStream.close();
        reader.close();

        return words;
    }

    public static String cyrillicToAscii(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Word must not be null");
        }
        char[] cyrillic = word.toCharArray();
        char[] ascii = new char[cyrillic.length];
        for (int i = 0; i < cyrillic.length; i++) {
            ascii[i] = (char) (cyrillic[i] - 'А');
        }
        return String.valueOf(ascii);
    }

    public static String asciiToCyrillic(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Word must not be null");
        }
        char[] ascii = word.toCharArray();
        char[] cyrillic = new char[ascii.length];
        for (int i = 0; i < ascii.length; i++) {
            cyrillic[i] = (char) (ascii[i] + 'А');
        }
        return String.valueOf(cyrillic);
    }
}
