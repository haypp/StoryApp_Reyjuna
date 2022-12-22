package com.haypp.storyapp_reyjuna.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.haypp.storyapp_reyjuna.DataDummy
import com.haypp.storyapp_reyjuna.MainDispatcherRule
import com.haypp.storyapp_reyjuna.data.*
import com.haypp.storyapp_reyjuna.etc.Result
import com.haypp.storyapp_reyjuna.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel

    @Mock
    private val repo = mock(StoryRepository::class.java)
    private val dummyLogin = DataDummy.generateDummyLoginResponse()
    private val dummyUser = DataDummy.generateDummyGetUser()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repo)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `save user information success` () = runTest {
        loginViewModel.saveUser(dummyUser)
        Mockito.verify(repo).saveUserData(dummyUser)
    }
    @Test
    fun `remove user information success `() = runTest {
        loginViewModel.logout()
        Mockito.verify(repo).logout()
    }

    @Test
    fun `when Login Should Not Null and Return Success Value`() {
        val expectedUser = MutableLiveData<Result<LoginResponse>>()
        expectedUser.value = Result.Success(dummyLogin)
        `when`(repo.rLogin(LoginRequest(email, pw))).thenReturn(expectedUser)

        val actualUser = loginViewModel.meLogin(LoginRequest(email, pw)).getOrAwaitValue()

        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Success)
        Mockito.verify(repo).rLogin(LoginRequest(email, pw))
    }
    @Test
    fun `when Login Null and Return Error Value`() {
        val expectedUser = MutableLiveData<Result<LoginResponse>>()
        expectedUser.value = Result.Error("Error")
        `when`(repo.rLogin(LoginRequest(null, null))).thenReturn(expectedUser)

        val actualUser = loginViewModel.meLogin(LoginRequest(null, null)).getOrAwaitValue()

        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Error)
        Mockito.verify(repo).rLogin(LoginRequest(null, null))
    }

    companion object {
        private const val email = "email"
        private const val pw = "password"
    }
}
