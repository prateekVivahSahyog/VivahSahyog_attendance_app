package com.example.employattendance.AppUi.Employee.Ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.employattendance.AppUi.Employee.ViewModels.ActionViewModel
import com.example.employattendance.ui.theme.Action_Page_Card_Green
import com.example.employattendance.ui.theme.Action_Page_Card_Text_Green
import com.example.employattendance.ui.theme.Action_Page_Green
import com.example.employattendance.ui.theme.Action_Page_Red
import com.example.employattendance.ui.theme.CustomGreen2
import com.example.employattendance.ui.theme.CustomRed
import com.example.employattendance.ui.theme.History_Page_Light_Green

import com.example.employattendance.ui.theme.Iris
import com.example.employattendance.ui.theme.LightBlue
import com.example.employattendance.ui.theme.Yellow
import com.example.employattendance.ui.theme.check1

@Composable

fun ActionPage(actionViewModel: ActionViewModel = ActionViewModel()){

    val action by actionViewModel.action.observeAsState()
    val checkIn by actionViewModel.inTime.observeAsState()
    val checkOut by actionViewModel.outTime.observeAsState()

    val absent  by actionViewModel.absent.observeAsState(initial = 0)
    val workingDays  by actionViewModel.workingDays.observeAsState(initial = 1)
    var taskPerformed by remember { mutableStateOf("") }
    var taskInputStatus by remember { mutableStateOf(false) }

//    LaunchedEffect(action){
//        if(action ==2 ){
//            taskInputStatus = true
//        }
//    }


    Column(Modifier.padding(horizontal = 7.dp),horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        TopBar()

        Spacer(modifier = Modifier.height(20.dp))

        if (!taskInputStatus) { // task input box


        Text(
            text = "Status",
            color = Color(56, 142, 60),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            Modifier
                .size(400.dp, 310.dp)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            colors = CardDefaults.cardColors(check1),//Color(232, 245, 233)
            elevation = CardDefaults.elevatedCardElevation(2.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "Today's Status : ${actionViewModel.todayWorkingHours.floatValue} Hrs",
                        color = Action_Page_Card_Text_Green,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ) //Color(67, 160, 71 )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Weekly Status  : ${actionViewModel.weeksWorkingHours.floatValue} Hrs",
                        color = Action_Page_Card_Text_Green,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(13.dp))
                    Text(
                        "Monthly Status",
                        color = Action_Page_Card_Text_Green,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(7.dp))


                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                        androidx.compose.material.CircularProgressIndicator(
                            progress = (absent.toFloat() / workingDays.toFloat()),
                            color = Action_Page_Red,
                            strokeWidth = 10.dp,
                            backgroundColor = Yellow,
                            modifier = Modifier.size(120.dp)
                        )
                        val text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Action_Page_Red,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(absent.toString())
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color(67, 160, 71),
                                    fontSize = 30.sp
                                )
                            ) {
                                append("/")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Action_Page_Green,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(workingDays.toString())
                            }
                        }
                        Text(text = text, modifier = Modifier.padding(horizontal = 10.dp))

                    }

                    Spacer(modifier = Modifier.height(10.dp))



                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            Modifier.size(10.dp),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(Action_Page_Green),
                            elevation = CardDefaults.elevatedCardElevation(2.dp)
                        ) {}

                        Text(" Working days", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            Modifier.size(10.dp),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(Action_Page_Red),
                            elevation = CardDefaults.elevatedCardElevation(5.dp)
                        ) {}

                        Text(" Absent", fontWeight = FontWeight.SemiBold)
                    }

                }
            }
        }


        Spacer(Modifier.height(20.dp))

    } // - task input box

        Text(text = "Today's Activity", color = Color(94, 53, 177 ) ,fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Card(
            Modifier
                .size(400.dp, if(action==2) 100.dp else 150.dp)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            colors = CardDefaults.cardColors(Color(230, 220, 246)//237, 231, 246)
            ),
            elevation = CardDefaults.elevatedCardElevation(2.dp)) {

            Box( modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.Center){

                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {

                    Column {
                        Text(text = "Check In", color = Iris ,fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = checkIn?:"    --/--", color = Iris,fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Column {
                        Text(text = "Check Out", color = Iris,fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = checkOut?:"    --/--", color = Iris,fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {

            if (action==0) {
                Button(
                    onClick = {
                        actionViewModel.checkIn()},
                    Modifier.size(150.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(Iris),
                    shape = RoundedCornerShape(10.dp)
                ) {

                    Box( modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.Center){
                        Text(text = "Check In", fontSize = 17.sp)
                    }
                }
            }

            else if(action==1) {
                Button(
                    onClick = {actionViewModel.checkOut()},
                    Modifier.size(150.dp, 50.dp),
                    //enabled = (((action ?: 0) < 2))
                   // ,
                    colors = ButtonDefaults.buttonColors(Iris),
                    shape = RoundedCornerShape(10.dp)
                ) {

                    Box( modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.Center){
                        Text(text = "Check Out", fontSize = 17.sp)
                    }
                }
            }
            else if (action==2){

                Column {
                    OutlinedTextField(
                        value = taskPerformed,
                        onValueChange = { taskPerformed = it ; taskInputStatus = true},
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Today's Task Performed") },
                        maxLines = 7
                    )

                    Button(onClick = {  taskInputStatus = false ; actionViewModel.tasks(taskPerformed)}, Modifier.fillMaxWidth(), enabled = taskPerformed!="",shape = RoundedCornerShape(10.dp)){
                        Text("Submit task's")
                    }
                }

            }
        }
    }
}

@Composable
fun TopBar(){

    Card(
        Modifier
            .size(400.dp,60.dp)
            .fillMaxWidth().padding(top = 2.dp),
        colors = CardDefaults.cardColors(Color(230, 220, 246)
        ),
        elevation = CardDefaults.elevatedCardElevation(2.dp)) {

            Box(contentAlignment = Alignment.Center){
                Text(
                    text = "CheckMate",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(8.dp),
                    color = Color(20, 143, 119 ),
                    fontWeight = FontWeight.Bold
                )
            }
        }
}








