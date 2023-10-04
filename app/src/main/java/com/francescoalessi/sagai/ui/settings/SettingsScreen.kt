package com.francescoalessi.sagai.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.francescoalessi.sagai.data.TextGenerationHost
import com.francescoalessi.sagai.ui.common.AppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
) {
    val textGenerationHost = viewModel.textGenerationHost
        .collectAsStateWithLifecycle(
            initialValue = TextGenerationHost(0, "", "")
        )

    var mutableHostFields by remember {
        mutableStateOf(textGenerationHost.value)
    }

    mutableHostFields = textGenerationHost.value

    AppScaffold(
        navController = navController,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Settings") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                singleLine = true,
                value = mutableHostFields.ip4address,
                onValueChange = { value ->
                    mutableHostFields = mutableHostFields.copy(
                        ip4address = value
                    )
            }, label = {
                Text("IP Address")
            })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                singleLine = true,
                value = mutableHostFields.ip4port,
                onValueChange = { value ->
                    mutableHostFields = mutableHostFields.copy(
                    ip4port = value
                )
            }, label = {
                Text("Port")
            })
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                viewModel.saveTextGenerationHost(
                    mutableHostFields
                )
            }) {
                Text("Save")
            }
        }
    }

}