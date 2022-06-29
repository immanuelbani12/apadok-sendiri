package com.apadok.emrpreventive.socketchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apadok.emrpreventive.R
import com.apadok.emrpreventive.common.StringToTimeStampFormatting


class MessageListAdapter(context: Context, messageList: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mContext: Context
    var id_user: Int = 0
    private val mMessageList: List<Message>
    override fun getItemCount(): Int {
        return mMessageList.size
    }

    // Determines the appropriate ViewType according to the sender of the message.
    override fun getItemViewType(position: Int): Int {
        val message: Message = mMessageList[position] as Message
        return if (message.to_login_id.equals(id_user)) {
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_SENT
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    // Inflates the appropriate layout according to the ViewType.
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view: View
//        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
//            view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.activity_socketchat_to, parent, false)
//            return SentMessageHolder(view)
//        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
//            view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.activity_socketchat_from, parent, false)
//            return ReceivedMessageHolder(view)
//        }
//
//        return RecyclerView.ViewHolder(parent)
//    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_socketchat_from, parent, false)
            return SentMessageHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_socketchat_to, parent, false)
            return ReceivedMessageHolder(view)
        }
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Message = mMessageList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    private inner class SentMessageHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var timeText: TextView
        fun bind(message: Message) {
            messageText.setText(message.message)

            // Format the stored timestamp into a readable String using method.

            var timestamp = message.datetime
            val FormattedTimeStamp = StringToTimeStampFormatting.changeFormat(
                timestamp,
                "yyyy-MM-dd HH:mm:ss",
                "HH:mm"
            )
            timeText.setText(FormattedTimeStamp)
        }

        init {
            messageText = itemView.findViewById<View>(R.id.text_gchat_message_me) as TextView
            timeText = itemView.findViewById<View>(R.id.text_gchat_timestamp_me) as TextView
        }
    }

    private inner class ReceivedMessageHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var timeText: TextView
        var nameText: TextView
        var profileImage: ImageView
        fun bind(message: Message) {
            messageText.setText(message.message)

            // Format the stored timestamp into a readable String using method.
            var timestamp = message.datetime
            val FormattedTimeStamp = StringToTimeStampFormatting.changeFormat(
                timestamp,
                "yyyy-MM-dd HH:mm:ss",
                "mm:ss"
            )
            timeText.setText(FormattedTimeStamp)
            nameText.setText(message.from)

            // Insert the profile image from the URL into the ImageView.
//            Utils.displayRoundImageFromUrl(
//                mContext,
//                message.getSender().getProfileUrl(),
//                profileImage
//            )
        }

        init {
            messageText = itemView.findViewById<View>(R.id.text_gchat_message_other) as TextView
            timeText = itemView.findViewById<View>(R.id.text_gchat_timestamp_other) as TextView
            nameText = itemView.findViewById<View>(R.id.text_gchat_user_other) as TextView
            profileImage = itemView.findViewById<View>(R.id.image_gchat_profile_other) as ImageView
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    init {
        mContext = context
        mMessageList = messageList
    }

}