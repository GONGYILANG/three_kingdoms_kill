abstract class Strategy {
    abstract fun whomToAttack(listOfPlayers: List<Player>): Player
}

class LordStrategy: Strategy() {
    override fun whomToAttack(listOfPlayers: List<Player>): Player {
        lateinit var player: Player
        for(p in listOfPlayers) {
            if(p.identity is RebelStrategy) {
                player = p
            }
        }
        return player
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
            }
        }
        return player
    }

    override fun toString(): String {
        return "loyalist"
    }
}

class SpyStrategy: Strategy() {
    override fun whomToAttack(listOfPlayers: List<Player>): Player {
        lateinit var player: Player
        for(p in listOfPlayers) {
            if(p.identity is RebelStrategy) {
                player = p
            }
        }
        return player
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
            }
        }
        return player
    }

    override fun toString(): String {
        return "rebel"
    }
}