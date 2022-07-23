package dev.sasikanth.twine.auth

internal object AuthConstants {
  const val AUTH_AUTHORIZE_URI = "https://twitter.com/i/oauth2/authorize"
  const val AUTH_TOKEN_URI = "https://api.twitter.com/2/oauth2/token"
  const val AUTH_REDIRECT_URI = "dev.sasikanth.twine://auth-redirect"

  const val SCOPE_TWEET_READ = "tweet.read"
  const val SCOPE_TWEET_WRITE = "tweet.write"
  const val SCOPE_USERS_READ = "users.read"
  const val SCOPE_OFFLINE_ACCESS = "offline.access"

  const val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
  const val MESSAGE_DIGEST_ALGORITHM = "SHA-256"

  const val AUTH_PREF_FILE = "twine_auth_pref"
  const val PREF_KEY_AUTH_STATE = "key_twine_auth_state"
}
