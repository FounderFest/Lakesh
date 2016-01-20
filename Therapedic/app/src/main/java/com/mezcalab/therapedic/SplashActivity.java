package com.mezcalab.therapedic;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alex-T on 15/01/16.
 */
public class SplashActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Android M Permission check 
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        else {
            validateBluetooth();
        }
    }

    @TargetApi(23)
    private void requestPermission() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Esta aplicación necesita acceso a la localización");
            builder.setMessage("Por favor proporciona permisos de localización para continuar.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Se ha proporcionado permisos de localización");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionalidad limitada");
                    builder.setMessage("No podras disfrutar al 100% de la experiencia de esta aplicación.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
        openMainActivity();
    }

    private void validateBluetooth() {
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //mBluetoothAdapter.startDiscovery();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            AlertDialog.Builder alerta = new AlertDialog.Builder(this/*, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert*/)
                    .setTitle("Error")
                    .setMessage("Esta aplicación necesita Bluetooth y tu dispositivo no tiene soporte la tecnologia.")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            alerta.show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                AlertDialog.Builder alerta = new AlertDialog.Builder(this/*, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert*/)
                        .setTitle("Error")
                        .setMessage("Tu dispositivo no tiene prendido el Bluetooth.")
                        .setCancelable(false)
                        .setPositiveButton("Prender", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mBluetoothAdapter.enable();
                                openMainActivity();
                            }
                        });
                alerta.show();
            }
            else {
                openMainActivity();
            }
        }
    }
}
