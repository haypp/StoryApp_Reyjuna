package com.haypp.storyapp_reyjuna

import com.haypp.storyapp_reyjuna.data.*

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStory> {
        val item = arrayListOf<ListStory>()
        for (i in 0 until 10) {
            val story = ListStory(
                "siapa aja",
                "cek lokasi",
                "https://images.pexels.com/photos/3861972/pexels-photo-3861972.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "2022-11-02",
                106.64356,
                -6.1335033,
                "user-XNkh2yhu1ETa8Wvt",
            )
            item.add(story)
        }
        return item
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