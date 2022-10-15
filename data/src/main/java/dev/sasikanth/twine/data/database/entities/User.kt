package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
  @PrimaryKey
  val id: String,
  val name: String,
  val username: String,
  @ColumnInfo(name = "profile_image_url")
  val profileImage: String
)
