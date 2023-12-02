package com.example.employattendance.AppUi.Employee.Ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.employattendance.Navigation.Screen


@OptIn(ExperimentalFoundationApi::class)
@Composable

fun LeaveRequest(navController: NavHostController){

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { PageList.size }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage

    }

    Column(Modifier.fillMaxSize()) {

        Card(Modifier.height(45.dp).fillMaxWidth(), shape = RectangleShape) {

            IconButton(onClick = {navController.navigateUp()})
            {
                Icon(imageVector = Icons.Filled.ArrowBack,contentDescription = null)
            }
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            PageList.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index; },
                    text = { Text(text = item.title) },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->

            when (index) {

                0 -> LeaveApplicationPage()
                1 -> LeaveApplicationHistory()

            }
        }
    }
}

val PageList = listOf(
    PageItem(
        title = "Leave Application",
    ),
    PageItem(
        title = "Application History"
    ),

)

data class PageItem(
    val title: String,
)

