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

package dev.shreyaspatil.foodium.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.setContent
import com.shreyaspatil.MaterialDialog.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import dev.shreyaspatil.foodium.R
import dev.shreyaspatil.foodium.model.Post
import dev.shreyaspatil.foodium.ui.details.PostDetailsActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getPosts()
        val onPostClicked = { post: Post ->
            val intent = Intent(this, PostDetailsActivity::class.java)
            intent.putExtra(PostDetailsActivity.POST_ID, post.id)
            startActivity(intent)
        }
        setContent {
            val currentTheme = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(currentTheme) }
            val toggleDarkTheme = {
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }
                AppCompatDelegate.setDefaultNightMode(mode)
                darkTheme = !darkTheme
            }
            MainContent(viewModel = mViewModel, darkTheme, toggleDarkTheme, onPostClicked)
        }
    }

//    /**
//     * Observe network changes i.e. Internet Connectivity
//     */
//    private fun handleNetworkChanges() {
//        NetworkUtils.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
//            if (!isConnected) {
//                mViewBinding.textViewNetworkStatus.text =
//                    getString(R.string.text_no_connectivity)
//                mViewBinding.networkStatusLayout.apply {
//                    show()
//                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
//                }
//            } else {
//                if (mViewModel.postsLiveData.value is State.Error || mAdapter.itemCount == 0) {
//                    getPosts()
//                }
//                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
//                mViewBinding.networkStatusLayout.apply {
//                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))
//
//                    animate()
//                        .alpha(1f)
//                        .setStartDelay(ANIMATION_DURATION)
//                        .setDuration(ANIMATION_DURATION)
//                        .setListener(object : AnimatorListenerAdapter() {
//                            override fun onAnimationEnd(animation: Animator) {
//                                hide()
//                            }
//                        })
//                }
//            }
//        }
//    }

    override fun onBackPressed() {
        MaterialDialog.Builder(this)
            .setTitle(getString(R.string.exit_dialog_title))
            .setMessage(getString(R.string.exit_dialog_message))
            .setPositiveButton(getString(R.string.option_yes)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                super.onBackPressed()
            }
            .setNegativeButton(getString(R.string.option_no)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .build()
            .show()
    }
}
