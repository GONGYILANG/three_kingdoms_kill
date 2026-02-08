import kotlin.random.Random

abstract class WeiGeneral: General() {
    abstract var next: WeiGeneral?
    abstract fun handleRequest()

}

class CaoCao: WeiGeneral() {
    override val name = "Cao Cao"
    override var maxHP = 4
    override var next: WeiGeneral? = null

    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }

    override fun handleRequest() {
        println("[Entourage] $name activates Lord Skill Entourage.")
        if(next != null) {
            if(next!!.hasDodgeCard() && Random.nextDouble() >= 0.5) {
                println("${next!!.name} helps Cao Cao dodged an attack by spending a dodge card.")
                return
            }
            else {
                next?.handleRequest()
            }
        }
        dodgeAttack()
    }
}

class SimaYi: WeiGeneral() {
    override val name = "Sima Yi"
    override var maxHP = 3
    override var next: WeiGeneral? = null

    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }

    override fun handleRequest() {
        if(next != null) {
            if(next!!.hasDodgeCard() && Random.nextDouble() >= 0.5) {
                println("${next!!.name} helps Cao Cao dodged an attack by spending a dodge card.")
                return
            }
            else {
                next?.handleRequest()
            }
        }
    }
}
