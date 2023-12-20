package com.example.employattendance.AppUi.Employee.Ui

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.employattendance.AppUi.Employee.ViewModels.ActionViewModel
import com.example.employattendance.AppUi.Employee.ViewModels.HistoryViewModel
import com.example.employattendance.AppUi.Employee.ViewModels.ProfileViewModel
import com.example.employattendance.Navigation.Screen
import com.example.employattendance.R


import com.example.employattendance.ui.theme.CustomWhite
import com.example.employattendance.ui.theme.Iris
import com.example.employattendance.ui.theme.PrimaryBackGroundColor
//import com.exyte.animatednavbar.AnimatedNavigationBar
//import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
//import com.exyte.animatednavbar.animation.indendshape.Height
//import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
//import com.exyte.animatednavbar.utils.noRippleClickable
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

val tabItems = listOf(
    TabItem(
        title = "Action",
        unselected = Icons.Outlined.WorkOutline,
        selected = Icons.Filled.Work
    ),
    TabItem(
        title = "History",
        unselected = Icons.Outlined.WorkHistory,
        selected = Icons.Filled.WorkHistory
    ),
    TabItem(
        title = "Profile",
        unselected = Icons.Outlined.Person,
        selected = Icons.Filled.Person
    )
)

data class TabItem(
    val title: String,
    val unselected: ImageVector,
    val selected: ImageVector,

)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeLogin(navController: NavHostController ){

    var varification by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Log.d("TAG","IN Employee page")


    val historyViewModel = HistoryViewModel()
    val profileViewModel = ProfileViewModel()


    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val screens = listOf(
        Screen.Action,
        Screen.History,
        Screen.Profile
    )

    val auth = FirebaseAuth.getInstance()

    if(auth.currentUser?.isEmailVerified == true ) {



        Scaffold(
            Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(
                    Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    backgroundColor = Color(169, 223, 191)
                ) {
                    screens.forEachIndexed { index, screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = if (selectedIndex == index
                                    ) tabItems[index].selected else tabItems[index].unselected,
                                    contentDescription = null,
                                    Modifier.size(26.dp),
                                    tint = if (selectedIndex == index) Iris else PrimaryBackGroundColor
                                )
                            },
                            selected = selectedIndex == index,
                            onClick = {
                                if (selectedIndex != index)
                                    selectedIndex = index
                            }
                        )
                    }
                }
            }
        ) {
            when (screens[selectedIndex]) {
                is Screen.Action -> ActionPage() //actionViewModel
                is Screen.History -> History(historyViewModel) //historyViewModel
                is Screen.Profile -> Profile(navController,profileViewModel) //profileViewModel
                else -> {}
            }
        }

    }

    else{
        val imagePainter: Painter = rememberAsyncImagePainter(model = R.drawable.email_error)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Column (Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp, 150.dp),
                    contentScale = ContentScale.Fit
                )
                Text("Email not Verified", fontSize = 20.sp, fontWeight = FontWeight.Medium)


                Button(onClick = {
                    varification = false
                    Toast.makeText(context, "Sending verification", Toast.LENGTH_SHORT).show()
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {

                        Toast.makeText(context, "Verification Sent", Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        varification = true
                    }
                },Modifier.widthIn(320.dp).heightIn(55.dp),
                    enabled = varification,
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(20, 143, 119 ))
                ){
                    Text("Send Email Verification")
                }
            }
        }
    }
}


