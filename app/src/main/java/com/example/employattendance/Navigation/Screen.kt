package com.example.employattendance.Navigation

sealed class Screen(val route:String){
    data object Login: Screen(route = "Login")
    data object SignUp: Screen(route = "SignUp")
    data object OtpScreen: Screen(route = "OtpScreen")
    data object Attendance: Screen(route = "Attendance")
    data object Action: Screen(route = "Action")
    data object Profile:Screen(route = "Profile")
    data object History:Screen(route = "History")
    data object EmployeeLogin :Screen(route = "EmployeeLogin")
    data object Verification: Screen(route = "Verification")
    data object LeaveRequest:Screen(route = "LeaveRequest")
//    data object LeaveApplicationHistoryAdmin:Screen(route = "LeaveApplicationHistoryAdmin")
    object LeaveApplicationHistoryAdmin : Screen("LeaveApplicationHistoryAdmin/{UserId}")
    fun withArgs(UserId: String): String = "LeaveApplicationHistoryAdmin/$UserId"
}
