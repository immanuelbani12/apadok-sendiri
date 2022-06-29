package com.apadok.emrpreventive.socketchat

data class Message (
    var to_login_id: String,
    var from_login_id: String,
    var message: String,
    var datetime: String,
    var from: String
)


