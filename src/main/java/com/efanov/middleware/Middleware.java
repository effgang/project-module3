package com.efanov.middleware;

import com.efanov.dto.student.AbstractPeople;

public abstract class Middleware {
    private Middleware next;


    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(AbstractPeople model);


    protected boolean checkNext(AbstractPeople model) {
        if (next == null) {
            return true;
        }
        return next.check(model);
    }
}