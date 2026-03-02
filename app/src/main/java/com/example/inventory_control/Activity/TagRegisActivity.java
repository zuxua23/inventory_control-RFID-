package com.example.inventory_control.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory_control.Adapter.TagAdapter;
import com.example.inventory_control.Models.TagModel;
import com.example.inventory_control.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TagRegisActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText resultScan;
    private TextView tvScanned;
    private Switch switchRfid;
    private CardView btnRefresh;
    private RecyclerView rvTags;
    private TagAdapter adapter;
    private List<TagModel> registeredTagList;

    private int scanCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        btnBack = findViewById(R.id.btnBack);
        resultScan = findViewById(R.id.resultScan);
        tvScanned = findViewById(R.id.tvScanned);
        switchRfid = findViewById(R.id.switchRfid);
        btnRefresh = findViewById(R.id.btnRefresh);
        rvTags = findViewById(R.id.rvTags);

        registeredTagList = new ArrayList<>();
        adapter = new TagAdapter(registeredTagList);
        rvTags.setLayoutManager(new LinearLayoutManager(this));
        rvTags.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        switchRfid.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "ON" : "OFF";
            Snackbar.make(findViewById(android.R.id.content), "RFID Mode: " + status, Snackbar.LENGTH_SHORT).show();
        });

        btnRefresh.setOnClickListener(v -> {
            scanCount = 0;
            tvScanned.setText("Scanned: " + scanCount);
            registeredTagList.clear();
            adapter.notifyDataSetChanged();
            Snackbar.make(v, "Data session di-refresh bre!", Snackbar.LENGTH_SHORT).show();
            resultScan.requestFocus();
        });

        resultScan.requestFocus();
        resultScan.setShowSoftInputOnFocus(false);

        resultScan.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String rfidData = resultScan.getText().toString().trim();

                if (!rfidData.isEmpty()) {
                    boolean isAlreadyInList = false;
                    for(TagModel t : registeredTagList) {
                        if(t.getEpcTag().equals(rfidData)) {
                            isAlreadyInList = true;
                            break;
                        }
                    }

                    if (!isAlreadyInList) {
                        showConfirmDialog(rfidData);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Tag ini udah ada di list bre!", Snackbar.LENGTH_SHORT).show();
                    }
                    resultScan.setText("");
                }
                resultScan.requestFocus();
                return true;
            }
            return false;
        });
    }

    private void showConfirmDialog(String scannedData) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_regist);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        btnNo.setOnClickListener(v -> {
            Snackbar.make(findViewById(android.R.id.content), "Registrasi dibatalkan", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnYes.setOnClickListener(v -> {
            scanCount++;
            tvScanned.setText("Scanned: " + scanCount);

            registeredTagList.add(new TagModel(scannedData, "New Tag Registered"));
            adapter.notifyItemInserted(registeredTagList.size() - 1);
            rvTags.scrollToPosition(registeredTagList.size() - 1);

            View rootView = findViewById(android.R.id.content);
            Snackbar.make(rootView, "Tag " + scannedData + " sukses terdaftar!", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}