package com.app.collections.data.services

import android.util.Log
import com.app.collections.data.models.CollectionDTO
import com.app.core.utils.failures.Failure
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    companion object {
        private const val TAG = "FirestoreService"
    }

    private val collectionRef = db.collection("collections")

    suspend fun getCollectionByName(collectionName: String): List<CollectionDTO> {
        return try {
            val uid = auth.currentUser?.uid

            if (uid == null) {
                throw Failure.Unauthorized("User not authenticated")
            }

            val snapshot = collectionRef
                .whereEqualTo("uid", uid)
                .whereEqualTo("name", collectionName)
                .get().await()

            if (snapshot.isEmpty) return emptyList()
            snapshot.documents.mapNotNull { document ->
                document.toObject<CollectionDTO>()
            }

        } catch (e: Throwable) {
            Log.e(TAG, "getCollectionByName: ", e)
            when (e) {
                is Failure -> throw e
                else -> throw Failure.DataError("Failed to fetch collection: ${e.message}")
            }
        }
    }

    suspend fun getCollections(): List<CollectionDTO> {
        return try {
            val uid = auth.currentUser?.uid

            if (uid == null) {
                throw Failure.Unauthorized("User not authenticated")
            }

            val snapshot = collectionRef
                .whereEqualTo("uid", uid)
                .get().await()

            if (snapshot.isEmpty) return emptyList()
            snapshot.documents.mapNotNull { document ->
                document.toObject<CollectionDTO>()
            }

        } catch (e: Throwable) {
            Log.e(TAG, "getCollections: ", e)
            when (e) {
                is Failure -> throw e
                else -> throw Failure.DataError("Failed to fetch collections: ${e.message}")
            }
        }
    }

    suspend fun addCollection(collection: CollectionDTO): Boolean {
        return try {
            val uid = auth.currentUser?.uid

            if (uid == null) {
                throw Failure.Unauthorized("User not authenticated")
            }
            collectionRef.add(collection.copy(uid = uid)).await()
            true
        } catch (e: Throwable) {
            Log.e(TAG, "addCollection: ", e)
            when (e) {
                is Failure -> throw e
                else -> throw Failure.DataError("Failed to add collection: ${e.message}")
            }
        }
    }
}