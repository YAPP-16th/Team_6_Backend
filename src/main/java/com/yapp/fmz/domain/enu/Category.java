package com.yapp.fmz.domain.enu;

public enum Category {
    MT1("Mart"),
    CS2("ConvenienceStore"),
    SW8("Subway"),
    CT1("CT1"),
    PO3("PO3"),
    CE7("CE7"),
    HP8("HP8"),
    AT4("AT4"),
    BK9("BK9"),
    FD6("FD6"),
    PM9("PM9");


    private String description;

    Category(String description)
    {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
