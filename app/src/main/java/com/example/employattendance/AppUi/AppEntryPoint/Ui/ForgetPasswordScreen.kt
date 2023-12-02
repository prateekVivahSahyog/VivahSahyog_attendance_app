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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.employattendance.ui.theme.Green1
import com.example.employattendance.ui.theme.Iris
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)

@Preview(showSystemUi = true)
@Composable
fun OtpScreen(
){
    var email by remember { mutableStateOf("") }
    var enable by remember { mutableStateOf(true) }
    var opt by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var text by remember {   mutableStateOf("")}

    Box(Modifier.fillMaxSize().background(color = Color(234, 250, 241 )), contentAlignment = Alignment.TopCenter) {

        Column(
            Modifier.padding(horizontal = 15.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Text("Forget Password", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                Modifier.widthIn(320.dp).padding(vertical = 7.dp)
                    .background(color = Green1, shape = RoundedCornerShape(13.dp)),
                enabled = enable,
                textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
                leadingIcon = { Icon(Icons.Outlined.Email, null) },
                placeholder = { Text("Email", fontWeight = FontWeight.Medium) },
                isError = !isEmailValid(email),
                maxLines = 1,
                shape = RoundedCornerShape(13.dp),
            )

//            OutlinedTextField(
//                value = opt, onValueChange = { opt = it },
//                Modifier.widthIn(320.dp).padding(vertical = 7.dp)
//                    .background(color = Green1, shape = RoundedCornerShape(13.dp)),
//                textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
//                leadingIcon = { Icon(Icons.Outlined.Lock, null) },
//                placeholder = { Text("OTP", fontWeight = FontWeight.Medium) },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.NumberPassword
//                ),
//                maxLines = 1,
//                shape = RoundedCornerShape(13.dp),
//            )
//
//            OutlinedTextField(value = password, onValueChange = {password = it},
//                Modifier.widthIn(320.dp).padding(vertical = 7.dp)
//                    .background(color = Green1, shape = RoundedCornerShape(13.dp)),
//                textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
//                leadingIcon = { Icon(Icons.Outlined.Lock,null) },
//                placeholder = { Text("New Password", fontWeight = FontWeight.Medium) },
//                maxLines = 1,
//                shape = RoundedCornerShape(13.dp),
//            )

            Text(text, fontSize = 12.sp, color = Iris)

            Spacer(Modifier.height(90.dp))
            Button(onClick = {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                enable = false
                text ="Resent email sent to $email"

                             }, Modifier.widthIn(320.dp).heightIn(55.dp),
                shape = RoundedCornerShape(13.dp),
                enabled = isEmailValid(email) && enable,
                colors = ButtonDefaults.buttonColors(containerColor = Color(20, 143, 119 ))
            ){
                Text("Reset Password", fontSize = 20.sp)
            }
        }
    }
}

fun isEmailValid(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}