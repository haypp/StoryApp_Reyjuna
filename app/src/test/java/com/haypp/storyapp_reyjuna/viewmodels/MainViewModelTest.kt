package com.haypp.storyapp_reyjuna.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.haypp.storyapp_reyjuna.DataDummy
import com.haypp.storyapp_reyjuna.MainDispatcherRule
import com.haypp.storyapp_reyjuna.adaptor.StoryAdaptor
import com.haypp.storyapp_reyjuna.data.*
import com.haypp.storyapp_reyjuna.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    @Mock
    private val dummy = DataDummy.generateDummyStoryResponse()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get List Story is Successful`() = runTest {
        val mDummyStory = dummy
        val mData: PagingData<ListStory> = PagingTestStorySources.snapshot(mDummyStory)
        val mStory = MutableLiveData<PagingData<ListStory>>()

        mStory.postValue(mData)
        `when`(mainViewModel.getStory()).thenReturn(mStory)

        val mActualStoryResponse: PagingData<ListStory> =
            mainViewModel.getStory().getOrAwaitValue()

        val mDiffer = AsyncPagingDataDiffer(
            diffCallback = StoryAdaptor.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        mDiffer.submitData(mActualStoryResponse)

        verify(mainViewModel).getStory()
        assertNotNull(mDiffer.snapshot())
        assertEquals(mDummyStory.size, mDiffer.snapshot().size)
    }
}

class PagingTestStorySources:
    PagingSource<Int, LiveData<List<ListStory>>>() {
    companion object {
        fun snapshot(mItems: List<ListStory>): PagingData<ListStory> {
            return PagingData.from(mItems)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStory>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStory>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}