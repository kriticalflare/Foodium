/*
 * MIT License
 *
 * Copyright (c) 2020 Shreyas Patil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.shreyaspatil.foodium.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.core.app.ShareCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.shreyaspatil.foodium.R
import dev.shreyaspatil.foodium.model.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PostDetailsActivity : AppCompatActivity() {

    val mViewModel: PostDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val postId = intent.extras?.getInt(POST_ID)
            ?: throw IllegalArgumentException("`postId` must be non-null")

        val navigateBack = {
            finish()
        }

        val shareMsg = { post: Post ->
            val shareMsg = getString(
                R.string.share_message,
                post.title,
                post.author
            )

            val intent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(shareMsg)
                .intent

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        setContent {
            PostDetails(
                postDetailsViewModel = mViewModel,
                postId,
                onNavIconClick = navigateBack,
                onShareIconClick = shareMsg
            )
        }
    }

    companion object {
        const val POST_ID = "postId"
    }
}
