package com.apt.aket.model;

import org.openlogics.cjb.jdbc.annotation.Column;

/**
 * @project aketPrototype
 * @package com.apt.aket.model
 * @class WordTag.java (UTF-8)
 * @date 19/07/2013
 * @author Arnold Paye
 */
public class WordTag {
    @Column("wtg_id")
    private long worTagId;
    @Column("wtg_txt_id")
    private int textId;
    @Column("wtg_value")
    private String value;
    @Column("wtg_tag")
    private String tag;

    public long getWorTagId() {
        return worTagId;
    }

    public void setWorTagId(long worTagId) {
        this.worTagId = worTagId;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public WordTag(int textId, String value, String tag) {
        this.textId = textId;
        this.value = value;
        this.tag = tag;
    }
}