package com.example.feedarticlesjetpackcompose.utils
import androidx.navigation.NavController

object NavCallbackRegistry {
    private val callbackMap = mutableMapOf<String, Any>()

    fun <T> registerCallback(key: String, callback: NavResultCallback<T>) {
        callbackMap[key] = callback
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getCallback(key: String): NavResultCallback<T>? {
        val callBack = callbackMap[key] as? NavResultCallback<T>
        unregisterCallback(key)
        return callBack
    }

    private fun unregisterCallback(key: String) {
        callbackMap.remove(key)
    }
}

typealias NavResultCallback<T> = (T) -> Unit

private fun generateNavResultCallbackKey(route: String?): String {
    return "NavResultCallbackKey_$route"
}

fun <T> NavController.setNavResultCallback(callback: NavResultCallback<T>) {
    val currentRouteId = currentBackStackEntry?.destination?.route
    val key = generateNavResultCallbackKey(currentRouteId)
    NavCallbackRegistry.registerCallback(key, callback)
}

fun <T> NavController.getNavResultCallback(): NavResultCallback<T>? {
    val previousRouteId = previousBackStackEntry?.destination?.route
    return NavCallbackRegistry.getCallback(generateNavResultCallbackKey(previousRouteId))
}


fun <T> NavController.popBackStackWithResult(result: T) {
    val callback = getNavResultCallback<T>()
    if (popBackStack()) {
        callback?.invoke(result)
    }
}


fun <T> NavController.navigateForResult(
    route: String,
    navResultCallback: NavResultCallback<T>,
) {
    setNavResultCallback(navResultCallback)
    navigate(route)
}


fun String.isStrongPassword(): Boolean {
    return Regex(STRONG_PASSWORD_REGEX).matches(this)
}

fun String.isConfirmPasswordValid(password: String): Boolean {
    return this == password
}