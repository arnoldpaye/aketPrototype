package com.apt.textrank;

import com.apt.util.Util;
import java.io.File;
import java.io.IOException;
import java.util.List;
import opennlp.tools.lang.spanish.PosTagger;
import opennlp.tools.lang.spanish.SentenceDetector;
import opennlp.tools.lang.spanish.Tokenizer;
import org.tartarus.snowball.ext.spanishStemmer;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class Language.java (UTF-8)
 * @date 25/07/2013
 * @author Arnold Paye
 */
public class Language {

    public static SentenceDetector splitter = null;
    public static Tokenizer tokenizer = null;
    public static PosTagger tagger = null;
    public static spanishStemmer stemmer = null;
    
    public final static int TOKEN_LENGTH_LIMIT = 50;

    public void loadResources(String path) throws IOException {
        splitter = new SentenceDetector((new File(path, "SpanishSent.bin.gz")).getPath());
        tokenizer = new Tokenizer((new File(path, "SpanishTokChunk.bin.gz")).getPath()); //TODO: tok or tokchunk
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
        String[] tokenList = tokenizer.tokenize(sentence);
        String[] tokenListClean = new String[tokenList.length];
        for (int i = 0; i < tokenList.length; i++) {
            tokenListClean[i] = Util.deletePuntuationSigns(tokenList[i]);
        }
        return tokenListClean;
    }

    public String[] tagTokens(String[] tokenList) {
        return tagger.tag(tokenList);
    }
    
    public String scrubToken(String token) {
        //TODO: in Util?
        String scrubbed = token;
        if (scrubbed.length() > TOKEN_LENGTH_LIMIT) {
            scrubbed = scrubbed.substring(0, TOKEN_LENGTH_LIMIT);
        }
        return scrubbed;
    }
    
    public String stemToken(String token) {
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }
    
    public String getNodeKey(String token, String pos) {
        return pos.substring(0, 2) + stemToken(scrubToken(token)).toLowerCase();
    }
    
    public boolean isRelevant(String pos) {
        return isNoun(pos) || isAdjective(pos);
    }
    
    public boolean isRelevant(String pos, List<String> posFilterList) {
        return posFilterList.contains(pos);
    }
    
    public boolean isNoun(String pos) {
        return pos.startsWith("NC");
    }
    
    public boolean isAdjective(String pos) {
        return pos.startsWith("AQ");
    }
}