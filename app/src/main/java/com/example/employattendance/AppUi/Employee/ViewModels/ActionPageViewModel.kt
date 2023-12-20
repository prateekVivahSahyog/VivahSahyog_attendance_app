package com.example.employattendance.AppUi.Employee.ViewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employattendance.data.AttendanceRecords
import com.example.employattendance.data.CreateUser
import com.example.employattendance.data.EMPLOYEE_COLLECTION_REF
import com.example.employattendance.dataClass.AttendanceRecord
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import java.time.temporal.ChronoUnit


class ActionViewModel(private val repository:AttendanceRecords = AttendanceRecords()

) : ViewModel() {


    private val empRef: CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)

    private val attendanceRef = Firebase.firestore.collection("AttendanceRecords")

    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    private val hasUser :Boolean
        get() = repository.hasUser()

    private val formatter = DateTimeFormatter.ofPattern("h:mm a")
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    @SuppressLint("SimpleDateFormat")
    private val ddFormat = SimpleDateFormat("dd")
    @SuppressLint("SimpleDateFormat")
    private val MMFormat = SimpleDateFormat("MM")
    @SuppressLint("SimpleDateFormat")
    private val YYYYFormat = SimpleDateFormat("yyyy")

    private  var elapsedTime:Long = 0L

    val date:String = dateFormat.format(Date())
    val dd = ddFormat.format(Date()).toInt()
    val mm = MMFormat.format(Date()).toInt()
    val yy = YYYYFormat.format(Date()).toInt()


    private val _action = MutableLiveData<Int>()
    val action: LiveData<Int> = _action

    private val _inTime = MutableLiveData<String>()
    val inTime: LiveData<String> = _inTime

    private val _outTime = MutableLiveData<String>()
    val outTime: LiveData<String> = _outTime

    private var startTime: LocalDateTime = LocalDateTime.now()
    var todayWorkingHours = mutableFloatStateOf(0f)
    var weeksWorkingHours = mutableFloatStateOf(0f)
    private var lastInteractionDate : String = LocalDate.now().toString()

    private val _absent = MutableLiveData<Int>()
    var absent :LiveData<Int> = _absent

    private val _workingDays = MutableLiveData<Int>()
    var workingDays :LiveData<Int> = _workingDays

    init {

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchData()


            val info = CreateUser().FetchData()

            withContext(Dispatchers.Main) {
                _inTime.value = repository.data.checkIn
                _outTime.value = repository.data.checkOut
                _action.value = repository.data.action
                weeksWorkingHours.floatValue = info.workedTime.toFloat()
                todayWorkingHours.floatValue = repository.data.todayTime.toFloat()
                lastInteractionDate = info.lastInteractionDate
                calculateWorkingDays()
            }

            resetWeeksWorkingHoursIfNeeded()
            absent()

            while (true) {

                if (action.value == 1) {

                    withContext(Dispatchers.IO){
                        val current = LocalDateTime.now()
                        val checkInTime = LocalTime.parse(inTime.value, formatter)
                        val checkOutTime = LocalTime.parse(formatTime(current),formatter)
                        elapsedTime =  ChronoUnit.MINUTES.between(checkInTime, checkOutTime)

                    }

                    withContext(Dispatchers.Main) {
                        todayWorkingHours.floatValue = String.format("%.1f", elapsedTime/60.0).toFloat()
                    }
                }
                delay(1000L)
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun checkIn() {
        if(hasUser){

            startTime = LocalDateTime.now()
            _inTime.value = formatTime(startTime)
            _action.value = 1
            val day = getDayOfWeekAbbreviated(dd,mm,yy)
            val u = inTime.value?.let {
                outTime.value?.let { it1 ->
                    AttendanceRecord(
                        date,
                        day,
                        it,
                        it1,
                        todayWorkingHours.floatValue.toString(),"Absent",1)
                }
            }
            if (u != null) {
                repository.checkIn(u)
            }
        }
    }

    fun checkOut() {

        if(hasUser){

            weeksWorkingHours.floatValue = (todayWorkingHours.floatValue + weeksWorkingHours.floatValue)
            _outTime.value = formatTime(LocalDateTime.now())
            _action.value = 2
            outTime.value?.let { repository.checkOut(it,todayWorkingHours.floatValue.toString(),"Present",weeksWorkingHours.floatValue.toString()
            ) }
        }
    }



    private fun formatTime(dateTime: LocalDateTime): String {
        return try {
            dateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
        } catch (e: DateTimeException) {
            "Invalid Time"
        }
    }

    private fun getDayOfWeekAbbreviated(date: Int, month: Int, year: Int): String {
        val formatter = DateTimeFormatter.ofPattern("d-M-yyyy",Locale.ENGLISH)
        val dateString = "$date-$month-$year"
        val localDate = LocalDate.parse(dateString, formatter)
        return localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    }

    private fun resetWeeksWorkingHoursIfNeeded() {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()
        val l = LocalDate.parse(lastInteractionDate, formatter)
        val daysBetween = ChronoUnit.DAYS.between(  l , currentDate)
        Log.d("TAG","currentDate = $currentDate , daysBetween = $daysBetween ")

        if (daysBetween >= 7 ) {
            val i = getMondayOfCurrentWeek().toString()
            weeksWorkingHours.floatValue = 0f
            lastInteractionDate = i
            Log.d("TAG","get current monday = $i")

            val updated = hashMapOf(
                "workedTime" to weeksWorkingHours.floatValue.toString(),
                "lastInteractionDate" to i
            )

            empRef.document(getUserId()).update(updated as Map<String, Any>)
        }

    }

    fun tasks(value:String){
        repository.taskUpdate(value)
    }

private fun getMondayOfCurrentWeek(): LocalDate {
    val currentDate = LocalDate.now() // Gets the current date
    val daysFromMonday = currentDate.dayOfWeek.value - DayOfWeek.MONDAY.value
    val monday = if (daysFromMonday >= 0) currentDate.minusDays(daysFromMonday.toLong()) else currentDate.minusDays((daysFromMonday + 7).toLong())
    return monday
}


    suspend fun absent() {
        var daysAbsent = 0

        val dd = ddFormat.format(Date()).toInt()
        val mm = MMFormat.format(Date()).toInt()
        val yy = YYYYFormat.format(Date()).toInt()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for (i in 1..dd) {
            val day = if (i < 10) "0$i" else i.toString()  // Fix: Use i.toString() instead of dd
            val selectedDate = "$yy-$mm-$day"
            Log.d("TAG", "checking for $selectedDate")  // Added log statement
            val date = LocalDate.parse(selectedDate, formatter)

            // Exclude Saturdays and Sundays
            if (date.dayOfWeek != DayOfWeek.SATURDAY && date.dayOfWeek != DayOfWeek.SUNDAY) {
                val attendanceDocRef = attendanceRef.document(getUserId()).collection("Attendance").document(selectedDate)
                val attendanceDocument = attendanceDocRef.get().await()
                val attendanceRecord = attendanceDocument?.toObject(AttendanceRecord::class.java)

                if (attendanceRecord == null ) {
                    daysAbsent++
                    Log.d("TAG", "absent date = $selectedDate")
                }
                else{
                    if(attendanceRecord.status !="Present"){
                        daysAbsent++
                        Log.d("TAG", "absent date = $selectedDate")
                    }
                }
            }
        }

        Log.d("TAG","absent days = $daysAbsent")

        withContext(Dispatchers.Main) {
            _absent.value = daysAbsent
        }
    }

    fun calculateWorkingDays(){
        val currentDate = LocalDate.now()
        val firstDayOfMonth = YearMonth.now().atDay(1)
        var workingDays = 0
        var date = firstDayOfMonth

        while (date.isBefore(currentDate) || date.isEqual(currentDate)) {
            if (date.dayOfWeek != DayOfWeek.SATURDAY && date.dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays++
            }
            date = date.plusDays(1)
        }
        Log.d("TAG","working days = $workingDays")
        _workingDays.value =  workingDays
    }

    }


