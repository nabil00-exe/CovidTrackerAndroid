package com.projet.covidtracker.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.projet.covidtracker.R

class CheckInQrScannerActivity : AppCompatActivity() {

    val CAMERA_PERMISSION_CODE = 100
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in_qr_scanner)

        val scannerView = findViewById<CodeScannerView>(R.id.checkin_scanner_view)

        codeScanner = CodeScanner(this, scannerView)


        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false


        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val intent = Intent(this@CheckInQrScannerActivity,ConfirmCheckInActivity::class.java)
                intent.putExtra("location_id","${it.text}")
                startActivity(intent)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@CheckInQrScannerActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this@CheckInQrScannerActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@CheckInQrScannerActivity, "Permission already granted", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@CheckInQrScannerActivity, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@CheckInQrScannerActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}