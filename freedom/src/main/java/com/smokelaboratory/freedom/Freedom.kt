package com.smokelaboratory.freedom

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Freedom(private val activity: Activity) {

    private var fragment: Fragment? = null

    constructor(activity: Activity, fragment: Fragment) : this(activity) {
        this.fragment = fragment
    }

    var permissions: Array<String>? = null
    var userResponse: ((Boolean, Map<String, FreedomStatus>) -> Unit)? = null

    private var popupConfigurations = PopupConfigurations()

    var shouldShowSettingsPopup = false

    private val permissionReqCode = 23994
    private val settingsReqCode = 49932

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionReqCode) {
            val freedomStatusMap = mutableMapOf<String, FreedomStatus>()
            permissions.forEachIndexed { index, permission ->
                if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                        freedomStatusMap[permission] =
                            FreedomStatus.ALWAYS_DENIED
                    else
                        freedomStatusMap[permission] =
                            FreedomStatus.DENIED
                } else
                    freedomStatusMap[permission] =
                        FreedomStatus.GRANTED
            }

            userResponse?.let {
                it(
                    freedomStatusMap.values.all { it == FreedomStatus.GRANTED },
                    freedomStatusMap
                )
            }

            if (shouldShowSettingsPopup && freedomStatusMap.values.any { it == FreedomStatus.ALWAYS_DENIED })
                showSettingsPop()
        }
    }

    fun showSettingsPop(title: String? = null, message: String? = null) {
        popupConfigurations.run {
            MaterialAlertDialogBuilder(
                activity,
                android.R.style.Theme_Material_Dialog_NoActionBar
            )
                .setTitle(title ?: this.title)
                .setMessage(message ?: this.message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveButtonText) { dialogInterface, i ->
                    val settingsIntent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:${activity.packageName}")
                        }
                    if (fragment == null)
                        activity.startActivityForResult(settingsIntent, settingsReqCode)
                    else
                        fragment!!.startActivityForResult(settingsIntent, settingsReqCode)
                    dialogInterface.dismiss()
                }
                .setNegativeButton(negativeButtonText) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == settingsReqCode)
            processPermissions()
    }

    fun processPermissions() {
        permissions?.let {
            if (fragment == null)
                ActivityCompat.requestPermissions(activity, it, permissionReqCode)
            else
                fragment!!.requestPermissions(it, permissionReqCode)
        }
    }

    fun popupConfigurations(block: PopupConfigurations.() -> Unit) {
        popupConfigurations = PopupConfigurations().apply(block)
    }

    inner class PopupConfigurations {
        var title = "Permission required!"
        var message = "App needs the permission to run. Please grant it from app settings."
        var negativeButtonText = "Cancel"
        var positiveButtonText = "Open Settings"
        var cancelable = true
    }
}

fun freedom(activity: Activity, block: Freedom.() -> Unit): Freedom {
    return Freedom(activity).apply { block() }.apply {
        processPermissions()
    }
}

fun freedom(activity: FragmentActivity, fragment: Fragment, block: Freedom.() -> Unit): Freedom {
    return Freedom(activity, fragment).apply { block() }.apply {
        processPermissions()
    }
}


enum class FreedomStatus {
    GRANTED, DENIED, ALWAYS_DENIED
}