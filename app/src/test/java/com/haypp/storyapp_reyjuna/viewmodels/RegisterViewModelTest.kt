package com.haypp.storyapp_reyjuna.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.haypp.storyapp_reyjuna.data.*
import com.haypp.storyapp_reyjuna.DataDummy
import com.haypp.storyapp_reyjuna.etc.Result
import com.haypp.storyapp_reyjuna.getOrAwaitValue
import org.junit.Assert
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
class RegisterViewModelTest {
    private lateinit var regisViewModel: RegisterViewModel
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private var repo = mock(StoryRepository::class.java)
    private val dummyRegister = DataDummy.generateDummyRegisterResponse()

    @Before
    fun setUp() {
        regisViewModel = RegisterViewModel(repo)
    }

    @Test
    fun `when Login Should Not Null and Return Success Value`() {
        val expectedUser = MutableLiveData<Result<RegisterResponse>>()
        expectedUser.value = Result.Success(dummyRegister)
        `when`(repo.rRegister(name, email,pw)).thenReturn(expectedUser)

        val actualUser = regisViewModel.doRegister(name, email,pw).getOrAwaitValue()

        Mockito.verify(repo).rRegister(name, email,pw)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Success)
    }
    @Test
    fun `when Login Should Not Null and Return Error Value`() {
        val expectedUser = MutableLiveData<Result<RegisterResponse>>()
        expectedUser.value = Result.Error("true")
        `when`(repo.rRegister(name, email,pw)).thenReturn(expectedUser)

        val actualUser = regisViewModel.doRegister(name, email,pw).getOrAwaitValue()

        Mockito.verify(repo).rRegister(name, email,pw)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Error)
    }

    companion object {
        private const val name = "name"
        private const val email = "email"
        private const val pw = "password"
    }
}