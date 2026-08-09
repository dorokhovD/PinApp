package com.dorox.pinapp.data.repository

import android.util.Log
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.PostRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBasePostRepository @Inject constructor(
    private val firestoreDB: FirebaseFirestore
) : PostRepository {

    private val collection = firestoreDB.collection("posts") // создание переменной для работы с коллекцией постов

    override suspend fun createPost(post: Post) {
         try {
            val docRef = collection.document() // Создание документа со сгенерированным id
            val uid = AuthRepository().getCurrentUserUid() ?: ""
             Log.d("Firestore", "UID: $uid")
            val newPost  = post.copy( // копировние переданного post с передачей в него id из документа
                postId = docRef.id,
                userId = uid
            )

            docRef.set(newPost).await() // Сохранение поста в Firestore
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
        }
    }

    override fun getAllPosts(): Flow<List<Post>> {
        return collection
            .orderBy("createdAt", Query.Direction.DESCENDING) // сортирует посты - сначала новые
            .dataObjects<Post>() // создает слушатель и выдает новый список в потоке Flow
            .catch { e ->
                Log.e("FireBase", "ошибка загрузки всех постов", e)
                emit(emptyList())
            }.map {
                it
            }
    }

    // Посмотреть подробную информацию о посте
    override suspend fun getPostByPostId(postId: String) : Post {
        val docPost = collection.document(postId).get().await()
        // Выбросить исключение если поста не оказалось/удалили

        if (!docPost.exists()) {
            throw NoSuchElementException("Пост $postId не найден")
        }
        return docPost.toObject(Post::class.java)
            ?: throw IllegalStateException("Не удалось конвертировать в объект Post")
    }

    // отобразить все посты конкретного пользователя в своем профиле
     override suspend fun getPostsByUserId(userId: String): Flow<List<Post>> {
         return collection
             .whereEqualTo("userId", userId)
             .orderBy("createdAt", Query.Direction.DESCENDING)
             .dataObjects<Post>()
             .catch { e ->
                 Log.e("FireBase", "ошибка загрузки постов по iserId", e)
                emit(emptyList())
             }
     }
}