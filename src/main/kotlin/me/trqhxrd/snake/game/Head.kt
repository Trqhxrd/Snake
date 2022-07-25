package me.trqhxrd.snake.game

import me.trqhxrd.snake.utils.Locational
import org.apache.logging.log4j.kotlin.Logging

class Head(x: Int, y: Int) : Locational(x, y),Logging {
    init {
        this.logger.debug("Created new head at x=$x,y=$y.")
    }
}