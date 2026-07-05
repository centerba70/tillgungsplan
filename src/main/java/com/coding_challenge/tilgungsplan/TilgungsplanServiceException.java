package com.coding_challenge.tilgungsplan;

import java.io.Serial;

public class TilgungsplanServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TilgungsplanServiceException(String message) {
        super(message);
    }
}
