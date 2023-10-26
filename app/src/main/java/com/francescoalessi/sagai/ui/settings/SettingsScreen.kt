package com.francescoalessi.sagai.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.francescoalessi.sagai.R
import com.francescoalessi.sagai.data.TextGenerationHost
import com.francescoalessi.sagai.data.isValidHost
import com.francescoalessi.sagai.data.isValidIP
import com.francescoalessi.sagai.data.isValidPort
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
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.settings)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.text_generation_webui_api_endpoint),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(
                        horizontal = 32.dp,
                        vertical = 4.dp
                    ),
                style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                singleLine = true,
                value = mutableHostFields.ipAddress,
                isError = (
                            !mutableHostFields.ipAddress.isValidIP()
                            && mutableHostFields.ipAddress.isNotBlank()
                        ),
                onValueChange = { value ->
                    mutableHostFields = mutableHostFields.copy(
                        ipAddress = value
                    )
                },
                label = {
                    Text(stringResource(R.string.ip_address))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                singleLine = true,
                value = mutableHostFields.ipPort,
                isError = (
                            !mutableHostFields.ipPort.isValidPort()
                            && mutableHostFields.ipPort.isNotBlank()
                        ),
                onValueChange = { value ->
                    mutableHostFields = mutableHostFields.copy(
                    ipPort = value
                )
            }, label = {
                Text(stringResource(R.string.port))
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                    viewModel.saveTextGenerationHost(
                        mutableHostFields
                    )
                },
                enabled = mutableHostFields.isValidHost()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

}