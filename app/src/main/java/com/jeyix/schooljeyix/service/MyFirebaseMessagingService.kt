package com.jeyix.schooljeyix.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jeyix.schooljeyix.domain.usecase.users.UserUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userUseCases: UserUseCases

    /**
     * Se llama automáticamente cuando llega una notificación y la app está en primer plano.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "Mensaje recibido: ${message.notification?.title}")
        // Aquí puedes mostrar una notificación personalizada en la app
    }

    /**
     * ¡LA FUNCIÓN MÁS IMPORTANTE!
     * Se llama automáticamente cuando Firebase genera un nuevo token para este dispositivo.
     * (Ocurre en la primera instalación, al reinstalar, al borrar datos, etc.)
     */
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token generado: $token")

        GlobalScope.launch {
            userUseCases.updateDeviceToken(token)
        }
    }
}