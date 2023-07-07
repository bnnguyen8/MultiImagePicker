package ca.bnnguyen.multiimagepicker

class PermissionResult {
    var permissionStatus: HashMap<String, PermissionStatus> = hashMapOf()
    var finalStatus: PermissionStatus = PermissionStatus.NOT_GIVEN
}