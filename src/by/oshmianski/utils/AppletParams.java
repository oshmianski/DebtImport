package by.oshmianski.utils;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 01.04.13
 * Time: 16:00
 */
public class AppletParams {
    private static AppletParams ourInstance = new AppletParams();

    private String server = "";
    private String dbReplicaID;
    private String dbGEO;
    private String viewTI;
    private String viewObjectRef;
    private String viewLinkRef;
    private String viewKeyRef;
    private String viewFieldRef;
    private String viewRuleRef;
    private String viewGEOCity;

    public static AppletParams getInstance() {
        return ourInstance;
    }

    private AppletParams() {
    }

    public void getParams(JApplet applet){
        server = applet.getParameter("server");
        dbReplicaID = applet.getParameter("dbReplicaID");
        viewTI = applet.getParameter("viewTI");
        viewObjectRef = applet.getParameter("viewObjectRef");
        viewLinkRef = applet.getParameter("viewLinkRef");
        viewKeyRef = applet.getParameter("viewKeyRef");
        viewFieldRef = applet.getParameter("viewFieldRef");
        viewRuleRef = applet.getParameter("viewRuleRef");
        dbGEO = applet.getParameter("dbGEO");
        viewGEOCity = applet.getParameter("viewGEOCity");
    }

    public String getDbReplicaID() {
        return dbReplicaID;
    }

    public void setDbReplicaID(String dbReplicaID) {
        this.dbReplicaID = dbReplicaID;
    }

    public String getViewTI() {
        return viewTI;
    }

    public String getViewObjectRef() {
        return viewObjectRef;
    }

    public String getViewLinkRef() {
        return viewLinkRef;
    }

    public String getViewKeyRef() {
        return viewKeyRef;
    }

    public String getViewFieldRef() {
        return viewFieldRef;
    }

    public String getViewRuleRef() {
        return viewRuleRef;
    }

    public void setViewTI(String viewTI) {
        this.viewTI = viewTI;
    }

    public void setViewObjectRef(String viewObjectRef) {
        this.viewObjectRef = viewObjectRef;
    }

    public void setViewLinkRef(String viewLinkRef) {
        this.viewLinkRef = viewLinkRef;
    }

    public void setViewKeyRef(String viewKeyRef) {
        this.viewKeyRef = viewKeyRef;
    }

    public void setViewFieldRef(String viewFieldRef) {
        this.viewFieldRef = viewFieldRef;
    }

    public void setViewRuleRef(String viewRuleRef) {
        this.viewRuleRef = viewRuleRef;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDbGEO() {
        return dbGEO;
    }

    public void setDbGEO(String dbGEO) {
        this.dbGEO = dbGEO;
    }

    public String getViewGEOCity() {
        return viewGEOCity;
    }

    public void setViewGEOCity(String viewGEOCity) {
        this.viewGEOCity = viewGEOCity;
    }
}