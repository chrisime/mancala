package com.bol.mancala

import com.bol.mancala.controller.GameController
import com.bol.mancala.service.business.GameService
import com.bol.mancala.service.business.GameServiceImpl
import com.bol.mancala.service.persistence.GameInMemoryPersistenceServiceImpl
import com.bol.mancala.service.persistence.GamePersistenceService
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.getRequiredProperty

val myBeans = beans {
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

// https://stackoverflow.com/questions/45935931/how-to-use-functional-bean-definition-kotlin-dsl-with-spring-boot-and-spring-w/46033685#46033685
@SpringBootConfiguration
class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        myBeans.initialize(applicationContext)
    }
}
