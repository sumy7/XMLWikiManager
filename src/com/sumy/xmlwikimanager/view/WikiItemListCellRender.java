package com.sumy.xmlwikimanager.view;

import com.sumy.xmlwikimanager.bean.WikiItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sumy on 2015/11/30 0030.
 */
public class WikiItemListCellRender extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        WikiItem item = (WikiItem) value;
        if (item.getItemid() == null || item.getItemid().equals("")) {
            setText("NO_ID");
        } else {
            setText(item.getItemid());
        }
        return this;
    }
}
