// In snehil208001/hobbyhub/HobbyHub-102bbb5bfeae283b4c3e52ca5e13f3198e956095/HobbyHub/app/src/main/java/com/example/hobbyhub/data/AuthRepository.kt

package com.example.hobbyhub.data

import android.net.Uri
import android.util.Log
import com.example.hobbyhub.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    // ... (signInWithEmailAndPassword function is unchanged) ...
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    // --- MODIFIED: createUserWithEmailAndPassword ---
    suspend fun createUserWithEmailAndPassword(
        fullName: String,
        email: String,
        password: String,
        imageUri: Uri?
    ) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: throw Exception("Failed to create user details: user is null.")

            var profileImageUrl = ""
            if (imageUri != null) {
                profileImageUrl = uploadProfileImage(firebaseUser.uid, imageUri)
            }

            val user = User(
                uid = firebaseUser.uid,
                fullName = fullName,
                email = email,
                profileImageUrl = profileImageUrl,
                joinedDate = System.currentTimeMillis() // <-- ADDED: Set joined date on creation
                // isPrivate defaults to false
            )
            firestore.collection("users").document(firebaseUser.uid)
                .set(user)
                .await()

        } catch (e: Exception) {
            Log.e("AuthRepository", "User creation failed", e)
            throw e
        }
    }
    // --- END MODIFICATION ---

    // ... (uploadProfileImage function is unchanged) ...
    private suspend fun uploadProfileImage(uid: String, imageUri: Uri): String {
        return try {
            val storageRef = storage.reference.child("profile_images/$uid.jpg")
            val metadata = storageMetadata {
                cacheControl = "no-store"
            }
            storageRef.putFile(imageUri, metadata).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Image upload failed", e)
            ""
        }
    }

    // ... (getUserProfile function is unchanged) ...
    suspend fun getUserProfile(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val documentSnapshot = firestore.collection("users").document(uid)
                .get()
                .await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to get user profile", e)
            null
        }
    }

    // --- MODIFIED: updateProfile ---
    suspend fun updateProfile(
        bio: String,
        hobbies: List<String>,
        location: String,
        website: String,
        isPrivate: Boolean // <-- ADDED
    ) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        try {
            val updates = mapOf(
                "bio" to bio,
                "hobbies" to hobbies,
                "location" to location,
                "website" to website,
                "isPrivate" to isPrivate // <-- ADDED
            )
            firestore.collection("users").document(uid)
                .update(updates)
                .await()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Profile update failed", e)
            throw e
        }
    }
    // --- END MODIFICATION ---

    // ... (updateUserProfileImage function is unchanged) ...
    suspend fun updateUserProfileImage(uid: String, imageUri: Uri): String {
        return try {
            val newImageUrl = uploadProfileImage(uid, imageUri)
            if (newImageUrl.isNotEmpty()) {
                firestore.collection("users").document(uid)
                    .update("profileImageUrl", newImageUrl)
                    .await()
            }
            newImageUrl
        } catch (e: Exception) {
            Log.e("AuthRepository", "Profile image update failed", e)
            throw e
        }
    }

    // --- MODIFIED: signInWithGoogle ---
    suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        val firebaseUser = authResult.user
        if (firebaseUser != null) {
            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
            if (!userDoc.exists()) {
                val newUser = User(
                    uid = firebaseUser.uid,
                    fullName = firebaseUser.displayName ?: "Google User",
                    email = firebaseUser.email ?: "",
                    profileImageUrl = firebaseUser.photoUrl?.toString() ?: "",
                    joinedDate = System.currentTimeMillis() // <-- ADDED: Set joined date for Google sign-up
                )
                firestore.collection("users").document(firebaseUser.uid).set(newUser).await()
            }
        }
        return authResult
    }
    // --- END MODIFICATION ---

    // ... (signOut and getCurrentUser functions are unchanged) ...
    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser
}