package dev.shreyaspatil.foodium.ui.details

import androidx.compose.animation.animate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.shreyaspatil.foodium.R
import dev.shreyaspatil.foodium.model.Post
import dev.shreyaspatil.foodium.ui.theme.FoodiumTheme
import kotlin.math.min

@Composable
fun PostDetails(
    postDetailsViewModel: PostDetailsViewModel,
    postId: Int,
    onNavIconClick: () -> Unit,
    onShareIconClick: (Post) -> Unit
) {
    val post by postDetailsViewModel.getPost(postId).asFlow()
        .collectAsState(initial = Post(id = -1))
    if (post.id != -1) {
        val scrollState = rememberScrollState()
        FoodiumTheme {
            Scaffold { innerPadding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    PostDetailBody(
                        post = post,
                        scrollState = scrollState,
                        modifier = Modifier.padding(innerPadding)
                    )
                    PostDetailsAppbar(
                        post,
                        scrollState,
                        onNavIconClick,
                        onShareIconClick
                    )
                }
            }
        }
    }
}


@Composable
private fun PostDetailsAppbar(
    post: Post,
    scrollState: ScrollState,
    onNavIconClick: () -> Unit,
    onShareIconClick: (Post) -> Unit
) {
    /*
     To mimic collapsing toolbar behaviour, we animate in and out the `title` text.
     */
    val titleOpacity = min(scrollState.value / 250, 1f) == 1f
    val animatedOpacity = animate(if (titleOpacity) 1f else 0f)

    Surface(
        color = MaterialTheme.colors.background.copy(alpha = animatedOpacity),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            IconButton(onClick = onNavIconClick,modifier = Modifier.padding(horizontal = 8.dp)) {
                Icon(
                    asset = vectorResource(id = R.drawable.ic_arrow_back_24)
                )
            }
            Text(
                "Foodium",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(horizontal = 16.dp).drawOpacity(animatedOpacity)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onShareIconClick(post) }) {
                Icon(
                    asset = vectorResource(id = R.drawable.ic_share)
                )
            }
        }
    }
}

@Composable
private fun PostDetailBody(post: Post, scrollState: ScrollState, modifier: Modifier = Modifier) {
    ScrollableColumn(scrollState = scrollState, modifier = modifier) {
        val horizontalPadding = 16.dp
        val verticalPadding = 4.dp
        CoilImage(
            data = post.imageUrl ?: "",
            fadeIn = true,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth().height(192.dp)
                .padding(bottom = verticalPadding + 4.dp)
        )
        Text(
            post.title ?: "",
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
        ) {
            Icon(asset = vectorResource(id = R.drawable.ic_person))
            ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                Text(
                    post.author ?: "",
                    style = MaterialTheme.typography.body2
                )
            }

        }
        Text(
            post.body ?: "",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding)
        )
        val extraPadding = ConfigurationAmbient.current.screenHeightDp * 0.75
        Spacer(modifier = Modifier.fillMaxWidth().height(extraPadding.dp))
    }
}