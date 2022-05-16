package com.example.wmc.a_No_USE_Files;

public class ListSearchItem {

    String search_cafeName;
    String search_cafeAddress;

    public ListSearchItem(String search_cafeName, String search_cafeAddress) {
        this.search_cafeName = search_cafeName;
        this.search_cafeAddress = search_cafeAddress;
    }

    public String getSearch_cafeName() {
        return search_cafeName;
    }

    public void setSearch_cafeName(String search_cafeName) {
        this.search_cafeName = search_cafeName;
    }

    public String getSearch_cafeAddress() {
        return search_cafeAddress;
    }

    public void setSearch_cafeAddress(String search_cafeAddress) {
        this.search_cafeAddress = search_cafeAddress;
    }
}
