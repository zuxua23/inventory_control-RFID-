package com.example.inventory_control.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.inventory_control.R;

public class StockPrepActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CardView btnRefresh;
    private CardView cardlistTag; // Kita pake area ini buat trigger klik pindah halaman sementara

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastiin XML ini nampilin daftar Delivery Order
        setContentView(R.layout.activity_stock_prep_delivery_order);

        btnBack = findViewById(R.id.btnBack);
        btnRefresh = findViewById(R.id.btnRefresh);
        cardlistTag = findViewById(R.id.cardlistTag);

        // Tombol Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Tombol Refresh
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StockPrepActivity.this, "Refreshing DO List...", Toast.LENGTH_SHORT).show();
            }
        });

        // SIMULASI KLIK ITEM DO (Klik area putih list)
        // Nanti kalau Adapter RecyclerView udah jadi, code Intent ini dipindah ke dalam Adapter
        cardlistTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buka halaman detail DO
                Intent intent = new Intent(StockPrepActivity.this, StockPrepProductActivity.class);

                // Opsional: Ngirim data DO dummy ke halaman sebelah
                intent.putExtra("NO_DO", "DO-9999-XYZ");
                intent.putExtra("DATE_DO", "23-02-2026");

                startActivity(intent);
            }
        });
    }
}