package com.hiddenramblings.tagmo.amiibo;

import androidx.annotation.NonNull;

public class AmiiboType implements Comparable<AmiiboType> {

    static final long MASK = 0x000000FF00000000L;
    static final int BITSHIFT = 4 * 8;

    public final AmiiboManager manager;
    public final long id;
    public final String name;

    public AmiiboType(AmiiboManager manager, long id, String name) {
        this.manager = manager;
        this.id = id;
        this.name = name;
    }

    public AmiiboType(AmiiboManager manager, String id, String name) {
        this(manager, hexToId(id), name);
    }

    static long hexToId(String value) {
        return (Long.decode(value) << BITSHIFT) & MASK;
    }

    @Override
    public int compareTo(@NonNull AmiiboType amiiboType) {
        return Long.compare(this.id, amiiboType.id);
    }
}
