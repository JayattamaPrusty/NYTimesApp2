package com.promobi.nyt.nytimesapp.model;

import android.os.Bundle;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by barbara on 2/11/16.
 * see http://developer.nytimes.com/docs/read/article_search_api_v2 and
 * http://developer.nytimes.com/docs/read/article_search_api_v2#filters for valid values
 */
public class SearchFilter implements Serializable {
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd"; //"YYYYMMDD";
    public static final String FORMAT_MONTH_DAY_YEAR = "MM/dd/yyyy";

    public static final String SORT_ORDER_DEFAULT = "Default";
    public static final String SORT_ORDER_NEWEST = "Newest";
    public static final String SORT_ORDER_OLDEST = "Oldest";

    public static final String NEWS_DESK_ARTS = "Arts";
    public static final String NEWS_DESK_FASHION_AND_STYLE = "Fashion & Style";
    public static final String NEWS_DESK_SPORTS = "Sports";

    public static final String BEGIN_DATE_TIMESTAMP_ID = "beginDateTimestamp";
    public static final String SORT_ORDER_ID = "sortOrder";
    public static final String TOPICS_ID = "topics";

    Long beginDateTimestamp;
    String sortOrder;
    HashMap<String, String> topicFilter;

    public SearchFilter() {
        topicFilter = new HashMap<String, String>();
    }

    //	Create from a bundle
    public SearchFilter(Bundle b) {
        topicFilter = new HashMap<String, String>();
        if (b != null) {
            this.setBeginDateTimestamp(b.getLong(BEGIN_DATE_TIMESTAMP_ID));
            this.setSortOrder(b.getString(SORT_ORDER_ID));
            String commaSeparatedTopics = b.getString(TOPICS_ID);
            if (!TextUtils.isEmpty(commaSeparatedTopics)) {
                for (String topic : commaSeparatedTopics.split(",")) {
                    this.addNewsDeskTopic(topic);
                }
            }
        }
    }

    public void clear() {
        beginDateTimestamp = null;
        sortOrder = null;
        topicFilter.clear();
    }

    //	Package data for transfer between activities
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if (this.beginDateTimestamp != null) {
            b.putLong(BEGIN_DATE_TIMESTAMP_ID, this.beginDateTimestamp);
        }
        if (this.sortOrder != null) {
            b.putString(SORT_ORDER_ID, this.sortOrder);
        }
        if (this.topicFilter != null) {
            b.putString(TOPICS_ID, StringUtils.join(getNewsDeskTopics().toArray(), ","));
        }
        return b;
    }

    // begin date

    public void setBeginDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        beginDateTimestamp = calendar.getTime().getTime();
    }

    public void setBeginDateTimestamp(Long timestamp) {
        if (timestamp > 0) {
            beginDateTimestamp = timestamp;
        } else {
            beginDateTimestamp = null;
        }
    }

    public void clearBeginDate() {
        beginDateTimestamp = null;
    }

    public Long getBeginDateTimestamp() {
        return beginDateTimestamp;
    }

    public String getBeginDate(String format) {
        CharSequence str = "";
        if (beginDateTimestamp != null) {
            str = android.text.format.DateFormat.format(format, beginDateTimestamp);
        }
        return str.toString();
    }

    // sort order

    public void setSortOrder(String newSortOrder) {
        sortOrder = null;
        if (newSortOrder != null) {
            switch (newSortOrder) {
                case SORT_ORDER_NEWEST:
                    sortOrder = SORT_ORDER_NEWEST;
                    break;
                case SORT_ORDER_OLDEST:
                    sortOrder = SORT_ORDER_OLDEST;
                    break;
            }
        }
    }

    public String getSortOrder() {
        if (!TextUtils.isBlank(sortOrder)) {
            if ((sortOrder.equals(SORT_ORDER_NEWEST)) || (sortOrder.equals(SORT_ORDER_OLDEST))) {
                return sortOrder;
            }
        }
        return null;
    }

    // topics

    public void addNewsDeskTopic(String topic) {
        if (!TextUtils.isEmpty(topic)) {
            topicFilter.put(topic, topic);
        }
    }

    public void removeNewsDeskTopic(String topic) {
        topicFilter.remove(topic);
    }

    public Set getNewsDeskTopics() {
        return topicFilter.keySet();
    }

    public boolean hasTopicChecked(String topic) {
        return topicFilter.containsKey(topic);
    }

    // general

    public boolean isFilterSet() {
        if ((this.beginDateTimestamp != null) ||
                (getSortOrder() != null) ||
                this.topicFilter.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        if (!isFilterSet()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        if (getNewsDeskTopics().size() > 0) {
            sb.append(StringUtils.join(getNewsDeskTopics().toArray(), ", "));
        }

        String str = getBeginDate(FORMAT_MONTH_DAY_YEAR);
        if (!TextUtils.isEmpty(str)) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append("since " + str);
        }
        if (getSortOrder() != null) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(getSortOrder());
        }


        return sb.toString();
    }

}
