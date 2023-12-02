package com.example.employattendance.AppUi.AppEntryPoint.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.employattendance.Navigation.Screen
import com.example.employattendance.data.EMPLOYEE_COLLECTION_REF
import com.example.employattendance.dataClass.EmployeeInfo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class LoginViewModel : ViewModel() {

    val _errorString = MutableLiveData<String>("")
    val errorString: LiveData<String> = _errorString

    val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error


    private val auth = FirebaseAuth.getInstance()
    //val user = auth.currentUser
    private val empRef: CollectionReference = Firebase.firestore.collection(EMPLOYEE_COLLECTION_REF)
//    var error = mutableStateOf(false)
    var userPrivilege = mutableStateOf("Employee")
    var loginAs = mutableStateOf("Employee")

//    init {
//        if()
//
//    }


    suspend fun fetchData(): EmployeeInfo {
        return try {
            empRef.document(getUserId()).get().await().toObject(EmployeeInfo::class.java) ?: EmployeeInfo()
        } catch (e: Exception) {
            throw e
        }
    }



    fun login(email: String, pass: String, navController: NavHostController, context: Context) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        userPrivilege.value = fetchData().userType
                        Log.d("TAG","Data fetch successfully")
                    }
                    withContext(Dispatchers.Main) {
                        when {
                            userPrivilege.value == "Employee" && loginAs.value == "Employee" -> navController.navigate(route = Screen.EmployeeLogin.route)
                            userPrivilege.value == "Admin" && loginAs.value == "Admin" -> navController.navigate(route = Screen.Attendance.route)
                            userPrivilege.value == "Admin" && loginAs.value == "Employee" -> navController.navigate(route = Screen.EmployeeLogin.route)
                            else -> {_error.value = true ;   _errorString.value = "Incorrect login Privileges"}
                        }
                    }
                }
            } else {
                _error.value = true
                _errorString.value = task.exception?.localizedMessage
                task.exception?.message
                Log.d("TAG", " Login UnSuccessful reason : ${errorString.value}")
            }
            val user = Firebase.auth.uid
            Log.d("TAG", " login successfully user = ${user}  , userPrivilege: ${userPrivilege.value}, loginAs: ${loginAs.value}")
        }
    }


    private fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()
}

//class LoginViewModel : ViewModel() {
//
//    private val _errorString = MutableLiveData<String>("")
//    val errorString: LiveData<String> get() = _errorString
//
//    val _error = MutableLiveData<Boolean>()
//    val error: LiveData<Boolean> get() = _error
//
//    private val auth = FirebaseAuth.getInstance()
//    private val empRef: CollectionReference = Firebase.firestore.collection(EMPLOYEE_COLLECTION_REF)
//
//    var userPrivilege = mutableStateOf("Employee")
//    var loginAs = mutableStateOf("Employee")
//
//    suspend fun fetchData(): EmployeeInfo {
//        return empRef.document(getUserId()).get().await().toObject(EmployeeInfo::class.java) ?: EmployeeInfo()
//    }
//
//    fun login(email: String, pass: String, navController: NavHostController,context:Context) {
//        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                viewModelScope.launch {
//                    userPrivilege.value = fetchData().userType
//                    Log.d("TAG","Data fetch successfully")
//                    Toast.makeText(context, "Wait logging in..", Toast.LENGTH_LONG).show()
//                    navigateBasedOnUserType(navController)
//                }
//            } else {
//                _error.value = true
//                _errorString.value = task.exception?.localizedMessage
//                Log.d("TAG", " Login UnSuccessful reason : ${errorString.value}")
//
//            }
//            Log.d("TAG", " login successfully user = ${auth.uid}  , userPrivilege: ${userPrivilege.value}, loginAs: ${loginAs.value}")
//        }
//    }
//
//    private fun navigateBasedOnUserType(navController: NavHostController) {
//        when {
//            loginAs.value == "Employee" -> navController.navigate(route = Screen.EmployeeLogin.route)
//            userPrivilege.value == "Admin" && loginAs.value == "Admin" -> navController.navigate(route = Screen.Attendance.route)
//            else -> {_error.value = true ;   _errorString.value = "Incorrect login Privileges"}
//        }
//    }
//
//    private fun getUserId(): String = auth.currentUser?.uid.orEmpty()
//}

