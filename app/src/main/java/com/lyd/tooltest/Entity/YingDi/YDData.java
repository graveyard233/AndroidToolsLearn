package com.lyd.tooltest.Entity.YingDi;

import java.util.List;

public class YDData<T> {
    private int total;

    private T cards;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getCards() {
        return cards;
    }

    public void setCards(T cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "YDData{" +
                "total=" + total +
                ", list=" + cards +
                '}';
    }
}
