package com.sumy.xmlwikimanager.view;

import com.sumy.xmlwikimanager.bean.WikiItem;
import com.sumy.xmlwikimanager.controller.DatabaseController;
import com.sumy.xmlwikimanager.dao.XMLDatabase;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sumy on 2015/11/26 0026.
 */
public class MainForm {
    public static String[] DATABASETYPE = {"JAVA", "JAVAWEB", "ANDROID", "DEFAULT"};
    public static String[] LEVELTYPE = {"LOW", "MIDDLE", "HIGH", "UNKNOWN"};

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JPanel mainPanel;
    private JButton btn_loadDatabase;
    private JButton btn_saveDatabase;
    private JTextField text_ID;
    private JTextField text_Title;
    private JButton btn_delItem;
    private JButton btn_addItem;
    private JList list_Database;
    private JButton btn_formatteItem;
    private JComboBox cbo_Level;
    private JEditorPane editor_description;
    private JEditorPane editor_recommand;
    private JEditorPane editor_reference;
    private JLabel lab_stateBar;
    private JButton btn_newDatabase;
    private JComboBox cbo_databaseType;
    private JLabel label_review;
    private JEditorPane editor_review;
    private JLabel label_itemSize;
    private JTextField text_classify;
    private JTextField text_databaseName;
    private JSplitPane splitPane;

    private JFrame outerFrame;
    private JFileChooser fileChooser;

    private WikiItem currentWikiItem;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        outerFrame.setTitle(title + " - " + DatabaseController.getDatabasePath());
    }

    public void refreshTitle() {
        outerFrame.setTitle(title + " - " + DatabaseController.getDatabasePath());
    }

    public MainForm(JFrame outerFrame) {
        this.outerFrame = outerFrame;

        init();
        configKeyStroke();
        initReviewPanel();
        initListener();
        disableAll();
        refreshleft();
        btn_saveDatabase.addActionListener(e -> {
            DatabaseController.saveDatabase();
            showStateBar("已保存。");
        });
        btn_newDatabase.addActionListener(e -> {
            fileChooser.setDialogTitle("选择数据库保存位置");
            int flag = fileChooser.showSaveDialog(null);
            if (flag == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                MyFileFilter filter = (MyFileFilter) fileChooser.getFileFilter();
                String ends = filter.getEnds();
                String filepath = file.getAbsolutePath();
                if (!filepath.toUpperCase().endsWith(ends.toUpperCase())) {
                    filepath = filepath + ends;
                }
                DatabaseController.createDatabase(filepath);
                list_Database.setModel(XMLDatabase.getInstance());
                text_databaseName.setText(DatabaseController.getDatabaseName());
                label_itemSize.setText(list_Database.getModel().getSize() + "条");
                cbo_databaseType.setSelectedItem(DATABASETYPE[3]);
                currentWikiItem = new WikiItem();
                disableAll();
                refresh();
                refreshleft();
                refreshTitle();
            }
        });
        btn_loadDatabase.addActionListener(e -> {
            try {
                fileChooser.setDialogTitle("加载数据库");
                int flag = fileChooser.showOpenDialog(null);
                if (flag == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    DatabaseController.openDatabase(file.getAbsolutePath());
                    text_databaseName.setText(DatabaseController.getDatabaseName());
                    label_itemSize.setText(list_Database.getModel().getSize() + "条");
                    switch (DatabaseController.getDatabaseType()) {
                        case XMLDatabase.TYPE_JAVA:
                            cbo_databaseType.setSelectedItem(DATABASETYPE[0]);
                            break;
                        case XMLDatabase.TYPE_JAVAWEB:
                            cbo_databaseType.setSelectedItem(DATABASETYPE[1]);
                            break;
                        case XMLDatabase.TYPE_ANDROID:
                            cbo_databaseType.setSelectedItem(DATABASETYPE[2]);
                            break;
                        default:
                            cbo_databaseType.setSelectedItem(DATABASETYPE[3]);
                            break;
                    }
                    currentWikiItem = new WikiItem();
                    disableAll();
                    refresh();
                    refreshleft();
                    refreshTitle();
                    list_Database.setModel(XMLDatabase.getInstance());
                    list_Database.updateUI();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "加载数据库失败\n" + ex.toString(), "加载数据库失败", JOptionPane.ERROR_MESSAGE);
            }
        });
        cbo_databaseType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String select = (String) cbo_databaseType.getSelectedItem();
                if (select.equals(DATABASETYPE[0])) {
                    DatabaseController.setDatabaseType(XMLDatabase.TYPE_JAVA);
                } else if (select.equals(DATABASETYPE[1])) {
                    DatabaseController.setDatabaseType(XMLDatabase.TYPE_JAVAWEB);
                } else if (select.equals(DATABASETYPE[2])) {
                    DatabaseController.setDatabaseType(XMLDatabase.TYPE_ANDROID);
                } else if (select.equals(DATABASETYPE[3])) {
                    DatabaseController.setDatabaseType(XMLDatabase.TYPE_DEFAULT);
                }
            }
        });
    }

    public void refreshleft() {
        if (DatabaseController.isOffline()) {
            btn_saveDatabase.setEnabled(false);
            btn_addItem.setEnabled(false);
            btn_delItem.setEnabled(false);
            text_databaseName.setEnabled(false);
            cbo_databaseType.setEnabled(false);
            list_Database.setEnabled(false);
        } else {
            btn_saveDatabase.setEnabled(true);
            btn_addItem.setEnabled(true);
            btn_delItem.setEnabled(true);
            text_databaseName.setEnabled(true);
            cbo_databaseType.setEnabled(true);
            list_Database.setEnabled(true);
        }
    }

    public void disableAll() {
        text_ID.setEnabled(false);
        text_Title.setEnabled(false);
        text_classify.setEnabled(false);
        editor_description.setEnabled(false);
        editor_recommand.setEnabled(false);
        editor_reference.setEnabled(false);
        cbo_Level.setEnabled(false);
    }

    public void enableAll() {
        text_ID.setEnabled(true);
        text_Title.setEnabled(true);
        text_classify.setEnabled(true);
        editor_description.setEnabled(true);
        editor_recommand.setEnabled(true);
        editor_reference.setEnabled(true);
        cbo_Level.setEnabled(true);
    }

    private void initListener() {
        editor_description.getDocument().addDocumentListener(new DocumentChangeListener(editor_description));
        editor_recommand.getDocument().addDocumentListener(new DocumentChangeListener(editor_recommand));
        editor_reference.getDocument().addDocumentListener(new DocumentChangeListener(editor_reference));
        text_ID.getDocument().addDocumentListener(new DocumentChangeListener(text_ID));
        text_Title.getDocument().addDocumentListener(new DocumentChangeListener(text_Title));
        text_classify.getDocument().addDocumentListener(new DocumentChangeListener(text_classify));
        text_databaseName.getDocument().addDocumentListener(new DocumentChangeListener(text_databaseName));

        btn_formatteItem.addActionListener(e -> {
            String origin = text_ID.getText();
            String newString = origin.replace(" ", "_").toUpperCase()
                    .replace(":", "").replace("()", "").replace("-", "_")
                    .replace(".", "_");
            text_ID.setText(newString);
        });

        cbo_Level.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (cbo_Level.getSelectedItem().equals(LEVELTYPE[0])) {
                    currentWikiItem.setLevel(WikiItem.LEVEL_LOW);
                } else if (cbo_Level.getSelectedItem().equals(LEVELTYPE[1])) {
                    currentWikiItem.setLevel(WikiItem.LEVEL_MIDDLE);
                } else if (cbo_Level.getSelectedItem().equals(LEVELTYPE[2])) {
                    currentWikiItem.setLevel(WikiItem.LEVEL_HIGH);
                } else {
                    currentWikiItem.setLevel(WikiItem.LEVEL_UNKNOWN);
                }
            }
        });

        btn_addItem.addActionListener(e -> {
            WikiItem item = new WikiItem();
            DatabaseController.addItem(item);
            currentWikiItem = item;

            list_Database.setSelectedValue(item, true);
            int size = list_Database.getModel().getSize();
            label_itemSize.setText(size + "条");
            enableAll();
            refresh();
            refreshReviewPanel();
        });
        btn_delItem.addActionListener(e -> {
            if (list_Database.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, "没有选中任何条目。");
            } else {
                int response = JOptionPane.showConfirmDialog(null, "确定要删除该条目？", "删除确认", JOptionPane.YES_NO_OPTION);
                if (response == 0) {
                    int index = list_Database.getSelectedIndex();
                    DatabaseController.delete(currentWikiItem);
                    int size = list_Database.getModel().getSize();
                    if (size == 0) {
                        currentWikiItem = new WikiItem();
                        disableAll();
                    } else if (index >= size) {
                        currentWikiItem = (WikiItem) list_Database.getModel().getElementAt(size - 1);
                    } else {
                        currentWikiItem = (WikiItem) list_Database.getModel().getElementAt(index);
                    }
                    list_Database.setSelectedValue(currentWikiItem, true);
                    label_itemSize.setText(size + "条");
                    refresh();
                    refreshReviewPanel();

                }
            }
        });
    }

    private void init() {
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        MyFileFilter filter = new MyFileFilter(".xml", "数据库文件 (*.xml)");
        fileChooser.addChoosableFileFilter(filter);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(10);
        splitPane.setDividerLocation(-100);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String item : DATABASETYPE) {
            model.addElement(item);
        }
        cbo_databaseType.setModel(model);
        model = new DefaultComboBoxModel<>();
        for (String item : LEVELTYPE) {
            model.addElement(item);
        }
        cbo_Level.setModel(model);

        list_Database.setModel(XMLDatabase.getInstance());
        list_Database.setCellRenderer(new WikiItemListCellRender());
        list_Database.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                if (list_Database.getSelectedIndex() != -1) {

                    currentWikiItem = (WikiItem) list_Database.getSelectedValue();
                    refresh();
                    refreshReviewPanel();
                    enableAll();
                }
            }
        });
    }

    private void configKeyStroke() {
        KeyStroke likeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        KeyStroke olkeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        KeyStroke ulkeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        KeyStroke pkeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        KeyStroke prekeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        KeyStroke bkeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        KeyStroke ikeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        KeyStroke brkeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);

        editor_description.getInputMap().put(likeystroke, "likeystroke");
        editor_description.getActionMap().put("likeystroke", KeyStrokeActionFactory.getAction("li", editor_description));
        editor_description.getInputMap().put(ulkeystroke, "ulkeystroke");
        editor_description.getActionMap().put("ulkeystroke", KeyStrokeActionFactory.getAction("ul", editor_description));
        editor_description.getInputMap().put(olkeystroke, "olkeystroke");
        editor_description.getActionMap().put("olkeystroke", KeyStrokeActionFactory.getAction("ol", editor_description));
        editor_description.getInputMap().put(pkeystroke, "pkeystroke");
        editor_description.getActionMap().put("pkeystroke", KeyStrokeActionFactory.getAction("p", editor_description));
        editor_description.getInputMap().put(prekeystroke, "prekeystroke");
        editor_description.getActionMap().put("prekeystroke", KeyStrokeActionFactory.getAction("pre", editor_description));
        editor_description.getInputMap().put(ikeystroke, "ikeystroke");
        editor_description.getActionMap().put("ikeystroke", KeyStrokeActionFactory.getAction("i", editor_description));
        editor_description.getInputMap().put(bkeystroke, "bkeystroke");
        editor_description.getActionMap().put("bkeystroke", KeyStrokeActionFactory.getAction("b", editor_description));
        editor_description.getInputMap().put(brkeystroke, "brkeystroke");
        editor_description.getActionMap().put("brkeystroke", KeyStrokeActionFactory.getAction("br", editor_description));

        editor_recommand.getInputMap().put(likeystroke, "likeystroke");
        editor_recommand.getActionMap().put("likeystroke", KeyStrokeActionFactory.getAction("li", editor_recommand));
        editor_recommand.getInputMap().put(ulkeystroke, "ulkeystroke");
        editor_recommand.getActionMap().put("ulkeystroke", KeyStrokeActionFactory.getAction("ul", editor_recommand));
        editor_recommand.getInputMap().put(olkeystroke, "olkeystroke");
        editor_recommand.getActionMap().put("olkeystroke", KeyStrokeActionFactory.getAction("ol", editor_recommand));
        editor_recommand.getInputMap().put(pkeystroke, "pkeystroke");
        editor_recommand.getActionMap().put("pkeystroke", KeyStrokeActionFactory.getAction("p", editor_recommand));
        editor_recommand.getInputMap().put(prekeystroke, "prekeystroke");
        editor_recommand.getActionMap().put("prekeystroke", KeyStrokeActionFactory.getAction("pre", editor_recommand));
        editor_recommand.getInputMap().put(ikeystroke, "ikeystroke");
        editor_recommand.getActionMap().put("ikeystroke", KeyStrokeActionFactory.getAction("i", editor_recommand));
        editor_recommand.getInputMap().put(bkeystroke, "bkeystroke");
        editor_recommand.getActionMap().put("bkeystroke", KeyStrokeActionFactory.getAction("b", editor_recommand));
        editor_recommand.getInputMap().put(brkeystroke, "brkeystroke");
        editor_recommand.getActionMap().put("brkeystroke", KeyStrokeActionFactory.getAction("br", editor_recommand));

        editor_reference.getInputMap().put(likeystroke, "likeystroke");
        editor_reference.getActionMap().put("likeystroke", KeyStrokeActionFactory.getAction("li", editor_reference));
        editor_reference.getInputMap().put(ulkeystroke, "ulkeystroke");
        editor_reference.getActionMap().put("ulkeystroke", KeyStrokeActionFactory.getAction("ul", editor_reference));
        editor_reference.getInputMap().put(olkeystroke, "olkeystroke");
        editor_reference.getActionMap().put("olkeystroke", KeyStrokeActionFactory.getAction("ol", editor_reference));
        editor_reference.getInputMap().put(pkeystroke, "pkeystroke");
        editor_reference.getActionMap().put("pkeystroke", KeyStrokeActionFactory.getAction("p", editor_reference));
        editor_reference.getInputMap().put(prekeystroke, "prekeystroke");
        editor_reference.getActionMap().put("prekeystroke", KeyStrokeActionFactory.getAction("pre", editor_reference));
        editor_reference.getInputMap().put(ikeystroke, "ikeystroke");
        editor_reference.getActionMap().put("ikeystroke", KeyStrokeActionFactory.getAction("i", editor_reference));
        editor_reference.getInputMap().put(bkeystroke, "bkeystroke");
        editor_reference.getActionMap().put("bkeystroke", KeyStrokeActionFactory.getAction("b", editor_reference));
        editor_reference.getInputMap().put(brkeystroke, "brkeystroke");
        editor_reference.getActionMap().put("brkeystroke", KeyStrokeActionFactory.getAction("br", editor_reference));

    }

    private void initReviewPanel() {
        editor_review.setContentType("text/html");
        editor_review.setEditable(false);
        HTMLEditorKit kit = new HTMLEditorKit();
        editor_review.setEditorKit(kit);
        Document doc = kit.createDefaultDocument();
        editor_review.setDocument(doc);

    }

    private void saveTextIntoCurrentWiki(JTextComponent component) {
        if (component.equals(text_ID)) {
            currentWikiItem.setItemid(text_ID.getText());
        } else if (component.equals(text_Title)) {
            currentWikiItem.setTitle(text_Title.getText());
        } else if (component.equals(text_classify)) {
            currentWikiItem.setClassify(text_classify.getText());
        } else if (component.equals(editor_description)) {
            currentWikiItem.setDescription(editor_description.getText());
        } else if (component.equals(editor_recommand)) {
            currentWikiItem.setRecommand(editor_recommand.getText());
        } else if (component.equals(editor_reference)) {
            currentWikiItem.setReferences(editor_reference.getText());
        } else if (component.equals(text_databaseName)) {
            DatabaseController.setDatabaseName(text_databaseName.getText());
        } else {
            System.out.println("unknown text component: " + component);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void showStateBar(String text) {
        lab_stateBar.setText(df.format(new Date()) + text);
    }

    private String getReviewHtmlText(WikiItem item) {
        return "<html>" +
                "<h1>" + item.getTitle() + "</h1>" +
                "<h2>" + item.getItemid() + "</h2>" +
                "<h1>说明</h1>" +
                "<div>" + item.getDescription() + "</div>" +
                "<h1>建议</h1>" +
                "<div>" + item.getRecommand() + "</div>" +
                "<h1>参考文献</h1>" +
                "<div>" + item.getReferences() + "</div>" +
                "</html>";
    }

    private void refresh() {
        text_ID.setText(currentWikiItem.getItemid());
        text_Title.setText(currentWikiItem.getTitle());
        text_classify.setText(currentWikiItem.getClassify());
        editor_description.setText(currentWikiItem.getDescription());
        editor_description.setCaretPosition(0);
        editor_recommand.setText(currentWikiItem.getRecommand());
        editor_recommand.setCaretPosition(0);
        editor_reference.setText(currentWikiItem.getReferences());
        editor_reference.setCaretPosition(0);

        switch (currentWikiItem.getLevel()) {
            case WikiItem.LEVEL_LOW:
                cbo_Level.setSelectedItem(LEVELTYPE[0]);
                break;
            case WikiItem.LEVEL_MIDDLE:
                cbo_Level.setSelectedItem(LEVELTYPE[1]);
                break;
            case WikiItem.LEVEL_HIGH:
                cbo_Level.setSelectedItem(LEVELTYPE[2]);
                break;
            default:
                cbo_Level.setSelectedItem(LEVELTYPE[3]);
                break;
        }

    }

    private void refreshReviewPanel() {
        editor_review.setText(getReviewHtmlText(currentWikiItem));
        editor_review.setCaretPosition(0);
    }

    class DocumentChangeListener implements DocumentListener {

        JTextComponent component;

        public DocumentChangeListener(JTextComponent component) {
            this.component = component;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            if (currentWikiItem == null) {
                currentWikiItem = new WikiItem();
                refresh();
            }
            saveTextIntoCurrentWiki(component);
            refreshReviewPanel();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (currentWikiItem == null) {
                currentWikiItem = new WikiItem();
                refresh();
            }
            saveTextIntoCurrentWiki(component);
            refreshReviewPanel();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (currentWikiItem == null) {
                currentWikiItem = new WikiItem();
                refresh();
            }
            saveTextIntoCurrentWiki(component);
            refreshReviewPanel();
        }
    }
}
