package com.apt.aket.model;

import org.openlogics.cjb.jdbc.annotation.Column;

/**
 * Project: aketPrototype 
 * Package: com.apt.aket.model 
 * Class : Text.java (UTF-8)
 *
 * @date 05/07/2013
 * @author Arnold Paye
 */
public class Text {

    @Column("txt_id")
    private int txtId;
    @Column("txt_title")
    private String txtTitle;
    @Column("txt_author")
    private String txtAuthor;
    @Column("txt_text")
    private String txtText;

    public int getTxtId() {
        return txtId;
    }

    public void setTxtId(int txtId) {
        this.txtId = txtId;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public String getTxtAuthor() {
        return txtAuthor;
    }

    public void setTxtAuthor(String txtAuthor) {
        this.txtAuthor = txtAuthor;
    }

    public String getTxtText() {
        return txtText;
    }

    public void setTxtText(String txtText) {
        this.txtText = txtText;
    }
}
