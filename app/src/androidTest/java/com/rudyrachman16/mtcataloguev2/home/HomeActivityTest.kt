package com.rudyrachman16.mtcataloguev2.home

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.rudyrachman16.mtcataloguev2.R
import com.rudyrachman16.mtcataloguev2.utils.IdlingResource
import com.rudyrachman16.mtcataloguev2.views.home.HomeActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeActivityTest {
    @Before
    fun setUp() {
        ActivityScenario.launch(HomeActivity::class.java)
        IdlingRegistry.getInstance().register(IdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.idlingResource)
    }

    @Test
    fun testLoadMovies() {
        onView(withId(R.id.tabRecycler)).check(matches(isDisplayed()))
        onView(withId(R.id.tabRecycler)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5)
        )
    }

    @Test
    fun testLoadTvShow() {
        onView(withText(R.string.tv_shows)).perform(click())
        onView(withId(R.id.tabRecycler)).check(matches(isDisplayed()))
        onView(withId(R.id.tabRecycler)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5)
        )
    }

    @Test
    fun testLoadDetail() {
        onView(withId(R.id.tabRecycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        onView(withId(R.id.detType)).check(matches(isDisplayed()))
        onView(withId(R.id.detAppBarLayout)).perform(swipeUp())
        onView(withId(R.id.detDesc)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoadFavorite() {
        onView(withId(R.id.favoriteClick)).perform(click())
        onView(withId(R.id.tabRecycler)).check(matches(isDisplayed()))
        onView(withId(R.id.tabRecycler)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5)
        )
    }

    @Test
    fun testAddCheckRemoveFavorite() {
        // make there is no item in favorite list
        onView(withId(R.id.tabRecycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        onView(withId(R.id.detType)).check(matches(isDisplayed()))
        val title = getTitle(onView(withId(R.id.toolbar)))
        onView(withId(R.id.detExpand)).perform(click())
        onView(withId(R.id.detFav)).perform(click())
        pressBack()
        onView(withId(R.id.favoriteClick)).perform(click())
        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(title)).perform(click())
        onView(withId(R.id.detExpand)).perform(click())
        onView(withId(R.id.detFav)).perform(click())
        pressBack()
    }

    private fun getTitle(matcher: ViewInteraction): String {
        var got = String()
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(Toolbar::class.java)

            override fun getDescription(): String = "Description of the Text View?"

            override fun perform(uiController: UiController?, view: View?) {
                val tv = view as Toolbar
                got = tv.title.toString()
            }
        })
        return got
    }
}