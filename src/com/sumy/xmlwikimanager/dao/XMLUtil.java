package com.sumy.xmlwikimanager.dao;

import com.sumy.xmlwikimanager.bean.WikiItem;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sumy on 2015/11/27 0027.
 */
public class XMLUtil {

    public static Document readXML2Document(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(file);

        DOMReader reader = new DOMReader();
        // XPP XML Pull Parser不能解析长文本 避免使用SAXReader
        // SAXReader reader = new SAXReader();

        return reader.read(doc);
    }

    public static String getDatabaseType(Document document) {
        Element rootElement = document.getRootElement();
        Attribute attr = rootElement.attribute("type");
        if (attr != null) {
            String type = attr.getValue().toUpperCase();
            switch (type) {
                case "JAVA":
                    return XMLDatabase.TYPE_JAVA;
                case "JAVAWEB":
                    return XMLDatabase.TYPE_JAVAWEB;
                case "ANDROID":
                    return XMLDatabase.TYPE_ANDROID;
                default:
                    return XMLDatabase.TYPE_DEFAULT;
            }
        }
        return XMLDatabase.TYPE_DEFAULT;
    }

    public static String getDatabaseName(Document document) {
        Element rootElement = document.getRootElement();
        Attribute attr = rootElement.attribute("name");
        if (attr != null) return attr.getValue();
        return "";
    }

    public static List<WikiItem> readXML2List(File file) throws IOException, SAXException, ParserConfigurationException {
        Document document = readXML2Document(file);
        return readXML2List(document);
    }

    public static List<WikiItem> readXML2List(Document document) {
        List<WikiItem> list = new ArrayList<>();

        Element rootnode = document.getRootElement();
        List<Element> patternnodes = rootnode.elements("pattern");
        for (Element node : patternnodes) {
            WikiItem item = new WikiItem();
            Attribute attr = node.attribute("id");
            if (attr != null) {
                item.setItemid(attr.getValue());
            }
            item.setId(list.size());
            for (Element child : node.elements()) {
                String childName = child.getName();
                switch (childName) {
                    case "name":
                        item.setTitle(child.getText());
                        break;
                    case "grade":
                        item.setLevel(WikiItem.parseLevel(child.getText()));
                        break;
                    case "level":
                        item.setLevel(WikiItem.parseLevel(child.getText()));
                        break;
                    case "classify":
                        item.setClassify(child.getText());
                        break;
                    case "explanation":
                        item.setDescription(child.getText());
                        break;
                    case "demo":
                        break;
                    case "recommand":
                        item.setRecommand(child.getText());
                        break;
                    case "references":
                        item.setReferences(child.getText());
                        break;
                }
            }
            list.add(item);
        }
        return list;
    }

    public static boolean dump2XML(String filePath, List<WikiItem> list, String databaseType, String databaseName) {
        Document document = DocumentHelper.createDocument();
        Element databaseElement = document.addElement("database").addAttribute("name", databaseName).addAttribute("type", databaseType);
        for (WikiItem item : list) {
            Element itemElement = databaseElement.addElement("pattern");
            itemElement.addAttribute("id", item.getItemid());
            itemElement.addElement("name").addText(item.getTitle());
            itemElement.addElement("level").addText(item.getLevel() + "");
            itemElement.addElement("classify").addText(item.getClassify());
            itemElement.addElement("explanation").addCDATA(item.getDescription());
            itemElement.addElement("recommand").addCDATA(item.getRecommand());
            itemElement.addElement("references").addCDATA(item.getReferences());
        }
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        XMLWriter writer;
        try {
            writer = new XMLWriter(new FileWriter(filePath), outputFormat);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            readXML2List(new File("Category.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
