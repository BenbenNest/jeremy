package com.jeremy.lychee.utils.transformer;

import java.util.Iterator;

import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func2;

public final class MapWithIndex<T> implements Transformer<T, MapWithIndex.Indexed<T>> {

    private static class Holder {
        static final MapWithIndex<?> INSTANCE = new MapWithIndex<Object>();
    }

    @SuppressWarnings("unchecked")
    public static <T> MapWithIndex<T> instance() {
        return (MapWithIndex<T>) Holder.INSTANCE;
    }

    @Override
    public Observable<Indexed<T>> call(Observable<T> source) {

        return source.zipWith(NaturalNumbers.instance(), new Func2<T, Integer, Indexed<T>>() {

            @Override
            public Indexed<T> call(T t, Integer n) {
                return new Indexed<T>(t, n);
            }
        });
    }

    public static class Indexed<T> {
        private final int index;
        private final T value;

        public Indexed(T value, int index) {
            this.index = index;
            this.value = value;
        }

        @Override
        public String toString() {
            return index + "->" + value;
        }

        public int index() {
            return index;
        }

        public T value() {
            return value;
        }
    }

    private static final class NaturalNumbers implements Iterable<Integer> {

        private static class Holder {
            static final NaturalNumbers INSTANCE = new NaturalNumbers();
        }

        static NaturalNumbers instance() {
            return Holder.INSTANCE;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {

                private int n = 0;

                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Integer next() {
                    return n++;
                }

                @Override
                public void remove() {
                    throw new RuntimeException("not supported");
                }
            };
        }
    }
}