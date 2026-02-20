interface Subject {
    fun attachObserver(observer: Observer)
    fun detachObserver(observer: Observer)
    fun notifyObservers(result: Boolean)
}

interface Observer {
    fun update(result: Boolean)
}

abstract class Strategy {
    abstract fun whomToAttack(listOfPlayers: List<Player>): Player
}

class LordStrategy: Strategy(), Subject {
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

class LoyalistStrategy: Strategy() {
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

class SpyStrategy: Strategy(), Observer {
    private var risk: Double = 50.0

    fun getRisk(): Double = risk

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
            risk *= 1.5
        else
            risk *= 0.5
        println("Spy on Lord's Risk Level: $risk")
    }

    override fun toString(): String {
        return "spy"
    }
}

class RebelStrategy: Strategy() {
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