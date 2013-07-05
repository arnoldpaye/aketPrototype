package com.apt.aket.manager;

import com.apt.aket.model.Text;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.openlogics.cjb.jsf.controller.DefaultManager;

/**
 * Project: aketPrototype
 * Package: com.apt.aket.manager
 * Class  : TextManager.java (UTF-8)
 * @date 05/07/2013
 * @author Arnold Paye
 */

@ManagedBean
@ViewScoped
public class TextManager extends DefaultManager<Text> {

    @Override
    protected List<Text> fetchDataFromDataSource() throws Exception {
        data.clear();
        Text text1 = new Text();
        text1.setTxtId(1);
        text1.setTxtTitle("Seleccion automatica de palabras clave mediante el modelo TextRank");
        text1.setTxtAuthor("Arnold Paye");
        text1.setTxtText("XXXXXXXXXXXXX");
        data.add(text1);
        return data;
        
    }

    @Override
    public void selectionFeaturePerformed() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
