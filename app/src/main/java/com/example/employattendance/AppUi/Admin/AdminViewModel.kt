package com.example.employattendance.AppUi.Admin

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.employattendance.Navigation.Screen
import com.example.employattendance.data.EMPLOYEE_COLLECTION_REF
import com.example.employattendance.dataClass.AttendanceRecord
import com.example.employattendance.dataClass.EmployeeInfo
import com.example.employattendance.dataClass.LeaveApplication
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class AdminViewModel() : ViewModel(){

    val auth = FirebaseAuth.getInstance()

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> get() = _logout

    private val _employeeList = MutableLiveData<List<Info>>()
    val employeeList: LiveData<List<Info>> get() = _employeeList

    private val empRef: CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)


    val d = Instant.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault())


    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    val attendanceRef = Firebase.firestore.collection("AttendanceRecords")

    lateinit var sheet: Sheet
//    private val EXCEL_SHEET_NAME = " Attendance_${selectedDate.value}"
    private var cell: Cell? = null
    val workbook: Workbook = HSSFWorkbook()
  //  var filename = "Attendance_${selectedDate.value}.xls"

    init {
        viewModelScope.launch {
            _selectedDate.value = formatter.format(d)
            withContext(Dispatchers.IO) {
                fetchEmployees()
            }
        }
    }

    fun logOut(){
        auth.signOut()
        _logout.value = true

    }



    fun updateDate(new:String){
        _selectedDate.value = new
    }


    fun fetchEmployees() {
        viewModelScope.launch {
            val querySnapshot = empRef.orderBy(FieldPath.documentId()).get().await()
            val resultList = mutableListOf<Info>()

            for (document in querySnapshot) {
                val employeeRecord = document.toObject(EmployeeInfo::class.java)

                val attendanceDocRef =
                    selectedDate.value?.let {
                        attendanceRef.document(employeeRecord.empId).collection("Daily").document(
                            it
                        )
                    }
                val attendanceDocument = attendanceDocRef?.get()?.await()
                val attendanceRecord = attendanceDocument?.toObject(AttendanceRecord::class.java)

                if (attendanceRecord != null) {
                    resultList.add(Info(employeeRecord, attendanceRecord))
                } else {
                    val newAttendanceRecord = selectedDate.value?.let {
                        AttendanceRecord(
                            it,
                            day = getDayOfWeekAbbreviated(selectedDate.value!!),
                            checkIn = "    --/--",
                            checkOut = "    --/--",
                            todayTime = "0",
                            status = "Absent",
                            action = 0,
                        )
                    }
                    newAttendanceRecord?.let { Info(employeeRecord, it) }
                        ?.let { resultList.add(it) }
                }
            }

            _employeeList.value = resultList
            Log.d("TAG", "_employeeList  for ${selectedDate.value}= ${_employeeList.value}")
        }
    }

    fun createExcelWorkbook(FileName:String) {
        // New Workbook

        cell = null

        // Cell style for header row
        val cellStyle = workbook.createCellStyle()
        cellStyle.fillForegroundColor = HSSFColor.AQUA.index
        cellStyle.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
        cellStyle.alignment = CellStyle.ALIGN_CENTER

        // New Sheet
        sheet = workbook.createSheet(FileName)

        sheet.setColumnWidth(0, (15 * 350));
        sheet.setColumnWidth(1, (15 * 500));
        sheet.setColumnWidth(2, (15 * 600));
        sheet.setColumnWidth(3, (15 * 200));
        sheet.setColumnWidth(4, (15 * 200));
        sheet.setColumnWidth(5, (15 * 300));
        sheet.setColumnWidth(6, (15 * 200));
        sheet.setColumnWidth(7, (50 * 600))
        sheet.setColumnWidth(8, (15 * 400));


        var index = 0
        var row = sheet.createRow(index++)

        cell = row.createCell(0)
        if (cell is Cell) {
            cell!!.setCellValue("Name")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(1)
        if (cell is Cell) {
            cell!!.setCellValue("Email")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(2)
        if (cell is Cell) {
            cell!!.setCellValue("EmployeeID")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(3)
        if (cell is Cell) {
            cell!!.setCellValue("CheckIn")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(4)
        if (cell is Cell) {
            cell!!.setCellValue("CheckOut")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(5)  ///
        if (cell is Cell) {
            cell!!.setCellValue("TodayWorkingHours")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(6)///
        if (cell is Cell) {
            cell!!.setCellValue("Attendance")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(7)  ///
        if (cell is Cell) {
            cell!!.setCellValue("Tasks Performed")
            cell!!.cellStyle = cellStyle
        }

        cell = row.createCell(8)  ///
        if (cell is Cell) {
            cell!!.setCellValue("WeeklyWorkingHours")
            cell!!.cellStyle = cellStyle
        }





        for (data in employeeList.value!!){
            row = sheet.createRow(index++)

            cell = row.createCell(0)
            (cell)?.setCellValue("${data.employeeInfo.firstName} ${data.employeeInfo.lastName}")

            cell = row.createCell(1)
            (cell)?.setCellValue(data.employeeInfo.email)

            cell = row.createCell(2)
            (cell)?.setCellValue(data.employeeInfo.empId)

            cell = row.createCell(3)
            (cell)?.setCellValue(data.attendanceRecord.checkIn)

            cell = row.createCell(4)
            (cell)?.setCellValue(data.attendanceRecord.checkOut)

            cell = row.createCell(5)
            (cell)?.setCellValue(data.attendanceRecord.todayTime)

            cell = row.createCell(6)
            (cell)?.setCellValue(data.attendanceRecord.status)

            cell = row.createCell(7)
            (cell)?.setCellValue(data.attendanceRecord.taskPerformed)

            cell = row.createCell(8)
            (cell)?.setCellValue(data.employeeInfo.workedTime)




        }
    }

    fun saveWorkbookToStorage(context: Context,FileName:String) {
        createExcelWorkbook(FileName)

        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FileName)
            val fileOut = FileOutputStream(file)
            workbook.write(fileOut)
            fileOut.close()
            // Notify the MediaScanner about the new file so that it appears in the device's file system
            MediaScannerConnection.scanFile(
               context, arrayOf(file.toString()), null, null
            )

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun updateAttendance(status: String, empid:String) {
        val updated = hashMapOf(
            "status" to status
        )
        selectedDate.value?.let {
            attendanceRef.document(empid).collection("Daily").document(it).update(updated as Map<String, Any>)
        }
    }

}

    private fun getDayOfWeekAbbreviated(selectedDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
        val localDate = java.time.LocalDate.parse(selectedDate, formatter)
        return localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    }

data class Info(val employeeInfo: EmployeeInfo,val attendanceRecord: AttendanceRecord )


