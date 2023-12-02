package com.example.employattendance.dataClass

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Date


data class Employee(val id:Int,val name:String,val dept:String,var present:Boolean = false)

data class EmployeeInfo (                                               var empId:String="",//
                                                                        var employeePhoto:String ="",//
                            //Uri.parse("android.resource://com.example.employattendance/" + R.drawable.user)
                                                                        var firstName:String="",//
                                                                        var lastName:String="",//
                                                                        val email:String="",//
                                                                        var number:String="",//
                                                                        var department:String="",//
                                                                        var position:String="",//
                                                                        var joining:String="",//
                                                                        var userType:String = "Employee",//
                                                                        var lastInteractionDate: String = getMondayOfCurrentWeek().toString(),
                                                                        var workedTime: String = "0",
                                                                        var notification:Boolean = false//
){

    constructor() : this("", "", "","", "", "","", "", "","", getMondayOfCurrentWeek().toString(), "",false)

}

data class AttendanceRecord(
    val date:String="",
    val day:String="",
    val checkIn:String="    --/--",
    val checkOut:String="    --/--",
    var todayTime:String = "0",
    var status:String="Absent",
    var action:Int = 0,
    var taskPerformed:String = ""){
    constructor() : this("", "", "","", "", "",0,"")

}

data class LeaveApplication(
    var applicationDate:String="",
    var from :String="",
    var till : String="",
    var reason:String="",
    var status:String="Pending"
)


//data class EmployeesRecords(
//    val empId:String,
//    var employeeAttendanceRecords: List<AttendanceRecord>
//)

fun getMondayOfCurrentWeek(): LocalDate {
    val currentDate = LocalDate.now()
    val daysUntilMonday = (DayOfWeek.MONDAY.value - currentDate.dayOfWeek.value + 7) % 7
    return currentDate.minusDays(daysUntilMonday.toLong())
}


