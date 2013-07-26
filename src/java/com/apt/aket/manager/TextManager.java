package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.Text;
import com.apt.aket.model.WordTag;
import com.apt.textrank.Language;
import com.apt.textrank.TextRank;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.DSDescriptor;
import org.openlogics.cjb.jee.jdbc.StatementReader;
import org.openlogics.cjb.jee.util.JEEContext;
import org.openlogics.cjb.jsf.controller.DefaultManager;
import org.primefaces.model.UploadedFile;

/**
 * @project aketPrototype
 * @package com.apt.aket.manager
 * @class TextManager.java (UTF-8)
 * @date 05/07/2013
 * @author Arnold Paye
 */
@ManagedBean
@SessionScoped
@DSDescriptor("sql/text.xml")
public class TextManager extends DefaultManager<Text> {

    static Logger log = Logger.getLogger(TextManager.class);
    private UploadedFile txtFile;
    private UploadedFile pdfFile;
    private List<WordTag> wordTags = null;

    public UploadedFile getTxtFile() {
        return txtFile;
    }

    public void setTxtFile(UploadedFile txtFile) {
        this.txtFile = txtFile;
    }

    public UploadedFile getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(UploadedFile pdfFile) {
        this.pdfFile = pdfFile;
    }

    public List<WordTag> getWordTags() {
        return wordTags;
    }

    public void setWordTags(List<WordTag> wordTags) {
        this.wordTags = wordTags;
    }

    @Override
    protected List<Text> fetchDataFromDataSource() {
        log.debug("Enter fetchDataFromDataSource method");
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            data.clear();
            log.info("DataStore select" + getStatementReader().getStatement("getAllTexts"));
            dataStore.select(getStatementReader().getStatement("getAllTexts"), Text.class, new MappedResultVisitor<Text>() {
                @Override
                public void visit(Text text, DataStore dataStore, ResultSet resultSet) {
                    data.add(text);
                }
            });
        } catch (SQLException sqle) {
            log.error("SQLException in fetchDataFromDataSource method->" + sqle.getMessage());
        } catch (IOException ioe) {
            log.error("IOException in fetchDataFromDataSource method->" + ioe.getMessage());
        } finally {
            return data;
        }
    }

    @Override
    public void selectionFeaturePerformed() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String insertItem() throws FileNotFoundException, IOException, SQLException {
        log.debug("Enter insertItem method");
        Text text = JEEContext.getRequestScopedBean(Text.class);
        if (text.getTxtTitle().trim().isEmpty() || text.getTxtAuthor().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Titulo y Autor son requeridos."));
            return "/";
        } else {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.setAutoCommit(false);
            try {
                dataStore.execute(getStatementReader().getStatement("insertText"), text);
                dataStore.commit();
                return "/index?faces-redirect=true";
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, sqle.getMessage()));
                dataStore.rollBack();
                return "/";
            }
        }

    }

    public String editSelected() throws FileNotFoundException, IOException, SQLException {
        if (selected.getTxtTitle().isEmpty() || selected.getTxtAuthor().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Titulo y Autor son requeridos."));
            return "/";
        } else {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.setAutoCommit(false);
            try {
                dataStore.execute(getStatementReader().getStatement("editText"), selected);
                dataStore.commit();
                return "/index?faces-redirect=true";
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, sqle.getMessage()));
                dataStore.rollBack();
                return "/";
            }
        }
    }

    public void deleteSelected() throws FileNotFoundException, IOException, SQLException {
        DataStore dataStore = DataStoreManager.getDataStore();
        dataStore.setAutoCommit(false);
        try {
            dataStore.execute(getStatementReader().getStatement("deleteWordTag"), selected);
            dataStore.execute(getStatementReader().getStatement("deleteText"), selected);
            dataStore.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Texto eliminado."));
        } catch (SQLException sqle) {
            dataStore.rollBack();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, sqle.getMessage()));
        } finally {
            refresh();
        }
    }

    public void loadFromTxt() throws IOException {
        Text text = JEEContext.getRequestScopedBean(Text.class);
        byte[] buffer = new byte[6124];
        int bulk;
        StringBuilder stringBuilder = new StringBuilder();
        if (text != null) {
            if (txtFile != null) {
                InputStream inputStream = txtFile.getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    System.out.write(buffer, 0, bulk);
                    stringBuilder.append(new String(buffer));
                }
                text.setTxtText(stringBuilder.toString());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Archivo importado."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Archivo no seleccionado."));
            }
        }
    }

    public void loadEditFromTxt() throws IOException {
        Text text = selected;
        byte[] buffer = new byte[6124];
        int bulk;
        StringBuilder stringBuilder = new StringBuilder();
        if (text != null) {
            if (txtFile != null) {
                InputStream inputStream = txtFile.getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    System.out.write(buffer, 0, bulk);
                    stringBuilder.append(new String(buffer));
                }
                text.setTxtText(stringBuilder.toString());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Archivo importado."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Archivo no seleccionado."));
            }
        }
    }

    public void cleanNewTexForm() {
        Text text = JEEContext.getRequestScopedBean(Text.class);
        if (text != null) {
            text.setTxtTitle("");
            text.setTxtAuthor("");
            text.setTxtText("");
        }
    }

    public void cleanEditTexForm() {
        Text text = selected;
        if (text != null) {
            text.setTxtTitle("");
            text.setTxtAuthor("");
            text.setTxtText("");
        }
    }

    /**
     * Classify selected's text.
     */
    public void classifySelected() {
        log.info("Texto a ser clasificado:---------------------------------------------\n" + selected.getTxtText());
        List<WordTag> words = new ArrayList<WordTag>();
        try {
            if (selected != null) {
                Language language = Language.buildLanguage(System.getProperty("catalina.home") + "/resourcesNLP");
                // Split raw text in sentences.
                String[] sentences = language.splitParagraph(selected.getTxtText());
                // Tokenize and tag each sentence
                for (String sentence : sentences) {
                    String[] tokens = language.tokenizeSentence(sentence);
                    String[] tags = language.tagTokens(tokens);
                    for (int i = 0; i < tokens.length; i++) {
                        words.add(new WordTag(selected.getTxtId(), tokens[i], tags[i]));
                    }

                }
                // Create the output text.
                wordTags = words;
            }
        } catch (IOException ioe) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, ioe.getMessage()));
        }

    }

    /**
     * Insert in the database the word-tag of selected's text.
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public String saveSelectedClassified() throws FileNotFoundException, IOException, SQLException {
        if (wordTags == null || wordTags.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "No existen palabras clasificadas."));
            return "/";
        } else {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.setAutoCommit(false);
            PreparedStatement statement = null;
            StatementReader reader = getStatementReader();
            try {
                dataStore.execute(getStatementReader().getStatement("deleteWordTag"), selected);
                // Insert each tag into database.
                for (WordTag wordTag : wordTags) {
                    statement = dataStore.addBatch(reader.getStatement("insertWordTag"), statement, wordTag);
                }
                if (statement != null) {
                    dataStore.executeBatch(statement);
                }
                dataStore.execute(getStatementReader().getStatement("classifyText"), selected);
                dataStore.commit();
                return "/index?faces-redirect=true";
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, sqle.getMessage()));
                dataStore.rollBack();
                return "/";
            } finally {
                statement.close();
                wordTags = null;
            }
        }
    }

    public String cancelSelectedClassified() {
        wordTags = null;
        return "/index?faces-redirect=true";
    }

    public void stuff() {
        TextRank textRank = new TextRank();
        try {
            if (selected != null) {
                textRank.init(selected.getTxtText());
            }
        } catch (IOException ioe) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage()));
        }
    }
}