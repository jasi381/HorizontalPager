package com.example.horizontalpager

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.horizontalpager.navigation.Navigation
import com.example.horizontalpager.screens.Screens
import com.example.horizontalpager.ui.theme.HorizontalPagerTheme
import com.example.horizontalpager.viewModel.UserListViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HorizontalPagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()


                }
            }
        }
    }
}

@Composable
fun UserListScreen(viewModel: UserListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchUsersList()
    }

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.error.isNotEmpty()) {
            Text(
                text = state.error,
                color = Color.Black,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                state.users.forEach { user ->
                    Text(
                        text = "Name: ${user.name}, Email: ${user.email}",
                        color = Color.Blue,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState) {

    val list  = listOf(
        "Home" to Icons.TwoTone.Home,
        "Search" to Icons.TwoTone.Search,
        "Profile" to Icons.TwoTone.Person,
    )

    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex =pagerState.currentPage,
        containerColor = Color.Green.copy(alpha = 0.75f),
        contentColor = Color.White,
    ) {

        list.forEach {
            val index = list.indexOf(it)
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(text = it.first)
                },
                icon = {
                    Icon(imageVector = it.second, contentDescription = null)
                },
                modifier = Modifier
                    .height(50.dp)
                    .padding(10.dp),
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.5f),
            )
        }

    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) {page->
        when(page){
            0 -> WebViewContent(url = "https://www.google.com/")
            1 -> UserListScreen()
            2 -> TabContentScreen(data = " Welcome to Profile Screen")
        }

    }

}

@Composable
fun TabContentScreen(data: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = data, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout() {
    val pagerState = rememberPagerState(pageCount = 3)
    Column {
        Tabs(pagerState = pagerState)
        TabContent(pagerState = pagerState)
    }
}

@Composable
fun FirstScreen(
    navController: NavController

) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to First Screen",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Click on below button to navigate to Second Screen",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                navController.navigate(Screens.HomeScreen.route)
            }
        ) {
            Text(text = "Navigate to Second Screen")
        }
    }

}

@Composable
fun WebViewContent(url: String) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            webViewClient = WebViewClient()
        }
    }
    var isLoading by remember { mutableStateOf(true) }
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.loadUrl(url)
                view.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        isLoading = false
                    }
                }
            }
        )
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Green
                )
            }
        }
    }
    DisposableEffect(webView) {
        onDispose {
            webView.destroy()
        }
    }
}








