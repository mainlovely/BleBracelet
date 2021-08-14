package com.zhj.bluetooth.sdkdemo.utils.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.zhj.bluetooth.sdkdemo.R

/**
 * 申请权限
 * 拒绝则跳转到setting的app信息中
 */
object PermissionUtils {
    const val CAMERA_PERMISSION = 1
    const val GALLERY_PERMISSION = 2
    const val AUDIO_PERMISSION = 3
    const val LOCATION_PERMISSION = 4
    const val READCONTACT_PERMISSION = 5


    fun checkHavePermission(context: Context?, permissionMode: Int): Boolean {
        context ?: return false
        val writeStoragePermission = ContextCompat.checkSelfPermission(context, Permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val readStoragePermission = ContextCompat.checkSelfPermission(context, Permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val readPhonePermission = ContextCompat.checkSelfPermission(context, Permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        val readContacts = ContextCompat.checkSelfPermission(context, Permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

        return when (permissionMode) {
            CAMERA_PERMISSION -> {
                val cameraPermission = ContextCompat.checkSelfPermission(context, Permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                writeStoragePermission && readStoragePermission && cameraPermission
            }
            GALLERY_PERMISSION -> {
                readStoragePermission && writeStoragePermission
            }
            AUDIO_PERMISSION -> {
                val recordAudioPermission = ContextCompat.checkSelfPermission(context, Permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                writeStoragePermission && recordAudioPermission
            }
            LOCATION_PERMISSION -> ContextCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            READCONTACT_PERMISSION -> readContacts
            else -> false
        }
    }

    fun checkHavePermission(context: Context?, permissionMode: Int, listener: PermissionListener) {
        when (checkHavePermission(
            context,
            permissionMode
        )) {
            true -> listener.onSuccess()
            else -> requestPermission(
                context,
                permissionMode,
                listener
            )
        }
    }

    fun requestPermission(context: Context?, permissionMode: Int, listener: PermissionListener) {
        context ?: return
        when (permissionMode) {
            CAMERA_PERMISSION -> {
                AndPermission
                        .with(context)
                        .permission(
                                Permission.READ_EXTERNAL_STORAGE,
                                Permission.WRITE_EXTERNAL_STORAGE,
                                Permission.CAMERA
                        )
                        .onGranted { permissions ->
                            listener.onSuccess()
                        }
                        .onDenied { permissions ->
                            val builder = AlertDialog.Builder(context)
                            builder
                                    .setMessage(
                                            context.getString(R.string.chat_permission1) + Permission.transformText(context, permissions)[0] + context.getString(R.string.chat_permission2)
                                    )
                                    .setTitle(context.getString(R.string.notif))
                                    .setPositiveButton(context.getString(R.string.common_ok)) { dialog, _ ->
                                        doSomething(
                                            context
                                        )
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(
                                            context.getString(R.string.common_cancel)
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        listener.onFail()
                                    }
                                    .setCancelable(false)
                            builder.create().show()
                        }
                        .start()
            }
            GALLERY_PERMISSION -> {
                AndPermission
                        .with(context)
                        .permission(
                                Permission.READ_EXTERNAL_STORAGE,
                                Permission.WRITE_EXTERNAL_STORAGE
                        )
                        .onGranted { permissions ->
                            listener.onSuccess()
                        }
                        .onDenied { permissions ->
                            val builder = AlertDialog.Builder(context)
                            builder
                                    .setMessage(
                                            context.getString(R.string.chat_permission1) + Permission.transformText(context, permissions)[0] + context.getString(R.string.chat_permission2)
                                    )
                                    .setTitle(context.getString(R.string.notif))
                                    .setPositiveButton(context.getString(R.string.common_ok)) { dialog, _ ->
                                        doSomething(
                                            context
                                        )
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(
                                            context.getString(R.string.common_cancel)
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        listener.onFail()
                                    }
                                    .setCancelable(false)
                            builder.create().show()
                        }
                        .start()
            }
            AUDIO_PERMISSION -> {
                AndPermission
                        .with(context)
                        .permission(
                                Permission.WRITE_EXTERNAL_STORAGE,
                                Permission.RECORD_AUDIO
                        ).onGranted { permissions ->
                            listener.onSuccess()
                        }
                        .onDenied { permissions ->
                            val builder = AlertDialog.Builder(context)
                            builder
                                    .setMessage(
                                            context.getString(R.string.chat_permission1) + Permission.transformText(context, permissions)[0] + context.getString(R.string.chat_permission2)
                                    )
                                    .setTitle(context.getString(R.string.notif))
                                    .setPositiveButton(context.getString(R.string.common_ok)) { dialog, _ ->
                                        doSomething(
                                            context
                                        )
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(
                                            context.getString(R.string.common_cancel)
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        listener.onFail()
                                    }
                                    .setCancelable(false)
                            builder.create().show()
                        }
                        .start()
            }

            LOCATION_PERMISSION -> {
                AndPermission
                        .with(context)
                        .permission(
                                Permission.ACCESS_FINE_LOCATION,
                                Permission.ACCESS_COARSE_LOCATION
                        )
                        .onGranted { permissions ->
                            listener.onSuccess()
                        }
                        .onDenied { permissions ->
                            val builder = AlertDialog.Builder(context)
                            builder
                                    .setMessage(
                                            context.getString(R.string.chat_permission1) + Permission.transformText(context, permissions)[0] + context.getString(R.string.chat_permission2)
                                    )
                                    .setTitle(context.getString(R.string.notif))
                                    .setPositiveButton(context.getString(R.string.common_ok)) { dialog, _ ->
                                        doSomething(
                                            context
                                        )
                                        listener.onFail()
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(
                                            context.getString(R.string.common_cancel)
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        listener.onFail()
                                    }
                                    .setCancelable(false)
                            builder.create().show()
                        }
                        .start()
            }

            READCONTACT_PERMISSION -> {
                AndPermission
                        .with(context)
                        .permission(
                                Permission.READ_CONTACTS
                        )
                        .onGranted { permissions ->
                            listener.onSuccess()
                        }
                        .onDenied { permissions ->
                            val builder = AlertDialog.Builder(context)
                            builder
                                    .setMessage(
                                            context.getString(R.string.chat_permission1) + Permission.transformText(context, permissions)[0] + context.getString(R.string.chat_permission2)
                                    )
                                    .setTitle(context.getString(R.string.notif))
                                    .setPositiveButton(context.getString(R.string.common_ok)) { dialog, _ ->
                                        doSomething(
                                            context
                                        )
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(
                                            context.getString(R.string.common_cancel)
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        listener.onFail()
                                    }
                                    .setCancelable(false)
                            builder.create().show()
                        }
                        .start()
            }
        }
    }

    private fun doSomething(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", AppUtils.getAppPackageName(), null)
        context.startActivity(intent)
    }

    interface PermissionListener {
        fun onSuccess() {}
        fun onFail() {}
    }
}