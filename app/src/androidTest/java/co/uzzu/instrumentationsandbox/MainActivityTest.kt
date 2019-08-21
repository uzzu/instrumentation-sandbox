package co.uzzu.instrumentationsandbox

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.quality.Strictness
import org.mockito.junit.MockitoJUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @get:Rule
    val activityFactoryRule = SpySingleActivityFactory.Rule<MainActivity>()

    @get:Rule
    val activityRule = ActivityTestRule(activityFactoryRule.factory, true, true)

    @Test
    fun replacePresenter() {
        val expectIntentMessage = "intent message from instrumentation"
        val expectMessage = "instrumentation"
        activityFactoryRule.factory.onNewInstance { activity ->
            Mockito.doReturn(InstrumentationPresenter(activity, Contract.Message(expectMessage))).`when`(activity).createPresenter()
            Mockito.doReturn(ApplicationProvider.getApplicationContext<Context>().getApplicationInfo()).`when`(activity).getApplicationInfo()
        }

        activityRule.launchActivity(Intent().apply { putExtra("intent_message", expectIntentMessage) })

        Assert.assertEquals(expectIntentMessage, activityRule.activity.text_intent_message.text)
        Assert.assertEquals(expectMessage, activityRule.activity.text_message.text)
    }
}

class InstrumentationPresenter(
    private val view: Contract.View,
    private val message: Contract.Message
) : Contract.Presenter {

    override fun onQueryMessage() {
        view.render(message)
    }
}
