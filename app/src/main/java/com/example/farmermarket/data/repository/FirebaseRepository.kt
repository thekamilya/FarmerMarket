package com.example.farmermarket.data.repository

import android.util.Log
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.data.remote.dto.Participants
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DatabaseOperationException(message: String) : Exception(message)
class FirebaseRepository {

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val conversationsRef = database.getReference("conversations")
    val messagesRef = database.getReference("messages")


    // Step 2: Fetch conversations where the user is a participant
    fun fetchConversationsForUser(
        userId: String,
        onChatRoomsRetrieved: (List<Conversation>) -> Unit
    ) {
        conversationsRef.orderByChild("participants/$userId").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(conversationSnapshot: DataSnapshot) {
                    val conversations = mutableListOf<Conversation>()
                    val participantsToFetch = mutableSetOf<String>()

                    // Step 1: Collect all user IDs from participants in conversations
                    for (snapshot in conversationSnapshot.children) {
                        val participants = snapshot.child("participants").children.map { it.key!! }
                        participantsToFetch.addAll(participants)
                    }

                    if (participantsToFetch.isEmpty()) {
                        onChatRoomsRetrieved(conversations)
                        return
                    }

                    // Step 2: Fetch usernames for all collected user IDs
                    val userIdToNameMap = mutableMapOf<String, String>()
                    val fetchTasks = participantsToFetch.map { participantId ->
                        CompletableDeferred<Pair<String, String>>().apply {
                            usersRef.child(participantId).child("name")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(userSnapshot: DataSnapshot) {
                                        val userName = userSnapshot.getValue(String::class.java)
                                        if (userName != null) {
                                            complete(participantId to userName)
                                        } else {
                                            completeExceptionally(
                                                DatabaseOperationException("Failed to fetch user name for ID: $participantId")
                                            )
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        completeExceptionally(error.toException())
                                    }
                                })
                        }
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val results = fetchTasks.awaitAll()
                            userIdToNameMap.putAll(results.toMap())

                            // Step 3: Create conversations with both IDs and usernames
                            for (snapshot in conversationSnapshot.children) {
                                val conversationId = snapshot.key ?: continue

                                // Prepare participants map
                                val participants = snapshot.child("participants").children.associate {
                                    val uid = it.key!!
                                    val username = userIdToNameMap[uid] ?: uid // Fallback to user ID
                                    Participants(uid, username) to (it.value as? Boolean ?: false)
                                }

                                // Get the last message
                                val lastMessageSnapshot = snapshot.child("lastMessage")
                                val lastMessage = lastMessageSnapshot.getValue(Message::class.java) ?: continue


                                // Add the conversation to the list
                                val conversation = Conversation(
                                    id = conversationId,
                                    participants = participants,
                                    lastMessage = lastMessage,
                                )
                                conversations.add(conversation)
                            }

                            // Pass the results to the callback
                            onChatRoomsRetrieved(conversations)
                        } catch (e: Exception) {
                            Log.e("FirebaseRepository", "Failed to fetch participant names", e)
                            onChatRoomsRetrieved(conversations) // Return partial results if name fetching fails
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseRepository", "Failed to fetch conversations", error.toException())
                    onChatRoomsRetrieved(emptyList()) // Return an empty list in case of failure
                }
            })
    }








    suspend fun getChatsForUser(userName: String, onConversationsRetrieved: (List<Conversation>) -> Unit) {

        // Step 1: Find userId by userName

        usersRef.orderByChild(userName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    fetchConversationsForUser(userName){chats->
                        Log.d("kama", chats.toString())
                        onConversationsRetrieved(chats)

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("kama", error.toString())
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
    fun getUserNameById(
        userId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        usersRef.child(userId).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.getValue(String::class.java)
                        if (userName != null) {
                            onSuccess(userName)
                        } else {
                            onFailure(DatabaseOperationException("User name not found for the given ID"))
                        }
                    } else {
                        onFailure(DatabaseOperationException("No user found with the ID: $userId"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.toException())
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
            "timestamp" to System.currentTimeMillis() / 1000, // Timestamp in seconds,
        )

        // Step 2: Store the message under the specific conversation in the messages node
        messagesRef.child(conversationId).child(messageId).setValue(messageData)
            .addOnSuccessListener {
                Log.i("FirebaseRepository", "Message sent successfully")

                // Step 3: Optionally update the lastMessage field in the conversation
                val lastMessageData = mapOf(
                    "senderId" to userName,
                    "text" to message,
                    "timestamp" to System.currentTimeMillis() / 1000,
                    "readStatus" to  false
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

    fun markLastMessageAsRead(conversationId: String) {
        // Directly reference the "lastMessage/readStatus" field in the conversation
        val readStatusRef = conversationsRef.child(conversationId).child("lastMessage").child("readStatus")

        // Set the readStatus to true
        readStatusRef.setValue(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MarkAsRead", "Last message marked as read for conversation $conversationId")
                } else {
                    Log.e("MarkAsRead", "Failed to update readStatus for last message")
                }
            }
    }



    fun createConversation(
        participants: Map<String, Boolean>, // Map of userId to their active status
        onSuccess: (Conversation) -> Unit, // Callback for successfully created conversation
        onFailure: (Exception) -> Unit     // Callback for handling failures
    ) {
        // Step 1: Fetch usernames for all participant userIds
        val userIds = participants.keys
        val participantsWithNames = mutableMapOf<Participants, Boolean>()

        val fetchTasks = userIds.map { userId ->
            CompletableDeferred<Pair<Participants, Boolean>>().apply {
                usersRef.child(userId).child("name")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userName = snapshot.getValue(String::class.java) ?: userId
                            complete(Participants(userId, userName) to participants[userId]!!)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            completeExceptionally(error.toException())
                        }
                    })
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resolvedParticipants = fetchTasks.awaitAll()
                participantsWithNames.putAll(resolvedParticipants.toMap())

                // Step 2: Check for an existing conversation with the same participants
                conversationsRef.orderByChild("participants")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (conversationSnapshot in snapshot.children) {
                                val existingParticipants = conversationSnapshot.child("participants").value as? Map<String, Boolean>
                                if (existingParticipants != null && existingParticipants == participants) {
                                    val existingConversationId = conversationSnapshot.key
                                    if (existingConversationId != null) {
                                        Log.i("FirebaseRepository", "Conversation already exists")
                                        onSuccess(
                                            Conversation(
                                                id = existingConversationId,
                                                lastMessage = Message(),
                                                participants = participantsWithNames
                                            )
                                        )
                                        return
                                    }
                                }
                            }

                            // Step 3: Create a new conversation
                            val conversationId = conversationsRef.push().key
                            if (conversationId == null) {
                                onFailure(DatabaseOperationException("Failed to generate conversation ID"))
                                return
                            }

                            val conversationData = mapOf(
                                "participants" to participants, // Save raw userId map to the database
                                "lastMessage" to mapOf(
                                    "senderId" to "",
                                    "text" to "",
                                    "timestamp" to 0L,
                                    "readStatus" to true
                                )
                            )

                            conversationsRef.child(conversationId).setValue(conversationData)
                                .addOnSuccessListener {
                                    Log.i("FirebaseRepository", "Conversation created successfully")
                                    onSuccess(
                                        Conversation(
                                            id = conversationId,
                                            lastMessage = Message(),
                                            participants = participantsWithNames
                                        )
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseRepository", "Failed to create conversation", e)
                                    onFailure(e)
                                }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onFailure(error.toException())
                        }
                    })
            } catch (e: Exception) {
                Log.e("FirebaseRepository", "Failed to resolve participant names", e)
                onFailure(e)
            }
        }
    }



    fun saveProductDetail(
        userId: String,
        productDetail: ProductDetailDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance()
        val userProductsRef = database.getReference("userProducts/$userId") // Reference for user's products

        // Generate a unique ID for the product (or use the provided ID if not zero)
        val productId = if (productDetail.id != 0) productDetail.id.toString() else userProductsRef.push().key
        if (productId == null) {
            onFailure(Exception("Failed to generate a unique ID for the product"))
            return
        }

        // Save the product details under the user's node and the generated ID
        userProductsRef.child(productId).setValue(productDetail)
            .addOnSuccessListener {
                onSuccess() // Callback for success
            }
            .addOnFailureListener { exception ->
                onFailure(exception) // Callback for failure
            }
    }



    fun getAllProductDetails(
        onSuccess: (List<ProductDetailDto>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance()
        val productsRef = database.getReference("products")

        // Fetch all product details
        productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductDetailDto>()
                for (productSnapshot in snapshot.children) {
                    val productDetail = productSnapshot.getValue(ProductDetailDto::class.java)
                    if (productDetail != null) {
                        productList.add(productDetail)
                    }
                }
                onSuccess(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.toException())
            }
        })
    }


    fun getCart(
        userId: String,
        onSuccess: (List<ProductDetailDto>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance()
        val userProductsRef = database.getReference("userProducts/$userId") // Reference for the user's products

        // Fetch all product details for the user
        userProductsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductDetailDto>()
                for (productSnapshot in snapshot.children) {
                    val productDetail = productSnapshot.getValue(ProductDetailDto::class.java)
                    if (productDetail != null && productSnapshot.exists() && productDetail.id!=0) {
                        productList.add(productDetail)
                    }
                }
                onSuccess(productList) // Pass the list of products to the success callback
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.toException()) // Pass the error to the failure callback
            }
        })
    }


    fun addUserToFirebase(userId: String, name: String) {
        usersRef.child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                println("User with ID $userId already exists.")
            } else {
                // Add the user to the database
                usersRef.child(userId).setValue(mapOf("name" to name))
                    .addOnSuccessListener {
                        println("User added: ID = $userId, Name = $name")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding user: ${e.message}")
                    }
            }
        }.addOnFailureListener { e ->
            println("Error checking user existence: ${e.message}")
        }
    }






}