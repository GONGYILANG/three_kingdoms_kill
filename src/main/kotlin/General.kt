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
    // if flag is true then skip the play phase in a round, otherwise proceed the play phase

    fun beingAttacked() {
        println("$name is being attacked.")
    }

    fun hasDodgeCard(): Boolean {
        var b = true
        // each card has a chance of 15% being a dodge card,
        // so the probability of having no dodge card is (1-0.15)^numOfCard
        if(Random.nextDouble() < 0.85.pow(numOfCards))
            b = false
        return b
    }

    fun dodgeAttack() {
        if(hasDodgeCard()) {
            // consume one dodge card to dodge the attack
            numOfCards--
            println("$name dodged attack by spending a dodge card.")
            println("$name has $numOfCards of card(s) after dodging the attack.")
        }
        else {
            currentHP--
            println("$name can't dodges the attack, current HP is $currentHP")
        }
    }

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
        println("$name has $numOfCards card(s), current HP is $currentHP.")
    }
    fun discardPhase() {
        val l = listOf(1, 2, 3)
        val numOfDiscard = l.random()
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

    fun createGenerals(lords: Int, nonLords: Int) {
        val lordFactory = LordFactory()
        val nonLordFactory = NonLordFactory(null)

        for(i in 1..lords) {
            addGeneral(lordFactory.createRandomGeneral())
        }

        for(i in 1..nonLords) {
            addGeneral(nonLordFactory.createRandomGeneral())
        }
    }

    // equip a player with an equipment
    fun equip(index: Int, equipment: (Player) -> Equipment): Player {
        list[index] = equipment(list[index])
        return list[index]
    }

    fun gameStart() {
        println("\n${list[3].name} being placed the Acedia card.")
        for (i in 0 until list.size) {
            if(i == 3)
                // Place the Acedia spell card to the fourth player
                Acedia(list[i]).execute()
            list[i].templateMethod()
        }

        // testing the dodge card on general Cao Cao, who is the first general in the list
        println()
        list[0].beingAttacked()
        (list[0] as WeiGeneral).handleRequest()
    }

}

abstract class GeneralFactory {
    abstract fun createRandomGeneral(): General

}

class LordFactory: GeneralFactory() {
    private val listOfGenerals: MutableList<General> = mutableListOf(  // a list of generals not created yet
        LiuBei(), CaoCao(), SunQuan()
    )

    override fun createRandomGeneral(): General {
        val lordGeneral = listOfGenerals.random()
        listOfGenerals.remove(lordGeneral)
        lordGeneral.maxHP++
        lordGeneral.currentHP = lordGeneral.maxHP
        println("General ${lordGeneral.name} created.")
        return lordGeneral
    }
}

class GeneralAdapter(general: GuanYu): General() {
    override val name: String = general.name
    override var maxHP: Int = general.maxHP

    override fun preparationPhase() {
    }

    override fun finalPhase() {
    }
}

class NonLordFactory(private var weiGeneral: WeiGeneral?): GeneralFactory() {
    private val listOfGenerals: MutableList<General> = mutableListOf(
        ZhangFei(), GeneralAdapter(GuanYu()), ZhaoYun(), XuChu(), ZhouYu(), DiaoChan(), SimaYi()
    )

    // a method to add another Wei general to the Wei chain started with the weiGeneral field
    fun addWeiGeneral(newWeiGeneral: WeiGeneral) {
        if(weiGeneral?.next == null) {
            weiGeneral?.next = newWeiGeneral
            println("${newWeiGeneral.name} added to the Wei chain.")
        }
        else {
            NonLordFactory(weiGeneral!!.next).addWeiGeneral(newWeiGeneral)
        }
    }

    override fun createRandomGeneral(): General {
        val nonLordGeneral = listOfGenerals.random()
        listOfGenerals.remove(nonLordGeneral)
        nonLordGeneral.currentHP = nonLordGeneral.maxHP
        println("General ${nonLordGeneral.name} created.")
        return nonLordGeneral
    }
}

fun main() {
    GeneralManager
    val g1 = CaoCao()
    g1.currentHP = g1.maxHP + 1
    println("General ${g1.name} created.")
    GeneralManager.addGeneral(g1)

    GeneralManager.createGenerals(0, 2)

    val g2 = SimaYi()
    g2.currentHP = g2.maxHP
    println("General ${g2.name} created.")
    GeneralManager.addGeneral(g2)

    val g3 = ZhenJi()
    g3.currentHP = g3.maxHP
    println("General ${g3.name} created.")
    GeneralManager.addGeneral(g3)

    val factory = NonLordFactory(g1)
    factory.addWeiGeneral(g2)
    factory.addWeiGeneral(g3)

    val size = GeneralManager.getGeneralCount()
    println("Total number of players: $size")
    GeneralManager.gameStart()

//    println()
//    val armoredGeneral = GeneralManager.equip(0, ::EightTrigrams)
//    armoredGeneral.beingAttacked()
}