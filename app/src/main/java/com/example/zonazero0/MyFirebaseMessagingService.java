package com.example.zonazero0;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Maneja aquí tus mensajes. Por ejemplo, podrías mostrar una notificación.
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Si necesitas manejar la rotación de tokens, puedes hacerlo aquí.
    }
}
