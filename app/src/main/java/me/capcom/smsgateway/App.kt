package me.capcom.smsgateway

import android.app.Application
import androidx.preference.PreferenceManager
import me.capcom.smsgateway.data.dbModule
import me.capcom.smsgateway.modules.gateway.GatewayModule
import me.capcom.smsgateway.modules.localserver.LocalServerModule
import me.capcom.smsgateway.modules.messages.messagesModule
import me.capcom.smsgateway.modules.settings.PreferencesStorage
import me.capcom.smsgateway.receivers.EventsReceiver
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                dbModule,
                messagesModule,
            )
        }

        instance = this

        EventsReceiver.register(this)
    }

    val settings by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    
    val gatewayModule by lazy {
        GatewayModule(
            get(),
            PreferencesStorage(settings, "gateway")
        )
    }
    val localServerModule by lazy {
        LocalServerModule(
            get(),
            PreferencesStorage(settings, "localserver")
        )
    }

    companion object {
        lateinit var instance: App
            private set
    }
}