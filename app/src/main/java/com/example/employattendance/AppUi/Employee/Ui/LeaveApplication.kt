package com.example.employattendance.AppUi.Employee.Ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.employattendance.AppUi.Employee.ViewModels.LeaveApplicationViewModel
import com.example.employattendance.dataClass.LeaveApplication

@Composable

fun LeaveApplicationPage(viewModel: LeaveApplicationViewModel= LeaveApplicationViewModel()){

    var fromDate by remember { mutableStateOf("") }
    var fromButton by remember { mutableStateOf(false) }
    var tillDate by remember { mutableStateOf("") }
    var tillButton by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }
    val context = LocalContext.current

    var application: LeaveApplication? = null

    if (fromButton) {
        DatePickerEmp { fromDate = it }
    }

    if (tillButton) {
        DatePickerEmp { tillDate = it }
    }

    LaunchedEffect(tillDate , fromDate, reason) {

        application = LeaveApplication(from = fromDate, till = tillDate, reason = reason)
        Log.d("TAG", "Leave from($fromDate) - till($tillDate) , reason : $reason   and $application")
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 15.dp, vertical = 20.dp), verticalArrangement = Arrangement.Top) {

        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
            Button(onClick = { fromButton = true; tillButton = false },
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(if (fromDate == "") "   From     " else fromDate)
            }
            Spacer(Modifier.width(30.dp))

            Button(onClick = { tillButton = true; fromButton = false },
                shape = RoundedCornerShape(5.dp)) {
                Text(if (tillDate == "") "   Till     " else tillDate)
            }
        }

        Spacer(Modifier.height(25.dp))

        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it ;},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Reason") },
            maxLines = 10
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {

            Button(onClick = { fromDate="";tillDate="";reason="";application=null }, shape = RoundedCornerShape(5.dp)){
                Text("Clear")
            }

            Spacer(Modifier.width(30.dp))

            Button(onClick = {
                viewModel.LeaveApply(application!!)
                Toast.makeText(context, "Application Submitted", Toast.LENGTH_SHORT).show()
                fromDate="";tillDate="";reason="";application=null
                             }
                , shape = RoundedCornerShape(5.dp), enabled = fromDate!="" && tillDate !="" && reason !="" ){
                Text("Submit")

            }
        }

    }

}