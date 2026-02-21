import kotlin.math.pow
import kotlin.random.Random

interface Player {
    val name: String
    var maxHP: Int
    var currentHP: Int
        get() = 0
        set(value) {
            println("Setting current HP to $value")
        }
    var numOfCards: Int
    var flag: Boolean  // a boolean variable used in the judgement phase,
    // if flag is true then skip the play phase for one round, otherwise the play phase proceeds
    var identity: Strategy

    /**
     * Procedure of a player being attacked by another player.
     * @return true means the attack was handled successfully and didn't harm
     * player's currentHP, false otherwise.
     */
    fun beingAttacked(): Boolean {
        println("$name is being attacked.")
        return dodgeAttack()
    }

    /**
     * Judging if a player has at least one dodge card, which can offset an ongoing attack.
     * Each hand card of a player has a chance of 15% being a dodge card.
     * @return true means the player has at least one dodge card, false otherwise.
     */
    fun hasDodgeCard(): Boolean {
        var b = true
        // each card has a chance of 15% being a dodge card,
        // so the probability of having no dodge card is (1-0.15)^numOfCard
        if(Random.nextDouble() < 0.85.pow(numOfCards))
            b = false
        return b
    }

    /**
     * Procedure of dodging an ongoing attack.
     * @return true means the attack was dodged successfully, false otherwise.
     */
    fun dodgeAttack(): Boolean {
        // returning true means the attack is dodged, false means otherwise
        var result = true
        if(hasDodgeCard()) {
            // consume one dodge card to dodge the attack
            numOfCards--
            println("$name dodged attack by spending a dodge card.")
            println("$name has $numOfCards of card(s) after dodging the attack.")
        }
        else {
            currentHP--
            println("$name can't dodges the attack, current HP is $currentHP")
            result = false
        }
        return result
    }

    /**
     * Judging if a player has at least one attack card, which can launch an attack.
     * Each hand card of a player has a chance of 20% being an attack card.
     * @return true means the player has at least one attack card, false otherwise.
     */
    fun hasAttackCard(): Boolean {
        var b = true
        if(Random.nextDouble() < 0.8.pow(numOfCards)) {
            b = false
        }
        return b
    }

    /**
     * The gameplay procedure that is executed on each player. In each round of the game,
     * a player goes through five phases including Preparation phase, Draw phase,
     * Judgement Phase, Play phase, Discard Phase, and Final phase.
     */
    fun templateMethod() {
        preparationPhase()
        drawPhase()
        if(judgementPhase()) {
            playPhase()
        }
        discardPhase()
        finalPhase()
    }

    // component methods invoked at a player's turn
    fun preparationPhase()
    fun judgementPhase(): Boolean {
        return flag
    }
    fun drawPhase() {
        numOfCards += 2
        println("$name draws 2 cards and now has $numOfCards card(s).")
    }
    fun playPhase() {
        if(hasAttackCard()) {
            val enemy: Player = identity.whomToAttack(GeneralManager.getGeneralList())
            println("$name spends a card to attack a ${enemy.identity}, ${enemy.name}")
            val attackResult = enemy.beingAttacked()
            if(enemy.identity is LordStrategy) {
                (enemy.identity as Subject).notifyObservers(attackResult)
            }
            numOfCards--
        }
        else
            println("$name doesn't have attack card.")
        println("$name has $numOfCards card(s), current HP is $currentHP.")

    }
    fun discardPhase() {
        val l = listOf(1, 2, 3)
        val numOfDiscard = l.random()
        if(numOfDiscard > numOfCards)
            numOfCards = 0
        else
            numOfCards -= numOfDiscard
        println("$name discards $numOfDiscard card(s), now has $numOfCards card(s).")

    }
    fun finalPhase()
}

abstract class General: Player {
    override var currentHP: Int = 0
    override var numOfCards: Int = 4
    override var flag: Boolean = true


}

object GeneralManager {
    init {
        println("Setting up the general manager.")
    }
    private val list: MutableList<Player> = mutableListOf()

    fun addGeneral(general: General) {
        list.add(general)
        println("${general.name} (with ${general.currentHP} HP) has been added to the list.")
    }

    fun removeGeneral(general: General) {
        list.remove(general)
        println("${general.name} has been removed from the list.")
    }

    fun getGeneralCount(): Int {
        return list.size
    }

    fun getGeneralList(): List<Player> {
        return list.toList()
    }

    fun createGenerals(numOfLords: Int, numOfNonLords: Int) {
        val lordFactory = LordFactory()
        for(i in 1..numOfLords) {
            addGeneral(lordFactory.createRandomGeneral())
        }

        val nonLordFactory = NonLordFactory(list[0] as? WeiGeneral, numOfLords+numOfNonLords)
        for(i in 1..numOfNonLords) {
            addGeneral(nonLordFactory.createRandomGeneral())
        }
    }

    // equip a player with an equipment
    fun equip(index: Int, equipment: (Player) -> Equipment): Player {
        list[index] = equipment(list[index])
        return list[index]
    }

    fun gameStart() {
        println("${list[3].name} being placed the Acedia card.")
        for (i in 0 until list.size) {
            if(i == 3)
                // Place the Acedia spell card to the fourth player
                Acedia(list[i]).execute()
            list[i].templateMethod()
        }

        // testing the Eight Trigrams card on the first general in the general list
//        println()
//        equip(0, ::EightTrigrams)
//        list[0].beingAttacked()
    }

}

abstract class GeneralFactory {
    abstract fun createRandomGeneral(): General

}

class LordFactory: GeneralFactory() {
    private val listOfGenerals: MutableList<General> = mutableListOf(  // a list of generals not created yet
        CaoCao(), LiuBei(), SunQuan()
    )

    override fun createRandomGeneral(): General {
        // initialize a random lord general
        val lordGeneral = listOfGenerals.random()
        listOfGenerals.remove(lordGeneral)
        lordGeneral.maxHP++
        if(lordGeneral is LiuBei)
            lordGeneral.currentHP = 1
        else
            lordGeneral.currentHP = lordGeneral.maxHP
        if(lordGeneral is LiuBei) {
            val liuBeiStrategy = LiuBeiStrategy(lordGeneral)
            liuBeiStrategy.state = HealthyState()
            lordGeneral.identity = liuBeiStrategy
        }
        else
            lordGeneral.identity = LordStrategy(lordGeneral)

        println("General ${lordGeneral.name} created.")
        println("${lordGeneral.name}, a lord, has ${lordGeneral.currentHP} health point(s).")
        return lordGeneral
    }
}

class NonLordFactory(private var weiGeneral: WeiGeneral?, numOfParticipants: Int): GeneralFactory() {
    private val listOfGenerals: MutableList<General> = mutableListOf(
        ZhangFei(), GeneralAdapter(GuanYu()), ZhaoYun(), XuChu(), ZhouYu(), DiaoChan(),
        SimaYi(), ZhenJi(), XiahouDun(), GuoJia()
    )

    private val identityList: List<Int> = when(numOfParticipants) {
        4 -> listOf(1, 1, 1)
        5 -> listOf(1, 2, 1)
        6 -> listOf(1, 3, 1)
        7 -> listOf(2, 3, 1)
        8 -> listOf(2, 3, 2)
        9 -> listOf(3, 4, 1)
        10 -> listOf(3, 4, 2)
        else -> throw RuntimeException("Invalid number of participants!")
    }
    private var numOfLoyalists = identityList[0]
    private var numOfRebels = identityList[1]
    private var numOfSpies = identityList[2]

    // a method to add another Wei general to the Wei chain started with the weiGeneral field
    fun addToWeiChain(newWeiGeneral: WeiGeneral) {
        if(weiGeneral == null) return

        while(weiGeneral?.next != null
            && weiGeneral!!.next!!.javaClass !== newWeiGeneral.javaClass) {
            weiGeneral = weiGeneral!!.next
        }
        weiGeneral?.next = newWeiGeneral
        println("${newWeiGeneral.name} added to the Wei chain.")

    }

    override fun createRandomGeneral(): General {
        val nonLordGeneral = listOfGenerals.random()
        listOfGenerals.remove(nonLordGeneral)
        nonLordGeneral.currentHP = nonLordGeneral.maxHP

        val listOfGen = GeneralManager.getGeneralList()
        val subject = listOfGen[0].identity as? Subject ?: error("failed to convert to subject")
        if(numOfLoyalists > 0) {
            nonLordGeneral.identity = LoyalistStrategy(nonLordGeneral)
            numOfLoyalists--
        } else if(numOfRebels > 0) {
            nonLordGeneral.identity = RebelStrategy(nonLordGeneral)
            numOfRebels--
        } else if(numOfSpies > 0) {
            val spyStrategy = SpyStrategy(nonLordGeneral)
            nonLordGeneral.identity = spyStrategy
            numOfSpies--
            // Let every spy become an observer to the lord
            subject.attachObserver(spyStrategy)
        } else {
            throw RuntimeException("Failed to create more generals.")
        }

        println("${nonLordGeneral.name}, a ${nonLordGeneral.identity}, " +
                "has ${nonLordGeneral.currentHP} health point(s).")
        println("General ${nonLordGeneral.name} created.")
        return nonLordGeneral
    }
}

fun main() {
    GeneralManager
    GeneralManager.createGenerals(1, 3)
    val listOfGen: List<Player> = GeneralManager.getGeneralList()
    if(listOfGen[0] is WeiGeneral) {
        val factory = NonLordFactory(listOfGen[0] as WeiGeneral, 4)
        for (i in 1 until listOfGen.size) {
            if (listOfGen[i] is WeiGeneral)
                factory.addToWeiChain(listOfGen[i] as WeiGeneral)
        }
    }

    val size = GeneralManager.getGeneralCount()
    println("Total number of players: $size\n")
    GeneralManager.gameStart()
    println()
    GeneralManager.gameStart()

}
