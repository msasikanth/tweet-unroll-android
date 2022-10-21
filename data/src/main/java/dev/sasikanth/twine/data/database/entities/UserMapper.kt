package dev.sasikanth.twine.data.database.entities

import dev.sasikanth.twine.data.api.models.UserPayload

fun User.Companion.from(payload: UserPayload) = User(
  id = payload.id,
  name = payload.name,
  username = payload.username,
  profileImage = payload.profileImage
)
