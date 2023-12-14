package com.example.employattendance.AppUi.Employee.Ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.employattendance.AppUi.Employee.ViewModels.HistoryViewModel
import com.example.employattendance.dataClass.AttendanceRecord
import com.example.employattendance.ui.theme.History_Page_Dark_Yellow
import com.example.employattendance.ui.theme.History_Page_Light_Green
import com.example.employattendance.ui.theme.Iris
import com.example.employattendance.ui.theme.LightBlue
import com.example.employattendance.ui.theme.PrimaryTextColor



@Composable
fun History(historyViewModel: HistoryViewModel = HistoryViewModel()){

    val recordsList by historyViewModel.recordsList

    var selectedYear by remember { mutableStateOf(historyViewModel.selectedYear.value) }
    var selectedMonth by  remember { mutableStateOf(historyViewModel.selectedMonth.value) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(selectedMonth,selectedYear){
        historyViewModel.fetchAttendanceRecords()
    }


    if (visible) {
        MonthYearPickerDialog(
            currentYear = selectedYear!!,
            currentMonth = selectedMonth!!,
            onDismiss = { y, m ->
                selectedYear = y
                selectedMonth = m
                historyViewModel.updateYearMonth(y,m)
                visible = false
            }
        )
    }

    Column(Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp)) {

        Card(
            Modifier
                .size(400.dp,80.dp)
                .fillMaxWidth().padding(top = 5.dp),
            colors = CardDefaults.cardColors(
                Color(174, 214, 241)
            ),
            elevation = CardDefaults.elevatedCardElevation(5.dp)) {

            Column {
                Spacer(Modifier.height(2.dp))


                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.Center){
                    Text(
                        "My Attendance",
                        color = Color(125, 60, 152)//20, 143, 119)
                        ,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }


                Spacer(Modifier.height(5.dp))


                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${historyViewModel.months[selectedMonth!!]}  ${selectedYear}",
                            color = Iris,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                            Spacer(Modifier.width(10.dp))
                        IconButton(
                            onClick = { visible = true },
                            content = { Icon(Icons.Filled.CalendarMonth, null, tint = Iris) })
                    }
                }
            }
        }

        LazyColumn(Modifier.padding(bottom = 60.dp, top = 7.dp ).fillMaxSize()) {
            items(recordsList){
               // Log.d("TAG","${historyViewModel.recordsList}")
                Log.d("TAG","Display =  $it")
                    HistoryCard(it)
            }
        }
    }
}



@Composable
fun HistoryCard(today:AttendanceRecord){



    Card (Modifier.size(367.dp,150.dp).padding(vertical = 7.dp),
        colors = CardDefaults.cardColors(containerColor = History_Page_Light_Green
        ),
        elevation = CardDefaults.elevatedCardElevation(5.dp)){

        Row(Modifier.fillMaxSize()) {
            Card (Modifier.size(120.dp,150.dp),
                colors = CardDefaults.cardColors(containerColor = History_Page_Dark_Yellow)){
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Column {
                        Text(today.day, color = PrimaryTextColor,fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text(today.date, color = PrimaryTextColor,fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.width(17.dp))

            Box(Modifier.size(90.dp,150.dp), contentAlignment = Alignment.Center){
                Column() {
                        Text("Check In" ,color = Color.White,fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text(today.checkIn, color = Color.White,fontSize = 17.sp, fontWeight = FontWeight.Medium)
                    }

            }

            Spacer(Modifier.width(17.dp))

            Box(Modifier.size(90.dp,150.dp).padding(end = 10.dp), contentAlignment = Alignment.Center){
                Column {
                    Text("Check Out", color = Color.White,fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Text(today.checkOut, color = Color.White,fontSize = 17.sp, fontWeight = FontWeight.Medium)
                }
            }
        }


    }
}

@Composable
fun MonthYearPickerDialog(
    currentYear: Int,
    currentMonth: Int,
    onDismiss: (Int, Int) -> Unit
) {
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var selectedMonth by remember { mutableIntStateOf(currentMonth) }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val monthNames = listOf(
        "",
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )

    AlertDialog(
        onDismissRequest = {
            onDismiss(selectedYear, selectedMonth)
        },
        title = {
            Text(text = "   Pick a Month/Year")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(7.dp))
                Row() {

                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .rotate(90f)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = {
                                    selectedYear--
                                }
                            ),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = selectedYear.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .rotate(-90f)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = {
                                    selectedYear++
                                }
                            ),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null
                    )

                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    content = {
                        items((1..12).toList()){
                                Card(
                                    Modifier.size(50.dp, 40.dp).padding(3.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedMonth = it },
                                    colors = if (selectedMonth == it)
                                        CardDefaults.cardColors(Color(67, 160, 71 ))
                                    else
                                        CardDefaults.cardColors(Color(200, 230, 201 )),

                                    elevation = CardDefaults.cardElevation(10.dp)
                                ) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(monthNames[it])
                                    }
                                }
                        }
                    }
                )
            }
        }, containerColor = LightBlue,
        shape = RoundedCornerShape(20.dp),
        confirmButton = {
            Button(onClick = {
                onDismiss(selectedYear, selectedMonth)
            },
                Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Iris)

            ) {
                Text(text = "OK")
            }
        }
    )
}


