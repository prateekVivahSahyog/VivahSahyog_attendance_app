package com.example.employattendance.AppUi.AppEntryPoint.Ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employattendance.Navigation.Screen


@Composable
fun Verification(navController: NavController){
    Box(Modifier.fillMaxSize().background(color = Color(234, 250, 241 )), contentAlignment = Alignment.TopCenter) {

        Column(
            Modifier.padding(horizontal = 15.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Email verification is sent, verify and Login ", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)

            Spacer(Modifier.height(20.dp))

            Button(onClick = {
                             navController.navigate(route = Screen.Login.route)
            }, Modifier.widthIn(320.dp).heightIn(55.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(20, 143, 119 ))
            ){
                Text("Back to Login", fontSize = 20.sp)
            }

        }
    }
}