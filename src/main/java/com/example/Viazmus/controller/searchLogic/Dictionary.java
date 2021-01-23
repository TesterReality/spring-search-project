package com.example.Viazmus.controller.searchLogic;

import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.List;

public class Dictionary {
    protected Trie trie;

    public Dictionary() {
        this.trie = new Trie();
    }

    public void put(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Word must not be null");
        }
        trie.put(word.toLowerCase(), true);
    }

    public void put(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Word must not be null");
        }

        for (String word : words) {
            put(word);
        }
    }

    public boolean contains(String word) {
        Object value = trie.get(word);
        return value != null;
    }
}
