package com.dorox.pinapp.data.repository

import androidx.lifecycle.viewmodel.compose.viewModel
import com.dorox.pinapp.domain.exception.AuthException
import com.dorox.pinapp.domain.repository.AuthRepository
import com.dorox.pinapp.presentation.navigation.Screen
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {


    override fun getCurrentUser() : FirebaseUser? {
        return auth.currentUser
    }

    override fun getCurrentUserUid() : String? {
        return auth.currentUser?.uid
    }

    //
    override suspend fun provideAuthenticated(): String {

        // Возвращаем uid текушего пользователя
        getCurrentUserUid()?.let { uid ->
            return uid
        }
        // Если текущий не найден - входим как Анонимный
        return signInAnonymously()
    }

    // Вход как анонимный пользователь
    override suspend fun signInAnonymously(): String {
        val result = auth.signInAnonymously().await()
        return result.user?.uid ?: throw Exception("uid не найден при попытке анонимного входа")

    }

    // Вход в учетную запись
    override suspend fun signIn(email: String, password: String): String {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw AuthException.Unknown("User не получен")
            user.uid
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw AuthException.InvalidCredentials()
        } catch (e: FirebaseAuthInvalidUserException) {
            throw AuthException.UserNotFound()
        } catch (e: Exception) {
            throw Exception("Ошибка при входе: ${e.message}", e)
        }
    }

    // Регистрация - привязка анонимного пользователя к созданной учетной записью
    override suspend fun signUp(displayName: String, email: String, password: String): String {

        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }

        return try {
            val currentUser = auth.currentUser ?: throw AuthException.Unknown("Нет текущего пользователя")

            val credential = EmailAuthProvider.getCredential(email, password)
            val result = currentUser.linkWithCredential(credential).await()

            val user = result.user ?: throw AuthException.Unknown("Пользователь не получен")
            // Обновление данных в Firebase Auth
            user.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
            ).await()

            user.uid

        } catch (e: FirebaseAuthUserCollisionException) {
            throw AuthException.EmailAlreadyInUse()
        } catch (e: FirebaseAuthWeakPasswordException) {
            throw AuthException.WeakPassword()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw AuthException.InvalidCredentials()
        } catch (e: Exception) {
            throw AuthException.Unknown("Ошибка при регистрации: ${e.message}")
        }
    }

    override suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    // Проверка на анонимного пользователя
    override fun isAnonymous(): Boolean {
        return auth.currentUser?.isAnonymous == true
    }

    // выход их профиля
    override fun signOut() {
        auth.signOut()
    }
}