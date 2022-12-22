package com.haypp.storyapp_reyjuna

import com.haypp.storyapp_reyjuna.data.*

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStory> {
    val items: MutableList<ListStory> = arrayListOf()
    for (i in 0..100) {
        val story = ListStory(
            i.toString(),"created + $i","name + $i","createAt + $i",i.toDouble(),i.toDouble(),"id + $i")
        items.add(story)
    }
    return items
    }

    fun generateDummyLoginResponse(): LoginResponse {
    return LoginResponse(
        LoginResult(
            "name",
            "id",
            "token"
        ),
        false,
        "message"
    )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
    return RegisterResponse(
        false,
        "success"
    )
    }

    fun generateDummyStoryWithMapsResponse(): AllStoriesResponse {
    val items: MutableList<ListStory> = arrayListOf()
    for (i in 0..100) {
        val story = ListStory(
            i.toString(),
            "created + $i",
            "name + $i",
            "description + $i",
            i.toDouble(),
            i.toDouble(),
            "id + $i",
        )
        items.add(story)
    }
    return AllStoriesResponse(
        false,
        "success",
        items as ArrayList<ListStory>
    )
    }

    fun generateDummyAddNewStoryResponse(): FileUploadResponse {
    return FileUploadResponse(
        false,
        "success"
    )
    }
    fun generateDummyGetUser(): UserModels {
        return UserModels(
            "namaku",
            "token",
            true
        )
    }
}