package com.example.ecommerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        // Bind cart item data to the views
        holder.productName.setText(cartItem.getName());
        holder.productPrice.setText("$" + cartItem.getPrice());
        holder.productQuantity.setText(String.valueOf(cartItem.getQuantity()));
        Glide.with(context).load(cartItem.getImg()).into(holder.productImage);

        // Handle "+" button click
        holder.plusButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() < 9) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                updateCartInSharedPreferences();
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Maximum quantity allowed is 9.", Toast.LENGTH_SHORT).show();
            }
        });


        // Handle "-" button click
        holder.minusButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                // Reduce the quantity
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                updateCartInSharedPreferences();
                notifyDataSetChanged();
            } else {
                // Remove the item when quantity is 1 and minus is pressed
                cartItemList.remove(position);
                updateCartInSharedPreferences();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItemList.size());

                Toast.makeText(context, "Item removed from cart.", Toast.LENGTH_SHORT).show();
            }
            if (cartItemList.isEmpty()) {
                ((CartActivity) context).togglePlaceOrderButton(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productQuantity;
        Button plusButton, minusButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_item_image);
            productName = itemView.findViewById(R.id.cart_item_name);
            productPrice = itemView.findViewById(R.id.cart_item_price);
            productQuantity = itemView.findViewById(R.id.cart_item_quantity);
            plusButton = itemView.findViewById(R.id.cart_item_plus);
            minusButton = itemView.findViewById(R.id.cart_item_minus);
        }
    }

    private void updateCartInSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String updatedCartJson = gson.toJson(cartItemList);
        editor.putString("cart_data", updatedCartJson);
        editor.apply();
    }
}

