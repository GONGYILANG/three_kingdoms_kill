import kotlin.random.Random

abstract class Equipment(protected val player: Player): Player by player {
    fun unwrap(): Player {
        return if (player is Equipment) {
            player.unwrap()
        } else {
            player
        }
    }

    override fun beingAttacked() {
        println("$name is being attacked.")
        dodgeAttack()
    }
}

class EightTrigrams(player: Player): Equipment(player) {
    override fun dodgeAttack() {
        println("Triggering the Eight Trigrams")
        if(Random.nextDouble() >= 0.5) {
            println("Judgement is true")
            println("$name dodged the attack with the eight trigrams, current HP is $currentHP")
        }
        else {
            println("Judgement is false")
            if(player is WeiGeneral)
                player.handleRequest()
            else
                player.dodgeAttack()
        }
    }
}
