package com.example.chattersnest

class User {
    var fullName: String? = null
    var email: String? = null
    var uid: String? = null

    constructor()

    constructor(fullName: String?, email: String?, uid: String?) {
        this.fullName = fullName
        this.email = email
        this.uid = uid
    }
}