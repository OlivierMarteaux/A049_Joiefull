package com.oliviermarteaux.a049_joiefull

import android.app.Application
import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.api.KtorItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.shared.data.DataRepository
import com.oliviermarteaux.shared.data.WebDataRepository
import com.oliviermarteaux.shared.utils.AndroidLogger
import com.oliviermarteaux.shared.utils.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class JoiefullApplication: Application() {

//    ❌ Remove manual container
//    lateinit var container: AppContainer<Item>
//
//    ❌ Remove Retrofit initialization
//    private val apiService: ItemApiService by lazy {
//        RetrofitFactory.createFromUrl(CLOTHES_API_URL).create(ItemApiService::class.java)
//    }

    override fun onCreate() {
        super.onCreate()
//        val apiService = apiService
//        container = WebAppContainer(
//            log = AndroidLogger,
//            apiServiceGetData = apiService::getItems,
//            mapper = {itemDto: ItemDto -> itemDto.toDomain()},
//        )
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
                    json(Json { ignoreUnknownKeys = true })
                }
            }
        }

        // ✅ Provide ItemApiService using Ktor implementation
        single<ItemApiService> { KtorItemApiService(get()) }

        // ✅ Provide DataRepository directly, no WebAppContainer needed
        single<DataRepository<Item>> {
            WebDataRepository(
                apiServiceGetData = { get<ItemApiService>().getItems() },
                mapper = { dto: ItemDto -> dto.toDomain() },
                log = get()
            )
        }
    }
}