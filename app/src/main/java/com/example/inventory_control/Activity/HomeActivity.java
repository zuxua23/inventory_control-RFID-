package com.example.inventory_control.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.inventory_control.R;

public class HomeActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private ImageButton btnStockIn, btnStockPrep, btnStockTaking, btnTagRegis, btnSearchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Inisialisasi View
        ivProfile = findViewById(R.id.ivProfile);
        btnStockIn = findViewById(R.id.ButtonStockIn);
        btnStockPrep = findViewById(R.id.ButtonStockPreparation);
        btnStockTaking = findViewById(R.id.ButtonStockTaking);
        btnTagRegis = findViewById(R.id.ButtonTagRegis);
        btnSearchItem = findViewById(R.id.ButtonSearchItem);

        // Logic klik Profile -> Munculin pop-up Logout melayang
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutPopup(v);
            }
        });

        // Logic klik Menu -> Pindah Halaman
        View.OnClickListener menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = null;

                if (id == R.id.ButtonStockIn) {
                    intent = new Intent(HomeActivity.this, StockInActivity.class);
                }
                else if (id == R.id.ButtonStockPreparation) {
                    intent = new Intent(HomeActivity.this, StockPrepActivity.class);
                }
                else if (id == R.id.ButtonStockTaking) {
                    intent = new Intent(HomeActivity.this, StockTakingActivity.class);
                }
                else if (id == R.id.ButtonTagRegis) {
                    intent = new Intent(HomeActivity.this, TagRegisActivity.class);
                }
                else if (id == R.id.ButtonSearchItem) {
                    intent = new Intent(HomeActivity.this, SearchItemActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        };

        // Konfirmasi kalau user pencet tombol Back di HP
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Keluar Aplikasi")
                        .setMessage("Yakin mau keluar dari Inventory Control?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish(); // Beneran nutup aplikasi
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

        // Terapin listener ke semua tombol menu
        btnStockIn.setOnClickListener(menuClickListener);
        btnStockPrep.setOnClickListener(menuClickListener);
        btnStockTaking.setOnClickListener(menuClickListener);
        btnTagRegis.setOnClickListener(menuClickListener);
        btnSearchItem.setOnClickListener(menuClickListener);
    }

    private void showLogoutPopup(View anchorView) {
        CardView cardView = new CardView(this);
        // Warna normal: Merah (#C62828)
        cardView.setCardBackgroundColor(Color.parseColor("#C62828"));
        cardView.setRadius(40f);
        cardView.setCardElevation(8f);

        TextView tvLogout = new TextView(this);
        tvLogout.setText("Logout");
        tvLogout.setTextColor(Color.WHITE);
        tvLogout.setTextSize(16f);
        tvLogout.setPadding(50, 20, 50, 20);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.raleway_bold);
        tvLogout.setTypeface(typeface);
        cardView.addView(tvLogout);

        PopupWindow popupWindow = new PopupWindow(cardView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAsDropDown(anchorView, 0, -20);

        // --- TAMBAHIN EFEK HANGOVER DI SINI BRE ---
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, android.view.MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    // Pas ditekan: Warna jadi Merah lebih gelap (#8E1C1C)
                    cardView.setCardBackgroundColor(Color.parseColor("#8E1C1C"));
                    // Efek mengecil dikit biar interaktif
                    cardView.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).start();
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP ||
                        event.getAction() == android.view.MotionEvent.ACTION_CANCEL) {
                    // Pas dilepas: Balikin ke warna asal
                    cardView.setCardBackgroundColor(Color.parseColor("#C62828"));
                    cardView.animate().scaleX(1f).scaleY(1f).setDuration(50).start();
                }
                return false; // False biar onClickListener di bawah tetep jalan
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showLogoutConfirmationDialog();
            }
        });
    }

    // Method baru buat nampilin Dialog Konfirmasi Logout
    private void showLogoutConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_alert_logout);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        // Kalau batal (No)
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Tutup dialog aja
            }
        });

        // Kalau yakin mau logout (Yes)
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // Balik ke halaman Login
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }
}