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

    private String server;
    private String server_cn;
    private String dbReplicaID;
    private String viewTI;
    private String viewObjectRef;
    private String viewLinkRef;
    private String viewKeyRef;
    private String viewFieldRef;
    private String viewRuleRef;

    public static AppletParams getInstance() {
        return ourInstance;
    }

    private AppletParams() {
    }

    public void getParams(JApplet applet){
        server = applet.getParameter("server");
        server_cn = applet.getParameter("server_cn");
        dbReplicaID = applet.getParameter("dbReplicaID");
        viewTI = applet.getParameter("viewTI");
        viewObjectRef = applet.getParameter("viewObjectRef");
        viewLinkRef = applet.getParameter("viewLinkRef");
        viewKeyRef = applet.getParameter("viewKeyRef");
        viewFieldRef = applet.getParameter("viewFieldRef");
        viewRuleRef = applet.getParameter("viewRuleRef");
    }

    public String getServer() {
        return server;
    }

    public String getServer_cn() {
        return server_cn;
    }

    public String getDbReplicaID() {
        return dbReplicaID;
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
}