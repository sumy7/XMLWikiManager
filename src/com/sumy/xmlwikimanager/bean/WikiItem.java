package com.sumy.xmlwikimanager.bean;

/**
 * Created by Sumy on 2015/11/26 0026.
 */
public class WikiItem {
    public static final int LEVEL_LOW = 0;
    public static final int LEVEL_MIDDLE = 1;
    public static final int LEVEL_HIGH = 2;
    public static final int LEVEL_UNKNOWN = -1;

    public int id;
    public String itemid;
    public String title;
    public int level;
    public String classify;
    public String description;
    public String recommand;
    public String references;
    public String code;

    public WikiItem() {
        this.id = -1;
        this.itemid = "NO_ID";
        this.title = "";
        this.level = LEVEL_UNKNOWN;
        this.classify = "";
        this.description = "";
        this.code = "";
        this.recommand = "";
        this.references = "";

    }

    public WikiItem(String itemid, String title, int level, String classify, String description, String code, String recommand, String references) {
        this.itemid = itemid;
        this.title = title;
        this.level = level;
        this.classify = classify;
        this.description = description;
        this.code = code;
        this.recommand = recommand;
        this.references = references;
    }

    public WikiItem(WikiItem item) {
        this.itemid = item.itemid;
        this.title = item.title;
        this.level = item.level;
        this.classify = item.classify;
        this.description = item.description;
        this.code = item.code;
        this.recommand = item.recommand;
        this.references = item.references;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommand() {
        return recommand;
    }

    public void setRecommand(String recommand) {
        this.recommand = recommand;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static int parseLevel(String level) {
        switch (level) {
            case "0":
                return LEVEL_LOW;
            case "1":
                return LEVEL_MIDDLE;
            case "2":
                return LEVEL_HIGH;
            default:
                return LEVEL_UNKNOWN;
        }
    }
}
