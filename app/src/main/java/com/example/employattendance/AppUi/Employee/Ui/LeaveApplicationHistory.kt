package com.example.employattendance.AppUi.Employee.Ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.employattendance.AppUi.Employee.ViewModels.LeaveApplicationHisViewModel
import com.example.employattendance.dataClass.LeaveApplication

@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun LeaveApplicationHistory(viewModel: LeaveApplicationHisViewModel = LeaveApplicationHisViewModel()){
    val recordsList by viewModel.recordsList
    LazyColumn(Modifier.padding(bottom = 20.dp, top = 7.dp ).fillMaxSize()) {
        items(recordsList){
           ApplicationCard(it)
        }
    }


}

@Composable

fun ApplicationCard(application: LeaveApplication){

    val date by remember { mutableStateOf(application.applicationDate) }
    val from by remember { mutableStateOf(application.from) }
    val till by remember { mutableStateOf(application.till) }

    val reason by remember { mutableStateOf(application.reason) }
    var status by remember { mutableStateOf(application.status) }

    Card(
        Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(Color(177, 233, 179, 255))) {

        Column(Modifier.fillMaxSize().padding(vertical = 20.dp, horizontal = 17.dp)) {

            Text("Application Date: $date",fontSize = 15.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                Text("From : $from",fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(" - ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Till : $till",fontSize = 13.sp, fontWeight = FontWeight.Bold)

            }

            Text("Reason : ",fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text(reason,fontSize = 15.sp, fontWeight = FontWeight.Normal)

            Spacer(Modifier.height(10.dp))

            Text("Status :- $status",fontWeight = FontWeight.Bold)
        }


    }


}