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
                                                                        var lastInteractionDate: String = getMondayOfCurrentWeek(),
                                                                        var workedTime: String = "0",
                                                                        var notification:Boolean = false//
){

    constructor() : this("", "", "","", "", "","", "", "","", getMondayOfCurrentWeek(), "",false)

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



private fun getMondayOfCurrentWeek(): String {
    val currentDate = LocalDate.now() // Gets the current date
    val daysFromMonday = currentDate.dayOfWeek.value - DayOfWeek.MONDAY.value
    val monday = if (daysFromMonday >= 0) currentDate.minusDays(daysFromMonday.toLong()) else currentDate.minusDays((daysFromMonday + 7).toLong())
    return monday.toString()
}

