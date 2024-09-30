package com.example.farmermarket.data.remote.dto

data class Conversation(
    val id: String,
    val participants: Map<String, Boolean>,
    val lastMessage: Message)