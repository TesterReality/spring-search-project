package com.example.Viazmus.controller.searchLogic;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class EnLemmatizer extends Lemmatizer {

    public EnLemmatizer() throws IOException, XMLStreamException {
        initAffixes();
        initDictionary();
    }

    @Override
    protected void initAffixes() throws IOException, XMLStreamException {
        InputStream stream = getClass().getResourceAsStream("/affix_en.xml");
        this.affixes = Util.readAffixes(stream);
    }

    @Override
    protected void initDictionary() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/dict_en.txt");
        List<String> words = Util.readDictionary(stream);
        this.dictionary = new Dictionary();
        this.dictionary.put(words);
    }
}
