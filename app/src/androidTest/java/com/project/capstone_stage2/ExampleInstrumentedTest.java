package com.project2.capstone_stage2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.project.capstone_stage2.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.project2.capstone_stage2", appContext.getPackageName());
    }

    @Test
    public void testSelectCategory() {


    }

    @Test
    public void testAddFavorite(){


    }

    @Test
    public void testRemoveFavorite(){


    }

    @Test
    public void testDisplayExerciseVideo(){


    }


}
