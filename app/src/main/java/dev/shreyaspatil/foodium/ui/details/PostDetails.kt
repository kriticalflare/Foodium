package dev.shreyaspatil.foodium.ui.details

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.shreyaspatil.foodium.R
import dev.shreyaspatil.foodium.model.Post
import dev.shreyaspatil.foodium.ui.theme.FoodiumTheme

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
        FoodiumTheme {
            Scaffold(topBar = {
                PostDetailsAppbar(
                    post,
                    onNavIconClick,
                    onShareIconClick
                )
            }) { innerPadding ->
                PostDetailBody(post = post, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}


@Composable
private fun PostDetailsAppbar(
    post: Post,
    onNavIconClick: () -> Unit,
    onShareIconClick: (Post) -> Unit
) {
    TopAppBar(
        title = { Text("Foodium") },
        navigationIcon = {
            IconButton(onClick = onNavIconClick) {
                Icon(
                    asset = vectorResource(id = R.drawable.ic_arrow_back_24)
                )
            }
        },
        actions = {
            IconButton(onClick = { onShareIconClick(post) }) {
                Icon(
                    asset = vectorResource(id = R.drawable.ic_share)
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
private fun PostDetailBody(post: Post, modifier: Modifier = Modifier) {
    ScrollableColumn(modifier = modifier) {
        val horizontalPadding = 16.dp
        val verticalPadding = 4.dp
        CoilImage(
            data = post.imageUrl ?: "",
            fadeIn = true,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth().height(192.dp).padding(bottom = verticalPadding + 4.dp)
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding, vertical = verticalPadding)
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
    }
}