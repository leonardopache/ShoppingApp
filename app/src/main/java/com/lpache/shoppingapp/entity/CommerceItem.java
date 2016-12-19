package com.lpache.shoppingapp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lpache on 12/17/16.
 */

public class CommerceItem implements Serializable{

    /*The commerce item id, unique per commerce item, used to identify the commerce item inside the shopping cart.*/
    private String id;
    /*The product id inside this commerce item.*/
    private String product_id;
    /*The quantity added to the shopping cart.*/
    private Integer quantity;
    /*Item amount, calculated multiplying quantity by the product price.*/
    private BigDecimal amount;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
