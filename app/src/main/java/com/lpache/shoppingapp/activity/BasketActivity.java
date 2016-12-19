package com.lpache.shoppingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lpache.shoppingapp.R;
import com.lpache.shoppingapp.adapter.ProductAdapter;
import com.lpache.shoppingapp.adapter.ShoppingCartAdapter;
import com.lpache.shoppingapp.entity.CommerceItem;
import com.lpache.shoppingapp.entity.ShoppingCart;

import java.util.Calendar;

public class BasketActivity extends Activity {

    private ListView basketItens;
    public ShoppingCart basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        if(basket == null && getIntent().getSerializableExtra("basket") == null) {
            getIntent().putExtra("basket", new ShoppingCart(String.valueOf(Calendar.getInstance().getTimeInMillis())));
        } else {
            basket = (ShoppingCart) getIntent().getSerializableExtra("basket");
        }

        basketItens = (ListView) findViewById(R.id.basket_commerce_item);

        TextView idShopping = (TextView) findViewById(R.id.id_shopping);
        idShopping.setText(basket.getId());

        registerForContextMenu(basketItens);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBasket();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final CommerceItem itemBasket = (CommerceItem) basketItens.getItemAtPosition(info.position);

        MenuItem itemDelete = menu.add("Remove");
        itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                removeItemBasket(itemBasket);
                loadBasket();
                return false;
            }
        });

    }

    private void loadBasket() {
        if (basket != null && basket.getItems() != null){
            basketItens.setAdapter(new ShoppingCartAdapter(BasketActivity.this, basket.getItems()));

            TextView amountShopping = (TextView) findViewById(R.id.amount_shopping);
            amountShopping.setText(String.valueOf(" $ "+basket.getAmount()));
        }

    }

    private void removeItemBasket(CommerceItem itemBasket) {
        if(basket != null && basket.getItems() != null){
            basket.setAmount(basket.getAmount().subtract(itemBasket.getAmount()));
            basket.getItems().remove(itemBasket);
        }
    }
}
