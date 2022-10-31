package com.example.movieapp.ui.frgaments.movies

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.ui.RecyclerViewItemCountAssertion
import com.example.domain.entities.ErrorResponseDomainEntity
import com.example.domain.entities.ResultsDomainEntity
import com.example.domain.repository.MoviesRepository
import com.example.domain.utils.DataState
import com.example.movieapp.R
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoviesFragmentTest {

    private lateinit var scenario: FragmentScenario<MoviesFragment>
    private var mockMoviesViewModel: MoviesViewModel = spyk()
    private val moviesFragment: MoviesFragment = MoviesFragment()
    private lateinit var fakeFragmentFactory: FragmentFactory
    private val fakeMoviesViewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = mockMoviesViewModel as T
    }
    @Before
    fun setUp() {
        fakeFragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return moviesFragment
            }
        }

        moviesFragment.provideMoviesViewModelFactory(fakeMoviesViewModelFactory)
    }

    @Test
    fun `Given_SuccessfulDataState_with_fakeMoviesList_when_getMostPopularMovies_then_check_ViewState`() {
        //arranges
        val fakeMoviesList = mutableListOf<ResultsDomainEntity>()
        fakeMoviesList.add(
            ResultsDomainEntity(
                1,
                "title",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )

        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.moveToState(newState = Lifecycle.State.STARTED)

        //Assertions
        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(1000)

        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun `Given_SuccessfulDataState_with_fakeMoviesList_when_getMostPopularMoviesSuccessfully_then_must_recyclerViewItemCount_equal_ItemCount_of_fakeMoviesList`() {
        //arranges
        val fakeMoviesList = mutableListOf<ResultsDomainEntity>()
        fakeMoviesList.add(
            ResultsDomainEntity(
                1,
                "title",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )
        fakeMoviesList.add(
            ResultsDomainEntity(
                2,
                "title2",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )

        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.moveToState(newState = Lifecycle.State.STARTED)
        //Assertions
        Thread.sleep(1000)
        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.rv_movies)).check(RecyclerViewItemCountAssertion(fakeMoviesList.size))
    }

    @Test
    fun `Given_EmptyDataState_when_getMostPopularMovies_then_check_ViewState`() {
        //arranges
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.moveToState(newState = Lifecycle.State.STARTED)

        //Assertions
        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(1000)

        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun `Given_EmptyDataState_with_message_when_getMostPopularMovies_then_tvEdgeCase_must_have_same_message`() {
        //arranges
        val message = "test message: sorry!!No data found"
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Empty(message)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.moveToState(newState = Lifecycle.State.STARTED)

        //Assertions
        Thread.sleep(1000)
        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.tv_edge_case)).check(matches(withText(message)))
    }

    @Test
    fun `Given_FailureDataState_when_getMostPopularMovies_then_check_ViewState`() {
        //arranges
        val fakeErrorResponse = ErrorResponseDomainEntity(404, "fake test", false)
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Failure(fakeErrorResponse)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.moveToState(newState = Lifecycle.State.STARTED)

        //Assertions
        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(1000)

        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun `Given_FailureDataState_with_ErrorResponse_when_getMostPopularMovies_then_tvEdgeCase_must_have_same_ErrorResponseMessage`() {
        //arranges
        val errorMessage = "test error message"
        val fakeErrorResponse = ErrorResponseDomainEntity(404, errorMessage, false)
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Failure(fakeErrorResponse)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.moveToState(newState = Lifecycle.State.STARTED)

        //Assertions
        Thread.sleep(1000)
        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.tv_edge_case)).check(matches(withText(errorMessage)))
    }


    //navigation
    @Test
    fun given_MovieList_when_clickOn_recyclerViewItem_then_must_navigate_to_MovieDetailsFragment() {
        //arranges
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val fakeMoviesList = mutableListOf<ResultsDomainEntity>()
        fakeMoviesList.add(
            ResultsDomainEntity(
                1,
                "title",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )


        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository


        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.movies_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        //action

        scenario.moveToState(newState = Lifecycle.State.STARTED)
        Thread.sleep(1000)
        //click on recycler item
        onView(withId(R.id.rv_movies)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        //Assertion
        Assert.assertEquals(
            R.id.movieDetailsFragment,
            navController.currentDestination?.id
        )
    }

    @Test
    fun navigate_to_MovieDetailsFragment_and_call_NavController_popBackStack_then_must_back_to_MovieFragment() {
        //arranges
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val fakeMoviesList = mutableListOf<ResultsDomainEntity>()
        fakeMoviesList.add(
            ResultsDomainEntity(
                1,
                "title",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )


        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository


        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.movies_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        //action

        scenario.moveToState(newState = Lifecycle.State.STARTED)
        Thread.sleep(1000)
        //click on recycler item
        onView(withId(R.id.rv_movies)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        //press popBackStack
        scenario.onFragment {
            navController.popBackStack()
        }

        //Assertion
        Assert.assertEquals(
            R.id.moviesFragment,
            navController.currentDestination?.id
        )
    }


    //option menu
    @Test
    fun check_mostPopularMenuItem_isDisplayed_when_click_on_optionMenu() {
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.onFragment {
            it.requireActivity().openOptionsMenu()
        }

        onView(withText(R.string.most_popular)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun check_topRatedMenuItem_isDisplayed_when_click_on_optionMenu() {
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.onFragment {
            it.requireActivity().openOptionsMenu()
        }

        onView(withText(R.string.top_rated)).check(
            matches(
                isDisplayed()
            )
        )
    }


    //Top rated
    @Test
    fun `Given_SuccessfulDataState_with_fakeMoviesList_when_getMostPopularMovies_and_click_on_topRatedMenuItem_then_must_invoke_fetchTopRatedMovies`() {
        //arranges
        val fakeMoviesList = mutableListOf<ResultsDomainEntity>()
        fakeMoviesList.add(
            ResultsDomainEntity(
                1,
                "title",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )

        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )

        scenario.moveToState(Lifecycle.State.STARTED)
        Thread.sleep(1000)
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { it.requireActivity().openOptionsMenu() }

        onView(withText(R.string.top_rated)).perform(click())

        //Assertions
        verify { mockMoviesViewModel.fetchTopRatedMovies(any()) }
    }

    @Test
    fun `Given_SuccessfulDataState_with_fakeMoviesList_when_click_on_topRatedMenuItem_then_check_ViewState`() {
        //arranges
        val fakeMoviesList = mutableListOf<ResultsDomainEntity>()
        fakeMoviesList.add(
            ResultsDomainEntity(
                1,
                "title",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )

        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )

        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(1000)
        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { it.requireActivity().openOptionsMenu() }

        onView(withText(R.string.top_rated)).perform(click())

        //Assertions
        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(1000)

        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun `Given_SuccessfulDataState_with_fakeMoviesList_when_fetchTopRatedMovies_then_must_recyclerViewItemCount_equal_ItemCount_of_fakeMoviesList`() {
        //arranges
        val fakeMoviesList = mutableListOf<ResultsDomainEntity>()
        fakeMoviesList.add(
            ResultsDomainEntity(
                1,
                "title",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )
        fakeMoviesList.add(
            ResultsDomainEntity(
                2,
                "title2",
                "sub_title",
                "overview",
                "posterPath",
                "200",
                10f
            )
        )

        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Successful(fakeMoviesList)
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )

        scenario.onFragment { it.requireActivity().openOptionsMenu() }
        onView(withText(R.string.top_rated)).perform(click())

        //Assertions
        Thread.sleep(1000)
        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.rv_movies)).check(RecyclerViewItemCountAssertion(fakeMoviesList.size))
    }

    @Test
    fun `Given_EmptyDataState_when_fetchTopRatedMovies_then_check_ViewState`() {
        //arranges
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Empty()
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )

        scenario.onFragment { it.requireActivity().openOptionsMenu() }
        onView(withText(R.string.top_rated)).perform(click())

        //Assertions
        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(1000)

        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun `Given_EmptyDataState_with_message_when_fetchTopRatedMovies_then_tvEdgeCase_must_have_same_message`() {
        //arranges
        val message = "test message: sorry!!No data found"
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Empty(message)
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.onFragment { it.requireActivity().openOptionsMenu() }
        onView(withText(R.string.top_rated)).perform(click())
        //Assertions
        Thread.sleep(1000)
        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.tv_edge_case)).check(matches(withText(message)))
    }

    @Test
    fun `Given_FailureDataState_when_fetchTopRatedMovies_then_check_ViewState`() {
        //arranges
        val fakeErrorResponse = ErrorResponseDomainEntity(404, "fake test", false)
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Failure(fakeErrorResponse)
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.onFragment { it.requireActivity().openOptionsMenu() }
        onView(withText(R.string.top_rated)).perform(click())

        //Assertions
        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(1000)

        onView(withId(R.id.progress_loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.rv_movies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun `Given_FailureDataState_with_ErrorResponse_when_fetchTopRatedMovies_then_tvEdgeCase_must_have_same_ErrorResponseMessage`() {
        //arranges
        val errorMessage = "test error message"
        val fakeErrorResponse = ErrorResponseDomainEntity(404, errorMessage, false)
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                Thread.sleep(1000)
                return DataState.Failure(fakeErrorResponse)
            }

        }
        //mock real MoviesRepository to fake fakeMoviesRepository
        every { mockMoviesViewModel.moviesRepository } returns fakeMoviesRepository

        //Action
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MovieApp,
            factory = fakeFragmentFactory
        )
        scenario.onFragment { it.requireActivity().openOptionsMenu() }
        onView(withText(R.string.top_rated)).perform(click())

        //Assertions
        Thread.sleep(1000)
        onView(withId(R.id.tv_edge_case)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.tv_edge_case)).check(matches(withText(errorMessage)))
    }
}