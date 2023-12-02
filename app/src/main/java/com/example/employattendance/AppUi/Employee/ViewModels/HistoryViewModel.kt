package com.example.employattendance.AppUi.Employee.ViewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employattendance.data.ATTENDANCE_RECORDS_REF
import com.example.employattendance.dataClass.AttendanceRecord
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




class HistoryViewModel() : ViewModel() {

    val months = listOf(
        "",
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )

    val userId = Firebase.auth.currentUser?.uid.orEmpty()
    val attendanceRef = Firebase.firestore.collection(ATTENDANCE_RECORDS_REF)
    val userDocRef = attendanceRef.document(userId)
    val dailyCollectionRef = userDocRef.collection("Daily")
   // val recordsList = mutableListOf<AttendanceRecord>()
   private val _recordsList = mutableStateOf<List<AttendanceRecord>>(emptyList())
    val recordsList: State<List<AttendanceRecord>> = _recordsList

    val selectedYear = MutableLiveData<Int>()
    val selectedMonth = MutableLiveData<Int>()

    init {
        // Initialize the selected year and month to the current date
        val currentDate = LocalDateTime.now()
        selectedYear.value = currentDate.year
        selectedMonth.value = currentDate.monthValue
        // Fetch the attendance records for the current month
        fetchAttendanceRecords()
    }

    fun fetchAttendanceRecords() {

        viewModelScope.launch {

            val currentYear = selectedYear.value ?: 0
            val currentMonth = selectedMonth.value ?: 1

            Log.d("TAG","year = ${currentYear} , month = $currentMonth")

            val start = LocalDate.of(currentYear, currentMonth, 1)
            val end = start.plusMonths(1).minusDays(1) // Calculate the last day of the current month

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            // Format the dates as per your requirement
            val startString = start.format(formatter)
            val endString = end.format(formatter)

            Log.d("TAG", " month = $currentMonth , year = $currentYear , start = $startString and end = $endString")

           // withTimeout(5000){
//            withContext(Dispatchers.IO){
                dailyCollectionRef
                    .orderBy(FieldPath.documentId())
                    .startAt(startString)
                    .endAt(endString)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
//                        recordsList.clear()
                        if (querySnapshot.isEmpty) {
                            Log.d("TAG", "No Records Found")
                        } else {
                            for (document in querySnapshot) {
                               // val record = document.toObject(AttendanceRecord::class.java)
                                //recordsList.add(record)
                                _recordsList.value = querySnapshot.documents.map { document ->
                                    document.toObject(AttendanceRecord::class.java)!!
                                }
//                                Log.d("TAG", "records = $record")
                            }
                           // Log.d("TAG", "Record size = ${recordsList.size}")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("TAG", "Error getting documents: ", exception)
                    }
           // }

        }
    }
}


