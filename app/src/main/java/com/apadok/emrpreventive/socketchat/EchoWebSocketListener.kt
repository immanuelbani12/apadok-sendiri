package com.apadok.emrpreventive.socketchat

import android.util.Log
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

internal class EchoWebSocketListener(
    val output: (String) -> Unit,
    val ping: (String) -> Unit,
    val closing: () -> Unit
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        ping("Connected!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.e("wow",text)
        output(text)
//        if (text.contains("id_connection")) {
//            output("A new user has joined the chat room!")
//        }
//        else{
//            val messagejson = Gson().fromJson(text, Message::class.java)
//            val author = messagejson.author
//            val message = messagejson.message
//            output("$author: $message")
//        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.e("wow",bytes.hex())
        output("Receiving bytes : " + bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        ping("Closing : $code / $reason")
        closing()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        ping("Error : " + t.message)
        closing()
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}