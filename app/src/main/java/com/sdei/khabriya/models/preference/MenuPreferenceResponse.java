package com.sdei.khabriya.models.preference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuPreferenceResponse {
    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("menu_item_parent")
    @Expose
    private String menuItemParent;
    @SerializedName("object_id")
    @Expose
    private String object_id;
    @SerializedName("title")
    @Expose
    private String title;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    Boolean isSelected = false;

    public MenuPreferenceResponse(int iD, String menuItemParent, String object_id, String title) {
        this.iD = iD;
        this.menuItemParent = menuItemParent;
        this.object_id = object_id;
        this.title = title;
    }

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public String getMenuItemParent() {
        return menuItemParent;
    }

    public void setMenuItemParent(String menuItemParent) {
        this.menuItemParent = menuItemParent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }
}
