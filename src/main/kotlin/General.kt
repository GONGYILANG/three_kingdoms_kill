interface Player {
    val name: String
    var maxHP: Int
    var currentHP: Int
        get() = 0
        set(value) {
            println("Setting current HP to $value")
        }

    fun beingAttacked() {
        val flag = true
        println("$name is being attacked.")
        if(flag) {
            currentHP--
            println("$name can't dodge the attack, current HP is $currentHP")
        }
    }

    fun dodgeAttack() {
        println("$name dodges the attack.")
    }
}

abstract class General: Player {
    override var currentHP: Int = 0
}


object GeneralManager {
    init {
        println("Setting up the general manager.")
    }
    private val list: MutableList<General> = mutableListOf()

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
        val nonLordFactory = NonLordFactory()

        for(i in 1..lords) {
            addGeneral(lordFactory.createRandomGeneral())
        }

        for(i in 1..nonLords) {
            addGeneral(nonLordFactory.createRandomGeneral())
        }
    }

    fun attackFirstGeneral() {
        list[0].beingAttacked()
    }
}

abstract class GeneralFactory {
    abstract fun createRandomGeneral(): General

//    fun createGeneral(name: String): General {
//        val general =  when(name) {
//            "Liu Bei" -> LiuBei()
//            "Cao Cao" -> CaoCao()
//            "Sun Quan" -> SunQuan()
//            else -> throw IllegalArgumentException("Invalid general name.")
//        }
//        println("General ${general.name} created.")
//        general.currentHP = general.maxHP
//        return general
//    }
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
        return lordGeneral
    }
}

class NonLordFactory: GeneralFactory() {
    private val listOfGenerals: MutableList<General> = mutableListOf(
        ZhangFei(), GuanYu(), ZhaoYun(), XuChu(), ZhouYu()
    )

    override fun createRandomGeneral(): General {
        val nonLordGeneral = listOfGenerals.random()
        listOfGenerals.remove(nonLordGeneral)
        nonLordGeneral.currentHP = nonLordGeneral.maxHP
        return nonLordGeneral
    }
}

fun main() {
    GeneralManager
    GeneralManager.createGenerals(1, 2)

    val size = GeneralManager.getGeneralCount()
    println("Total number of generals: $size")

    GeneralManager.attackFirstGeneral()
}