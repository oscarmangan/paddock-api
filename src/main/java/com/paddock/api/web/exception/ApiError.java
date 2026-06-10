package com.paddock.api.web.exception;

import java.time.OffsetDateTime;

public record ApiError (
    int status,
    String error,
    String message,
    String path,
    OffsetDateTime timestamp
) {}
