package com.dorox.pinapp.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth

    fun getCurrentUserUid() : String? {
        return auth.currentUser?.uid
    }

    suspend fun signInAnonymously(): Result<String> {
        return try {
            val result = auth.signInAnonymously().await()
            val uid = result.user?.uid ?: throw Exception("Error - uid не получен")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


//    suspend fun saveGuestToFireStore(uid: String, phone: String? = null) {
//        val user = mapOf(
//            "uid" to uid,
//            "phone" to phone,
//            "createdTime" to FieldValue.serverTimestamp()
//        )
//        FirebaseFirestore.getInstance()
//            .collection("users")
//            .document(uid)
//            .set(user)
//            .await()
//    }

    fun signOut() {
        auth.signOut()
    }
}