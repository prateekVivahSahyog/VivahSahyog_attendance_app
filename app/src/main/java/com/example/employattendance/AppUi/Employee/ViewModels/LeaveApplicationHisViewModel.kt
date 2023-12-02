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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LeaveApplicationHisViewModel:ViewModel() {

    fun getUserId():String {
        val userId = Firebase.auth.currentUser?.uid.orEmpty()
        Log.d("TAG", "Current user ID: $userId")
        return userId
    }

    private val empRef: CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)

    private val _recordsList = mutableStateOf<List<LeaveApplication>>(emptyList())
    val recordsList: State<List<LeaveApplication>> = _recordsList

    init {
        Log.d("TAG", "ViewModel initialized")
        fetchLeaveApplications()
    }



    fun fetchLeaveApplications() {
        Log.d("TAG", "Fetching leave applications")
        viewModelScope.launch(Dispatchers.IO) {
            empRef.document(getUserId()).collection("Leave Application")
                .get()
                .addOnSuccessListener { documents ->
                    Log.d("TAG", "Documents fetched successfully")
                    val list = documents.map { document ->
                        Log.d("TAG", "Document ${document.id} data: ${document.data}")
                        document.toObject(LeaveApplication::class.java)
                        //val leaveApplication = document.toObject(LeaveApplication::class.java)
                        // Assuming LeaveApplication has a date field
//                        leaveApplication.applicationDate = document.id
//                        leaveApplication
                    }
                    _recordsList.value = list
                    Log.d("TAG", "Records list updated")
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents: ", exception)
                }
        }
    }
}

