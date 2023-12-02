package com.example.employattendance.AppUi.Employee.Ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
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

    Log.d("TAG","IN Employee page")


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
//        Scaffold(Modifier.fillMaxSize(),
//            bottomBar = {
//                AnimatedNavigationBar(
//                    Modifier
//                        .height(60.dp)
//                        .fillMaxWidth()
//                        .size(410.dp, 50.dp),
//                    selectedIndex = selectedIndex,
//                    cornerRadius = shapeCornerRadius(cornerRadius = 24.dp),
//                    ballAnimation = Parabolic(tween(300)),
//                    indentAnimation = Height(tween(300)),
//                    barColor = CustomWhite,
//                    ballColor = Iris
//                ) {
//
//                    screens.forEachIndexed { index, screen ->
//                        Box(modifier = Modifier
//                            .noRippleClickable {
//                                if (selectedIndex != index)
//                                    selectedIndex = index
//                            }
//                            .fillMaxSize(),
//                            contentAlignment = Alignment.Center) {
//                            Icon(
//                                imageVector = if (selectedIndex == index
//                                ) tabItems[index].selected else tabItems[index].unselected,
//                                contentDescription = null,
//                                Modifier.size(26.dp),
//                                tint = if (selectedIndex == index) Iris else PrimaryBackGroundColor
//                            )
//                        }
//                    }
//                }
//
//            }) {
//
//
//            when (screens[selectedIndex]) {
//                is Screen.Action -> ActionPage()//actionViewModel)
//                is Screen.History -> History()//historyViewModel)
//                is Screen.Profile -> Profile(navController)//,profileViewModel)
//                else -> {}
//            }
//        }
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
                is Screen.History -> History() //historyViewModel
                is Screen.Profile -> Profile(navController) //profileViewModel
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
            }
        }
    }
}


