package com.apt.bean;

import com.apt.aket.model.Word;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Project: aketPrototype
 * Package: com.apt.bean
 * Class  : DataBean.java (UTF-8)
 * @date 06/07/2013
 * @author Arnold Paye
 */

@ManagedBean
@RequestScoped
public class DataBean {
    public List<Word> getWords() {
        List<Word> words = new ArrayList<Word>();
        words.add(new Word("Palabra 1", 0.5));
        words.add(new Word("Palabra 2", 0.5));
        words.add(new Word("Palabra 3", 0.5));
        words.add(new Word("Palabra 4", 0.5));
        words.add(new Word("Palabra 5", 0.5));
        words.add(new Word("Palabra 6", 0.5));
        return words;
    }
}
