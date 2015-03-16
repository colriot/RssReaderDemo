package io.github.colriot.rssreaderdemo;

import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.view.ConfigActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         16/03/15
 */
public class ConfigActivityTest extends BaseActivityTest<ConfigActivity> {
  public ConfigActivityTest() {
    super(ConfigActivity.class);
  }

  public void testFeedIsAddedToTheList() {
    onView(withId(R.id.add_rss_edit)).perform(clearText());
    onView(withId(R.id.add_rss_edit)).perform(typeText("http://cyrilmottier.com/atom.xml"));
    onView(withId(R.id.add_rss_edit)).perform(closeSoftKeyboard());
    onView(withId(R.id.add_rss_btn)).perform(click());

    onView(withId(android.R.id.list)).check(
        matches(withChild(withText("http://cyrilmottier.com/atom.xml"))));
  }

  public void testSearchGivesResults() {
    onView(withId(R.id.search_edit)).perform(typeText("substring"));
    onView(withId(R.id.search_edit)).perform(closeSoftKeyboard());
    onView(withId(R.id.search_btn)).perform(click());

    // Check that the list has specific number of elements.
    final int NUMBER_OF_ITEMS = 4;
    onData(is(instanceOf(Article.class)))
        .inAdapterView(withId(android.R.id.list))
        .atPosition(NUMBER_OF_ITEMS - 1)
        .perform(click());
  }
}
