package com.apt.aket.manager;

import com.apt.aket.util.NodeJson;
import com.apt.aket.data.CommonResultHandler;
import com.apt.aket.data.DataStoreManager;
import com.apt.aket.data.KeywordSelection;
import com.apt.aket.model.Evaluation;
import com.apt.aket.model.Keyword;
import com.apt.aket.model.Text;
import com.apt.aket.util.Util;
import com.apt.textrank.Edge;
import com.apt.textrank.Graph;
import com.apt.textrank.Node;
import com.apt.textrank.TextRank;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.context.RequestContext;
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

    /* Members */
    static Logger log = Logger.getLogger(TextManager.class);
    private int careerId;
    private boolean switchDisplayGraph;
    private UploadedFile txtFile;
    private UploadedFile pdfFile;
    private TextRank textrank;
    private Graph graph;
    private List<KeywordSelection> keywordSelectionList = new ArrayList<KeywordSelection>();
    private Map<String, List<String>> keywordShow;
    private List<String> posFilterList;
    private List<Evaluation> evaluationList = new ArrayList<Evaluation>();

    /* Getters and Setters */
    public int getCareerId() {
        return careerId;
    }

    public void setCareerId(int careerId) {
        this.careerId = careerId;
    }

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

    public List<KeywordSelection> getKeywordSelectionList() {
        return keywordSelectionList;
    }

    public Map<String, List<String>> getKeywordShow() {
        return keywordShow;
    }

    public List<String> getPosFilterList() {
        return posFilterList;
    }

    public void setPosFilterList(List<String> posFilterList) {
        this.posFilterList = posFilterList;
    }

    public List<Evaluation> getEvaluationList() {
        return evaluationList;
    }

    /**
     *
     * @return
     */
    @Override
    protected List<Text> fetchDataFromDataSource() {
        log.debug("Enter fetchDataFromDataSource method");
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            data.clear();
            log.info("DataStore select" + getStatementReader().getStatement("getAllTexts"));
            if (careerId == 0) {
                dataStore.select(getStatementReader().getStatement("getAllTexts"), Text.class, new MappedResultVisitor<Text>() {
                    @Override
                    public void visit(Text text, DataStore dataStore, ResultSet resultSet) throws SQLException {
                        KeywordManager keywordManager = new KeywordManager();
                        List<Keyword> keywords = keywordManager.getKeywords(dataStore, text);
                        for (Keyword keyword : keywords) {
                            if (keyword.getKwSource() == 1) {
                                text.setKeyword(keyword);
                            }
                        }
                        data.add(text);
                    }
                });
            } else {
                dataStore.select(getStatementReader().getStatement("getTextsByCareer"), careerId, Text.class, new MappedResultVisitor<Text>() {
                    @Override
                    public void visit(Text text, DataStore dataStore, ResultSet resultSet) throws SQLException {
                        KeywordManager keywordManager = new KeywordManager();
                        List<Keyword> keywords = keywordManager.getKeywords(dataStore, text);
                        for (Keyword keyword : keywords) {
                            if (keyword.getKwSource() == 1) {
                                text.setKeyword(keyword);
                            }
                        }
                        data.add(text);
                    }
                });
            }

        } catch (SQLException sqle) {
            log.error("SQLException in fetchDataFromDataSource method->" + sqle.getMessage());
        } catch (IOException ioe) {
            log.error("IOException in fetchDataFromDataSource method->" + ioe.getMessage());
        } finally {
//            posFilterList.add("AQ");
//            posFilterList.add("NC");
            return data;
        }
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void selectionFeaturePerformed() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Insert text item into database.
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public String insertItem() throws FileNotFoundException, IOException, SQLException {
        log.debug("Enter insertItem method");
        Text text = JEEContext.getRequestScopedBean(Text.class);
        if (text.getTxtTitle().trim().isEmpty() || text.getTxtAuthor().trim().isEmpty() || text.getTxtCode().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Codigo, Titulo y Autor son requeridos."));
            return "/";
        } else {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.setAutoCommit(false);
            try {
                int txtId = dataStore.select(getStatementReader().getStatement("insertText"), text, new CommonResultHandler.IntegerHandler());
                // Insert keywords
                KeywordManager keywordManager = new KeywordManager();
                Keyword keyword = new Keyword(txtId, text.getKeyword().getKwValue(), 1); // 1-> KOHA SYSTEM
                keywordManager.insertItem(dataStore, keyword);
                dataStore.commit();
                return "/index?faces-redirect=true";
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, sqle.getMessage()));
                dataStore.rollBack();
                return "/";
            }
        }
    }

    /**
     * Edit selected text.
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public String editSelected() throws FileNotFoundException, IOException, SQLException {
        if (selected.getTxtTitle().isEmpty() || selected.getTxtAuthor().isEmpty() || selected.getTxtCode().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Codigo, Titulo y Autor son requeridos."));
            return "/";
        } else {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.setAutoCommit(false);
            try {
                dataStore.execute(getStatementReader().getStatement("editText"), selected);
                //TODO: keywords list
                KeywordManager keywordManager = new KeywordManager();
                Keyword keyword = selected.getKeyword();
                keywordManager.updateItem(dataStore, keyword);
                dataStore.commit();
                return "/index?faces-redirect=true";
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, sqle.getMessage()));
                dataStore.rollBack();
                return "/";
            }
        }
    }

    /**
     * Delete selected text.
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public void deleteSelected() throws FileNotFoundException, IOException, SQLException {
        DataStore dataStore = DataStoreManager.getDataStore();
        dataStore.setAutoCommit(false);
        try {
            // Delete evaluations
            dataStore.select(new StatementReader("sql/keyword.xml").getStatement("getKeywords"), selected, Keyword.class, new MappedResultVisitor<Keyword>() {
                @Override
                public void visit(Keyword keyword, DataStore ds, ResultSet rs) throws SQLException {
                    ds.execute(new StatementReader("sql/evaluation.xml").getStatement("deleteEvaluation"), keyword);

                }
            });
            // Delete keywords
            dataStore.execute(new StatementReader("sql/keyword.xml").getStatement("deleteKeyword"), selected);
            // Delete text
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

    /**
     * Load data from a text file.
     *
     * @throws IOException
     */
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

    /**
     * Load data from a text file in edit mode.
     *
     * @throws IOException
     */
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

    /**
     * Clear form.
     */
    public void cleanNewTexForm() {
        Text text = JEEContext.getRequestScopedBean(Text.class);
        if (text != null) {
            text.setTxtTitle("");
            text.setTxtCode("");
            text.setTxtAuthor("");
            text.setTxtKeyword("");
            text.setTxtText("");
        }
    }

    /**
     * Clear form in edit mode.
     */
    public void cleanEditTexForm() {
        Text text = selected;
        if (text != null) {
            text.setTxtTitle("");
            text.setTxtCode("");
            text.setTxtAuthor("");
            text.setTxtKeyword("");
            text.setTxtText("");
        }
    }

    /**
     * Build a graph from the selected text.
     */
    public void buildGraph() {
        try {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            posFilterList = new ArrayList<String>();
            posFilterList.add("NC");
            posFilterList.add("AQ");
            if (posFilterList == null || posFilterList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Debe seleccionar las categorias gramaticales."));
            } else {
                if (selected != null) {
                    textrank = new TextRank(System.getProperty("catalina.home") + "/resourcesNLP");
                    graph = textrank.buildGraph(selected.getTxtText());
                    List<NodeJson> graphNodes = new ArrayList<NodeJson>();
                    int i = 0;
                    for (Node node : graph.values()) {
                        NodeJson nodeJson = new NodeJson(node.getKey(), node.getKey());

                        for (Edge edge : node.getEdges()) {
                            nodeJson.getNodeList().add(new NodeJson(edge.getNode().getKey(), edge.getNode().getKey()));
                        }
                        graphNodes.add(nodeJson);
                        if (++i == 10) {
                            break;
                        }
                    }
                    // Create json data
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonData = gson.toJson(graphNodes);
                    System.out.println("jsonData " + jsonData);
                    requestContext.addCallbackParam("graphData", jsonData);

                    switchDisplayGraph = false;

                }
            }

        } catch (IOException ioe) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
            System.err.println("IOException: " + ioe.getMessage());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage()));
        }
    }

    /**
     * After build graph, start the textrank algorithm.
     */
    public void selectSelected() {
        try {
            if (selected != null) {
                if (graph != null) {
                    keywordSelectionList = new ArrayList<KeywordSelection>();
                    textrank.runTextRank(graph);
                    List<com.apt.textrank.Keyword> keywordList = textrank.getKeywords(graph);
                    for (com.apt.textrank.Keyword keyword : keywordList) {
                        keywordSelectionList.add(new KeywordSelection(keyword.getValue(), keyword.getRank()));
                    }

//                    for (com.apt.textrank.Keyword keyword : keywordList) {
//                        keywordSelectionList.add(new KeywordSelection(keyword.getText(), keyword.getRank()));
//                    }
                    switchDisplayGraph = true;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Debe construir el grafo."));
                }

            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage()));
        }
    }

    /**
     * Save keyword selection set into database.
     *
     * @return
     */
    public String saveSelectSelected() {
        if (keywordSelectionList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Debe realizar la seleccion de palabras clave."));
            return "/";
        } else {
            try {
                String keyWordsText = "";
                for (KeywordSelection keywordSelection : keywordSelectionList) {
                    keyWordsText += keywordSelection.getValue().trim().toUpperCase() + ";";
                }
                DataStore dataStore = DataStoreManager.getDataStore();
                dataStore.setAutoCommit(false);
                KeywordManager keywordManager = new KeywordManager();
                EvaluationManager evaluationManager = new EvaluationManager();
                Keyword keyword = new Keyword();
                keyword.setKwTxtId(selected.getTxtId());
                keyword.setKwSource(0);
                keyword = keywordManager.getKeywordBySource(dataStore, keyword);
                if (keyword == null) {
                    keyword = new Keyword(selected.getTxtId(), keyWordsText, 0); // 0 -> TEXTRANK
                    keywordManager.insertItem(dataStore, keyword);
                } else {
                    evaluationManager.deleteEvaluation(keyword);
                    keywordManager.updateItem(dataStore, keyword);

                }
                dataStore.commit();
                graph = null;
                keywordSelectionList.clear();
                return "/index?faces-redirect=true";
            } catch (IOException ioe) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
                return "/";
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, sqle.getMessage()));
                return "/";
            }
        }
    }

    /**
     * Cancel keyword selection.
     *
     * @return
     */
    public String cancelSelectSelected() {
        graph = null;
        keywordSelectionList.clear();
        return "/index?faces-redirect=true";
    }

    /**
     * Get evaluation values (precision, recall, f-measure).
     */
    public void evaluation() throws FileNotFoundException, SQLException, IOException {
        if (selected != null) {
            evaluationList.clear();
            String[] k = selected.getTxtKeyword().split(";");
            KeywordManager keywordManager = new KeywordManager();
            DataStore dataStore = DataStoreManager.getDataStore();
            List<Keyword> keywordList = keywordManager.getKeywords(dataStore, selected);
            for (Keyword keyword : keywordList) {
                double e[] = Util.evaluate(k, keyword.getKwValue().split(";"));
                Evaluation evaluation = new Evaluation(keyword.getKwId(), e[0], e[1], e[2]);
                // TODO: Don't hardcode
                if (keyword.getKwSource() == 1) {
                    evaluation.setSource("KOHA");
                } else if (keyword.getKwSource() == 0) {
                    evaluation.setSource("TEXTRANK");
                }
                evaluationList.add(evaluation);
            }
        }
    }

    /**
     * Cancel evaluation.
     */
    public void cancelEvaluation() {
        evaluationList.clear();
    }

    /**
     * Save evaluation into database.
     */
    public void saveEvaluation() {
        if (selected != null) {
            try {
                EvaluationManager evaluationManager = new EvaluationManager();
                for (Evaluation evaluation : evaluationList) {
                    evaluationManager.insertItem(evaluation);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Evaluacion guardada."));
                evaluationList.clear();
            } catch (IOException ioe) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, sqle.getMessage()));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage()));
            }
        }
    }

    /**
     * Load the keyword list from database.
     */
    public void loadKeywords() {
        if (selected != null) {
            try {
                keywordShow = new HashMap<String, List<String>>();
                List<String> l = new ArrayList<String>();
                // Original
                l.addAll(Arrays.asList(selected.getTxtKeyword().split(";")));
                keywordShow.put("ORIGINAL", l);
                // Others
                KeywordManager keywordManager = new KeywordManager();
                DataStore dataStore = DataStoreManager.getDataStore();
                List<Keyword> keywords = keywordManager.getKeywords(dataStore, selected);
                for (Keyword k : keywords) {
                    l = new ArrayList<String>();
                    l.addAll(Arrays.asList(k.getKwValue().split(";")));
                    if (k.getKwSource() == 1) {
                        keywordShow.put("KOHA", l);
                    } else if (k.getKwSource() == 0) {
                        keywordShow.put("TEXTRANK", l);
                    }
                }
            } catch (IOException ioe) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
            } catch (SQLException sqle) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, sqle.getMessage()));

            }
        }
        System.out.println("DBG " + keywordShow.size());
    }
}