package com.example.hobbyhub.data

import android.net.Uri // Import Uri
import com.example.hobbyhub.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage // Import Storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage // 1. Inject Storage
) {

    // 2. Keep this function as-is
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    // 3. Update createUser to handle image upload
    suspend fun createUserWithEmailAndPassword(
        fullName: String,
        email: String,
        password: String,
        imageUri: Uri? // Accept the image Uri
    ) {
        try {
            // Create user in Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: throw Exception("Failed to create user details: user is null.")

            // Handle Image Upload
            var profileImageUrl = ""
            if (imageUri != null) {
                // Upload the image and get the URL
                profileImageUrl = uploadProfileImage(firebaseUser.uid, imageUri)
            }

            // Save user details to Firestore
            val user = User(
                uid = firebaseUser.uid,
                fullName = fullName,
                email = email,
                profileImageUrl = profileImageUrl // Save the URL
            )
            firestore.collection("users").document(firebaseUser.uid)
                .set(user)
                .await()

        } catch (e: Exception) {
            throw e
        }
    }

    // 4. Add this new function to upload the image
    private suspend fun uploadProfileImage(uid: String, imageUri: Uri): String {
        return try {
            val storageRef = storage.reference.child("profile_images/$uid.jpg")
            storageRef.putFile(imageUri).await() // Upload file
            storageRef.downloadUrl.await().toString() // Get download URL
        } catch (e: Exception) {
            e.printStackTrace()
            "" // Return empty string on failure
        }
    }

    // 5. This function is correct and fetches the User object
    suspend fun getUserProfile(): User? {
        val uid = auth.currentUser?.uid ?: return null

        return try {
            val documentSnapshot = firestore.collection("users").document(uid)
                .get()
                .await()
            documentSnapshot.toObject(User::class.java) // Returns User object
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 6. Keep these functions
    suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential).await()
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser
}