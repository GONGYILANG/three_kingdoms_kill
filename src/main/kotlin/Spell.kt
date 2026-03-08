import kotlin.random.Random

interface Spell: Card {
    val target: Player
    fun execute()
}

class Acedia(override val suit: String, override val number: Int): Spell {
    override val name: String = "Acedia"
    override lateinit var target: Player

    override fun execute() {
        println("Judging $name over ${target.name}...")
        if (Random.nextDouble() >= 0.25) {
            println("${target.name} can't dodge the Acedia card. Skipping one round of Play Phase.")
            target.skipPlayPhase = true
        } else {
            println("${target.name} dodged the Acedia card.")
        }
    }
}