package com.example.employattendance.AppUi.AppEntryPoint.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.employattendance.Navigation.Screen
import com.example.employattendance.data.CreateUser
import com.example.employattendance.dataClass.EmployeeInfo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Date

class SignUpViewModel  : ViewModel(){

    val user = CreateUser()
    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    val _firstName  = MutableLiveData<String>("")
    val firstName : LiveData<String> = _firstName

    val _lastName  = MutableLiveData<String>("")
    val lastName :MutableLiveData<String> = _lastName

    val _email =  MutableLiveData<String>("")
    val email = _email

    val _password =  MutableLiveData<String>("")
    val password = _password

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val today  = dateFormat.format(Date())


    fun signUp(navController: NavHostController)
    {
        val auth = FirebaseAuth.getInstance()

        email.value?.let {
            password.value?.let { it1 ->
                auth
                    .createUserWithEmailAndPassword(it, it1)
                    .addOnCompleteListener {
                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                            Log.d("TAG"," Email Verification sent ")
                            firstName.value?.let { it1 -> lastName.value?.let { it2 -> email.value?.let { it3 -> EmployeeInfo(getUserId(), firstName = it1, lastName = it2, email = it3, joining = today ) } } }
                                ?.let { it2 -> user.MakeUser(it2) }
                            navController.navigate(route = Screen.Verification.route)
                        }?.addOnFailureListener {
                            Log.d("TAG"," Error in  sending Email Verification")
                        }
                        Log.d("TAG"," SignUP Successful ")
                    }
                    .addOnFailureListener {
                        Log.d("TAG"," SignUP UnSuccessful") }
            }
        }
    }
}