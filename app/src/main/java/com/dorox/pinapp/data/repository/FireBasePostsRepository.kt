package com.dorox.pinapp.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.PostsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.emptyList

class FireBasePostsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : PostsRepository {

    private val collection = firestore.collection("posts") // создание переменной для работы с коллекцией постов

    override suspend fun createPost(post: Post, imageUri: Uri?): Result<String> {
         return try {

             var imageUrl: String? = null


             if (imageUri != null) {
                 val uploadImageResult = uploadImage(imageUri)
                 if (uploadImageResult.isFailure) {
                     return Result.failure(uploadImageResult.exceptionOrNull() ?: Exception("Ошибка при загрузке изображения"))
                 }
                 imageUrl = uploadImageResult.getOrThrow()
             }
             val docRef = collection.document() // Создание документа со сгенерированным id
             val uid = AuthRepository().getCurrentUserUid() ?: return Result.failure(Exception("Пользовательне не авторизирован"))
             val newPost  = post.copy( // копировние переданного post с передачей в него id из документа
                postId = docRef.id,
                userId = uid,
                imageUrl = imageUrl ?: ""

             )
             docRef.set(newPost).await() // Сохранение поста в Firestore
             Result.success(newPost.postId)
        } catch (e: Exception) {
             Result.failure(e)
        }
    }




    suspend fun uploadImage(imageUri: Uri): Result<String> {
        return try {
            // Создание уникального имени для файла в Storage
            val imageRef = storage.reference.child("${UUID.randomUUID()}.jpg")

            // Загрузка файла
            imageRef.putFile(imageUri).await()

            // получение URL для скачивания
            val downloadUrl = imageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveUrlToFirestore(imageUrl: String) {

    }

    override suspend fun getAllPosts(): List<Post> {
        return try {
            val snapshot = firestore.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING) // сортирует посты - сначала новые
                .get()
                .await()
            snapshot.toObjects(Post::class.java)
        } catch (e: Exception) {
            Log.e("FireBase", "ошибка загрузки всех постов", e)
            emptyList()
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