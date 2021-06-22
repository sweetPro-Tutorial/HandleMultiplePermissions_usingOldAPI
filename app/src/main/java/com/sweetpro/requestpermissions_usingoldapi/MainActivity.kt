package com.sweetpro.requestpermissions_usingoldapi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.sweetpro.requestpermissions_usingoldapi.databinding.ActivityMainBinding

//
// ==========
// main process:
// ----------
// if this app needs multiple 'runtime permissions'(= dangerous permissions)
//
// step1:
// verify permissions and see if any of them are not granted
// step2:
// if (not granted) then
//   request all the permissions
// step3: <<<--- optional
// handle callback: it's the permission request callback for a permission after calling.
// ex) app can display additional context and justification
//

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    // for view binding
    private lateinit var binding: ActivityMainBinding

    // needed multiple permissions
    val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    val LOCATION              = Manifest.permission.ACCESS_FINE_LOCATION
    val CONTACTS              = Manifest.permission.READ_CONTACTS

    val neededPermissions = arrayOf(READ_EXTERNAL_STORAGE, LOCATION, CONTACTS)

    val GRANTED = PackageManager.PERMISSION_GRANTED

    private lateinit var layout: View  // main_layout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        layout = binding.root
        setContentView(layout)

        binding.button.setOnClickListener {
            handlePermissions()
        }
    }


    private fun handlePermissions() {
        // main process: if this app needs multiple 'runtime permissions'(= dangerous permissions)
        // ----
        // step1:
        // verify permissions and see if any of them are not granted
        val granted: Boolean = verifyPermissions(neededPermissions)

        // step2:
        // if (not granted) then
        //   request all the permissions
        if (! granted) {
            myRequestPermission(neededPermissions)
        }
    }

    private fun verifyPermissions(neededPermissions: Array<String>): Boolean {
        val hasPermission = neededPermissions.all { permission ->
            ActivityCompat.checkSelfPermission(this, permission) == GRANTED
        }

        return hasPermission
    }


    // define request code used in ActivityCompat.requestPermissions()
    //  note: request code is app specific(= unique in the app).
    //  the request code will be returned on optional step 'handle callback'
    val REQUEST_ID_FOR_PERMISSION_ALL = 10001
    private fun myRequestPermission(neededPermissions: Array<String>) {
        // request all runtime permissions
        ActivityCompat.requestPermissions(this,
                neededPermissions, REQUEST_ID_FOR_PERMISSION_ALL)
    }

    // step3:   and handle the permission request callback <<<--- optional
    //  app can display additional context and justification for a permission after calling
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var granted: Boolean

        when {

        }
        if (requestCode == REQUEST_ID_FOR_PERMISSION_ALL) {
            Log.d(TAG, ">>> onRequestPermissionsResult: size=" + grantResults.size)
            // If request is cancelled, the result arrays are empty.
            if (grantResults.size <= 0) { return }

            permissions.forEachIndexed { index, permission ->
                granted = if (grantResults[index] == GRANTED) true else false

                Log.d(TAG, ">>> onRequestPermissionsResult: ${permission}=${granted}")

                // if any permission is not granted...
                if (! granted) {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    // ----
                    // Do anything if needed. ex) Show Snackbar with a button
                    layout.showSnackbar(R.string.permission_required,
                            Snackbar.LENGTH_INDEFINITE, R.string.ok) {

                    }
                }
            }
            return
        }
    }
}