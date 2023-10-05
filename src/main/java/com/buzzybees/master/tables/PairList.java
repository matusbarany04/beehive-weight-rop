package com.buzzybees.master.tables;

import org.apache.poi.ss.formula.functions.Function2Arg;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class PairList<F, S> extends ArrayList<Object[]> {

    @SuppressWarnings("unchecked")
    public void forEach(BiConsumer<F, S> consumer) {
        for(Object[] pair : this) {
            if (pair.length >= 2) {
                F first = (F) pair[0];
                S second = (S) pair[1];
                consumer.accept(first, second);
            }
        }
    }
}
