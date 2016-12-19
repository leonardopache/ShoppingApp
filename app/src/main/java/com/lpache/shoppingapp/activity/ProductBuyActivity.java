package com.lpache.shoppingapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lpache.shoppingapp.R;
import com.lpache.shoppingapp.entity.CommerceItem;
import com.lpache.shoppingapp.entity.Product;
import com.lpache.shoppingapp.entity.ShoppingCart;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;

public class ProductBuyActivity extends Activity {

    private final String REST_URL_BASE = "http://remote-lpache.rhcloud.com/rest";

    private ShoppingCart basket;
    private Product product;
    private CommerceItem commerceItem = new CommerceItem();
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_buy);

        product = (Product) getIntent().getSerializableExtra("product");
        basket = (ShoppingCart) getIntent().getSerializableExtra("basket");
        final TextView productName = (TextView) findViewById(R.id.productName);
        productName.setText(product.getName());

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void buy(View v){
        instanceCommerceItem(Integer.valueOf(((EditText) findViewById(R.id.quantity)).getText().toString()));

        addItem2Basket(commerceItem);
        try {
            new ProductBuyActivity.AddItemToBasketTask().execute(new URI(REST_URL_BASE+"/cart/add"));
        } catch (URISyntaxException e) {
            Log.v("ERRO", e.getMessage());
        }
        //finish();
    }

    private void addItem2Basket(CommerceItem commerceItem) {
        BigDecimal amount = BigDecimal.ZERO;

        for (CommerceItem item : basket.getItems()) {
            if (item.getProduct_id().equals(commerceItem.getProduct_id())) {
                item.setQuantity(item.getQuantity() + commerceItem.getQuantity());
                amount = commerceItem.getAmount();
            }
        }

        if(BigDecimal.ZERO.compareTo(amount) == 0){
            basket.getItems().add(commerceItem);
            amount = commerceItem.getAmount();
        }
        basket.setAmount(basket.getAmount().add(amount));
    }

    private void instanceCommerceItem(Integer quantity) {
        commerceItem.setProduct_id(product.getId());
        commerceItem.setQuantity(quantity);
        commerceItem.setAmount(new BigDecimal(quantity).multiply(product.getPrice()).setScale(2, BigDecimal.ROUND_FLOOR));
    }

    private class AddItemToBasketTask extends AsyncTask<URI, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(URI... urls) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(new Gson().toJson(basket),headers);
            return restTemplate.postForObject(urls[0], entity, String.class);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProductBuyActivity.this);
            progressDialog.setMessage("Sending Item...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(String result) {
            // convert json return to basket object list
            Gson gson = new Gson();
            ShoppingCart shoppingCart = gson.fromJson(result.toString(), ShoppingCart.class);

            // send basket to putExtra
            Intent intentProductList = new Intent(ProductBuyActivity.this, ProductListActivity.class);
            intentProductList.putExtra("basket", basket);
            startActivity(intentProductList);

            progressDialog.cancel();
        }
    }
}
