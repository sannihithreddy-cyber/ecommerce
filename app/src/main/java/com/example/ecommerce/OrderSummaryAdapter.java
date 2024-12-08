package com.example.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public OrderSummaryAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_summary, parent, false);
        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText("Qty: " + item.getQuantity());
        holder.itemPrice.setText("Price per item: $" + String.format("%.2f", item.getPrice()));
        holder.itemTotal.setText("Total: $" + String.format("%.2f", item.getPrice() * item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class OrderSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity, itemPrice, itemTotal;

        public OrderSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.order_item_name);
            itemQuantity = itemView.findViewById(R.id.order_item_quantity);
            itemPrice = itemView.findViewById(R.id.order_item_price);
            itemTotal = itemView.findViewById(R.id.order_item_total);
        }
    }
}
