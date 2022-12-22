package com.haypp.storyapp_reyjuna.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.haypp.storyapp_reyjuna.DataDummy
import com.haypp.storyapp_reyjuna.data.FileUploadResponse
import com.haypp.storyapp_reyjuna.data.StoryRepository
import com.haypp.storyapp_reyjuna.data.UserModels
import com.haypp.storyapp_reyjuna.etc.Result
import com.haypp.storyapp_reyjuna.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var addStoryViewModel: AddStoryViewModel

    @Mock
    private var repo = mock(StoryRepository::class.java)
    private val dummy= DataDummy.generateDummyAddNewStoryResponse()
    private val dummyUser = DataDummy.generateDummyGetUser()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(repo)
    }

    @Test
    fun `when Upload Should Not Null and Return Success`() {
        val desc = "ini desc".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val fileImg: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImageFile
        )

        val expectedStory = MutableLiveData<Result<FileUploadResponse>>()
        expectedStory.value = Result.Success(dummy)
        Mockito.`when`(repo.addStory(TOKEN, fileImg, desc, LAT, LON)).thenReturn(expectedStory)

        val actualStory = addStoryViewModel.addStory(TOKEN, fileImg, desc, LAT, LON).getOrAwaitValue()

        Mockito.verify(repo).addStory(TOKEN, fileImg, desc, LAT, LON)
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Success)
    }

    @Test
    fun `when Upload but no networks and Return Error`() {
        val desc = "ini desc".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val fileImg: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImageFile
        )

        val expectedStory = MutableLiveData<Result<FileUploadResponse>>()
        expectedStory.value = Result.Error("Error")
        Mockito.`when`(repo.addStory(TOKEN, fileImg, desc, LAT, LON)).thenReturn(expectedStory)

        val actualStory = addStoryViewModel.addStory(TOKEN, fileImg, desc, LAT, LON).getOrAwaitValue()

        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Error)
        Mockito.verify(repo).addStory(TOKEN, fileImg, desc, LAT, LON)
    }
    @Test
    fun `when Get User With Should Not Null and Return Success`() {
        val expectedGetUser = MutableLiveData<UserModels>()
        expectedGetUser.value = dummyUser
        Mockito.`when`(repo.getUserData()).thenReturn(expectedGetUser)

        val actualUser = addStoryViewModel.getUser().getOrAwaitValue()

        assertNotNull(actualUser)
        assertEquals(dummyUser, actualUser)
        Mockito.verify(repo).getUserData()
    }

    companion object {
        private const val LAT = 1.23
        private const val LON = 1.23
        private const val TOKEN = "Bearer TOKEN"
    }
}