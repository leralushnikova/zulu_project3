package com.kupreychik.middleware;

import com.kupreychik.dto.request.AbstractRequest;

public abstract class Middleware {
    private Middleware next;

    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextChain : chain) {
            head.next = nextChain;
            head = nextChain;
        }
        return first;
    }

    public abstract boolean check(AbstractRequest model);

    protected boolean checkNext(AbstractRequest model) {
        if (next == null) {
            return true;
        }
        return next.check(model);
    }
}
