package com.francescoalessi.sagai.ui.conversation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.francescoalessi.sagai.R
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ConversationDetailScreen(
    viewModel: ConversationViewModel,
    onBackPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val conversation by viewModel.conversation.collectAsStateWithLifecycle(initialValue = null)
    var lastMessageSent by remember { mutableStateOf("") }

    val textGenerationState by viewModel.textGenerationState.collectAsStateWithLifecycle(
        initialValue = UiState.Idle
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    if(textGenerationState is UiState.Error)
    {
        LaunchedEffect(textGenerationState) {
            snackbarHostState.showSnackbar(
                "Couldn't generate a response.",
                withDismissAction = true)
        }
    }

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = conversation?.character?.image,
                            contentDescription = stringResource(R.string.avatar),
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(32.dp),
                            contentScale = ContentScale.FillBounds,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(conversation?.character?.name ?: stringResource(R.string.no_name))
                    }
                        },
                navigationIcon = {
                    IconButton(onBackPressed) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.back))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        val messages =
            viewModel.messages.collectAsLazyPagingItems()
        ErrorSnackbar(uiState = textGenerationState) {
            viewModel.dismissError()  // Function to reset the error state
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .navigationBarsPadding()
        ) {
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                reverseLayout = true,
                state = listState
            ) {
                if (textGenerationState is UiState.Loading /*messages.loadState.refresh == LoadState.Loading ||
                    messages.loadState.append == LoadState.Loading*/) {
                    item {
                        BaseChatBubble(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.Start),
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = stringResource(
                                            R.string.is_typing,
                                            conversation?.character?.name
                                                ?: stringResource(R.string.no_name)
                                        ),
                                        modifier = Modifier.padding(16.dp),
                                        fontStyle = FontStyle.Italic
                                    )
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(18.dp)
                                    )
                                }
                            },
                            secondaryContainerColor = false
                        )
                    }
                    item {
                        BaseChatBubble(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.End),
                            content = {
                                Text(lastMessageSent,
                                    modifier = Modifier.padding(16.dp))
                            },
                            secondaryContainerColor = true
                        )
                    }
                }

                items(
                    count = messages.itemCount
                ) { index ->
                    val item = messages[index]
                    item?.let {
                        ChatMessage(
                            content = item.content,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(
                                    align =
                                    if (item.characterId == -1) // TODO: Change
                                        Alignment.End
                                    else
                                        Alignment.Start
                                ),
                            isUser = item.characterId == -1 //TODO: Change
                        )
                    }
                }
            }
            ChatInput(sendEnabled = textGenerationState !is UiState.Loading) { message ->
                lastMessageSent = message
                viewModel.sendMessage(message = message)
            }
        }
    }
}

@Composable
fun ErrorSnackbar(
    uiState: UiState,
    onDismiss: () -> Unit
) {
    if (uiState is UiState.Error) {
        Snackbar(
            action = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.dismiss))
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(uiState.message)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun reverseEnterAlwaysScrollBehavior(
    state: TopAppBarState = rememberTopAppBarState(),
    canScroll: () -> Boolean = { true },
    snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
    flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay()
): TopAppBarScrollBehavior =
    ReverseLayoutEnterAlwaysScrollBehavior(
        state = state,
        snapAnimationSpec = snapAnimationSpec,
        flingAnimationSpec = flingAnimationSpec,
        canScroll = canScroll
    )

@OptIn(ExperimentalMaterial3Api::class)
class ReverseLayoutEnterAlwaysScrollBehavior(
    override val state: TopAppBarState,
    override val snapAnimationSpec: AnimationSpec<Float>?,
    override val flingAnimationSpec: DecayAnimationSpec<Float>?,
    val canScroll: () -> Boolean = { true }
) : TopAppBarScrollBehavior {
    override val isPinned: Boolean = false
    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (!canScroll()) return Offset.Zero
                val prevHeightOffset = state.heightOffset
                state.heightOffset = state.heightOffset - available.y
                return if (prevHeightOffset != state.heightOffset) {
                    // We're in the middle of top app bar collapse or expand.
                    // Consume only the scroll on the Y axis.
                    available.copy(x = 0f)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (!canScroll()) return Offset.Zero
                state.contentOffset -= consumed.y
                if (state.heightOffset == 0f || state.heightOffset == state.heightOffsetLimit) {
                    if (available.y == 0f && consumed.y > 0f) {
                        // Reset the total content offset to zero when scrolling all the way down.
                        // This will eliminate some float precision inaccuracies.
                        state.contentOffset = 0f
                    }
                }
                state.heightOffset = state.heightOffset - consumed.y
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val superConsumed = super.onPostFling(consumed, available)
                return superConsumed + reverseSettleAppBar(
                    state,
                    available.y,
                    flingAnimationSpec,
                    snapAnimationSpec
                )
            }
        }
}

/**
 * Settles the app bar by flinging, in case the given velocity is greater than zero, and snapping
 * after the fling settles.
 */
@OptIn(ExperimentalMaterial3Api::class)
suspend fun reverseSettleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
): Velocity {
    // Check if the app bar is completely collapsed/expanded. If so, no need to settle the app bar,
    // and just return Zero Velocity.
    // Note that we don't check for 0f due to float precision with the collapsedFraction
    // calculation.
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 &&
            state.heightOffset > state.heightOffsetLimit
        ) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) { state.heightOffset = value }
        }
    }

    return Velocity(0f, remainingVelocity)
}