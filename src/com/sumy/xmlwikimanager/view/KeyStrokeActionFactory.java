package com.sumy.xmlwikimanager.view;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Sumy on 2015/12/4 0004.
 */
public class KeyStrokeActionFactory {
    public static AbstractAction getAction(String actionName, JEditorPane editor) {
        if ("li".equals(actionName)) {
            return new LiStrokeAction(editor);
        }
        if ("ul".equals(actionName)) {
            return new UlStrokeAction(editor);
        }
        if ("ol".equals(actionName)) {
            return new OlStrokeAction(editor);
        }
        if ("i".equals(actionName)) {
            return new IStrokeAction(editor);
        }
        if ("b".equals(actionName)) {
            return new BStrokeAction(editor);
        }
        if ("p".equals(actionName)) {
            return new PStrokeAction(editor);
        }
        if ("pre".equals(actionName)) {
            return new PreStrokeAction(editor);
        }
        throw new IllegalArgumentException("unknown action name: " + actionName);
    }
}

class LiStrokeAction extends AbstractAction {
    private JEditorPane editor;

    public LiStrokeAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selecttext = editor.getSelectedText();
        String newtext = "<li>" + (selecttext == null ? "" : selecttext) + "</li>";
        editor.replaceSelection(newtext);
    }
}

class UlStrokeAction extends AbstractAction {
    private JEditorPane editor;

    public UlStrokeAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selecttext = editor.getSelectedText();
        String newtext = "<ul>\n" + (selecttext == null ? "" : selecttext) + "\n</ul>";
        editor.replaceSelection(newtext);
    }
}

class OlStrokeAction extends AbstractAction {
    private JEditorPane editor;

    public OlStrokeAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selecttext = editor.getSelectedText();
        String newtext = "<ol>\n" + (selecttext == null ? "" : selecttext) + "\n</ol>";
        editor.replaceSelection(newtext);
    }
}

class PStrokeAction extends AbstractAction {
    private JEditorPane editor;

    public PStrokeAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selecttext = editor.getSelectedText();
        String newtext = "<p>\n" + (selecttext == null ? "" : selecttext) + "\n</p>";
        editor.replaceSelection(newtext);
    }
}

class PreStrokeAction extends AbstractAction {
    private JEditorPane editor;

    public PreStrokeAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selecttext = editor.getSelectedText();
        String newtext = "<pre>\n" + (selecttext == null ? "" : selecttext) + "\n</pre>";
        editor.replaceSelection(newtext);
    }
}

class BStrokeAction extends AbstractAction {
    private JEditorPane editor;

    public BStrokeAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selecttext = editor.getSelectedText();
        String newtext = "<b>" + (selecttext == null ? "" : selecttext) + "</b>";
        editor.replaceSelection(newtext);
    }
}

class IStrokeAction extends AbstractAction {
    private JEditorPane editor;

    public IStrokeAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selecttext = editor.getSelectedText();
        String newtext = "<i>" + (selecttext == null ? "" : selecttext) + "</i>";
        editor.replaceSelection(newtext);
    }
}