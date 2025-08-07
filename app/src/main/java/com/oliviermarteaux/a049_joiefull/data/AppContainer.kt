package com.oliviermarteaux.a049_joiefull.data

import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.shared.data.DataRepository

interface AppContainer {

    val itemRepository: DataRepository<Item>
}