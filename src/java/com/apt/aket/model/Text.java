package com.apt.aket.model;

/**
 * Project: aketPrototype 
 * Package: com.apt.aket.model 
 * Class : Text.java (UTF-8)
 * @date 05/07/2013
 * @author Arnold Paye
 */

public class Text {

    private int txtId;
    private String txtTitle;
    private String txtAuthor;
    private String txtText;

    public int getTxtId() {
        return txtId;
    }
    
    public Text(int txtId, String txtTitle, String txtAuthor, String txtText) {
        this.txtId = txtId;
        this.txtTitle = txtTitle;
        this.txtAuthor = txtAuthor;
        this.txtText = txtText;
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
