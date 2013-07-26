package com.apt.textrank;

import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class Context.java (UTF-8)
 * @date 26/07/2013
 * @author Arnold Paye
 */
public class Context {

    static Logger log = Logger.getLogger(Context.class);
    private Sentence sentence;
    private int start;
    
    public Context(Sentence sentence, int start) {
        this.sentence = sentence;
        this.start = start;
    }
}
