package me.trqhxrd.snake.game

import me.trqhxrd.snake.gui.Scene
import me.trqhxrd.snake.handler.CollisionHandler
import org.apache.logging.log4j.kotlin.Logging
import java.util.concurrent.CopyOnWriteArrayList

class Snake : Logging {
    val head: Head
    val tails: MutableList<Tail>
    var pickup: Pickup
        private set
    var direction = Direction.RIGHT
        set(value) {
            if (value == Direction.UNDEFINED) return
            if (this.waitToMove) return
            if (field.opposite() == value) {
                this.logger.debug("Failed to set direction to $value. [0]")
                return
            }
            this.logger.debug("Set direction from $field to $value.")
            field = value
            this.waitToMove = true
        }
    var score = 0
        set(value) {
            if (value > highscore) highscore = value
            field = value
            this.logger.info("Score: $value Highscore: $highscore.")
        }
    var highscore = 0
    var collisionHandler = CollisionHandler(this)
    var waitToMove = false

    init {
        this.logger.debug("Creating new snake.")

        this.head = Head(Scene.GRID_WIDTH / 2, Scene.GRID_HEIGHT / 2)
        this.tails = CopyOnWriteArrayList()
        this.pickup = Pickup()

        this.logger.debug("Created new snake.")
    }

    fun move() {
        if (this.tails.size >= 2) {
            this.tails
                .subList(1, this.tails.size)
                .reversed()
                .forEachIndexed { index, tail -> tail.move(this.tails.reversed()[index + 1]) }
        }

        if (tails.size >= 1) this.tails[0].move(this.head)

        this.head.add(this.direction)
        this.waitToMove = false
    }

    fun reset() {
        this.head.set(Scene.GRID_WIDTH / 2, Scene.GRID_HEIGHT / 2)
        this.tails.clear()
        this.score = 0
        this.direction = Direction.RIGHT
        this.logger.info("You died! Score: $score Highscore: $highscore.")
    }

    fun regeneratePickup() {
        this.pickup = Pickup()
    }

    fun addTail() {
        val last =
            if (this.tails.isNotEmpty()) this.tails[this.tails.size - 1]
            else this.head
        this.tails.add(Tail(last.x, last.y))
    }
}