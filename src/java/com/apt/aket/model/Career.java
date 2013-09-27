package com.apt.aket.model;

import org.openlogics.cjb.jdbc.annotation.Column;

/**
 * @project aketPrototype
 * @package com.apt.aket.model
 * @class Career.java (UTF-8)
 * @date 26/09/2013
 * @author Arnold Paye
 */
public class Career {
    /* Members */

    @Column("cr_id")
    private int crId;
    @Column("cr_name")
    private String crName;

    /* Getters and Setters */
    public int getCrId() {
        return crId;
    }

    public void setCrId(int crId) {
        this.crId = crId;
    }

    public String getCrName() {
        return crName;
    }

    public void setCrName(String crName) {
        this.crName = crName;
    }
}
