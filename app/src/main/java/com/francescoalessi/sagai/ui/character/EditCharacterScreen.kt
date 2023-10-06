package com.francescoalessi.sagai.ui.character

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.francescoalessi.sagai.R
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.TextGenerationHost

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
            title = { Text("Edit Character") },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, "Back")
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
                value = characterField.name,
                onValueChange = { value ->
                    characterField =
                        characterField.copy(name = value)
                },
                label = {
                    Text("Character Name")
                }
            )
            OutlinedTextField(
                value = characterField.attributes,
                onValueChange = { value ->
                    characterField =
                        characterField.copy(attributes = value)
                },
                label = {
                    Text(
                        if(characterField.attributes.isBlank())
                            "Description"
                        else
                            "${characterField.name} is..."
                    )
                })
            Button(onClick = {
                viewModel.saveCharacter(Character(
                    id = characterField.id,
                    name = characterField.name,
                    attributes = characterField.attributes,
                    image = characterField.image
                ))
                onBackPressed()
            }) {
                Text("Save")
            }
        }
    }

}