package co.uzzu.instrumentationsandbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Contract.View {

    private lateinit var presenter: Contract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = createPresenter()

        text_intent_message.setText(intent.extras?.getString("intent_message") ?: "no intent message")

        presenter.onQueryMessage()
    }

    internal fun createPresenter():Contract.Presenter = ProductionPresenter(this)

    override fun render(message: Contract.Message) {
        text_message.setText(message.value)
    }
}



interface Contract {
    interface Presenter {
        fun onQueryMessage()
    }

    interface View {
        fun render(message: Message)
    }

    data class Message(val value: String)
}


private class ProductionPresenter(private val view: Contract.View) : Contract.Presenter {
    override fun onQueryMessage() {
        view.render(Contract.Message("production"))
    }
}
