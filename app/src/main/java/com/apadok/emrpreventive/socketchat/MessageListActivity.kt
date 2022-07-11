package com.apadok.emrpreventive.socketchat

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apadok.emrpreventive.R
import com.apadok.emrpreventive.common.SetupToolbar
import com.apadok.emrpreventive.user.ConfirmLogOut
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject


class MessageListActivity : AppCompatActivity() {

    private var mMessageRecycler: RecyclerView? = null
    private var mMessageAdapter: MessageListAdapter? = null
    private var messageList: MutableList<Message> = mutableListOf()
    private val message : Button by lazy { findViewById(R.id.button_gchat_send) }
    private val entryText : EditText by lazy { findViewById(R.id.edit_gchat_message) }
    private val client by lazy {
        OkHttpClient()
    }
    private var ws: WebSocket? = null

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
        // val request: Request = Request.Builder().url("ws://10.0.2.2:8082/").build()
        Log.e("req", request.toString())
        val listener = EchoWebSocketListener(this::output, this::ping) { ws = null }
        listener.activity = supportFragmentManager
        ws = client.newWebSocket(request, listener)
    }

    private fun stop() {
        ws?.close(EchoWebSocketListener.NORMAL_CLOSURE_STATUS, "Goodbye !")
    }

    override fun onDestroy() {
        super.onDestroy()
        client.dispatcher.executorService.shutdown()
    }

    private fun output(txt: String) {
        runOnUiThread {
            val messagejson = Gson().fromJson(txt, Message::class.java)
            messageList.add(messagejson)
            mMessageAdapter!!.notifyDataSetChanged()
            Log.e("meong", messageList.toString())
            mMessageRecycler?.scrollToPosition(mMessageAdapter!!.itemCount - 1)
//            "${output.text}\n$txt".also { output.text = it }
        }
    }

    private fun ping(txt: String) {
        runOnUiThread {
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socketchat_new)
        SetupItemView()
        mMessageRecycler = findViewById<View>(R.id.recycler_gchat) as RecyclerView
        // Populate messageList here

        mMessageAdapter = MessageListAdapter(this, messageList)
        mMessageAdapter!!.id_user = intent.getIntExtra("loginid",0)
        mMessageRecycler!!.layoutManager = LinearLayoutManager(this)
        mMessageRecycler!!.adapter = mMessageAdapter

        val id_user = intent.getIntExtra("loginid",0)
        val logininstitusiid = intent.getIntExtra("logininstitusiid",0) //Disamain sama fromloginid di EchoWebSocketListener
        message.setOnClickListener {
            ws?.apply {
                val text = entryText.text.toString()
                val data = JSONObject("""{"to_login_id":"$logininstitusiid", "from_login_id":"$id_user", "message":"$text"}""")
                val datatext = data.toString()
                send(datatext)
                entryText.text.clear()
            } ?: ping("Gagal mengirim pesan, silahkan buka ulang laman chat untuk menghubungkan kembali")
        }
    }

    private fun SetupItemView() {
        // Code to Setup Toolbar
        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        SetupToolbar.changeToolbarFont(myToolbar, this)
        val ClinicName = intent.getStringExtra("clinicname")
        val clinic = findViewById<View>(R.id.tv_clinic) as TextView
        clinic.setText(ClinicName)

        // Init Logo RS
        val ClinicLogo = intent.getStringExtra("cliniclogo")
        val cliniclogo = findViewById<View>(R.id.iv_cliniclogo) as ImageView
        val url = "http://apadok.com/media/institusi/$ClinicLogo"
        if (ClinicLogo != null) {
            Log.e("tag",url)
        }
        Picasso.get().load(url).into(cliniclogo)
    }

    fun showPopUp(v: View?) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener { item: MenuItem -> onMenuItemClick(item) }
        popup.inflate(R.menu.option_menu)
        popup.show()
    }

    fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                val newFragment: DialogFragment = ConfirmLogOut()
                newFragment.show(supportFragmentManager, "")
                false
            }
            else -> false
        }
    }
}
