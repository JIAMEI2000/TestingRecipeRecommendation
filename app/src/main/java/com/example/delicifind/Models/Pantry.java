package com.example.delicifind.Models;

public class Pantry {
    String pName,quantity;

    Pantry()
    {

    }
    public Pantry(String pName,String quantity) {
        this.pName = pName;
        this.quantity = quantity;
    }

    public String getpName() {
        return pName;
    }


    public String getQuantity() {
        return quantity;
    }

}
