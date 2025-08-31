package com.oliviermarteaux.a049_joiefull

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.util.DebugLogger
import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.api.KtorItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDto
import com.oliviermarteaux.a049_joiefull.data.repository.WebDataRepository
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeViewModel
import com.oliviermarteaux.a049_joiefull.ui.screens.item.ItemViewModel
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.utils.AndroidLogger
import com.oliviermarteaux.shared.utils.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class JoiefullApplication: Application(), SingletonImageLoader.Factory {

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context = context)
            .logger(DebugLogger()) // logs to Logcat with tag "Coil"
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin { // ✅ Start Koin when the app launches
            androidContext(this@JoiefullApplication)
            modules(appModule) // ✅ Load our Koin module
        }
    }

//--------------------------- Koin module ------------------------------------
    val appModule = module {

        single<Logger> { AndroidLogger } // ✅ Inject our logger

        single { // ✅ Provide Ktor HttpClient with JSON
            HttpClient(Android) {
                install(ContentNegotiation) {
                    val jsonConfig = Json { ignoreUnknownKeys = true }
                    // default json register:
                    json(jsonConfig)
                    // Github-specific json register:
                    // GitHub's raw content server always returns "Content-Type: text/plain"
                    // even for JSON files. Without this registration, Ktor won't attempt
                    // to deserialize the response body as JSON automatically.
                    // We explicitly tell Ktor to treat "text/plain" as JSON content.
                    json(jsonConfig, contentType = ContentType.Text.Plain)
                }
            }
        }

        // ✅ Provide ItemApiService using Ktor implementation
        single<ItemApiService> { KtorItemApiService(get()) }

        // ✅ Provide DataRepository directly, no WebAppContainer needed
        single<DataRepository<Item>> {
            WebDataRepository(
                apiServiceGetData = { get<ItemApiService>().getItems() },
                apiServicePutData = { get<ItemApiService>().updateItem(it) },
                dtoToDomain = { dto: ItemDto -> dto.toDomain() },
                domainToDto = { domain: Item -> domain.toDto() },
                log = get()
            )
        }

        viewModelOf(::HomeViewModel)
//        viewModelOf(::ItemViewModel)

        // Provide ItemViewModel with SavedStateHandle injection
        viewModel { (savedStateHandle: SavedStateHandle) ->
            ItemViewModel(get(), savedStateHandle)
        }
    }
}