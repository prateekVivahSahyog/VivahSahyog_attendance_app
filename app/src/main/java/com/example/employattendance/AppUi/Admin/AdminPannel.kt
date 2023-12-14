package com.example.employattendance.AppUi.Admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold


import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.employattendance.Navigation.Screen
import com.example.employattendance.ui.theme.Iris
import com.example.employattendance.ui.theme.LightYellow
import com.example.employattendance.ui.theme.PrimaryTextColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar( drawerState: DrawerState,adminViewModel:AdminViewModel) {
    var dateButton by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    val date by adminViewModel.selectedDate.observeAsState()

    LaunchedEffect(date){
        scope.launch {
            withContext(Dispatchers.IO) {
                adminViewModel.fetchEmployees()
            }
        }

    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 3.dp, vertical = 2.dp),
        shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp),
        colors = CardDefaults.cardColors(Color(237, 231, 246))
    ) {

        Row(Modifier.padding(top= 2.dp),horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null, tint = Iris)
            }

            Text(
                text = "CheckMate",
                fontSize = 25.sp,
                modifier = Modifier.padding(8.dp),
                color = Color(20, 143, 119 ),
                fontWeight = FontWeight.Bold
            )

            Button(onClick = { dateButton = true },
                Modifier.padding(start = 25.dp),
                shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Iris)
            ) {
                Text(text = date?:"" , color = Color.White,)
            }
        }

    }

    if(dateButton)
    {
        DatePicker(onDateSelected = { adminViewModel.updateDate(it)})
        dateButton = false
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "MutableCollectionMutableState"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AttendancePage(navController : NavController, adminViewModel:AdminViewModel = AdminViewModel()){

    val logout by adminViewModel.logout.observeAsState()
    if(logout == true)
    {
        navController.navigateUp()
    }

    Log.d("TAG","IN Attendance page")
    val list by adminViewModel.employeeList.observeAsState(initial = emptyList())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { NavDrawer(adminViewModel,navController) },
        content = {
            Scaffold(topBar = { TopBar(drawerState,adminViewModel) },
                bottomBar = { },
                ) {

                LazyColumn(Modifier.padding(top = 65.dp)){
                    Log.d("TAG"," main Page = ${list.size}")
                    items(list){ employee->
                        EmployeeCard(employee,navController){
                            adminViewModel.updateAttendance(it,employee.employeeInfo.empId)
                        }
                    }
                }
            }
        }
    )
}


@Composable

fun NavDrawer(adminViewModel: AdminViewModel,navController: NavController){



    val date by adminViewModel.selectedDate.observeAsState()
    val context = LocalContext.current


    Box(modifier = Modifier.background(Color(232, 245, 233), shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp))
        .fillMaxHeight()
        .fillMaxWidth(0.7f)
        .clip(RoundedCornerShape(15.dp))){

        Column(Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
               adminViewModel.logOut()
            },Modifier.size(320.dp,40.dp).fillMaxWidth().padding(horizontal = 5.dp).clip(RoundedCornerShape(2.dp))) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    Text(" Logout    ")
                    Icon(Icons.Filled.Logout,null)
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Download button
            Button(onClick = { adminViewModel.saveWorkbookToStorage(context,"Attendance_$date.xls")
                                Toast.makeText(context, "Attendance downloaded", Toast.LENGTH_SHORT).show();
                             },Modifier.size(320.dp,40.dp).fillMaxWidth().padding(horizontal = 5.dp).clip(RoundedCornerShape(2.dp))) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    Text("Download")
                    Icon(Icons.Filled.Download,null)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { navController.navigate(Screen.EmployeeLogin.route){popUpTo("EmployeeLogin") { inclusive = true }}
            },Modifier.size(320.dp,40.dp).fillMaxWidth().padding(horizontal = 5.dp).clip(RoundedCornerShape(2.dp))) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    Text("Account Switch")
                    Icon(Icons.Filled.Flip,null)
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun EmployeeCard(employee: Info,navController : NavController, onUpdate:(String) -> Unit){

    var extended by remember {
        mutableStateOf(false)
    }

    var cardHeight by remember { mutableStateOf(60.dp) }

    LaunchedEffect(extended){
       cardHeight =  if(extended) 200.dp else 80.dp
    }

    var buttonText by remember { mutableStateOf("" )}
    var text by remember { mutableStateOf("") }

    var buttonPressed by remember { mutableStateOf(employee.attendanceRecord.status == "Present") }
    var updateText = "Absent"

    LaunchedEffect(key1 = buttonPressed , key2 = !buttonPressed){
        if(buttonPressed){
            text = "Present"
            buttonText = "Mark Absent"
            updateText = "Absent"
        }
        else{
            text ="Absent"
            buttonText = "Mark Present"
            updateText = "Present"
        }
    }

    Card(
        Modifier
            .clickable { extended =!extended
            }
            .fillMaxWidth()
            .height(cardHeight)
            .padding(horizontal = 5.dp, vertical = 5.dp).clip(RectangleShape),
        colors = CardDefaults.cardColors(Color(177, 233, 179, 255)),

        elevation = CardDefaults.elevatedCardElevation(2.dp)) {

        Column(Modifier.fillMaxSize().padding(horizontal = 5.dp)) {
            Row (Modifier.padding(top = 5.dp, bottom = 5.dp),verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround){

                Text(text = "Name : ${employee.employeeInfo.firstName} ${employee.employeeInfo.lastName}",Modifier.weight(3f), fontSize =15.sp, fontWeight = FontWeight.Medium )


                if(extended) {

                    IconButton(onClick = {navController.navigate(Screen.LeaveApplicationHistoryAdmin.withArgs(employee.employeeInfo.empId))}){
                        Icon(imageVector = Icons.Filled.NotificationImportant,contentDescription = null, tint = if(employee.employeeInfo.notification) Color.Red else Color.Gray)
                    }


                    IconButton(
                        onClick = {
                            buttonPressed = !buttonPressed
                            onUpdate(updateText)
                            employee.attendanceRecord.status = updateText
                        },
                        Modifier
                            .width(100.dp)
                            .clip(RoundedCornerShape(10.dp)),

                        colors = IconButtonDefaults.iconButtonColors(LightYellow)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = buttonText,
                                color = PrimaryTextColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
                else
                {
                    Text(text =text,fontSize = 14.sp, fontWeight = FontWeight.Bold ,maxLines = 1)
                }


            }

            if(extended){
                Text(text = "Email : ${employee.employeeInfo.email}",Modifier.weight(3f), fontSize =15.sp, fontWeight = FontWeight.Medium )
                Text(text = "Dept : ${employee.employeeInfo.department} ",Modifier.weight(3f), fontSize =15.sp, fontWeight = FontWeight.Medium )
                Text(text = "Position : ${employee.employeeInfo.position} ",Modifier.weight(3f), fontSize =15.sp, fontWeight = FontWeight.Medium )
                Text(text = "CheckIn : ${employee.attendanceRecord.checkIn}",Modifier.weight(3f), fontSize =15.sp, fontWeight = FontWeight.Medium )
                Text(text = "CheckOut : ${employee.attendanceRecord.checkOut}",Modifier.weight(3f), fontSize =15.sp, fontWeight = FontWeight.Medium )
            }
        }




        }

    }

@Composable
fun DatePicker(onDateSelected: (String) -> Unit) {
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

                selectedDate = "$year-$formattedMonth-$formattedDay"
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





