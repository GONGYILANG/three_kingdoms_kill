import kotlin.random.Random

enum class EquipmentSlot {
    WEAPON, ARMOR, DEFENSIVE_HORSE, OFFENSIVE_HORSE
}

abstract class Equipment(
    override val suit: String,  // implementing the suit property of Card
    override val number: Int,  // implementing the number property of Card
    override val name: String,  // implementing the name property of Card
    val slot: EquipmentSlot  // the category of equipment
): Card {

    // a hook method, which is called when a general is being attacked
    open fun onBeingAttacked(target: Player): Boolean {
        return false // do not cancel attacks by default
    }

}

class EightTrigrams(suit: String, number: Int):
    Equipment(suit, number, "Eight Trigrams", EquipmentSlot.ARMOR) {

    override fun onBeingAttacked(target: Player): Boolean {
        println("--------- Triggering the $name ---------")
        return if (Random.nextDouble() >= 0.5) {
            println("Judgement is true, ${target.name} dodged the attack with the $name, " +
                    "current HP is ${target.currentHP}")
            true
        } else {
            println("Judgement is false")
            false
        }
    }

    override fun toString(): String {
        return "Eight Trigrams ($suit $number)"
    }
}
