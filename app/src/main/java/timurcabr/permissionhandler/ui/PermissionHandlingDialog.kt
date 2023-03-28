package timurcabr.permissionhandler.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(
	permission: PermissionTextProvider,
	isPermanentlyDeclined: Boolean,
	onDismiss: () -> Unit,
	onOkClick: () -> Unit,
	onGoToAppSettingsClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	
	AlertDialog(
		onDismissRequest = onDismiss,
		buttons = {
			Column(
				modifier = Modifier.fillMaxWidth()
			) {
				Divider()
				Text(
					text = if (isPermanentlyDeclined) {
						"Grant permission"
					} else "OK",
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							if (isPermanentlyDeclined) {
								onGoToAppSettingsClick()
							} else {
								onOkClick()
							}
						}
						.padding(16.dp)
				)
			}
		},
		title = {
			Text(text = "Permission required")
		},
		text = {
			Text(text = permission.getDescription(
				isPermanentlyDeclined = isPermanentlyDeclined
			))
		},
		modifier = modifier
	)
	
}

interface PermissionTextProvider {
	fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider: PermissionTextProvider {
	override fun getDescription(isPermanentlyDeclined: Boolean): String {
		return if (isPermanentlyDeclined) {
			"It seems like you declined camera permission." +
					"You can go to settings to grant it"
		} else {
			"This app needs access to your camera." +
					"So your friends can see you in a call"
		}
	}
}

class PhonePermissionTextProvider: PermissionTextProvider {
	override fun getDescription(isPermanentlyDeclined: Boolean): String {
		return if (isPermanentlyDeclined) {
			"It seems like you declined phone call permission." +
					"You can go to settings to grant it"
		} else {
			"This app needs access to your phone call." +
					"So you can call your friends"
		}
	}
}

class AudioPermissionTextProvider: PermissionTextProvider {
	override fun getDescription(isPermanentlyDeclined: Boolean): String {
		return if (isPermanentlyDeclined) {
			"It seems like you declined audio permission." +
					"You can go to settings to grant it"
		} else {
			"This app needs access to your audio." +
					"So your friends can hear you in a call"
		}
	}
}