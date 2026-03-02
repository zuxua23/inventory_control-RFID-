package com.example.inventory_control.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventory_control.Helper.DummyRepository;
import com.example.inventory_control.Models.UserModel;
import com.google.android.material.snackbar.Snackbar;

import com.example.inventory_control.R;

public class LoginActivity extends AppCompatActivity {

    private ImageButton btnSetting;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSetting = findViewById(R.id.btnSetting);
        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingDialog();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = etUsername.getText().toString().trim();
                String inputPassword = etPassword.getText().toString().trim();

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Snackbar.make(v, "Username sama Password isi dulu bre!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                UserModel loggedInUser = DummyRepository.login(inputUsername, inputPassword);

                if (loggedInUser != null) {
                    android.content.SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_FULLNAME", loggedInUser.getUsr_fullname());
                    editor.putInt("ROLE_ID", loggedInUser.getRole_id());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(v, "Username atau password salah!", Snackbar.LENGTH_LONG)
                            .setAction("Tutup", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            }).show();
                }
            }
        });
    }

    private void showSettingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApplyIp = dialog.findViewById(R.id.btnApplyIp);
        EditText etIpAPI = dialog.findViewById(R.id.etIpAPI);
        ImageButton buttonCekIpInside = dialog.findViewById(R.id.buttonCekIp);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonCekIpInside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = etIpAPI.getText().toString().trim();

                if (ipAddress.isEmpty()) {
                    Snackbar.make(dialog.findViewById(android.R.id.content), "Isi dulu alamat IP-nya bre!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (ipAddress.startsWith("http://") || ipAddress.startsWith("https://")) {
                    Snackbar.make(dialog.findViewById(android.R.id.content), "Koneksi ke " + ipAddress + " Berhasil! (Dummy)", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.parseColor("#01C470")) // Warna ijo biar keliatan sukses
                            .show();
                } else {
                    Snackbar.make(dialog.findViewById(android.R.id.content), "Format URL salah! Harus pake http://", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.parseColor("#C62828")) // Warna merah gagal
                            .show();
                }
            }
        });

        btnApplyIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = etIpAPI.getText().toString();

                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "IP Berhasil disimpan: " + ipAddress, Snackbar.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}