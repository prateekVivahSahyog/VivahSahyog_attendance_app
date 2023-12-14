package com.example.employattendance.AppUi.Employee.ViewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.employattendance.AppUi.Admin.Info
import com.example.employattendance.data.CreateUser
import com.example.employattendance.data.EMPLOYEE_COLLECTION_REF
import com.example.employattendance.dataClass.AttendanceRecord
import com.example.employattendance.dataClass.EmployeeInfo
import com.example.employattendance.dataClass.LeaveApplication
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ProfileViewModel:ViewModel() {

    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    private val empRef: CollectionReference = Firebase
        .firestore.collection(EMPLOYEE_COLLECTION_REF)

    val auth = FirebaseAuth.getInstance()

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    private val _name = MutableLiveData<String>()
    var name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    var email : LiveData<String> = _email

    private val _mobile = MutableLiveData<String>()
    var mobile : LiveData<String> = _mobile

    private val _designation = MutableLiveData<String>()
    var designation : LiveData<String> = _designation

    private val _dept = MutableLiveData<String>()
    var dept : LiveData<String> = _dept

    private val _userType = MutableLiveData<String>()
    var userType: LiveData<String> = _userType

    fun updateName(newName: String) {
        _name.value = newName
        Log.d("TAG","name = ${_name.value}")
    }

    fun updateEmail(newName: String) {
        _email.value= newName
        Log.d("TAG","email = ${_email.value}")
    }
    fun updateMobile(newName: String) {
        _mobile.value = newName
        Log.d("TAG","mobile = ${mobile.value}")
    }
    fun updateDesignation(newName: String) {
        _designation.value = newName
        Log.d("TAG","desig = ${designation.value}")
    }
    fun updateDept(newName: String){
        _dept.value = newName
        Log.d("TAG","dept = ${dept.value}")
    }

    fun logOut(){
        _logout.value = true
        viewModelScope.launch(Dispatchers.IO) {
            auth.signOut()
        }

    }

    fun save(name: String,mobile:String,designation:String,dept:String){

    Log.d("TAG","Save called")

        viewModelScope.launch(Dispatchers.IO) {

            val updated = hashMapOf(
                "firstName" to name,
                "number" to mobile,
                "position" to designation,
                "department" to dept
            )

            empRef.document(getUserId()).update(updated as Map<String,Any>).addOnSuccessListener {
                Log.d("TAG"," Profile Updated on Database")
            }.addOnFailureListener {
                Log.d("TAG"," Failed to Save profile due to ${it.message} ")
            }
        }
    }

        init {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val  info = FetchData()

                    withContext(Dispatchers.Main) {
                        _name.value= info.firstName
                        _email.value = info.email
                        _mobile.value = info.number
                        _designation.value = info.position
                        _dept.value = info.department
                        _userType.value= info.userType
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "Profile ViewModel : Error fetching data due to -> ", e)
                }
            }
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
                Log.e("TAG", "Profile ViewModel , Fetch Fun ->  Error fetching employee info", exception)
                continuation.resumeWithException(exception)
            }
    }


}





