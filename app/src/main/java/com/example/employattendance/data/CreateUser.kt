package com.example.employattendance.data

import android.util.Log
import com.example.employattendance.dataClass.EmployeeInfo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val  EMPLOYEE_INFO_RECORDS_REF = "EmployeeInfo"

class CreateUser {

    val user = Firebase.auth.currentUser
    fun hasUser():Boolean = Firebase.auth.currentUser !=null
    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()
    private val empRef:CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)


    fun MakeUser(emp:EmployeeInfo){
        empRef.document(getUserId()).set(emp)
    }

    fun UpdateUser(name: String, email: String, mobile: String, designation: String, dept: String){

        val updated = hashMapOf(
            "firstName" to name,
            "email" to email,
            "number" to mobile,
            "position" to designation,
            "department" to dept
        )
        empRef.document(getUserId()).update(updated as Map<String,Any>)
        Log.d("TAG"," save fun called")
    }

    suspend fun FetchData(): EmployeeInfo = suspendCancellableCoroutine { continuation ->
        var data = EmployeeInfo()
        empRef.document(getUserId()).get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val record = it.toObject(EmployeeInfo::class.java)
                    if(record != null){
                        data = record
                    }
                }
                continuation.resume(data)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

}