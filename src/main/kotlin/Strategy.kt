interface Subject {
    fun attachObserver(observer: Observer)
    fun detachObserver(observer: Observer)
    fun notifyObservers(result: Boolean)
}

interface Observer {
    fun update(result: Boolean)
}

abstract class Strategy(protected val owner: General) {
    abstract fun whomToAttack(listOfPlayers: List<Player>): Player
}

open class LordStrategy(owner: General): Strategy(owner), Subject {
    private val observers: MutableList<Observer> = mutableListOf()

    override fun whomToAttack(listOfPlayers: List<Player>): Player {
        lateinit var player: Player
        for(p in listOfPlayers) {
            if(p.identity is RebelStrategy) {
                player = p
                break
            }
        }
        return player
    }

    override fun attachObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun detachObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers(result: Boolean) {
        observers.forEach{
            it.update(result)
        }
    }

    override fun toString(): String {
        return "lord"
    }
}

class LiuBeiStrategy(owner: General): LordStrategy(owner) {
    lateinit var state: State

    fun playNextCard() {
        state.playNextCard(owner)
    }
}

class LoyalistStrategy(owner: General): Strategy(owner) {
    override fun whomToAttack(listOfPlayers: List<Player>): Player {
        lateinit var player: Player
        for(p in listOfPlayers) {
            if(p.identity is RebelStrategy) {
                player = p
                break
            }
        }
        return player
    }

    override fun toString(): String {
        return "loyalist"
    }
}

class SpyStrategy(owner: General): Strategy(owner), Observer {
    private var risk: Double = 50.0

    override fun whomToAttack(listOfPlayers: List<Player>): Player {
        lateinit var player: Player
        for(p in listOfPlayers) {
            if(p.identity is RebelStrategy) {
                player = p
                break
            }
        }
        return player
    }

    override fun update(result: Boolean) {
        // if a lord handled an attack successfully(without losing HP),
        // the result is true; false otherwise
        if(result)
            // if the lord dodge the attack, the risk level decreases to 0.5 of the original value
            risk *= 0.5
        else
            // if the lord can't dodge the attack, the risk level increases to 1.5 of the original value
            risk *= 1.5
        println("${owner.name} on Lord's Risk Level: $risk")
    }

    override fun toString(): String {
        return "spy"
    }
}

class RebelStrategy(owner: General): Strategy(owner) {
    override fun whomToAttack(listOfPlayers: List<Player>): Player {
        lateinit var player: Player
        for(p in listOfPlayers) {
            if(p.identity is LordStrategy) {
                player = p
                break
            }
        }
        return player
    }

    override fun toString(): String {
        return "rebel"
    }
}
