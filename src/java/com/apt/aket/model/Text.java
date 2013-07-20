package com.apt.aket.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.openlogics.cjb.jdbc.annotation.Column;

/**
 * @project aketPrototype
 * @package com.apt.aket.model
 * @class Text.java (UTF-8)
 * @date 05/07/2013
 * @author Arnold Paye
 */
@ManagedBean
@RequestScoped
public class Text {

    @Column("txt_id")
    private int txtId;
    @Column("txt_title")
    private String txtTitle;
    @Column("txt_author")
    private String txtAuthor;
    @Column("txt_text")
    private String txtText;
    @Column("txt_text_classified")
    private boolean txtTextClassified;
    @Column("txt_keywords_mac")
    private String txtKeywordsMac;
    @Column("txt_keywords_textrank")
    private String txtKeywordsTextRank;

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

    public boolean isTxtTextClassified() {
        return txtTextClassified;
    }

    public void setTxtTextClassified(boolean txtTextClassified) {
        this.txtTextClassified = txtTextClassified;
    }

    public String getTxtKeywordsMac() {
        return txtKeywordsMac;
    }

    public void setTxtKeywordsMac(String txtKeywordsMac) {
        this.txtKeywordsMac = txtKeywordsMac;
    }

    public String getTxtKeywordsTextRank() {
        return txtKeywordsTextRank;
    }

    public void setTxtKeywordsTextRank(String txtKeywordsTextRank) {
        this.txtKeywordsTextRank = txtKeywordsTextRank;
    }
}
