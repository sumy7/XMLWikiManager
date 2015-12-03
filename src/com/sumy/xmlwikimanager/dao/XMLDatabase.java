package com.sumy.xmlwikimanager.dao;

import com.sumy.xmlwikimanager.bean.WikiItem;
import org.dom4j.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sumy on 2015/11/26 0026.
 */
public class XMLDatabase extends AbstractListModel<WikiItem> {
    public static XMLDatabase _instance = null;

    public static final String TYPE_DEFAULT = "Default";
    public static final String TYPE_JAVA = "Java";
    public static final String TYPE_JAVAWEB = "JavaWeb";
    public static final String TYPE_ANDROID = "Android";

    private boolean isOffline = true;
    private boolean isSaved = true;
    private int autoIncrease = 0;

    private String databaseType;
    private String databaseName;

    private String xmlPath;

    private List<WikiItem> list;

    private XMLDatabase() {
        list = new ArrayList<>();
    }

    public static XMLDatabase getInstance() {
        if (_instance == null) {
            _instance = new XMLDatabase();
        }
        return _instance;
    }

    public void insertItem(WikiItem item) {
        item.setId(autoIncrease);
        list.add(item);
        autoIncrease++;
        int index = list.size();
        fireIntervalAdded(this, index, index);
        isSaved = false;
    }

    public Collection<WikiItem> getItemList() {
        return list;
    }

    public int delete(int id) {
        int cnt = 0;
        for (Iterator<WikiItem> iter = list.iterator(); iter.hasNext(); ) {
            WikiItem item = iter.next();
            if (item.getId() == id) {
                iter.remove();
                cnt++;
            }
        }
        isSaved = false;
        return cnt;
    }

    public boolean delete(WikiItem item) {
        int index = list.indexOf(item);
        boolean rv = list.remove(item);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        isSaved = false;
        return rv;
    }

    public boolean commit() {
        if (isOffline)
            return false;
        boolean result = XMLUtil.dump2XML(xmlPath, list, databaseType, databaseName);
        isSaved = true;
        return result;
    }

    public boolean open(String databasePath) {
        xmlPath = databasePath;
        isOffline = false;
        File file = new File(databasePath);
        if (!file.exists())
            return false;
        try {
            Document document = XMLUtil.readXML2Document(file);
            list = XMLUtil.readXML2List(document);
            databaseName = XMLUtil.getDatabaseName(document);
            databaseType = XMLUtil.getDatabaseType(document);
            autoIncrease = list.size() + 1;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void news(String databasePath) {
        xmlPath = databasePath;
        isOffline = false;
        list = new ArrayList<>();
        databaseType = TYPE_DEFAULT;
        databaseName = "NEW_DATABASE";
        autoIncrease = 1;
        commit();
    }

    public String getDatabaseType() {
        if (databaseType == null) {
            return TYPE_DEFAULT;
        }
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseName() {
        if (databaseName == null) {
            return "";
        }
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public WikiItem getElementAt(int index) {
        return list.get(index);
    }

    public boolean isOffline() {
        return isOffline;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public String getXMLPath() {
        return xmlPath;
    }
}
