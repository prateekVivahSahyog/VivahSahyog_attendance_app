package com.example.employattendance.AppUi.AppEntryPoint.Ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.employattendance.Navigation.Screen
import com.example.employattendance.AppUi.AppEntryPoint.ViewModel.LoginViewModel
import com.example.employattendance.ui.theme.Green1
import com.example.employattendance.ui.theme.Iris

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val error by loginViewModel.error.observeAsState()
    val errorString by loginViewModel.errorString.observeAsState()
    val context = LocalContext.current

    val loginModifier = Modifier.widthIn(320.dp).padding(vertical = 2.dp)
        .background(color = Green1, shape = RoundedCornerShape(13.dp))
    var switchState by remember { mutableStateOf(false) }
   // var switchText by remember { mutableStateOf("Employee") }
    val scope = rememberCoroutineScope()
    var hide by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var validEmail by remember { mutableStateOf(false) }
    var validpass by remember { mutableStateOf(false) }
    var allValid by remember { mutableStateOf(false) }
    var loginInProgress by remember { mutableStateOf(false) }


    LaunchedEffect(email, password) {
        validEmail = isEmailValid(email)
        validpass = validatePassword(password)
        allValid = validEmail && validpass
    }

    LaunchedEffect(switchState) {
        loginViewModel.loginAs.value  = if (switchState) "Admin" else "Employee"
        Log.d("TAG", "Switched to role: ${loginViewModel.loginAs.value}")
    }

    Box(
        Modifier.fillMaxSize().background(color = Color(234, 250, 241)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier.padding(horizontal = 15.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Hey there,", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Text("Welcome Back", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    loginViewModel._error.value = false
                },
                modifier = loginModifier,
                textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
                leadingIcon = { Icon(Icons.Outlined.Email, null) },
                placeholder = { Text("Email", fontWeight = FontWeight.Medium) },
                isError = !validEmail,
                singleLine = true,
                shape = RoundedCornerShape(13.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    loginViewModel._error.value = false
                },
                modifier = loginModifier,
                textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
                leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                placeholder = { Text("Password", fontWeight = FontWeight.Medium) },
                trailingIcon = {
                    IconButton(onClick = { hide = !hide }) {
                        Icon(
                            if (hide) Icons.Outlined.VisibilityOff
                            else Icons.Outlined.Visibility, null
                        )
                    }
                },
                visualTransformation = if (hide) PasswordVisualTransformation() else VisualTransformation.None,
                isError = !validpass,
                singleLine = true,
                shape = RoundedCornerShape(13.dp)
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { switchState = !switchState },
                Modifier.widthIn(320.dp).padding(vertical = 2.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Login as ${loginViewModel.loginAs.value}")

            }
            Spacer(Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth()) {
                Spacer(Modifier.width(200.dp))
                Text(
                    " Forget Password",
                    Modifier.clickable {
                        navController.navigate(route = Screen.OtpScreen.route)
                    },
                    fontSize = 15.sp,
                    color = Iris,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(Modifier.height(40.dp))

            if (error == true) {
                errorString?.let {
                    Text(
                        it,
                        Modifier.padding(vertical = 10.dp),
                        color = Color.Red,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }
            Spacer(Modifier.height(40.dp))
            Button(
                onClick = {

                        scope.launch(Dispatchers.IO) {
                            loginInProgress = true
                            loginViewModel.login(email, password, navController,context )
                            loginInProgress = false
                        }
                    if(error !=true){
                        Toast.makeText(context, "Wait logging in..", Toast.LENGTH_LONG).show()
                    }

                },
                Modifier.widthIn(320.dp).heightIn(55.dp),
                enabled = allValid,
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(20, 143, 119))
            ) {

                if (loginInProgress) {
                    CircularProgressIndicator()
                } else {
                    Text("Login", fontSize = 20.sp)
                }
            }

            Row(
                Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Not yet registered  ", fontSize = 14.sp)
                Text(
                    "Signup",
                    Modifier.clickable {
                        navController.navigate(route = Screen.SignUp.route)
                    },
                    color = Iris,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

fun validatePassword(password:String): Boolean {
    val minLength = 8
    val hasUppercase = Regex("[A-Z]").containsMatchIn(password)
    val hasLowercase = Regex("[a-z]").containsMatchIn(password)
    val hasDigit = Regex("\\d").containsMatchIn(password)
    val hasSpecialChar = Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\",<.>/?]").containsMatchIn(password)

    return (password.length >= minLength && hasUppercase && hasLowercase && hasDigit && hasSpecialChar)
}

