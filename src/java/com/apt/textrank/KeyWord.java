package com.apt.textrank;

import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class KeyWord.java (UTF-8)
 * @date 25/07/2013
 * @author Arnold Paye
 */
public class KeyWord extends NodeValue {

    static Logger log = Logger.getLogger(NodeValue.class);
    private String pos;

    public String getPos() {
        return pos;
    }

    public KeyWord(String text, String pos) {
        this.text = text;
        this.pos = pos;
    }

    @Override
    public String getDescription() {
        return "KEYWORD" + '\t' + ' ' + text + ' ' + pos;
    }
}
