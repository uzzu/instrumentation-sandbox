package co.uzzu.instrumentationsandbox

import android.app.Activity
import android.content.Intent
import androidx.test.runner.intercepting.SingleActivityFactory
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mockito

class SpySingleActivityFactory<T : Activity>(
    private val clazz: Class<T>
) : SingleActivityFactory<T>(clazz) {

    private var onNewInstanceBlock: ((T) -> Unit)? = null

    override fun create(intent: Intent?): T =
        Mockito.spy(clazz.newInstance()).also { onNewInstanceBlock?.invoke(it) }


    fun onNewInstance(block: (T) -> Unit) {
        onNewInstanceBlock = block
    }

    fun teardown() {
        onNewInstanceBlock = null
    }

    companion object {
        @Suppress("TestFunctionName")
        inline fun <reified T : Activity> Rule() = SpySingleActivityFactoryRule(T::class.java)
    }
}

class SpySingleActivityFactoryRule<T : Activity>(clazz: Class<T>) : TestRule {

    val factory = SpySingleActivityFactory(clazz)

    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            try {
                base.evaluate()
            } finally {
                factory.teardown()
            }
        }
    }
}
