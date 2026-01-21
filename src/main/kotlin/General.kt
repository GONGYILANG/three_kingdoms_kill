import kotlin.math.max

abstract class General {
//    init {
//        println("General $name has been created.")
//    }
    abstract val name: String
    abstract var maxHP: Int
    var currentHP: Int = 0
}

class LiuBei: General() {
    override val name = "Liu Bei"
    override var maxHP = 4
}

class CaoCao: General() {
    override val name = "Cao Cao"
    override var maxHP = 4
}

class SunQuan: General() {
    override val name = "Sun Quan"
    override var maxHP = 4
}

class ZhangFei: General() {
    override val name = "Zhang Fei"
    override var maxHP = 4
}

class GuanYu: General() {
    override val name = "Guan Yu"
    override var maxHP = 4
}

class ZhaoYun: General() {
    override val name = "Zhao Yun"
    override var maxHP = 4
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
        ZhangFei(), GuanYu(), ZhaoYun()
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
    val lordFactory = LordFactory()
    val nonLordFactory = NonLordFactory()

    for(i in 1..3) {
        GeneralManager.addGeneral(lordFactory.createRandomGeneral())
    }

    for(i in 1..3) {
        GeneralManager.addGeneral(nonLordFactory.createRandomGeneral())
    }


    val size = GeneralManager.getGeneralCount()
    println("Total number of generals: $size")
}