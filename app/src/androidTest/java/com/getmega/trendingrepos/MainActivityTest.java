package com.getmega.trendingrepos;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest extends TestCase {

    //launching the activity :
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTest = new ActivityScenarioRule<MainActivity>(MainActivity.class);
//    private ActivityScenario<MainActivity> mainActivity =null;

    public void setUp() throws Exception {
        super.setUp();


//        mainActivity = mActivityTest.getScenario();

    }

    @Test
    public void testUser(){

        //check on click

        //preform button click
//        View view = mActivityTest.onView.(with(R.id.cardview1));
//     onView(withId(R.id.cardview1)).check(matches(isDisplayed()));
        onView(withId(R.id.shimmerFrameLayout)).check(matches(isDisplayed()));
//     mainActivity.onView(withText(R.id.cardview1)).check(matches(isDisplayed()));

//        assertNotNull(view);

//        Espresso.onView(withId(R.id.actionbarmenu)).perform(click());
//        Espresso.onView(withId(R.menu.menu)).perform()


    }

    public void tearDown() throws Exception {
//        mainActivity=null;
    }
}