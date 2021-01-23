package com.example.Viazmus.controller.searchLogic;

import com.sun.org.apache.xml.internal.utils.Trie;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Lemmatizer {

    protected List<Affix> affixes;
    protected Dictionary dictionary;

    protected abstract void initAffixes() throws IOException, XMLStreamException;
    protected abstract void initDictionary() throws IOException;

    public String getLemma(String word) throws IllegalArgumentException {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Word must not be empty. ");
        }

        if (dictionary.contains(word)) {
            return word;
        }

        for (Affix affix : affixes) {
            // Смотрим в подходящее окончание
            if (word.endsWith(affix.getEnding())) {
                String candidate = word.replaceAll(affix.getEnding() + "$", affix.getBase());

                Pattern pattern = Pattern.compile(".*" + affix.getRegex());
                Matcher matcher = pattern.matcher(candidate);

                // Если файл подходит по правилам и есть в словаре,
                // это и есть нормализованная форма слова
                if (matcher.matches() && dictionary.contains(candidate)) {
                    return candidate;
                }
            }
        }

        return word;
    }

}