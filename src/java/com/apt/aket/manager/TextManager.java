package com.apt.aket.manager;

import com.apt.aket.model.Text;
import com.apt.bean.DataBean;
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
        DataBean dataBean = new DataBean();
        data = dataBean.getTexts();
        return data;
        
    }

    @Override
    public void selectionFeaturePerformed() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
