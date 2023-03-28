package timurcabr.permissionhandler

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
	
	val permissionDialogQueue = mutableStateListOf<String>()
	
	fun dismissDialog() {
		permissionDialogQueue.removeLast()
	}
	
	fun onPermissionResult(
		permission: String, onGranted: Boolean
	) {
		if (!onGranted) {
			permissionDialogQueue.add(0, permission)
		}
	}
	
}