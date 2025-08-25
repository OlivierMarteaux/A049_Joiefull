package com.oliviermarteaux.shared.data

interface AppContainer<T> {

    val dataRepository: DataRepository<T>
}