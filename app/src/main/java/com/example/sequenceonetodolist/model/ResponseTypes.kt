package com.example.sequenceonetodolist.model

data class AuthenticateResponse(val version: Int, val success: Boolean, val status: Int, val hash: String? = null)