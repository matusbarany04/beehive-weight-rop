package com.buzzybees.master.utils.json;

import java.util.ArrayList;
import java.util.Arrays;

public enum ParamType {
    ANY(value -> {}),
    NUMERIC(value -> {
        if (value instanceof Integer) {
            return;
        }
        throw new IllegalArgumentException("Invalid numeric value");
    }),
    BOOLEAN(value -> {
        if (value instanceof Boolean) {
            return;
        }
        throw new IllegalArgumentException("Invalid boolean value");
    }),
    ARRAY(value -> {
        if (value instanceof String[]) {
            // Assuming you want to convert an array of strings to an ArrayList
            ArrayList<Object> arrayList = new ArrayList<>(Arrays.asList((String[]) value));
            return;
        }
        throw new IllegalArgumentException("Invalid array value");
    }),
    STRING(value -> {
        if (value instanceof String) {
            return;
        }
        throw new IllegalArgumentException("Invalid string value");
    });

    final Caster caster;

    ParamType(Caster caster) {
        this.caster = caster;
    }

    public boolean isValidType(Object paramValue) {
        try {
            caster.tryCast(paramValue);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static ParamType getDefault() {
        return ParamType.ANY;
    }

    private interface Caster {
        void tryCast(Object value);

    }

}
