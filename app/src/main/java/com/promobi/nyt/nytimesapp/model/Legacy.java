
package com.promobi.nyt.nytimesapp.model;

import java.util.HashMap;
import java.util.Map;

public class Legacy {

    private String wide;
    private String wideheight;
    private String widewidth;
    private String xlargewidth;
    private String xlarge;
    private String xlargeheight;
    private String thumbnailheight;
    private String thumbnail;
    private String thumbnailwidth;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getWide() {
        return wide;
    }

    public void setWide(String wide) {
        this.wide = wide;
    }

    public String getWideheight() {
        return wideheight;
    }

    public void setWideheight(String wideheight) {
        this.wideheight = wideheight;
    }

    public String getWidewidth() {
        return widewidth;
    }

    public void setWidewidth(String widewidth) {
        this.widewidth = widewidth;
    }

    public String getXlargewidth() {
        return xlargewidth;
    }

    public void setXlargewidth(String xlargewidth) {
        this.xlargewidth = xlargewidth;
    }

    public String getXlarge() {
        return xlarge;
    }

    public void setXlarge(String xlarge) {
        this.xlarge = xlarge;
    }

    public String getXlargeheight() {
        return xlargeheight;
    }

    public void setXlargeheight(String xlargeheight) {
        this.xlargeheight = xlargeheight;
    }

    public String getThumbnailheight() {
        return thumbnailheight;
    }

    public void setThumbnailheight(String thumbnailheight) {
        this.thumbnailheight = thumbnailheight;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailwidth() {
        return thumbnailwidth;
    }

    public void setThumbnailwidth(String thumbnailwidth) {
        this.thumbnailwidth = thumbnailwidth;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
