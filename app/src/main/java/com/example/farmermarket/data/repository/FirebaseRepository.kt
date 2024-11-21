package com.example.farmermarket.data.repository

import android.util.Log
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseOperationException(message: String) : Exception(message)
class FirebaseRepository {

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val conversationsRef = database.getReference("conversations")
    val messagesRef = database.getReference("messages")


    // Step 2: Fetch conversations where the user is a participant
    fun fetchConversationsForUser(userId: String, onChatRoomsRetrieved: (List<Conversation>) -> Unit) {
        conversationsRef.orderByChild("participants/$userId").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(conversationSnapshot: DataSnapshot) {
                    val conversations = mutableListOf<Conversation>()
                    val fetchChatRoomDetailsTasks = mutableListOf<ValueEventListener>()

                    for (snapshot in conversationSnapshot.children) {
                        val conversationId = snapshot.key ?: continue
                        val participants = snapshot.child("participants").children.associate { it.key!! to it.value as Boolean }
                        val lastMessageSnapshot = snapshot.child("lastMessage")

                        if (lastMessageSnapshot.exists()) {
                            val lastMessage = lastMessageSnapshot.getValue(Message::class.java) ?: continue

                            val chatRoom = Conversation(conversationId, participants, lastMessage)
                            conversations.add(chatRoom)
                        }
                    }

                    // Return chat rooms
                    onChatRoomsRetrieved(conversations)
                    Log.d("LAB", conversations.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.d("LAB", error.toString())
                    throw DatabaseOperationException("Disconnected from Firebase server. Please try again later.")
                }
            })
    }

    suspend fun getChatsForUser(userName: String, onConversationsRetrieved: (List<Conversation>) -> Unit) {

        // Step 1: Find userId by userName

        usersRef.orderByChild(userName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    val userId = userSnapshot.children.firstOrNull()?.key ?: return
                    fetchConversationsForUser(userId){chats->
                        Log.d("LAB", chats.toString())
                        onConversationsRetrieved(chats)

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("LAB", error.toString())
                    // Handle error
//                    throw DatabaseOperationException("Disconnected from Firebase server. Please try again later.")
                }
            })

    }

    fun getChat(conversationId: String, onConversationRetrieved: (List<Message>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val messagesRef = database.getReference("messages/$conversationId")

        // Step 1: Attach a listener to the messages node for the given conversationId
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList = mutableListOf<Message>()

                // Step 2: Iterate over the messages for the conversation
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messagesList.add(message)
                    }
                }

                // Step 3: Pass the list of messages to the callback
                onConversationRetrieved(messagesList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun sendMessage(conversationId: String, message: String, userName: String) {
        val messageId = messagesRef.child(conversationId).push().key // Create a unique ID for the message
        if (messageId == null) {
            Log.e("FirebaseRepository", "Error: messageId is null")
            return
        }

        // Step 1: Create a map of message data to store in Firebase
        val messageData = mapOf(
            "senderId" to userName,
            "text" to message,
            "timestamp" to System.currentTimeMillis() / 1000 // Timestamp in seconds
        )

        // Step 2: Store the message under the specific conversation in the messages node
        messagesRef.child(conversationId).child(messageId).setValue(messageData)
            .addOnSuccessListener {
                Log.i("FirebaseRepository", "Message sent successfully")

                // Step 3: Optionally update the lastMessage field in the conversation
                val lastMessageData = mapOf(
                    "senderId" to userName,
                    "text" to message,
                    "timestamp" to System.currentTimeMillis() / 1000
                )
                conversationsRef.child(conversationId).child("lastMessage").setValue(lastMessageData)
                    .addOnSuccessListener {
                        Log.i("FirebaseRepository", "Last message updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirebaseRepository", "Failed to update last message", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Failed to send message", e)
            }
    }




}