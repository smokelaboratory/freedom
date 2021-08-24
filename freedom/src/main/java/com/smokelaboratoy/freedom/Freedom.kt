package com.smokelaboratoy.freedom

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Freedom {

    constructor(activity: ComponentActivity) {
        this.activity = activity

        registerActivityCallbacks()
        registerPermissionCallbacks()
    }

    constructor(fragment: Fragment) {
        this.fragment = fragment

        registerActivityCallbacks()
        registerPermissionCallbacks()
    }

    private var permissions: Array<String>? = null
    private var activity: ComponentActivity? = null
    private var fragment: Fragment? = null
    private lateinit var startActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var response: ((Boolean, Map<String, FreedomStatus>) -> Unit)? = null
    private var popupConfigurations: PopupConfigurations? = null

    private fun registerPermissionCallbacks() {
        val permissionResponse = ActivityResultCallback<Map<String, Boolean>> {
            val freedomStatusMap = mutableMapOf<String, FreedomStatus>()

            for ((permission, isGranted) in it) {
                if (isGranted)
                    freedomStatusMap[permission] =
                        FreedomStatus.GRANTED
                else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            getActivity(),
                            permission
                        )
                    )
                        freedomStatusMap[permission] =
                            FreedomStatus.ALWAYS_DENIED
                    else
                        freedomStatusMap[permission] =
                            FreedomStatus.DENIED
                }

            }

            response?.let {
                it(
                    freedomStatusMap.values.all { it == FreedomStatus.GRANTED },
                    freedomStatusMap
                )
            }

            if (popupConfigurations != null && freedomStatusMap.values.any { it == FreedomStatus.ALWAYS_DENIED })
                showSettingsPop()
        }

        requestPermissionLauncher =
            if (fragment == null)
                activity!!.registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                    permissionResponse
                )
            else
                fragment!!.registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                    permissionResponse
                )
    }

    private fun registerActivityCallbacks() {
        val activityResponse = ActivityResultCallback<ActivityResult> {
            request()
        }

        startActivityLauncher = if (fragment == null)
            activity!!.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                activityResponse
            )
        else
            fragment!!.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                activityResponse
            )

    }

    fun showSettingsPop(title: String? = null, message: String? = null) {
        val currentActivity = getActivity()

        popupConfigurations?.run {
            MaterialAlertDialogBuilder(
                currentActivity,
                android.R.style.Theme_Material_Dialog_NoActionBar
            )
                .setTitle(title ?: this.title)
                .setMessage(message ?: this.message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveButtonText) { dialogInterface, i ->
                    startActivityLauncher.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${currentActivity.packageName}")
                    })

                    dialogInterface.dismiss()
                }
                .setNegativeButton(negativeButtonText) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    fun request() {
        requestPermissionLauncher.launch(permissions)
    }

    private fun getActivity(): Activity =
        if (fragment == null) activity!! else fragment!!.requireActivity()

    fun popupConfigurations(popupConfigurations: PopupConfigurations.() -> Unit) {
        this.popupConfigurations = PopupConfigurations().apply(popupConfigurations)
    }

    fun permissions(permissions: Array<String>) {
        this.permissions = permissions
    }

    fun response(response: ((Boolean, Map<String, FreedomStatus>) -> Unit)) {
        this.response = response
    }

    inner class PopupConfigurations {
        var title = "Permission required!"
        var message = "App needs the permission to run. Please grant it from app settings."
        var negativeButtonText = "Cancel"
        var positiveButtonText = "Open Settings"
        var cancelable = true
    }
}

fun freedom(
    activity: ComponentActivity,
    permissions: Array<String>,
    popupConfigurations: (Freedom.PopupConfigurations.() -> Unit)? = null,
    response: ((Boolean, Map<String, FreedomStatus>) -> Unit)
): Freedom {
    return Freedom(activity).apply {
        permissions(permissions)
        response(response)
        popupConfigurations?.let {
            popupConfigurations(it)
        }
    }
}

fun freedom(
    fragment: Fragment,
    permissions: Array<String>,
    popupConfigurations: (Freedom.PopupConfigurations.() -> Unit)? = null,
    response: ((Boolean, Map<String, FreedomStatus>) -> Unit)
): Freedom {
    return Freedom(fragment).apply {
        permissions(permissions)
        response(response)
        popupConfigurations?.let {
            popupConfigurations(it)
        }
    }
}

enum class FreedomStatus {
    GRANTED, DENIED, ALWAYS_DENIED
}