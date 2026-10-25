package com.example

import android.content.ClipboardManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.ClipboardHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `verify app name and package id`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SN Creativity", appName)
        assertEquals("com.sncreator.app", context.packageName)
    }

    @Test
    fun `verify clipboard copy helper copies text`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val copied = ClipboardHelper.copyToClipboard(context, "Prompt", "Neon 3D Thumbnail Prompt")
        assertTrue(copied)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        assertEquals("Neon 3D Thumbnail Prompt", clipboard.primaryClip?.getItemAt(0)?.text.toString())
    }
}
