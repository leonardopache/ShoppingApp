package com.lpache.shoppingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lpache.shoppingapp.R;
import com.lpache.shoppingapp.entity.CommerceItem;
import com.lpache.shoppingapp.entity.Product;
import com.lpache.shoppingapp.entity.ShoppingCart;

import java.util.List;

/**
 * Created by lpache on 12/18/16.
 */

public class ShoppingCartAdapter extends BaseAdapter {

    private final List<CommerceItem> listItemBasket;
    private final Context context;

    public ShoppingCartAdapter(Context context, List<CommerceItem> listItemBasket) {
        this.context = context;
        this.listItemBasket = listItemBasket;
    }

    @Override
    public int getCount() {
        return listItemBasket.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemBasket.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommerceItem commerceItem = listItemBasket.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.basket_item, parent, false);
        }

        TextView nameField = (TextView) view.findViewById(R.id.item_basket_id);
        nameField.setText("ID:"+commerceItem.getProduct_id());

        TextView quantityField = (TextView) view.findViewById(R.id.item_basket_quantity);
        quantityField.setText("( "+String.valueOf(commerceItem.getQuantity())+" )");


        TextView priceField = (TextView) view.findViewById(R.id.item_basket_price);
        priceField.setText("$ "+String.valueOf(commerceItem.getAmount()));


        return view;
    }
}
