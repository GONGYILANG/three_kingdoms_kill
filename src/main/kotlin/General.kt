abstract class General {
//    init {
//        println("General $name has been created.")
//    }
    abstract val name: String
    abstract val maxHP: Int
    var currentHP: Int = 0
}

class LiuBei: General() {
    override val name = "Liu Bei"
    override val maxHP = 4
}

class CaoCao: General() {
    override val name = "Cao Cao"
    override val maxHP = 4
}

class SunQuan: General() {
    override val name = "Sun Quan"
    override val maxHP = 4
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

object GeneralFactory {
    fun createGeneral(name: String): General {
        val general =  when(name) {
            "Liu Bei" -> LiuBei()
            "Cao Cao" -> CaoCao()
            "Sun Quan" -> SunQuan()
            else -> throw IllegalArgumentException("Invalid general name.")
        }
        println("General ${general.name} created.")
        general.currentHP = general.maxHP
        return general
    }
}

fun main() {
    GeneralManager
//    val liuBei = LiuBei()
//    liuBei.currentHP = liuBei.maxHP
//    val caoCao = CaoCao()
//    caoCao.currentHP = caoCao.maxHP
//    val sunQuan = SunQuan()
//    sunQuan.currentHP = sunQuan.maxHP
    val liuBei = GeneralFactory.createGeneral("Liu Bei")
    GeneralManager.addGeneral(liuBei)
    val caoCao = GeneralFactory.createGeneral("Cao Cao")
    GeneralManager.addGeneral(caoCao)
    val sunQuan = GeneralFactory.createGeneral("Sun Quan")
    GeneralManager.addGeneral(sunQuan)
    val size = GeneralManager.getGeneralCount()
    println("Total number of generals: $size")
}