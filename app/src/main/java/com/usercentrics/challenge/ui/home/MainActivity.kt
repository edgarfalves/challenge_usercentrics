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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usercentrics.challenge.R
import com.usercentrics.challenge.consentlib.ConsentBanner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConsentBanner.initializeUsercentricsSDK(context = this)
        setContent {
            MainScreen(
                consentScoreState = viewModel.totalConsentScore,
                onClick = { viewModel.showConsentBanner(context = this) },
            )
        }
    }
}

@Composable
fun MainScreen(consentScoreState: StateFlow<Int>, onClick: () -> Unit) {
    val consentScore = consentScoreState.collectAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ConsentLabel(
            modifier = Modifier.align(Alignment.Center),
            consentScore = consentScore,
        )
        ConsentBannerBtn(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = onClick,
        )
    }
}

@Composable
fun ConsentLabel(modifier: Modifier, consentScore: Int) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CreateText(
            text = consentScore.toString(),
            fontSize = 160.sp,
        )

        CreateText(
            text = stringResource(id = R.string.label_consent_string),
            fontSize = 40.sp,
        )
    }
}

@Composable
fun ConsentBannerBtn(modifier: Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp),
        onClick = { onClick() },
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
    MainScreen(consentScoreState = MutableStateFlow(0), onClick = {})
}