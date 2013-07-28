package com.apt.aket.manager;

import com.apt.Util;
import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.KeyWordSelection;
import com.apt.aket.model.Text;
import com.apt.textrank.Graph;
import com.apt.textrank.MetricVector;
import com.apt.textrank.NGram;
import com.apt.textrank.TextRank;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.DSDescriptor;
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
    private boolean switchDisplayGraph;
    private UploadedFile txtFile;
    private UploadedFile pdfFile;
    private Graph graph;
    private List<KeyWordSelection> keyWordSelectionList = new ArrayList<KeyWordSelection>();
    private List<String> posFilterList;

    // Getters and setters******************************************************
    public boolean isSwitchDisplayGraph() {
        return switchDisplayGraph;
    }

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

    public Graph getGraph() {
        return graph;
    }

    public List<KeyWordSelection> getKeyWordSelectionList() {
        return keyWordSelectionList;
    }

    public void setKeyWordSelectionList(List<KeyWordSelection> keyWordSelectionList) {
        this.keyWordSelectionList = keyWordSelectionList;
    }

    public List<String> getPosFilterList() {
        return posFilterList;
    }

    public void setPosFilterList(List<String> posFilterList) {
        this.posFilterList = posFilterList;
    }

    //**************************************************************************
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
        if (text.getTxtTitle().trim().isEmpty() || text.getTxtAuthor().trim().isEmpty() || text.getTxtCode().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Codigo, Titulo y Autor son requeridos."));
            return "/";
        } else if (!text.getTxtKeywordsMac().trim().isEmpty() && !Util.validateKeywordsText(text.getTxtKeywordsMac().trim())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Las palabras clave deben estar separadas por punto y coma (;)."));
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
        if (selected.getTxtTitle().isEmpty() || selected.getTxtAuthor().isEmpty() || selected.getTxtCode().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Codigo, Titulo y Autor son requeridos."));
            return "/";
        } else if (!selected.getTxtText().isEmpty() && !Util.validateKeywordsText(selected.getTxtKeywordsMac().trim())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Las palabras clave deben estar separadas por punto y coma (;)."));
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
            text.setTxtCode("");
            text.setTxtAuthor("");
            text.setTxtText("");
        }
    }

    public void cleanEditTexForm() {
        Text text = selected;
        if (text != null) {
            text.setTxtTitle("");
            text.setTxtCode("");
            text.setTxtAuthor("");
            text.setTxtText("");
        }
    }

    public void buildGraph() {
        TextRank textRank = new TextRank();
        String path = System.getProperty("catalina.home") + "/resourcesNLP";
        try {
            if (posFilterList == null || posFilterList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Debe seleccionar las categorias gramaticales."));
            } else {
                if (selected != null) {
                    graph = textRank.buildGraph(selected.getTxtText(), path, posFilterList);
                    switchDisplayGraph = false;
                }
            }

        } catch (IOException ioe) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage()));
        }
    }

    public void selectSelected() {
        TextRank textRank = new TextRank();
        String path = System.getProperty("catalina.home") + "/resourcesNLP";
        try {
            if (selected != null) {
                if (graph != null) {
                    keyWordSelectionList.clear();
                    Graph copyGraph = graph; // The structure of graph change within the process
                    Map<NGram, MetricVector> metricSpace = textRank.init(copyGraph, path);
                    TreeSet<MetricVector> keyPraseList = new TreeSet<MetricVector>(metricSpace.values());
                    for (MetricVector metricVector : keyPraseList) {
                        if (metricVector.getMetric() >= TextRank.MIN_NORMALIZED_RANK) {
                            keyWordSelectionList.add(new KeyWordSelection(metricVector.getNodeValue().getText(), metricVector.getMetric()));
                        }
                    }
                    switchDisplayGraph = true;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Debe construir el grafo."));
                }

            }
        } catch (IOException ioe) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage()));
        }
    }

    public String cancelSelectSelected() {
        graph = null;
        keyWordSelectionList.clear();
        return "/index?faces-redirect=true";
    }
}