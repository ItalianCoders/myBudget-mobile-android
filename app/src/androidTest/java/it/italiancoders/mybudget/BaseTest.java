/*
 * Project: myBudget-mobile-android
 * File: BaseTest
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import it.italiancoders.mybudget.activity.MainActivity_;
import it.italiancoders.mybudget.rest.RestClient;
import it.italiancoders.mybudget.utils.DataUtils;
import it.italiancoders.mybudget.utils.DataUtils_;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static junit.framework.Assert.assertEquals;

/**
 * @author fattazzo
 * <p/>
 * date: 20/04/18
 * <p>
 * Java class due to an Android Annotations bug on rule activity generic parameter for generated class
 */
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    protected static DataUtils dataUtils;
    private static MockWebServer server;
    @Rule
    public ActivityTestRule<MainActivity_> mainActivityTestRule = new ActivityTestRule<>(MainActivity_.class, true, false);

    @BeforeClass
    public static void setUp() throws Exception {
        dataUtils = DataUtils_.getInstance_(InstrumentationRegistry.getTargetContext());

        initPrefs();

        System.err.println("setUp");
        server = new MockWebServer();
        server.start();
        RestClient.INSTANCE.setBASE_URL(server.url("/").toString());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        clearPrefs();
        server.shutdown();
    }

    private static void initPrefs() {
        File root = InstrumentationRegistry.getTargetContext().getFilesDir().getParentFile();
        String[] sharedPreferencesFileNames = new File(root, "shared_prefs").list();
        for (String fileName : sharedPreferencesFileNames) {
            InstrumentationRegistry.getTargetContext().getSharedPreferences(fileName.replace(".xml", ""), Context.MODE_PRIVATE).edit()
                    .clear()
                    .putString("lastUserAccounts", "[{\"id\": \"3\",\"name\": \"Other\",\"status\": 0,\"numberOfUsers\": 1},{\"id\": \"2\",\"name\": \"Family\",\"status\": 0,\"numberOfUsers\": 3},{\"id\": \"1\",\"name\": \"Personal\",\"status\": 0,\"numberOfUsers\": 1}]")
                    .putInt("accountType", 0)
                    .putString("lastUser", "{\"alias\":\"qqqq\",\"firstname\":\"aaaa\",\"lastname\":\"aaa\",\"password\":null,\"socialType\":0,\"username\":\"qqqq\"}")
                    .putString("language", "EN")
                    .putString("currency", "EUR")
                    .commit();
        }
    }

    private static void clearPrefs() {
        File root = InstrumentationRegistry.getTargetContext().getFilesDir().getParentFile();
        String[] sharedPreferencesFileNames = new File(root, "shared_prefs").list();
        for (String fileName : sharedPreferencesFileNames) {
            InstrumentationRegistry.getTargetContext().getSharedPreferences(fileName.replace(".xml", ""), Context.MODE_PRIVATE).edit()
                    .clear()
                    .commit();
        }
    }

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("it.italiancoders.mybudget", appContext.getPackageName());
    }

    @Before
    public void initActivity() throws Exception {
        enqueueResponse(200, "personalAccountDetails.json");
        mainActivityTestRule.launchActivity(new Intent());

        // Wait splash screen
        Thread.sleep(1000);
    }

    protected void enqueueResponse(int code, String fileName) throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(code)
                .setBody(JsonTestHelper.INSTANCE.getStringFromFile(InstrumentationRegistry.getInstrumentation().getContext(), fileName)));
    }


    /**
     * Press on the back button.
     *
     * @throws PerformException if currently displayed activity is root activity, since pressing back
     *                          button would result in application closing.
     */
    public void pressBack() throws PerformException {
        onView(isRoot()).perform(ViewActions.pressBack());
    }

    public void rotatePortrait() {
        mainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void rotateLandscape() {
        mainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
