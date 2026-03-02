package com.example.inventory_control.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory_control.Adapter.ItemAdapter;
import com.example.inventory_control.Models.ItemModel;
import com.example.inventory_control.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class StockInActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnClear, btnSave;
    private Switch switchRfid;
    private EditText resultScan;
    private TextView tvScanned;
    private int scanCount = 0;
    private RecyclerView rvTags;
    private ItemAdapter adapter;
    private List<ItemModel> scannedItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in);

        btnBack = findViewById(R.id.btnBack);
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);
        switchRfid = findViewById(R.id.switchRfid);
        resultScan = findViewById(R.id.resultScan);
        tvScanned = findViewById(R.id.tvScanned);
        rvTags = findViewById(R.id.rvTags);

        scannedItemsList = new ArrayList<>();
        adapter = new ItemAdapter(scannedItemsList);
        rvTags.setLayoutManager(new LinearLayoutManager(this));
        rvTags.setAdapter(adapter);

        scannedItemsList.add(new ItemModel("112233", "ITM001", "Kemeja Anti Kusut", 1));
        scannedItemsList.add(new ItemModel("445566", "ITM002", "Vans Japan Edition", 2));
        adapter.notifyDataSetChanged();

        scanCount = 3;
        tvScanned.setText("Scanned: " + scanCount);

        resultScan.requestFocus();
        resultScan.setShowSoftInputOnFocus(false);
        resultScan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    String rfidData = resultScan.getText().toString().trim();

                    if (!rfidData.isEmpty()) {
                        ItemModel foundItem = lookupDummyData(rfidData);

                        if (foundItem != null) {
                            boolean isExist = false;
                            for (int i = 0; i < scannedItemsList.size(); i++) {
                                if (scannedItemsList.get(i).getEpcTag().equals(rfidData)) {
                                    int currentQty = scannedItemsList.get(i).getQty();
                                    scannedItemsList.get(i).setQty(currentQty + 1);
                                    adapter.notifyItemChanged(i);
                                    isExist = true;
                                    break;
                                }
                            }

                            if (!isExist) {
                                scannedItemsList.add(new ItemModel(foundItem.getEpcTag(), foundItem.getItemId(), foundItem.getItemName(), 1));
                                adapter.notifyItemInserted(scannedItemsList.size() - 1);
                                rvTags.scrollToPosition(scannedItemsList.size() - 1);
                            }

                            scanCount++;
                            tvScanned.setText("Scanned: " + scanCount);

                            View rootView = findViewById(android.R.id.content);
                            Snackbar.make(rootView, "Berhasil scan: " + foundItem.getItemName(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            View rootView = findViewById(android.R.id.content);
                            Snackbar.make(rootView, "Tag RFID gak dikenali bre!", Snackbar.LENGTH_SHORT).show();
                        }
                        resultScan.setText("");
                    }
                    resultScan.requestFocus();
                    return true;
                }
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchRfid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View rootView = findViewById(android.R.id.content);
                String msg = isChecked ? "Mode RFID: ON" : "Mode RFID: OFF";
                Snackbar.make(rootView, msg, 1000).show();

                if(isChecked) resultScan.requestFocus();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannedItemsList.clear();
                adapter.notifyDataSetChanged();
                scanCount = 0;
                tvScanned.setText("Scanned: 0");
                resultScan.requestFocus();

                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "List berhasil dibersihkan", Snackbar.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scannedItemsList.isEmpty()) {
                    View rootView = findViewById(android.R.id.content);
                    Snackbar.make(rootView, "Belum ada barang yang di-scan bre!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Data " + scanCount + " item berhasil disimpan!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private ItemModel lookupDummyData(String epcTag) {
        if (epcTag.equalsIgnoreCase("112233")) {
            return new ItemModel("112233", "ITM001", "Kemeja Anti Kusut", 1);
        } else if (epcTag.equalsIgnoreCase("445566")) {
            return new ItemModel("445566", "ITM002", "Vans Japan Edition", 1);
        } else if (epcTag.equalsIgnoreCase("778899")) {
            return new ItemModel("778899", "ITM003", "Trucker Hat Custom", 1);
        }
        return null;
    }
}