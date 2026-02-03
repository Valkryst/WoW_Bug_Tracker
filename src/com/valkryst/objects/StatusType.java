package com.valkryst.objects;

import lombok.Getter;

public class StatusType {
    /** The id. */
    @Getter private final int id;
    /** The name. */
    @Getter private final String name;

    /**
     * Constructs a new StatusType with the specified data.
     *
     * @param id
     *         The id to use.
     *
     * @param name
     *         The name to use.
     */
    public StatusType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
