package com.lpache.shoppingapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lpache.shoppingapp.R;
import com.lpache.shoppingapp.adapter.ProductAdapter;
import com.lpache.shoppingapp.entity.Product;
import com.lpache.shoppingapp.entity.ShoppingCart;

import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductListActivity extends Activity {

    private final String REST_URL_BASE = "http://remote-lpache.rhcloud.com/rest";

    public ListView products;
    public ShoppingCart basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if(basket == null && getIntent().getSerializableExtra("basket") == null) {
            getIntent().putExtra("basket", new ShoppingCart(String.valueOf(Calendar.getInstance().getTimeInMillis())));
        } else {
            basket = (ShoppingCart) getIntent().getSerializableExtra("basket");
        }
        products = (ListView) findViewById(R.id.product_list);
        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Product product = (Product) products.getItemAtPosition(position);

            Intent itentSelectQuantity = new Intent(ProductListActivity.this, ProductBuyActivity.class);
            itentSelectQuantity.putExtra("product", product);
            itentSelectQuantity.putExtra("basket", basket);
            startActivity(itentSelectQuantity);

            }
        });
        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getSerializableExtra("basket") != null)
            basket = (ShoppingCart) getIntent().getSerializableExtra("basket");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.basket:
                Intent itentBasket = new Intent(ProductListActivity.this, BasketActivity.class);
                itentBasket.putExtra("basket", basket);
                startActivity(itentBasket);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProducts() {
        try {
            // request list to web service
            new ProductListActivity.UpdateProductListTask().execute(new URI(REST_URL_BASE+"/products"));
            } catch (URISyntaxException e) {
            Log.v("ERRO", e.getMessage());
        }
    }

    private class UpdateProductListTask extends AsyncTask<URI, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(URI ...urls) {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(urls[0], String.class);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProductListActivity.this);
            progressDialog.setMessage("Loading Product list...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(String result) {
            ArrayList<Product> listProduct = new ArrayList<Product>();
            // convert json return to product object list
            Gson gson = new Gson();
            List obj = gson.fromJson(result, ArrayList.class );

            for (Object item : obj) {
                Product p = gson.fromJson(gson.toJson(item), Product.class);
                listProduct.add(p);
            }

            products.setAdapter(new ProductAdapter(ProductListActivity.this, listProduct));
            progressDialog.cancel();
        }
    }
}
