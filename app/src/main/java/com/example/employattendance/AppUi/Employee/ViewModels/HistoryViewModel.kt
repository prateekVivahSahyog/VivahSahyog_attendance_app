package com.example.employattendance.AppUi.Employee.ViewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employattendance.data.ATTENDANCE_RECORDS_REF
import com.example.employattendance.dataClass.AttendanceRecord
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
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

    val currentDate = LocalDateTime.now()

    val userId = Firebase.auth.currentUser?.uid.orEmpty()
    val attendanceRef = Firebase.firestore.collection(ATTENDANCE_RECORDS_REF)
    val userDocRef = attendanceRef.document(userId)



   private val _recordsList = mutableStateOf<List<AttendanceRecord>>(emptyList())
    val recordsList: State<List<AttendanceRecord>> = _recordsList

    private val _selectedYear = MutableStateFlow<Int?>(currentDate.year)
    val selectedYear: StateFlow<Int?> = _selectedYear
    private val _selectedMonth = MutableStateFlow<Int?>(currentDate.monthValue)
    val selectedMonth: StateFlow<Int?> = _selectedMonth

    fun updateYearMonth(year:Int, month:Int){
        _selectedYear.value = year
        _selectedMonth.value = month
        Log.d("TAG","updated _Year = ${_selectedYear.value}")
        Log.d("TAG","updated Month = ${selectedMonth.value}")

        //fetchAttendanceRecords()

        Log.d("TAG"," after fetching updated _Year = ${_selectedYear.value}")
        Log.d("TAG"," after fetching updated Month = ${selectedMonth.value}")
    }


    init {
        fetchAttendanceRecords()
    }


    fun fetchAttendanceRecords() {
        val year = selectedYear.value!!
        val month = if(selectedMonth.value!!>9 ) selectedMonth.value else "0${selectedMonth.value}"

        Log.d("TAG","Fetch data for year $year and month $month ")
        viewModelScope.launch {
            // Get a reference to the user's attendance records
            val attendanceRecordsRef = attendanceRef.document(userId).collection("Attendance")

            // Get the number of days in the month
            val daysInMonth = YearMonth.of(year, selectedMonth.value!!).lengthOfMonth()

            // Create a List to store the attendance records
            val deferredRecords = (1..daysInMonth).map { day ->
                async {
                    val dayStr = String.format("%02d", day) // Ensure two-digit day representation
                    val documentId = "$year-$month-$dayStr"
                    Log.d("TAG", "fetching records of  $documentId")

                    try {
                        val documentSnapshot = attendanceRecordsRef.document(documentId).get().await()

                        if (documentSnapshot.exists()) {
                            val record = documentSnapshot.toObject(AttendanceRecord::class.java)
                            if (record != null) {
                                Log.d("TAG", "Record for day $day = $record")
                                record
                            } else {
                                null
                            }
                        } else {
                            Log.d("TAG", "No Record Found for day $day")
                            null
                        }
                    } catch (exception: Exception) {
                        Log.d("TAG", "Error getting document for day $day: ", exception)
                        null
                    }
                }
            }

            // Wait for all async operations to complete
            val recordsList = deferredRecords.awaitAll().filterNotNull()

            // Update _recordsList with the list of attendance records
            withContext(Dispatchers.Main) {
                _recordsList.value = recordsList
                Log.d("TAG", "record_list = $recordsList")
            }
        }
    }

}


