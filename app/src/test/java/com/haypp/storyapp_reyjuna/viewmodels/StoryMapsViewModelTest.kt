package com.haypp.storyapp_reyjuna.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.haypp.storyapp_reyjuna.DataDummy
import com.haypp.storyapp_reyjuna.data.AllStoriesResponse
import com.haypp.storyapp_reyjuna.data.StoryRepository
import com.haypp.storyapp_reyjuna.etc.Result
import com.haypp.storyapp_reyjuna.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryWithMapsViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mapsViewModel: StoryMapsViewModel

    @Mock
    private var repo = mock(StoryRepository::class.java)
    private val dummy = DataDummy.generateDummyStoryWithMapsResponse()

    @Before
    fun setUp() {
        mapsViewModel = StoryMapsViewModel(repo)
    }

    @Test
    fun `when Get Story With Maps Should Not Null and Return Success`() {
        val expectedStory = MutableLiveData<Result<AllStoriesResponse>>()
        expectedStory.value = Result.Success(dummy)
        `when`(repo.getStoryLoc(TOKEN)).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoryLocation(TOKEN).getOrAwaitValue()

        Mockito.verify(repo).getStoryLoc(TOKEN)
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Success)
    }
    @Test
    fun `when Get Story With no Network and Return Error`() {
        val expectedStory = MutableLiveData<Result<AllStoriesResponse>>()
        expectedStory.value = Result.Error("Error")
        `when`(repo.getStoryLoc(TOKEN)).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoryLocation(TOKEN).getOrAwaitValue()

        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Error)
        Mockito.verify(repo).getStoryLoc(TOKEN)
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN"
    }
}
