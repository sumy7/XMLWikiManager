package com.sumy.xmlwikimanager.view;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by Sumy on 2015/11/30 0030.
 */
public class MyFileFilter extends FileFilter {

    String ends;
    String description;

    public MyFileFilter(String ends, String description) {
        this.ends = ends;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String fileName = f.getName();
        return fileName.toUpperCase().endsWith(this.ends.toUpperCase());
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getEnds() {
        return this.ends;
    }
}
