package com.bol.mancala.starter

import com.bol.mancala.mancalaBeans
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.bol.mancala.controller.advice"])
class MancalaStarter

fun main(vararg args: String) {
    runApplication<MancalaStarter>(*args) {
        setBannerMode(Banner.Mode.LOG)
        addInitializers(mancalaBeans)
    }
}
