package com.nvc.orderfood

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.nvc.orderfood", appContext.packageName)
    }
//    @Rule
//    @JvmField
//    val activityRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun demo() {
//        activityRule.scenario.onActivity {
//        }
//        val navController = TestNavHostController(
//            ApplicationProvider.getApplicationContext())
//
//        val titleScenario = launchFragmentInContainer<SignInFragment>()
//        FragmentScenario.launchInContainer(SignInFragment::class.java)

//        titleScenario.onFragment { fragment ->
//            // Set the graph on the TestNavHostController
//            navController.setGraph(R.navigation.nav_main)
//
//            // Make the NavController available via the findNavController() APIs
//            Navigation.setViewNavController(fragment.requireView(), navController)
//        }
//        // Verify that performing a click changes the NavController’s state
//        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())
//        assertEquals(navController.currentDestination?.id.toString(),R.id.homeFragment)
    }
}