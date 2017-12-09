package com.example.powerincode.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by powerman23rus on 09.12.17.
 * Enjoy ;)
 */

public class TestUtils {
    public static Matcher<View> withRecyclerViewSize(final int size) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                final int actualListSize = ((RecyclerView) view).getAdapter().getItemCount();
                return actualListSize == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("RecyclerView should have " + size + " items");
            }
        };
    }

    public static Matcher<View> withLinearLayoutiewSize(final int size) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                final int actualListSize = ((LinearLayout) view).getChildCount();
                return actualListSize == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("LinearLayout should have " + size + " items");
            }
        };
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }
}
