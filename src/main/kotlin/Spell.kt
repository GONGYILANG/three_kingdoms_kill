import kotlin.random.Random

//interface Spell {
//    fun execute()
//}

typealias Spell = () -> Unit
typealias SpellGenerator = (Player) -> Spell

val acedia: SpellGenerator = { player: Player ->
    {
        if (Random.nextDouble() >= 0.25) {
            println("${player.name} can't dodge the Acedia card. Skipping one round of Play Phase.")
            player.flag = false
        } else {
            println("${player.name} dodged the Acedia card.")
            player.flag = true
        }
    }
}