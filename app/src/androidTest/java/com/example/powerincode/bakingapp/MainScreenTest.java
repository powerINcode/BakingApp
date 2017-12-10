package com.example.powerincode.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.powerincode.bakingapp.screens.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.powerincode.bakingapp.TestUtils.withLinearLayoutiewSize;
import static com.example.powerincode.bakingapp.TestUtils.withRecyclerViewSize;

/**
 * Created by powerman23rus on 09.12.17.
 * Enjoy ;)
 */
@RunWith(AndroidJUnit4.class)
public class MainScreenTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityRule =  new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mMainActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void check_recipe_count() throws Exception {
        onView(withId(R.id.rv_recipe_list)).check(matches(withRecyclerViewSize(4)));
    }

    @Test
    public void click_on_second_recipe() throws Exception {
        onView(withId(R.id.rv_recipe_list)).perform(actionOnItemAtPosition(0, click()));
    }

    @Test
    public void check_step_count() throws Exception {
        onView(withId(R.id.rv_recipe_list)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ll_recipe_menu)).check(matches(withLinearLayoutiewSize(8)));
    }

    @Test
    public void click_on_recipe_introduction_step() throws Exception {
        check_step_count();
        onView(hasSibling(withText("Recipe Introduction"))).perform(click());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
