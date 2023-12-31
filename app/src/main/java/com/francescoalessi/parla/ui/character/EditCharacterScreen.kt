package com.francescoalessi.parla.ui.character

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.francescoalessi.parla.R
import com.francescoalessi.parla.data.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacterScreen(
    onBackPressed: () -> Unit,
    viewModel: EditCharacterViewModel
) {
    val context = LocalContext.current

    val character = viewModel.character.collectAsStateWithLifecycle(
        initialValue = Character(0, "", "")
    )

    var characterField by remember {
        mutableStateOf(character.value)
    }

    characterField = character.value

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        characterField = characterField.copy(
            image = uri
        )
        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(uri, flag)
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.edit_character)) },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, stringResource(R.string.back))
                }
            }
        )
    }) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .padding(paddingValues)
        ) {
            AsyncImage(
                model = characterField.image ?: R.drawable.baseline_account_circle_24,
                characterField.name,
                Modifier
                    .clip(CircleShape)
                    .size(128.dp)
                    .clickable {
                        launcher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentScale = ContentScale.FillBounds,
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = characterField.name,
                onValueChange = { value ->
                    characterField =
                        characterField.copy(name = value)
                },
                label = {
                    Text(stringResource(R.string.character_name))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = characterField.attributes,
                onValueChange = { value ->
                    characterField =
                        characterField.copy(attributes = value)
                },
                label = {
                        Text(
                            if(characterField.attributes.isBlank()) {
                                stringResource(R.string.description)
                            } else {
                                stringResource(R.string.character_is, characterField.name)
                            }
                        )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                enabled = characterField.name.isNotBlank()
                        && characterField.attributes.isNotBlank(),
                onClick = {
                viewModel.saveCharacter(Character(
                    id = characterField.id,
                    name = characterField.name,
                    attributes = characterField.attributes,
                    image = characterField.image
                ))
                onBackPressed()
            }) {
                Text(stringResource(R.string.save))
            }
        }
    }

}