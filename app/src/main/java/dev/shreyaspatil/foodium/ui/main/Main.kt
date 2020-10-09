package dev.shreyaspatil.foodium.ui.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.shreyaspatil.foodium.R
import dev.shreyaspatil.foodium.model.Post
import dev.shreyaspatil.foodium.model.State
import dev.shreyaspatil.foodium.ui.theme.FoodiumTheme
import dev.shreyaspatil.foodium.utils.NetworkUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalAnimationApi
@Composable
fun MainContent(
    viewModel: MainViewModel,
    darkTheme: Boolean,
    toggleTheme: () -> Unit,
    onPostClicked: (Post) -> Unit
) {
    FoodiumTheme(darkTheme) {
        Scaffold(topBar = { FoodiumAppBar(toggleTheme) }) { innerPadding ->
            Surface(color = MaterialTheme.colors.background) {

                val context = ContextAmbient.current
                val isConnected by NetworkUtils.getNetworkLiveData(context).asFlow()
                    .collectAsState(initial = true)

                ConstraintLayout(PostsConstraints(isConnected)) {
                    PostsList(
                        mainViewModel = viewModel,
                        modifier = Modifier.layoutId("posts_list").padding(innerPadding),
                        onPostClicked
                    )
                    NetworkConnectivityIndicator(isConnected,modifier = Modifier.layoutId("network_indicator"))
                }
            }
        }
    }
}

@Composable
private fun PostsConstraints(isConnected: Boolean): ConstraintSet {

    if (isConnected) {
        return ConstraintSet {
            val postsList = createRefFor("posts_list")

            constrain(postsList) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        }
    }

    return ConstraintSet {
        val networkIndicator = createRefFor("network_indicator")
        val postsList = createRefFor("posts_list")

        constrain(networkIndicator) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)

        }
        constrain(postsList) {
            top.linkTo(networkIndicator.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
    }
}

@Composable
private fun FoodiumAppBar(toggleTheme: () -> Unit) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            Icon(
                vectorResource(id = R.drawable.ic_lightbulb),
                modifier = Modifier.padding(end = 16.dp).clickable(onClick = toggleTheme)
            )
        })
}

@ExperimentalCoroutinesApi
@Composable
private fun PostsList(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onPostClicked: (Post) -> Unit
) {
    val postsState by mainViewModel.postsLiveData.asFlow()
        .collectAsState(initial = State.loading())

    var posts: List<Post> by remember { mutableStateOf(emptyList()) }

    when (postsState) {
        is State.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize().then(modifier),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is State.Success -> {
            posts = (postsState as State.Success<List<Post>>).data
        }
        is State.Error -> {
            Toast.makeText(
                ContextAmbient.current,
                (postsState as State.Error<List<Post>>).message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    if (posts.isNotEmpty()) {
        LazyColumnForIndexed(items = posts, modifier = modifier) { index, post ->
            if (index == 0) {
                Divider(Modifier.fillMaxWidth())
                PostsItem(post, onPostClicked)
            } else {
                PostsItem(post, onPostClicked)
            }
        }
    }
}

@Composable
private fun PostsItem(post: Post, onPostClicked: (Post) -> Unit) {
    val imageSize = 82
    val imagePadding = 8
    val screenWidth = ConfigurationAmbient.current.screenWidthDp - imageSize - imagePadding
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = { onPostClicked(post) })
            .padding(vertical = 16.dp)
    ) {
        Column(modifier = Modifier.width(screenWidth.dp).padding(start = 8.dp)) {
            Text(post.title ?: "")
            ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                Text(post.author ?: "", style = MaterialTheme.typography.body2)
            }
        }
        CoilImage(
            data = post.imageUrl ?: "",
            fadeIn = true,
            loading = {
                Image(asset = vectorResource(R.drawable.ic_broken_image))
            },
            error = {
                Image(asset = vectorResource(R.drawable.ic_broken_image))
            },
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.size(imageSize.dp)
                .weight(1f)
                .padding(end = imagePadding.dp)
        )
    }
    Divider(Modifier.fillMaxWidth())
}

@ExperimentalAnimationApi
@Composable
private fun NetworkConnectivityIndicator(isConnected: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = !isConnected, enter = slideInVertically(), modifier = modifier) {
        Indicator()
    }
}

@Composable
private fun Indicator(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().background(Color.Red).then(modifier)
    ) {
        Text("No Connection", modifier = Modifier.padding(4.dp))
    }
}