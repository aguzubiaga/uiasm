package dev.elmy.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import org.liquidplayer.javascript.JSContext
import org.liquidplayer.javascript.JSFunction
import dev.elmy.element
import dev.elmy.ElmyBinary


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binary = ElmyBinary(IntArray(0).iterator());

        val jsContext = JSContext()
        jsContext.setExceptionHandler { Log.e("Elmy", it.toString()) }

        val render: JSFunction = object : JSFunction(jsContext, "elmyRender") {
            fun elmyRender(bytes: Array<Int>): Unit {
//                Log.v("Current tree", bytes.joinToString { String.format("%02X", it) })
                binary.bytes = bytes.iterator()
            }
        }

        jsContext.property("elmyRender", render)
        val script = resources.openRawResource(R.raw.app)
                .bufferedReader().use { it.readText() }
        jsContext.evaluateScript(script)

        setContent {
            MaterialTheme {
                element(binary)
            }
        }
    }
}