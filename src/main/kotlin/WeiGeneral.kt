import kotlin.random.Random

abstract class WeiGeneral: General() {
    abstract var next: WeiGeneral?
    abstract fun handleRequest(): Boolean

    protected fun handleNextRequest(): Boolean {
        var result: Boolean = false
        if(next != null) {
            if(next!!.currentHP > 0 && next!!.hasDodgeCard() && Random.nextDouble() >= 0.5) {
                next!!.numOfCards--
                println("${next!!.name} helps Cao Cao dodged an attack by spending a dodge card.")
                println("${next!!.name} now has ${next!!.numOfCards} cards left after dodging the attack for Cao Cao.")
                result = true
            }
            else {
                result = next!!.handleRequest()
            }
        }
        return result
    }

    override fun beingAttacked() {
        println("$name is being attacked.")
        if(this is CaoCao)
            handleRequest()
        else
            dodgeAttack()
    }
}

class CaoCao: WeiGeneral() {
    override val name = "Cao Cao"
    override var maxHP = 4
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }

    override fun handleRequest(): Boolean {
        println("[Entourage] $name activates Lord Skill Entourage.")
        val result = handleNextRequest()  // whether another Wei general dodged the attack for Cao Cao
        // if all Wei generals in the Wei chain failed to dodge the attack, Cao Cao dodges it by himself
        if(!result)
            dodgeAttack()
        return false
    }
}

class SimaYi: WeiGeneral() {
    override val name = "Sima Yi"
    override var maxHP = 3
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}

class ZhenJi: WeiGeneral() {
    override val name = "Zhen Ji"
    override var maxHP = 3
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}

class XiahouDun: WeiGeneral() {
    override val name = "Xiahou Dun"
    override var maxHP = 4
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}

class GuoJia: WeiGeneral() {
    override val name = "Guo Jia"
    override var maxHP = 4
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}
