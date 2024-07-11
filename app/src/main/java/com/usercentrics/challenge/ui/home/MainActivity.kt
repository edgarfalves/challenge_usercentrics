package com.usercentrics.challenge.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usercentrics.challenge.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ConsentLabel(
            modifier = Modifier.align(Alignment.Center),
        )
        ConsentBannerBtn(
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
fun ConsentLabel(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CreateText(
            text = "0",
            fontSize = 80.sp,
        )

        CreateText(
            text = stringResource(id = R.string.label_consent_string),
            fontSize = 10.sp,
        )
    }
}

@Composable
fun ConsentBannerBtn(modifier: Modifier) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp),
        onClick = { },
    ) {
        CreateText(
            text = stringResource(id = R.string.label_show_consent_banner),
            fontSize = 15.sp,
        )
    }
}

@Composable
fun CreateText(text: String, fontSize: TextUnit) {
    Text(
        text = text,
        fontSize = fontSize,
    )
}

@Composable
@Preview
fun MainActivityPreview() {
    MainScreen()
}