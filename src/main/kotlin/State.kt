interface State {
    fun playNextCard(owner: General)
}

class HealthyState: State {
    override fun playNextCard(owner: General) {
    }
}

class UnhealthyState: State {
    override fun playNextCard(owner: General) {
        if(owner.numOfCards >= 2) {
            owner.numOfCards -= 2
            owner.currentHP++
            println(
                "[Benevolence] ${owner.name} gives away two cards and recover 1 HP, " +
                        "now his HP is ${owner.currentHP}. "
            )
        } else {
            println("${owner.name} doesn't have enough cards to trigger [Benevolence].")
        }
    }
}