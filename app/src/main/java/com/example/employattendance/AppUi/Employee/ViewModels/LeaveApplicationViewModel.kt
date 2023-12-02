package com.example.employattendance.AppUi.Employee.ViewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employattendance.data.EMPLOYEE_COLLECTION_REF
import com.example.employattendance.dataClass.AttendanceRecord
import com.example.employattendance.dataClass.LeaveApplication
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LeaveApplicationViewModel:ViewModel() {

    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    private val empRef: CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)



    fun LeaveApply(application: LeaveApplication){

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val todayDate = formatter.format(LocalDate.now())

        application.applicationDate= todayDate

        viewModelScope.launch(Dispatchers.IO) {
            empRef.document(getUserId()).collection("Leave Application").document(todayDate).set(application).addOnCompleteListener {
                if (it.isSuccessful)
                    Log.d("TAG", "leave Submitted")

                val updated = hashMapOf(
                    "notification" to true)
                empRef.document(getUserId()).update(updated as Map<String, Any>)

            }.addOnFailureListener {
                Log.d("TAG", "leave Submission Unsuccessfully due to ${it.cause}")
            }
        }
    }
}