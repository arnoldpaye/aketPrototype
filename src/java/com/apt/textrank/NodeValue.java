package com.apt.textrank;

import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class NodeValue.java (UTF-8)
 * @date 25/07/2013
 * @author Arnold Paye
 */
public class NodeValue {

    static Logger log = Logger.getLogger(NodeValue.class);
    protected String text;

    public String getText() {
        return text;
    }

    public String getDescription() {
        return "GENERIC" + '\t' + getCollocation();
    }

    public String getCollocation() {
        return text.replace(' ', '_');
    }
}
