package com.bol.mancala

import com.bol.mancala.controller.GameController
import com.bol.mancala.service.business.GameService
import com.bol.mancala.service.business.GameServiceImpl
import com.bol.mancala.service.persistence.GameInMemoryPersistenceServiceImpl
import com.bol.mancala.service.persistence.GamePersistenceService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

@Configuration
class Beans {

    @Value("\${mancala.stones}")
    private var stones: Int = 0

    @Bean
    fun gameController(): GameController {
        return GameController(GameServiceImpl(stones, GameInMemoryPersistenceServiceImpl()))
    }
}

// Unfortunately, we're not there yet, so sad...
// https://stackoverflow.com/questions/45935931/how-to-use-functional-bean-definition-kotlin-dsl-with-spring-boot-and-spring-w/46033685#46033685

val mancalaBeans = beans {
    bean<GamePersistenceService> {
        GameInMemoryPersistenceServiceImpl()
    }

    bean<GameService> {
        GameServiceImpl(6, ref())
    }

    bean {
        GameController(ref())
    }
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        mancalaBeans.initialize(applicationContext)
    }
}
