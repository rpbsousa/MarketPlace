package com.shiftleft.marketplace.model;

import java.util.List;

/**
 * Created by rsousa on 08/11/2016.
 */

public class NavigationItem {
    private String description;
    private String image;
    private List<NavigationItem> terms;

    public NavigationItem(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public List<NavigationItem> getTerms() {
        return terms;
    }

    public void setTerms(List<NavigationItem> terms) {
        this.terms = terms;
    }
}
