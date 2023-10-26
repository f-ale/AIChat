package com.francescoalessi.sagai.ui.home

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.francescoalessi.sagai.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationListItem(
    modifier: Modifier = Modifier,
    characterName: String,
    characterImage: Uri?,
    lastMessage: String,
    onItemClicked: () -> Unit,
    onItemDeleted: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
        DropdownMenuItem(text = { Text(stringResource(R.string.delete)) }, onClick = {
            onItemDeleted()
            isExpanded = false
        })
    }
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onItemClicked,
                onLongClick = {
                    isExpanded = true
                }
            )
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Spacer(modifier = Modifier.size(4.dp))
        if(characterImage != null)
        {
            AsyncImage(
                model = characterImage,
                characterName,
                Modifier
                    .clip(CircleShape)
                    .size(48.dp),
                contentScale = ContentScale.FillBounds,
            )
        }
        else {
            Icon(
                Icons.Default.AccountCircle,
                characterName,
                modifier = Modifier.size(64.dp)
            )
        }
        Spacer(modifier = Modifier.size(4.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(38.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                characterName,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                lastMessage,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}