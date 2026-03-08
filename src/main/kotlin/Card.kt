interface Card {
    val suit: String
    val number: Int
    val name: String
}

class Deck {
    private val deckOfCards: MutableList<Card> = mutableListOf()
    private val deckOfDiscardedCards: MutableList<Card> = mutableListOf()

    fun prepareCardDeck() {
        if(deckOfCards.isEmpty() && deckOfDiscardedCards.isEmpty()) {
            initializeBasicCards()
            initializeSpellCards()
            initializeEquipmentCards()
        }
        else if(deckOfCards.isEmpty() && deckOfDiscardedCards.isNotEmpty()) {
            // when the deckOfDiscardedCards is not empty, but the deck of cards is empty,
            // take all cards from discarded deck and shuffle then as the new deck of cards
            deckOfDiscardedCards.forEach { deckOfCards.add(it) }
            deckOfDiscardedCards.removeAll({ true })
        }
        else {

        }
        deckOfCards.shuffle()
    }

    fun initializeBasicCards() {

    }

    fun initializeSpellCards() {

    }

    fun initializeEquipmentCards() {

    }

    fun draw(numOfCards: Int) {

    }
}
