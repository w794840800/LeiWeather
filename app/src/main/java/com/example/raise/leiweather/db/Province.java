package com.example.raise.leiweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by raise on 18-3-1.
 */

public class Province extends DataSupport {

    private int ProviceCode;

    public int getProviceCode() {
        return ProviceCode;
    }

    public void setProviceCode(int proviceCode) {
        ProviceCode = proviceCode;
    }

    public String getProviceName() {
        return proviceName;
    }

    public void setProviceName(String proviceName) {
        this.proviceName = proviceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String proviceName;
    private int id;
}

