package com.jeremy.lychee.utils;

public class PairKey<T1, T2> {
    private final T1 x;
    private final T2 y;

    public PairKey(T1 x, T2 y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairKey)) return false;
        PairKey key = (PairKey) o;
        return x.equals(key.x) && y.equals(key.y);
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }
}
