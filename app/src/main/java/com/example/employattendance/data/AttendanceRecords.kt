package com.example.employattendance.data

import android.annotation.SuppressLint
import android.util.Log
import com.example.employattendance.dataClass.AttendanceRecord
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

const val  ATTENDANCE_RECORDS_REF = "AttendanceRecords"

class AttendanceRecords() {

    val user = Firebase.auth.currentUser
    fun hasUser():Boolean = Firebase.auth.currentUser !=null
    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    private val attendanceRef: CollectionReference =Firebase
        .firestore.collection(ATTENDANCE_RECORDS_REF)

    private val empRef: CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)

    @SuppressLint("SimpleDateFormat")
    val todayDate = SimpleDateFormat("yyyy-MM-dd").format(Date())

    var data : AttendanceRecord = AttendanceRecord("","", checkIn = "    --/--", checkOut = "    --/--" , todayTime = "0", status = "Absent", action = 0)



    fun fetchData() {
        try {
            // Assuming you still use the same date format for your documents


            attendanceRef.document(getUserId())
                .collection("Attendance")
                .document(todayDate)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val record = documentSnapshot.toObject(AttendanceRecord::class.java)
                        if (record != null) {
                            data = record
                            Log.d("TAG", "Data = ${data.date}, ${data.checkIn}")
                        } else {
                            Log.d("TAG", "Document exists but could not be converted to AttendanceRecord")
                        }
                    } else {
                        // If the document does not exist, create a new one

                        attendanceRef.document(getUserId())
                            .collection("Attendance")
                            .document(todayDate)
                            .set(data)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Log.d("TAG", "Document created successfully")
                                } else {
                                    Log.d("TAG", "Document creation unsuccessful")
                                }
                            }
                            .addOnFailureListener {
                                Log.e("TAG", "Document creation failed due to: ${it.message}", it)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("TAG", "Could not fetch data due to: ${exception.message}", exception)
                }
        } catch (e: Exception) {
            Log.e("TAG", "Error during data fetching: ${e.message}", e)
        }
    }


    fun checkIn(today: AttendanceRecord) {
        try {


            attendanceRef.document(getUserId())
                .collection("Attendance")
                .document(todayDate)
                .set(today)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("TAG", "Check In successful")
                    } else {
                        Log.d("TAG", "Check In unsuccessful")
                    }
                }
                .addOnFailureListener {
                    Log.e("TAG", "Check In failed due to: ${it.message}", it)
                }
        } catch (e: Exception) {
            Log.e("TAG", "Error during check-in: ${e.message}", e)
        }
    }

fun checkOut(outTime: String, todayWorkTime: String, status: String, workedTime: String) {
    try {
        val updated = hashMapOf(
            "checkOut" to outTime,
            "status" to status,
            "action" to 2,
            "todayTime" to todayWorkTime,
        )

        // Update checkout information in AttendanceRecords collection
        attendanceRef.document(getUserId())
            .collection("Attendance")
            .document(todayDate)
            .update(updated as Map<String, Any>)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "Check Out successfully")
                } else {
                    Log.d("TAG", "Check Out unsuccessful")
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Check Out failed due to: ${it.message}", it)
            }

        // Update worked time in the separate document (assuming a document structure for employees)
        val workedTimeUpdate = hashMapOf("workedTime" to workedTime)
        empRef.document(getUserId())
            .update(workedTimeUpdate as Map<String, Any>)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "Worked Time updated successfully")
                } else {
                    Log.d("TAG", "Worked Time update unsuccessful")
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Worked Time update failed due to: ${it.message}", it)
            }
    } catch (e: Exception) {
        Log.e("TAG", "Error during check-out: ${e.message}", e)
    }
}


    fun taskUpdate(tasks:String){

        val updated = hashMapOf(
            "taskPerformed" to tasks,
            "action" to 3
        )

        attendanceRef.document(getUserId())
            .collection("Attendance")
            .document(todayDate)
            .update(updated as Map<String, Any>)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "Check Out successfully")
                } else {
                    Log.d("TAG", "Check Out unsuccessful")
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Check Out failed due to: ${it.message}", it)
            }
    }

}