import kotlin.random.Random

interface Spell {
    fun execute()
}

class Acedia(private val receiver: Player): Spell {
    override fun execute() {
        val r = Random(System.currentTimeMillis())
        if(r.nextDouble() >= 0.25) {
            println("${receiver.name} can't dodge the Acedia card. Skipping one round of Play Phase.")
            receiver.flag = false
        }
        else {
            receiver.flag = true
        }
    }
}