class LiuBei: General() {
    override val name = "Liu Bei"
    override var maxHP = 4
    override lateinit var identity: Strategy
    override fun preparationPhase() {
        if(currentHP == 1) {
            (identity as LiuBeiStrategy).state = UnhealthyState()
        }
    }

    override fun playPhase() {
        val currentState: State = (identity as LiuBeiStrategy).state
        val beforeHP: Int = currentHP
        if(currentState is HealthyState) {
            currentState.playNextCard(this)
            super.playPhase()
        }
        else {
            currentState.playNextCard(this)
            if(currentHP == beforeHP)
                super.playPhase()
        }
    }

    override fun finalPhase() {
        if(currentHP > 1) {
            (identity as LiuBeiStrategy).state = HealthyState()
        }
    }

}

class SunQuan: General() {
    override val name = "Sun Quan"
    override var maxHP = 4
    override lateinit var identity: Strategy
    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }
}

class ZhangFei: General() {
    override val name = "Zhang Fei"
    override var maxHP = 4
    override lateinit var identity: Strategy
    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }
}

class GuanYu {
    val name = "Guan Yu"
    var maxHP = 4
}

class GeneralAdapter(general: GuanYu): General() {
    override val name: String = general.name
    override var maxHP: Int = general.maxHP
    override lateinit var identity: Strategy

    override fun preparationPhase() {
    }

    override fun finalPhase() {
    }
}

class ZhaoYun: General() {
    override val name = "Zhao Yun"
    override var maxHP = 4
    override lateinit var identity: Strategy
    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }
}

class XuChu: General() {
    override val name = "Xu Chu"
    override var maxHP = 4
    override lateinit var identity: Strategy
    override fun preparationPhase() {

    }

    override fun finalPhase() {

    }
}

class ZhouYu: General() {
    override val name = "Zhou Yu"
    override var maxHP = 3
    override lateinit var identity: Strategy
    override fun preparationPhase() {

    }

    override fun drawPhase() {
        numOfCards += 3
        println("[Heroism] $name draws 3 cards and now has $numOfCards card(s).")
    }

    override fun finalPhase() {

    }
}

class DiaoChan: General() {
    override val name = "Diao Chan"
    override var maxHP = 3
    override lateinit var identity: Strategy
    override fun preparationPhase() {

    }

    override fun discardPhase() {
        super.discardPhase()
        numOfCards += 1
        println("[Beauty Outshining the Moon] $name now has $numOfCards card(s).")
    }

    override fun finalPhase() {

    }
}