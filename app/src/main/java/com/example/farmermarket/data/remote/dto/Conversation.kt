package com.example.farmermarket.data.remote.dto

data class Conversation(
    val id: String,
    val participants: Map<Participants, Boolean>,
    val lastMessage: Message)

data class Participants(
    val userId: String,
    val userName: String
)