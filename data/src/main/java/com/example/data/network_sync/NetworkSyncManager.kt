package com.example.data.network_sync

import com.example.core.network_monitor.NetworkMonitor
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkSyncManager(
    private val networkMonitor: NetworkMonitor,
    private val firestore: FirebaseFirestore,
) {
    fun observeNetworkChanges(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            networkMonitor.networkStatus.collect { isConnected ->
                if (isConnected)
                    firestore.enableNetwork()
                else
                    firestore.disableNetwork()
            }
        }
    }
}