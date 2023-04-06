package timurcabr.permissionhandler

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import timurcabr.permissionhandler.ui.AudioPermissionTextProvider
import timurcabr.permissionhandler.ui.CameraPermissionTextProvider
import timurcabr.permissionhandler.ui.PermissionDialog
import timurcabr.permissionhandler.ui.PhonePermissionTextProvider

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				val viewModel = viewModel<MainViewModel>()
				val permissionQueue = viewModel.permissionDialogQueue
				
				val permissionsToRequest = arrayOf(
					Manifest.permission.RECORD_AUDIO,
					Manifest.permission.CALL_PHONE,
					Manifest.permission.CAMERA
				)
				
				val cameraPermissionActivityResult =
					rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
						onResult = { isGranted ->
							viewModel.onPermissionResult(
								permission = Manifest.permission.CAMERA, onGranted = isGranted
							)
						})
				
				val multiplePermissionActivityResult =
					rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
						onResult = { perms ->
							permissionsToRequest.forEach { permission ->
								viewModel.onPermissionResult(
									permission = permission, onGranted = perms[permission] == true
								)
							}
						})
				
				Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					
					Button(onClick = {
						cameraPermissionActivityResult.launch(
							Manifest.permission.CAMERA
						)
					}) {
						Text(text = "Request one permission")
					}
					
					Spacer(modifier = Modifier.height(20.dp))
					
					Button(onClick = {
						multiplePermissionActivityResult.launch(
							permissionsToRequest
						)
					}) {
						Text(text = "Request multiple permissions")
					}
					
				}
				
				permissionQueue.forEach { permission ->
					PermissionDialog(permission = when (permission) {
						Manifest.permission.RECORD_AUDIO -> {
							AudioPermissionTextProvider()
						}
						Manifest.permission.CALL_PHONE -> {
							PhonePermissionTextProvider()
						}
						Manifest.permission.CAMERA -> {
							CameraPermissionTextProvider()
						}
						else -> return@forEach
					},
						isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
						onDismiss = viewModel::dismissDialog,
						onOkClick = {
							viewModel.dismissDialog()
							multiplePermissionActivityResult.launch(
								arrayOf(
									permission
								)
							)
						},
						onGoToAppSettingsClick = ::OpenAppSettings
					)
				}
			}
		}
	}
}

fun Activity.OpenAppSettings() {
	Intent(
		Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
		Uri.fromParts("package", packageName, null)
	).also(::startActivity)
}