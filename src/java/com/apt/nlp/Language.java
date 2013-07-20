package com.apt.nlp;

import java.io.File;
import java.io.IOException;
import opennlp.tools.lang.spanish.PosTagger;
import opennlp.tools.lang.spanish.SentenceDetector;
import opennlp.tools.lang.spanish.Tokenizer;
import org.tartarus.snowball.ext.spanishStemmer;

/**
 * @project aketPrototype
 * @package com.apt.nlp
 * @class Language.java (UTF-8)
 * @date 19/07/2013
 * @author Arnold Paye
 */
public class Language {

    public static SentenceDetector splitter = null;
    public static Tokenizer tokenizer = null;
    public static PosTagger tagger = null;
    public static spanishStemmer stemmer = null;

    public void loadResources(String path) throws IOException {
        splitter = new SentenceDetector((new File(path, "SpanishSent.bin.gz")).getPath());
        tokenizer = new Tokenizer((new File(path, "SpanishTok.bin.gz")).getPath());
        tagger = new PosTagger((new File(path, "SpanishPOS.bin.gz")).getPath());
        stemmer = new spanishStemmer();
    }

    public Language(String path) throws IOException {
        if (splitter == null) {
            loadResources(path);
        }
    }

    public static Language buildLanguage(String pathResource) throws IOException {
        Language language = null;
        language = new Language(pathResource + "/");
        return language;
    }

    /**
     * Split text into sentences.
     *
     * @param text
     * @return
     */
    public String[] splitParagraph(String text) {
        return splitter.sentDetect(text);
    }

    /**
     * Tokenize sentence.
     *
     * @param sentence
     * @return
     */
    public String[] tokenizeSentence(String sentence) {
        return tokenizer.tokenize(sentence);
    }

    public String[] tagTokens(String[] tokenList) {
        return tagger.tag(tokenList);
    }
}
