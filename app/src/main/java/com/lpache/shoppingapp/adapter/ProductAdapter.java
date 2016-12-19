package com.lpache.shoppingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lpache.shoppingapp.R;
import com.lpache.shoppingapp.entity.Product;

import java.util.List;

/**
 * Created by lpache on 12/17/16.
 */

public class ProductAdapter extends BaseAdapter {

    private final List<Product> products;
    private final Context context;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = products.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView nameField = (TextView) view.findViewById(R.id.item_name);
        nameField.setText(product.getName());

        TextView priceField = (TextView) view.findViewById(R.id.item_price);
        priceField.setText("$ "+String.valueOf(product.getPrice()));

        ImageView imageField = (ImageView) view.findViewById(R.id.item_image);
        String imagePath = product.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            imageField.setImageBitmap(bitmapReduzido);
            imageField.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }
}
