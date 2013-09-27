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

    /* Members */
    @Column("txt_id")
    private int txtId;
    @Column("txt_cr_id")
    private int txtCrId;
    @Column("cr_name")
    private String crName;
    @Column("txt_code")
    private String txtCode;
    @Column("txt_title")
    private String txtTitle;
    @Column("txt_author")
    private String txtAuthor;
    @Column("txt_keyword")
    private String txtKeyword;
    @Column("txt_text")
    private String txtText;
    //TODO: others keywords
    private Keyword keyword = new Keyword();

    /* Getters and Setters */
    public int getTxtId() {
        return txtId;
    }

    public void setTxtId(int txtId) {
        this.txtId = txtId;
    }

    public int getTxtCrId() {
        return txtCrId;
    }

    public void setTxtCrId(int txtCrId) {
        this.txtCrId = txtCrId;
    }

    public String getCrName() {
        return crName;
    }

    public void setCrName(String crName) {
        this.crName = crName;
    }

    public String getTxtCode() {
        return txtCode;
    }

    public void setTxtCode(String txtCode) {
        this.txtCode = txtCode;
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

    public String getTxtKeyword() {
        return txtKeyword;
    }

    public void setTxtKeyword(String txtKeyword) {
        this.txtKeyword = txtKeyword;
    }

    public String getTxtText() {
        return txtText;
    }

    public void setTxtText(String txtText) {
        this.txtText = txtText;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }
}