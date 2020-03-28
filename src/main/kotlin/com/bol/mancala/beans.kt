package com.bol.mancala

import com.bol.mancala.controller.GameController
import com.bol.mancala.service.business.GameService
import com.bol.mancala.service.business.GameServiceImpl
import com.bol.mancala.service.persistence.GameInMemoryPersistenceServiceImpl
import com.bol.mancala.service.persistence.GamePersistenceService
import org.springframework.context.support.beans
import org.springframework.core.env.getRequiredProperty

val mancalaBeans = beans {

    bean<GamePersistenceService> {
        GameInMemoryPersistenceServiceImpl()
    }

    bean<GameService> {
        GameServiceImpl(env.getRequiredProperty<Int>("mancala.stones"), ref())
    }

    bean {
        GameController(ref())
    }

}
