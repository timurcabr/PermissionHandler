package timurcabr.permissionhandler

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
	
	val permissionDialogQueue = mutableStateListOf<String>()
	
	fun dismissDialog() {
		permissionDialogQueue.removeFirst()
	}
	
	fun onPermissionResult(
		permission: String, onGranted: Boolean
	) {
		if (!onGranted && !permissionDialogQueue.contains(permission)) {
			permissionDialogQueue.add(permission)
		}
	}
	
}