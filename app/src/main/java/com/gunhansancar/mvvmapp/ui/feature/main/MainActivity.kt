package com.gunhansancar.mvvmapp.ui.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gunhansancar.mvvmapp.network.catfacts.CatFact
import com.gunhansancar.mvvmapp.ui.theme.MVVMAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MVVMAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Screen(viewModel: MainViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val uiStateValue = uiState.value) {
        is MainUiState.Success -> MainList(list = uiStateValue.facts)
        is MainUiState.Error -> Message(name = uiStateValue.message)
        MainUiState.Init -> Message(name = "Welcome")
        MainUiState.Loading -> Message(name = "Loading")
    }
}

@Composable
fun MainList(list: List<CatFact>, modifier: Modifier = Modifier) {
    LazyColumn(
        Modifier.padding(16.dp)
    ) {
        itemsIndexed(list) { index, item ->
            CatFactRow(item)

            if (index < list.lastIndex)
                Divider(
                    color = Color.Black.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
        }
    }
}

@Composable
fun CatFactRow(fact: CatFact, modifier: Modifier = Modifier) {
    Text(
        text = "${fact.type.uppercase()} - ${fact.text}",
        modifier = modifier
    )
}

@Composable
fun Message(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name!",
        modifier = modifier.then(Modifier.padding(16.dp))
    )
}
