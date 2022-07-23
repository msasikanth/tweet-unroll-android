package dev.sasikanth.twine.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

/**
 * A activity result contract that parses the OAuth2 login result
 * and outputs [TwineLogin.Result]
 *
 * @param intentBuilder: Provide the login intent to open, usually an web browser or custom tab
 */
class TwineLogin internal constructor(
  private val intentBuilder: () -> Intent
) : ActivityResultContract<Unit, TwineLogin.Result>() {

  override fun createIntent(context: Context, input: Unit?): Intent {
    return intentBuilder()
  }

  override fun parseResult(resultCode: Int, intent: Intent?): Result? {
    return intent?.let {
      Result(
        AuthorizationResponse.fromIntent(it),
        AuthorizationException.fromIntent(it)
      )
    }
  }

  data class Result(
    val response: AuthorizationResponse?,
    val error: AuthorizationException?
  )
}
