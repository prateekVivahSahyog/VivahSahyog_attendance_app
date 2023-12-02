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
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    private val todayDate:String = dateFormat.format(Date())

    var data : AttendanceRecord = AttendanceRecord("","", checkIn = "    --/--", checkOut = "    --/--" , todayTime = "0", status = "Absent", action = 0)

    @SuppressLint("SuspiciousIndentation")
    fun fetchData(){

        attendanceRef.document(getUserId()).collection("Daily").document(todayDate).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val record = documentSnapshot.toObject(AttendanceRecord::class.java)
                if (record != null) {
                    data = record
                    Log.d("TAG","data = ${data.date} , ${data.checkIn}")
                } else {
                    Log.d("TAG", "Document exists but could not be converted to AttendanceRecord")
                }
            } else {
                attendanceRef.document(getUserId()).collection("Daily").document(todayDate).set(AttendanceRecord(date = todayDate)).addOnCompleteListener {
                    if (it.isSuccessful)
                        Log.d("TAG", "Check In successfully")
                }.addOnFailureListener {
                    Log.d("TAG", "Check In  Unsuccessfully due to ${it.cause}")
                }
            }
        }.addOnFailureListener {
            Log.d("TAG","Could not fetch data due to = ${it.cause}")
        }
    }


    fun checkIn(today:AttendanceRecord) {
            attendanceRef.document(getUserId()).collection("Daily").document(todayDate).set(today).addOnCompleteListener {
                if (it.isSuccessful)
                    Log.d("TAG", "Check In successfully")
            }.addOnFailureListener {
                Log.d("TAG", "Check In  Unsuccessfully due to ${it.cause}")
            }
   }

    @SuppressLint("SuspiciousIndentation")
    fun checkOut(
        outTime: String,
        todayWorkTime: String,
        status: String,
        workedTime:String
    ){
        val updated = hashMapOf(
            "checkOut" to outTime,
            "status" to status,
            "action" to 2,
            "todayTime" to todayWorkTime,
        )

        attendanceRef.document(getUserId()).collection("Daily").document(todayDate).update(updated as Map<String, Any>)

        // workedTime Update  Weekly wala
        val up2 = hashMapOf("workedTime" to workedTime )
        empRef.document(getUserId()).update(up2 as Map<String, Any>)
    }
    fun taskUpdate(tasks:String){

        val updated = hashMapOf(
            "taskPerformed" to tasks,
            "action" to 3
        )

        attendanceRef.document(getUserId()).collection("Daily").document(todayDate).update(updated as Map<String, Any>)
    }


}