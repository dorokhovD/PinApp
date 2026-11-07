package com.dorox.pinapp.domain.exception

sealed class AuthException(message: String) : Exception(message) {
    class InvalidCredentials : AuthException("Неверный email или пароль")
    class WeakPassword : AuthException("Пароль слишком слабый - минимум 6 символов")
    class EmailAlreadyInUse : AuthException("Этот email уже используется")
    class UserNotFound : AuthException("Аккаунт с таким email не найден")
    class NetworkError : AuthException("Проверьте подключение к интернету")
    class Unknown(message: String) : AuthException(message)
}