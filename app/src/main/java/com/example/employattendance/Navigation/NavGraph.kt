package com.example.employattendance.Navigation

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.employattendance.AppUi.Admin.AttendancePage
import com.example.employattendance.AppUi.Admin.LeaveApplicationHistoryAdmin
import com.example.employattendance.AppUi.Employee.Ui.EmployeeLogin
import com.example.employattendance.AppUi.AppEntryPoint.Ui.Login
import com.example.employattendance.AppUi.AppEntryPoint.Ui.OtpScreen
import com.example.employattendance.AppUi.AppEntryPoint.Ui.SignUp
import com.example.employattendance.AppUi.AppEntryPoint.Ui.Verification
import com.example.employattendance.AppUi.Employee.Ui.LeaveRequest
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SetupNavGraph(
    navController: NavHostController,

) {
    NavHost(
        navController = navController,
        startDestination = if(FirebaseAuth.getInstance().currentUser !=null) Screen.EmployeeLogin.route else Screen.Login.route
    ) {

       // FirebaseAuth.getInstance().updateCurrentUser()
        composable(route = Screen.Login.route) {
                Login(navController = navController)
        }

        composable(route = Screen.Verification.route) {
            Verification(navController = navController)
        }

        composable(route = Screen.SignUp.route) {
            SignUp(navController = navController)
        }

        composable(route = Screen.OtpScreen.route) {
            OtpScreen()
        }

        composable(route = Screen.Attendance.route) {
            AttendancePage(navController = navController)
        }

        composable(route = Screen.EmployeeLogin.route){
            EmployeeLogin(navController = navController)
        }
        composable(route = Screen.LeaveRequest.route){
            LeaveRequest(navController = navController)
        }

        composable(route= Screen.LeaveApplicationHistoryAdmin.route){
                navBackStackEntry ->
            val message = navBackStackEntry.arguments?.getString("UserId")
            if (message != null) {
                LeaveApplicationHistoryAdmin(navController, message)
            }
        }


    }

}

