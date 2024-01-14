package org.springframework.samples.dwarf.cardDeck;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardRepository;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
public class cardDeckServiceSocialTest {

    @Autowired
    private CardDeckService cardDeckService;

    @Autowired
    private CardService cardService;

    @Test
    @Transactional
    void testShuffleAndSaveCards() {
        CardDeck cd = cardDeckService.getCardDeckById(1);
        List<Card> originalCardList = cd.getCards(); 

        Card originalFirstCard = originalCardList.get(0);
        Card c1 = cardService.getById(2);
        Card c2 = cardService.getById(3);
        Card c3 = cardService.getById(4);

        assertEquals(originalCardList.contains(c1), false);
        assertEquals(originalCardList.contains(c2), false);
        assertEquals(originalCardList.contains(c3), false);

        originalCardList.addAll(List.of(c1,c2,c3));

        cd = cardDeckService.shuffleAndSaveCards(cd, originalCardList);
        List<Card> newCardList = cd.getCards();

        // Como se baraja de manera aleatoria cabe la posibilidad de que este test
        // de error. 
        assertNotEquals(newCardList.get(0), originalFirstCard);
        assertEquals(newCardList.contains(c1), true);
        assertEquals(newCardList.contains(c2), true);
        assertEquals(newCardList.contains(c3), true);
    }

    @Test
    @Transactional
    void testInitialize() {
        CardDeck cd = cardDeckService.initialiate();

        Card c1 = cardService.getById(1);
        Card c2 = cardService.getById(2);
        Card c3 = cardService.getById(3);
        Card c4 = cardService.getById(4);
        Card c5 = cardService.getById(5);
        Card c6 = cardService.getById(6);
        Card c7 = cardService.getById(7);
        Card c8 = cardService.getById(8);
        Card c9 = cardService.getById(9);

        List<Card> cards = cd.getCards();
        assertEquals(cards.contains(c1), false);
        assertEquals(cards.contains(c2), false);
        assertEquals(cards.contains(c3), false);
        assertEquals(cards.contains(c4), false);
        assertEquals(cards.contains(c5), false);
        assertEquals(cards.contains(c6), false);
        assertEquals(cards.contains(c7), false);
        assertEquals(cards.contains(c8), false);
        assertEquals(cards.contains(c9), false);

        Integer lengthExpected = 45;// 54 - 9
        assertEquals(cards.size(), lengthExpected);
    }

    @Test
    @Transactional
    void testGetNewCardsShouldReturnTwoCards() {
        CardDeck cd = cardDeckService.getCardDeckById(1);
        Integer currentCardsSize = cd.getCards().size();
        List<Card> returnedCards = cardDeckService.getNewCards(1);
        List<Card> currentCards = cardDeckService.getCardDeckById(1).getCards();

        Integer cardsTaken = 2;

        assertEquals(currentCards.contains(returnedCards.get(0)), false);
        assertEquals(currentCards.contains(returnedCards.get(1)), false);
        assertEquals(currentCardsSize-cardsTaken, currentCards.size());
    }

    @Test
    @Transactional
    void testGetNewCardsShouldHaveTakenThreeCards() {
        CardDeck cd = cardDeckService.getCardDeckById(2);
        Integer currentCardsSize = cd.getCards().size();
        List<Card> returnedCards = cardDeckService.getNewCards(2);
        List<Card> currentCards = cardDeckService.getCardDeckById(2).getCards();

        Integer cardsTaken = 3;

        assertEquals(currentCards.contains(returnedCards.get(0)), false);
        assertEquals(currentCards.contains(returnedCards.get(1)), false);
        assertEquals(currentCardsSize-cardsTaken, currentCards.size());
    }

    @Test
    @Transactional
    void testGetNewCardsShouldReturnOneCard() {
        CardDeck cd = cardDeckService.getCardDeckById(3);
        Integer currentCardsSize = cd.getCards().size();
        List<Card> returnedCards = cardDeckService.getNewCards(3);
        List<Card> currentCards = cardDeckService.getCardDeckById(3).getCards();

        Integer cardsTaken = 3;

        assertEquals(returnedCards.size(), 1);
        assertEquals(currentCards.contains(returnedCards.get(0)), false);
        assertEquals(currentCardsSize-cardsTaken, currentCards.size());
    }
}