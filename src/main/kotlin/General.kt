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
    var skipDrawPhase: Boolean
    var skipPlayPhase: Boolean
    var identity: Strategy
    var judgementZone: MutableList<Spell>  // a list of time-delayed spells to take effects
    // a map of equipments, the key is an equipment's category, the value is the equipment
    val equipmentZone: MutableMap<EquipmentSlot, Equipment>

    /**
     * Procedure of a player being attacked by another player.
     * @return true means the attack was handled successfully and didn't harm
     * player's currentHP, false otherwise.
     */
    fun beingAttacked(): Boolean {
        println("$name is being attacked.")
        // 1. First check if the equipment zone has any armor
        val armor = equipmentZone[EquipmentSlot.ARMOR]
        if (armor != null) {
            // return true if the armor cancels the attack successfully
            if (armor.onBeingAttacked(this)) return true
        }

        // 2. If no armor in the equipment zone, enter the normal dodge logic
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
            println("$name has $numOfCards card(s) after dodging the attack.")
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

    fun templateMethod()

}

abstract class General: Player {
    override var currentHP: Int = 0
    override var numOfCards: Int = 4
    override var judgementZone: MutableList<Spell> = mutableListOf()
    override var equipmentZone: MutableMap<EquipmentSlot, Equipment> = mutableMapOf()
    override var skipDrawPhase: Boolean = false
    override var skipPlayPhase: Boolean = false

    /**
     * The gameplay procedure that is executed on each player. In each round of the game,
     * a player goes through five phases including Preparation phase, Draw phase,
     * Judgement Phase, Play phase, Discard Phase, and Final phase.
     */
    override fun templateMethod() {
        println("--------- ${name}'s round begins ---------")

        skipDrawPhase = false
        skipPlayPhase = false
        preparationPhase()

        judgementPhase()

        if (!skipDrawPhase) {
            drawPhase()
        } else {
            println("$name's draw phase has been skipped.")
        }

        if (!skipPlayPhase) {
            playPhase()
        } else {
            println("$name's play phase has been skipped.")
        }

        discardPhase()

        finalPhase()

        println("--------- ${name}'s round ends ---------\n")
    }

    open fun preparationPhase() {

    }

    open fun judgementPhase() {
        if (judgementZone.isEmpty()) return

        println("$name enters the judgement phase," +
                " and there are ${judgementZone.size} card(s) in the judgement zone.")

        // 重要：判定顺序是“后发先至”(LIFO)，所以从列表末尾开始遍历
        val iterator = judgementZone.asReversed().iterator()
        while (iterator.hasNext()) {
            val spell = iterator.next()

            // 执行判定（Spell 内部会修改 player 的 skip 开关）
            // 建议：execute 方法可以传入一个 Deck 对象来抽取真实的判定牌
            spell.execute()

            iterator.remove()
        }
    }

    open fun drawPhase() {
        numOfCards += 2
        println("$name draws 2 cards and now has $numOfCards card(s).")
    }

    open fun playPhase() {
        if(hasAttackCard()) {
            val enemy: Player = identity.whomToAttack(GeneralManager.getGeneralList())
            println("$name spends a card to attack a ${enemy.identity}, ${enemy.name}.")
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

    open fun discardPhase() {
        var numOfDiscard = 0
        if(numOfCards > currentHP) {
            numOfDiscard = numOfCards - currentHP
            numOfCards -= numOfDiscard
        }
        println("$name discards $numOfDiscard card(s), now has $numOfCards card(s).")
    }

    open fun finalPhase() {

    }
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

    /**
     * Equip a player with a piece of equipment
     * @param playerList: a list of players that aren't out
     * @param targetIndex: the index of the player to be equipped
     * @param equipment: the equipment
     */
    fun equip(playerList: MutableList<Player>, targetIndex: Int, equipment: Equipment) {
        val targetPlayer = playerList[targetIndex]
        // val eightTrigrams = EightTrigrams("Spade", 2)
        targetPlayer.equipmentZone[equipment.slot] = equipment
        println("${targetPlayer.name} has been equipped with ${equipment.name}.")
    }

    fun gameStart() {
        equip(list, 0, EightTrigrams("Spade", 2))
        val acedia = Acedia("Heart", 2)
        acedia.target = list[3]
        list[3].judgementZone.add(0, acedia)
        println("${list[3].name} being placed the Acedia card.")
        for(r in 0..1) {
            for(i in 0 until list.size) {
                list[i].templateMethod()
            }
            println()
        }

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

}
