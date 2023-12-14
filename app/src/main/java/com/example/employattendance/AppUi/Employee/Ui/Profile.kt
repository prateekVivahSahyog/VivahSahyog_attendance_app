package com.example.employattendance.AppUi.Employee.Ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.provider.Settings.Global
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.employattendance.R
import com.example.employattendance.AppUi.Employee.ViewModels.ProfileViewModel
import com.example.employattendance.ui.theme.Iris
import com.example.employattendance.ui.theme.blue1

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.employattendance.AppUi.Admin.DatePicker
import com.example.employattendance.Navigation.Screen
import com.example.employattendance.dataClass.LeaveApplication
import com.example.employattendance.ui.theme.CustomRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun Profile(navController: NavHostController,
                    profile: ProfileViewModel
) {
        var edit by remember { mutableStateOf(false) }

        // Employee details
        val name by profile.name.observeAsState()
        val email by profile.email.observeAsState()
        val mobile by  profile.mobile.observeAsState()
        val designation by  profile.designation.observeAsState()
        val dept by profile.dept.observeAsState()
        var leaveButton by remember { mutableStateOf(false) }
        var saveVal by remember { mutableIntStateOf(0) }
        var isLeaveDialogOpen by remember { mutableStateOf(false) }
        val logOut by  remember { mutableStateOf(profile.logout.value) }
        val userType by profile.userType.observeAsState()

        val imagePainter: Painter = rememberAsyncImagePainter(model = R.drawable.user)

        LaunchedEffect(saveVal){

                if(saveVal==1)
                        edit = true
                else if(saveVal==2)
                { profile.save(name?:"",mobile?:"",designation?:"",dept?:"") }
                 saveVal %= 2
        }

        if(logOut==true){
                navController.navigateUp()
        }

        Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
        ) {

                Spacer(Modifier.height(30.dp))

                Row(Modifier.fillMaxWidth()) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                                Text(
                                        "Profile",
                                        Modifier.padding(horizontal = 10.dp),
                                        color = Color(20, 143, 119),
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold
                                )
                        }
                }

                Card(
                        Modifier
                                .size(320.dp, if (edit) 450.dp else 340.dp)
                                .padding(vertical = 10.dp)
                                .clip(RoundedCornerShape(15.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(200, 230, 201)),
                        elevation = CardDefaults.elevatedCardElevation(7.dp)
                ) {

                        Box(
                                Modifier.fillMaxSize().padding(top = 10.dp),
                                contentAlignment = Alignment.Center
                        ) {

                                IconButton(
                                        onClick = { edit = !edit
                                                saveVal++ },
                                        Modifier.align(Alignment.TopEnd),
                                ) {
                                        if (edit)
                                                Icon(Icons.Filled.Check, null, tint = Iris)
                                        else
                                                Icon(Icons.Filled.Edit, null, tint = Iris)
                                }

                                Image(
                                        painter = imagePainter,
                                        contentDescription = null,
                                        modifier = Modifier
                                                .size(150.dp, 150.dp)
                                                .border(2.dp, blue1, CircleShape)
                                                .align(Alignment.TopCenter)
                                                .clip(CircleShape),
                                        contentScale = ContentScale.Fit
                                )

                                Column(
                                        Modifier.align(Alignment.BottomCenter)
                                             //  .padding(top = 10.dp, bottom = 10.dp)
                                ) {
                                        EditableText(
                                                name?: "", "Name", edit
                                        ) { profile.updateName(it) }

                                        EditableText(
                                                email ?:"",
                                                "Email",
                                                edit
                                        ) { profile.updateEmail(it) }


                                        EditableText(
                                                mobile?:"",
                                                "Mobile",
                                                edit,
                                                maxLength = 10
                                        ) { profile.updateMobile(it) }

                                        EditableText(
                                                designation?:"",
                                                "Designation",
                                                edit
                                        ) { profile.updateDesignation(it) }

                                        EditableText(
                                                dept?:"",
                                                "Department",
                                                edit
                                        ) { profile.updateDept(it) }
                                }
                        }
                }



                if (leaveButton && !edit) {
                        isLeaveDialogOpen = true
                }

                if (isLeaveDialogOpen) {
                        navController.navigate(route = Screen.LeaveRequest.route)
                        isLeaveDialogOpen = false
                        leaveButton = false
                }


                if (!edit) {

                        Button(onClick = { leaveButton = true }, Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = ButtonDefaults.buttonElevation(10.dp)){
                                Text("Apply Leave")
                        }

                        Spacer(Modifier.height(10.dp))

                        Button(onClick = { navController.navigate(Screen.Attendance.route){popUpTo("Attendance") { inclusive = true }} },
                                Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
                                enabled = userType =="Admin",
                                shape = RoundedCornerShape(10.dp),
                                elevation = ButtonDefaults.buttonElevation(10.dp)
                        ) {
                                Box(
                                        Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text("Switch Account")
                                }
                        }

                        Spacer(Modifier.height(30.dp))

                        Button(
                                onClick = {profile.logOut() },
                                Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 50.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = ButtonDefaults.buttonElevation(10.dp)
                        ) {
                                Box(
                                        Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text("Log Out")
                                }
                        }

                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditableText(
        text: String,
        label: String,
        enabled: Boolean,
        maxLength: Int? = null,
        onValueChange: (String) -> Unit
) {
        // Only show editable text if in edit mode
        if (enabled) {
                var value by remember { mutableStateOf(text) }
                TextField(
                        value = value,
                        onValueChange = {
                                        value = it
                                        onValueChange(it)
                        },
                        label = { Text(label) },
                        modifier = Modifier.padding(vertical = 3.dp)
                )
        } else {
                Text("$label : $text", Modifier.padding(vertical = 3.dp),
                        color = Iris ,fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
}


@Composable
fun DatePickerEmp(onDateSelected: (String) -> Unit) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        var selectedDate by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
                val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                                // Format day and month with leading zeros if needed
                                val formattedDay = String.format("%02d", dayOfMonth)
                                val formattedMonth = String.format("%02d", month + 1)

                                selectedDate = "$formattedDay-$formattedMonth-$year"
                                onDateSelected(selectedDate)
                        },
                        year,
                        month,
                        day
                )

                // Show the dialog
                datePickerDialog.show()
        }
}

