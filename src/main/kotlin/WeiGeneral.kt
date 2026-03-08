import kotlin.random.Random

abstract class WeiGeneral: General() {
    abstract var next: WeiGeneral?
    abstract fun handleRequest(): Boolean

    protected fun handleNextRequest(): Boolean {
        var result = false
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
}

class CaoCao: WeiGeneral() {
    override val name = "Cao Cao"
    override var maxHP = 4
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    /**
     * Override the dodge logic for Cao Cao, first check if Entourage succeeds before using Dodge
     */
    override fun dodgeAttack(): Boolean {
        // 1. try to trigger Entourage
        println("[Entourage] $name activates Lord Skill Entourage.")
        val entourageResult = handleNextRequest()

        if (entourageResult) {
            println("$name successfully dodged the attack with Entourage.")
            return true
        }

        // 2. Entourage fails, call the dodgeAttack() method in Player
        println("Entourage failed, $name tries to dodge the attack with [Dodge].")
        return super.dodgeAttack()
    }

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }

}

class SimaYi: WeiGeneral() {
    override val name = "Sima Yi"
    override var maxHP = 3
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}

class ZhenJi: WeiGeneral() {
    override val name = "Zhen Ji"
    override var maxHP = 3
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}

class XiahouDun: WeiGeneral() {
    override val name = "Xiahou Dun"
    override var maxHP = 4
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}

class GuoJia: WeiGeneral() {
    override val name = "Guo Jia"
    override var maxHP = 4
    override var next: WeiGeneral? = null
    override lateinit var identity: Strategy

    override fun handleRequest(): Boolean {
        return handleNextRequest()
    }
}
