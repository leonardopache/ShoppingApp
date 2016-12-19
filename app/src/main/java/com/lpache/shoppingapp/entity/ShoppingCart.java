package com.lpache.shoppingapp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpache on 12/17/16.
 */

public class ShoppingCart implements Serializable {

    private String id;
    /* All the commerce items on the current shopping cart.*/
    private List<CommerceItem> items = new ArrayList<>();
    /*Order total value, calculated summing the amount of all commerce items.*/
    private BigDecimal amount = BigDecimal.ZERO;

    public ShoppingCart(){}

    public ShoppingCart(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<CommerceItem> getItems() {
        return items;
    }
    public void setItems(List<CommerceItem> items) {
        this.items = items;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
