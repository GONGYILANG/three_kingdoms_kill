import kotlin.random.Random

abstract class Equipment(private val player: Player): Player by player {
    override fun beingAttacked() {
        super.beingAttacked()
        dodgeAttack()
    }
}

class EightTrigrams(player: Player): Equipment(player) {
    override fun dodgeAttack() {
        val random = Random(System.currentTimeMillis())
        println("Triggering the Eight Trigrams")
        if(random.nextDouble() >= 0.5) {
            println("Judgement is true")
            println("$name dodged the attack with the eight trigrams, current HP is $currentHP")
        }
        else {
            println("Judgement is false")
            super.dodgeAttack()
        }
    }
}