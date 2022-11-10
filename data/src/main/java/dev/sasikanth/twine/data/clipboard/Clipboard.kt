package dev.sasikanth.twine.data.clipboard

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface Clipboard {
  fun getText(): String?
}

class AndroidClipboard @Inject constructor(
  @ApplicationContext context: Context
) : Clipboard {

  private val clipboardManager by lazy {
    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  }

  val content: String?
    get() {
      val clipItem = clipboardManager.primaryClip ?: return null
      val isPrimaryClipText =
        clipboardManager.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) ?: return null

      return if (isPrimaryClipText) {
        clipItem.getItemAt(0).text.toString()
      } else {
        null
      }
    }

  override fun getText(): String? {
    val clipItem = clipboardManager.primaryClip ?: return null
    val isPrimaryClipText =
      clipboardManager.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) ?: return null

    return if (isPrimaryClipText) {
      clipItem.getItemAt(0).text.toString()
    } else {
      null
    }
  }
}
