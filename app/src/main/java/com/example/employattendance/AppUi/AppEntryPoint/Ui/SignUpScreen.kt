package com.example.employattendance.AppUi.AppEntryPoint.Ui

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
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
import com.example.employattendance.AppUi.AppEntryPoint.ViewModel.SignUpViewModel


import com.example.employattendance.ui.theme.Green1
import com.example.employattendance.ui.theme.Iris

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SignUp(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel = viewModel()
){

    val signInModifier =  Modifier.widthIn(320.dp)
        .padding(vertical = 2.dp)
        .background(color = Green1, shape = RoundedCornerShape(13.dp))

    var hide by remember { mutableStateOf(true) }
    val firstName by signUpViewModel.firstName.observeAsState()
    val lastName by signUpViewModel.lastName.observeAsState()
    val email by signUpViewModel.email.observeAsState()
    val password by signUpViewModel.password.observeAsState()

    val scope = rememberCoroutineScope()

    var validEmail by remember { mutableStateOf(false) }
    var validpass by remember { mutableStateOf(false) }
    var allValid by remember { mutableStateOf(false) }
    var validName by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var loginInProgress by remember { mutableStateOf(false) }

    LaunchedEffect(firstName,lastName,email,password){

        validEmail = email?.let { isEmailValid(it) } == true
        validpass = password?.let { validatePassword(it) } == true
        validName = firstName?.isNotEmpty() ?: false && lastName?.isNotEmpty() ?: false
        allValid = validName && validEmail && validpass
    }

    Box(Modifier.fillMaxSize().background(color = Color(234, 250, 241 )), contentAlignment = Alignment.TopCenter) {

        Column(
            Modifier.padding(horizontal = 15.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Hey there,", fontSize = 20.sp, fontWeight = FontWeight.Medium)

            Text("Create an Account", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)

            Spacer(Modifier.height(20.dp))

            firstName?.let {
                OutlinedTextField(value = it, onValueChange = {
                    signUpViewModel._firstName.value = it  },
                    modifier =  signInModifier,
                    textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
                    leadingIcon = { Icon(Icons.Outlined.Person,null) },
                    placeholder = { Text("First Name", fontWeight = FontWeight.Medium) },
                    singleLine = true,
                    shape = RoundedCornerShape(13.dp),
                )
            }

            lastName?.let {
                OutlinedTextField(value = it,
                    onValueChange = {signUpViewModel._lastName.value = it},
                    modifier =  signInModifier,
                    textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
                    leadingIcon = { Icon(Icons.Outlined.Person,null) },
                    placeholder = { Text("Last Name", fontWeight = FontWeight.Medium) },
                    singleLine = true,
                    shape = RoundedCornerShape(13.dp),
                )
            }

            email?.let {
                OutlinedTextField(value = it, onValueChange = {signUpViewModel._email.value = it },
                    modifier =  signInModifier,
                    textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
                    leadingIcon = { Icon(Icons.Outlined.Email,null) },
                    placeholder = { Text("Email", fontWeight = FontWeight.Medium) },
                    singleLine = true,
                    isError = !validEmail,
                    shape = RoundedCornerShape(13.dp),
                )
            }

            password?.let {
                OutlinedTextField(value = it, onValueChange = {signUpViewModel._password.value = it; },
                    modifier =  signInModifier,
                    textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 19.sp),
                    leadingIcon = { Icon(Icons.Outlined.Lock,null) },
                    placeholder = { Text("Password", fontWeight = FontWeight.Medium) },
                    trailingIcon = { IconButton(onClick = {hide=!hide}){
                        Icon(if(hide)Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility,null) } },
                    visualTransformation = if(hide)PasswordVisualTransformation() else VisualTransformation.None ,
                    singleLine = true,
                    isError = !validpass,
                    shape = RoundedCornerShape(13.dp),
                )
            }

            Spacer(Modifier.height(90.dp))

            Button(onClick = {
                    scope.launch {
                        loginInProgress = true
                        signUpViewModel.signUp(navController)
                        loginInProgress = false
                    }
                Toast.makeText(context, "Wait Signing up..", Toast.LENGTH_LONG).show()
            },Modifier.widthIn(320.dp).heightIn(55.dp),
                enabled = allValid,
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(20, 143, 119 ))
            ){

                if (loginInProgress) {
                    CircularProgressIndicator()
                } else {
                    Text("Register", fontSize = 20.sp)
                }
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Already have account  ",fontSize = 14.sp)
                Text("Login", Modifier.clickable {
                    navController.navigate(route = Screen.Login.route) },
                    color = Iris ,fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
            }

        }
    }

}


