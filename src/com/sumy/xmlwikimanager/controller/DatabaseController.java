package com.sumy.xmlwikimanager.controller;

import com.sumy.xmlwikimanager.bean.WikiItem;
import com.sumy.xmlwikimanager.dao.XMLDatabase;
import org.xml.sax.SAXException;

import java.util.Collection;

/**
 * Created by Sumy on 2015/11/26 0026.
 */
public class DatabaseController {
    public static WikiItem findItemByID(int id) {
        XMLDatabase database = XMLDatabase.getInstance();
        for (WikiItem item : database.getItemList()) {
            if (id == item.getId()) {
                return item;
            }
        }
        return null;
    }

    public static void delete(WikiItem item) {
        XMLDatabase database = XMLDatabase.getInstance();
        database.delete(item);
    }

    public static void deleteByID(int id) {
        XMLDatabase database = XMLDatabase.getInstance();
        for (WikiItem item : database.getItemList()) {
            if (id == item.getId()) {
                database.delete(item);
            }
        }
    }

    public static void addItem(WikiItem item) {
        XMLDatabase database = XMLDatabase.getInstance();
        database.insertItem(item);
    }

    public static Collection<WikiItem> findAll() {
        XMLDatabase database = XMLDatabase.getInstance();
        return database.getItemList();
    }

    public static String getDatabaseName() {
        XMLDatabase database = XMLDatabase.getInstance();
        return database.getDatabaseName();
    }

    public static void setDatabaseName(String name) {
        XMLDatabase database = XMLDatabase.getInstance();
        database.setDatabaseName(name);
    }

    public static String getDatabaseType() {
        XMLDatabase database = XMLDatabase.getInstance();
        return database.getDatabaseType();
    }

    public static void setDatabaseType(String type) {
        XMLDatabase database = XMLDatabase.getInstance();
        database.setDatabaseType(type);
    }

    public static void saveDatabase() {
        XMLDatabase database = XMLDatabase.getInstance();
        database.commit();
    }

    public static void createDatabase(String databasePath) {
        XMLDatabase database = XMLDatabase.getInstance();
        database.news(databasePath);
    }

    public static void openDatabase(String databasePath) throws SAXException{
        XMLDatabase database = XMLDatabase.getInstance();
        database.open(databasePath);
    }

    public static boolean isOffline() {
        return XMLDatabase.getInstance().isOffline();
    }

    public static String getDatabasePath() {
        XMLDatabase database = XMLDatabase.getInstance();
        if (database.isOffline()) {
            return "离线";
        }
        return database.getXMLPath();
    }

    public static boolean isDatabaseSaved() {
        return XMLDatabase.getInstance().isSaved();
    }
}
