package com.example.employattendance.data

import android.annotation.SuppressLint
import android.net.Uri
import com.example.employattendance.dataClass.EmployeeInfo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date


const val EMPLOYEE_COLLECTION_REF = "EmployeeInfo"

//class EmployeeRepository (){
//
//    val user = Firebase.auth.currentUser
//    fun hasUser():Boolean = Firebase.auth.currentUser !=null
//    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()
//
//    private val employeeRef: CollectionReference =Firebase
//        .firestore.collection(EMPLOYEE_COLLECTION_REF)
//
//    fun getUserInfo(empId:String,
//                    onError:(Throwable?)->Unit,
//                    onSuccess:(EmployeeInfo)->Unit){
//
//        employeeRef.document(empId).get().addOnSuccessListener {
//            it.toObject(EmployeeInfo::class.java)?.let { it1 -> onSuccess.invoke(it1) }
//        }.addOnFailureListener{
//            onError.invoke(it.cause)
//        }
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    fun addEmployee(employeePhoto: Uri?,
//                    firstName:String?,
//                    lastName:String?,
//                    email:String?,
//                    number:Number?,
//                    department:String?,
//                    position:String?,
//                    onComplete:(Boolean)->Unit
//    ){
//        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
//        val joining = dateFormat.format(Date())
//        val id = employeeRef.document().id
//
////        val emp = EmployeeInfo(empId = id,employeePhoto, firstName, lastName, email, number, department, position, joining)
////        employeeRef.document(id).set(emp).addOnCompleteListener { res->
////             onComplete.invoke(res.isSuccessful)
////        }
//    }
//
//
//    fun deleteEmployee(id:String,onComplete:(Boolean)->Unit){
//        employeeRef.document(id).delete().addOnCompleteListener {
//            onComplete.invoke(it.isSuccessful)
//        }
//    }
//
//
//    fun updateEmployee(id:String,
//                       employeePhoto: Uri,
//                       firstName:String,
//                       lastName:String,
//                       number:Number,
//                       department:String,
//                       position:String,
//                       onResult:(Boolean)->Unit){
//
//
//                        val updated = hashMapOf(
//                            "employeePhoto" to employeePhoto,
//                            "firstName" to firstName,
//                            "lastName" to lastName,
//                            "number" to number,
//                            "department" to department,
//                            "position" to position
//                        )
//
//                        employeeRef.document(id).update(updated).addOnCompleteListener {
//                            onResult(it.isSuccessful)
//                        }
//                    }
//
//}
//
//sealed class Resources<T>(val data:T?= null,val throwable: Throwable? = null){
//    class Loading<T>:Resources<T>()
//    class Success<T>(data:T?):Resources<T>(data = data)
//    class Error<T>(throwable: Throwable?):Resources<T>(throwable = throwable)
//}
