package imui.jiguang.cn.imuisample.messages;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import imui.jiguang.cn.imuisample.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MessageListActivityTest {

    private static final String TEST_TEXT = "Hello test";

    @Rule
    public ActivityTestRule<MessageListActivity> mActivityRule = new ActivityTestRule<>(MessageListActivity.class);

    @Test
    public void sendTextTest() {
        onView(withId(R.id.aurora_et_chat_input))
                .perform(typeText(TEST_TEXT), closeSoftKeyboard());
        onView(withId(R.id.aurora_menuitem_ib_send)).perform(click());

        onView(withId(R.id.aurora_et_chat_input)).check(matches(withText("")));
        onView(withText(TEST_TEXT)).check(matches(isDisplayed()));
    }
}