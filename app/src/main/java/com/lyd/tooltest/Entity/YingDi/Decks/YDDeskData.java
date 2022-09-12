package com.lyd.tooltest.Entity.YingDi.Decks;

public class YDDeskData<T> {
    private T deck;

    private User user; //user是一致的，但deck是不同的

    public T getDeck() {
        return deck;
    }

    public void setDeck(T deck) {
        this.deck = deck;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "YDDeskData{" +
                "deck=" + deck +
                ", user=" + user.getUsername() +
                '}';
    }
}
