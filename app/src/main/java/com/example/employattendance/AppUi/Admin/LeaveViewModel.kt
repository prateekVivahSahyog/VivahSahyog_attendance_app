package com.example.employattendance.AppUi.Admin

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employattendance.data.EMPLOYEE_COLLECTION_REF
import com.example.employattendance.dataClass.LeaveApplication
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaveViewModel(private val userID: String):ViewModel() {

    private val empRef: CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)

    private val _recordsList = mutableStateOf<List<LeaveApplication>>(emptyList())
    val recordsList: State<List<LeaveApplication>> = _recordsList

    init {

        fetchLeaveApplications()
    }

    fun fetchLeaveApplications() {

        Log.d("TAG", "Fetching leave applications")

        _recordsList.value = emptyList()

        viewModelScope.launch(Dispatchers.IO) {

            empRef.document(userID).collection("Leave Application")
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    Log.d("TAG", "Documents fetched successfully")
                    val list = documents.map { document ->
                        Log.d("TAG", "Document ${document.id} data: ${document.data}")
                        document.toObject(LeaveApplication::class.java)
                    }
                    _recordsList.value = list
                    Log.d("TAG", "Records list updated")
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents: ", exception)
                }

                val updated = hashMapOf(
                "notification" to false)
                empRef.document(userID).update(updated as Map<String, Any>)
        }
    }

    fun updateStatus(value:String,documentId:String){
        val updated = hashMapOf(
            "status" to value)
        viewModelScope.launch(Dispatchers.IO) {
            empRef.document(userID).collection("Leave Application").document(documentId).update(updated as Map<String,Any>)
        }
    }
}