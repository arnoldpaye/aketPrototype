package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.Text;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.DSDescriptor;
import org.openlogics.cjb.jee.util.JEEContext;
import org.openlogics.cjb.jsf.controller.DefaultManager;
import org.primefaces.model.UploadedFile;

/**
 * Project: aketPrototype Package: com.apt.aket.manager Class : TextManager.java
 * (UTF-8)
 *
 * @date 05/07/2013
 * @author Arnold Paye
 */
@ManagedBean
@SessionScoped
@DSDescriptor("sql/text.xml")
public class TextManager extends DefaultManager<Text> {

    private UploadedFile txtFile;
    private UploadedFile pdfFile;

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

    @Override
    protected List<Text> fetchDataFromDataSource() {
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            data.clear();
            dataStore.select(getStatementReader().getStatement("getAllTexts"), Text.class, new MappedResultVisitor<Text>() {
                @Override
                public void visit(Text text, DataStore dataStore, ResultSet resultSet) {
                    data.add(text);
                }
            });
        } catch (SQLException sqle) {
            //TODO: handle with log
            System.out.println("SQLException in fetchDataFromDataSource-> " + sqle.getMessage());
        } catch (IOException ioe) {
            //TODO: handle with log
            System.out.println("IOException in fetchDataFromDataSource-> " + ioe.getMessage());
        } finally {
            return data;
        }
    }

    @Override
    public void selectionFeaturePerformed() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String insertItem() throws FileNotFoundException, IOException, SQLException {
        //TODO: handle exceptions
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
}