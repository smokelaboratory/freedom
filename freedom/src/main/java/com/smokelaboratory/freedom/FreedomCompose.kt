package com.smokelaboratory.freedom

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun Freedom(
    requester: FreedomComposeRequester,
    permissions: Array<String>,
    onPermissionResult: @Composable (Boolean, Map<String, FreedomStatus>) -> Unit,
    initialContent: @Composable () -> Unit
) {

    val context = LocalContext.current
    val activity = context.findActivity()

    val freedomStatusMap = remember {
        mutableMapOf<String, FreedomStatus>()
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->

        for ((permission, isGranted) in permissionsResult) {
            if (isGranted)
                freedomStatusMap[permission] =
                    FreedomStatus.GRANTED
            else {
                if (!activity.shouldShowRequestPermissionRationale(permission))
                    freedomStatusMap[permission] =
                        FreedomStatus.ALWAYS_DENIED
                else
                    freedomStatusMap[permission] =
                        FreedomStatus.DENIED
            }
        }

        requester.processed()
    }

    when (requester.state()) {
        FreedomComposeState.IDLE -> initialContent.invoke()
        FreedomComposeState.REQUESTING -> {
            initialContent.invoke()
            SideEffect {
                launcher.launch(permissions)
            }
        }
        FreedomComposeState.PROCESSED -> onPermissionResult.invoke(
            freedomStatusMap.values.all { it == FreedomStatus.GRANTED },
            freedomStatusMap
        )
    }
}

@Composable
fun rememberFreedomRequester(): FreedomComposeRequester =
    remember { FreedomComposeRequester() }

enum class FreedomComposeState {
    IDLE, REQUESTING, PROCESSED
}

class FreedomComposeRequester {
    private val state = mutableStateOf(FreedomComposeState.IDLE)

    fun request() {
        state.value = FreedomComposeState.REQUESTING
    }

    fun processed() {
        state.value = FreedomComposeState.PROCESSED
    }

    fun state() = state.value
}

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Activity not found")
}