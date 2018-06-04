
package com.promobi.nyt.nytimesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Headline {

    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("kicker")
    @Expose
    private Object kicker;
    @SerializedName("content_kicker")
    @Expose
    private Object contentKicker;
    @SerializedName("print_headline")
    @Expose
    private Object printHeadline;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("seo")
    @Expose
    private Object seo;
    @SerializedName("sub")
    @Expose
    private Object sub;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Object getKicker() {
        return kicker;
    }

    public void setKicker(Object kicker) {
        this.kicker = kicker;
    }

    public Object getContentKicker() {
        return contentKicker;
    }

    public void setContentKicker(Object contentKicker) {
        this.contentKicker = contentKicker;
    }

    public Object getPrintHeadline() {
        return printHeadline;
    }

    public void setPrintHeadline(Object printHeadline) {
        this.printHeadline = printHeadline;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getSeo() {
        return seo;
    }

    public void setSeo(Object seo) {
        this.seo = seo;
    }

    public Object getSub() {
        return sub;
    }

    public void setSub(Object sub) {
        this.sub = sub;
    }

}
