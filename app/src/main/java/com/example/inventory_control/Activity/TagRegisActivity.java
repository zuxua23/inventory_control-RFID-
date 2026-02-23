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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

// Pastiin import R sesuai nama package lu
import com.example.inventory_control.R;

public class TagRegisActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText resultScan;
    private TextView tvScanned;
    private Switch switchRfid;
    private CardView btnRefresh;

    private int scanCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastiin nama layout utama ini sesuai sama file XML lu
        setContentView(R.layout.activity_regist);

        // 1. Inisialisasi View
        btnBack = findViewById(R.id.btnBack);
        resultScan = findViewById(R.id.resultScan);
        tvScanned = findViewById(R.id.tvScanned);
        switchRfid = findViewById(R.id.switchRfid);
        btnRefresh = findViewById(R.id.btnRefresh);

        // 2. Logic Tombol Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 3. Logic Switch RFID
        switchRfid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String status = isChecked ? "ON" : "OFF";
                Toast.makeText(TagRegisActivity.this, "RFID " + status, Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Logic Tombol Refresh (Reset Counter)
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCount = 0;
                tvScanned.setText("Scanned: " + scanCount);
                Toast.makeText(TagRegisActivity.this, "Data di-refresh", Toast.LENGTH_SHORT).show();
                // Balikin fokus ke scanner lagi
                resultScan.requestFocus();
            }
        });

        // 5. Setup hidden EditText buat nangkap hasil Scanner
        resultScan.requestFocus();
        resultScan.setShowSoftInputOnFocus(false); // Biar keyboard HP gak muncul

        resultScan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    String rfidData = resultScan.getText().toString().trim();

                    if (!rfidData.isEmpty()) {
                        // Kalau berhasil scan, langsung munculin Dialog Konfirmasi
                        showConfirmDialog(rfidData);

                        // Kosongin field biar siap nangkep scan berikutnya
                        resultScan.setText("");
                    }

                    resultScan.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    // Method buat nampilin Dialog Konfirmasi Registrasi
    private void showConfirmDialog(String scannedData) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // TODO: Pastiin nama XML ini sesuai sama layout dialog konfirmasi lu
        dialog.setContentView(R.layout.dialog_regist);

        // Bikin background tembus pandang biar CardView radius 30dp-nya tetep melengkung cakep
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Cari tombol di dalam dialog
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TagRegisActivity.this, "Registrasi dibatalkan", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kalau di-klik Yes, hitung nambah dan muncul di TextView
                scanCount++;
                tvScanned.setText("Scanned: " + scanCount);

                // TODO: Nanti logic masukin data ke RecyclerView & hit API taruh di sini
                Toast.makeText(TagRegisActivity.this, "Tag " + scannedData + " berhasil diregistrasi!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}