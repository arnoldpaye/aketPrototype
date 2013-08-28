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
    @Column("txt_code")
    private String txtCode;
    @Column("txt_title")
    private String txtTitle;
    @Column("txt_author")
    private String txtAuthor;
    @Column("txt_text")
    private String txtText;
    @Column("txt_textrank")
    private String txtTextRank;
    private Keyword kwMarc21 = new Keyword();
    private Keyword kwRddu = new Keyword();
    private Keyword kwExpert = new Keyword();

    /* Getters and Setters */
    public int getTxtId() {
        return txtId;
    }

    public void setTxtId(int txtId) {
        this.txtId = txtId;
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

    public String getTxtText() {
        return txtText;
    }

    public void setTxtText(String txtText) {
        this.txtText = txtText;
    }

    public String getTxtTextRank() {
        return txtTextRank;
    }

    public void setTxtTextRank(String txtTextRank) {
        this.txtTextRank = txtTextRank;
    }

    public Keyword getKwMarc21() {
        return kwMarc21;
    }

    public void setKwMarc21(Keyword kwMarc21) {
        this.kwMarc21 = kwMarc21;
    }

    public Keyword getKwRddu() {
        return kwRddu;
    }

    public void setKwRddu(Keyword kwRddu) {
        this.kwRddu = kwRddu;
    }

    public Keyword getKwExpert() {
        return kwExpert;
    }

    public void setKwExpert(Keyword kwExpert) {
        this.kwExpert = kwExpert;
    }
}