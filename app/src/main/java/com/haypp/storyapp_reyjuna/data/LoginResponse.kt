package com.haypp.storyapp_reyjuna.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LoginResult(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class LoginRequest(
	@SerializedName("email")
	@Expose
	val email: String? = null,

	@SerializedName("password")
	@Expose
	val password: String? = null
)

data class UserModels(
	val name: String,
	val email: String,
	val password: String,
	val isLogin: Boolean
)
data class RegisterResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
data class AllStoriesResponse (
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("listStory")
	val ListStory: ArrayList<ListStory>
)

@Parcelize
data class ListStory(
	@field:SerializedName("name")
	var name: String?,

	@field:SerializedName("description")
	var description: String?,

	@field:SerializedName("photoUrl")
	var photoUrl: String?,

	@field:SerializedName("createdAt")
	var createdAt: Date?,
) : Parcelable

data class FileUploadResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
