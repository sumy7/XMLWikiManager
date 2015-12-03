package com.sumy.xmlwikimanager;

import com.sumy.xmlwikimanager.view.MainForm;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Sumy on 2015/11/26 0026.
 */
public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();
        MainForm form = new MainForm(frame);
        form.setTitle("知识库管理系统");
        frame.setContentPane(form.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                int ret = JOptionPane.showConfirmDialog(null, "确定要退出？", "退出", JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }
}
