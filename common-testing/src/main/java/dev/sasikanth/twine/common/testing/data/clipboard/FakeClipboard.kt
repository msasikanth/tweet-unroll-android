package dev.sasikanth.twine.common.testing.data.clipboard

import dev.sasikanth.twine.data.clipboard.Clipboard

class FakeClipboard : Clipboard {

  private var text: String? = null

  override fun getText(): String? {
    return text
  }

  fun setText(text: String) {
    this.text = text
  }
}
