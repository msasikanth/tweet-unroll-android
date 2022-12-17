package dev.sasikanth.twine.data.database.entities.mappers

import dev.sasikanth.twine.data.api.models.UserPayload
import dev.sasikanth.twine.data.database.entities.User

fun User.Companion.from(
  conversationId: String,
  payload: UserPayload
) = User(
  id = payload.id,
  name = payload.name,
  username = payload.username,
  profileImage = payload.profileImage,
  conversationId = conversationId
)
