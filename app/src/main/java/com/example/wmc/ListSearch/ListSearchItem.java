package com.example.wmc.ListSearch;

public class ListSearchItem {

    String cafeName;
    String cafeAddress;

    public ListSearchItem(String cafeName, String cafeAddress){
        this.cafeName = cafeName;
        this.cafeAddress = cafeAddress;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public String getCafeAddress() {
        return cafeAddress;
    }

    public void setCafeAddress(String cafeAddress) {
        this.cafeAddress = cafeAddress;
    }
}
