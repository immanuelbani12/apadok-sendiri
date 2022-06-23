package com.apadok.emrpreventive.socketchat

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apadok.emrpreventive.R
import com.apadok.emrpreventive.socketchat.EchoWebSocketListener.Companion.NORMAL_CLOSURE_STATUS
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject

class SocketChatActivity : AppCompatActivity() {
    private val message : Button by lazy { findViewById(R.id.message) }
    private val output: TextView by lazy { findViewById(R.id.output) }
    private val entryText: EditText by lazy { findViewById(R.id.text_entry) }
    private val client by lazy {
        OkHttpClient()
    }
    private var ws: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socketchat)

        val id_user = intent.getIntExtra("loginid",0)
        val logininstitusiid = intent.getIntExtra("logininstitusiid",0) //Disamain sama fromloginid di EchoWebSocketListener
        message.setOnClickListener {
            ws?.apply {
                val text = entryText.text.toString()
                output("ME: $text")
                val data = JSONObject("""{"to_login_id":"$logininstitusiid", "from_login_id":"$id_user", "message":"$text"}""")
                val datatext = data.toString()
                send(datatext)
                entryText.text.clear()
            } ?: ping("Error: Restart the App to reconnect")
        }
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    private fun start() {
        //Temporarily Get ID Pemeriksan From Main Activity
        val token = intent.getIntExtra("loginid",0)
        val request: Request = Request.Builder().url("ws://apadok.com:31686?id_login=$token").build()
        Log.e("req", request.toString())
        val listener = EchoWebSocketListener(this::output, this::ping) { ws = null }
        ws = client.newWebSocket(request, listener)
    }

    private fun stop() {
        ws?.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
    }

    override fun onDestroy() {
        super.onDestroy()
        client.dispatcher.executorService.shutdown()
    }

    private fun output(txt: String) {
        runOnUiThread {
            "${output.text}\n$txt".also { output.text = it }
        }
    }

    private fun ping(txt: String) {
        runOnUiThread {
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
        }
    }
}