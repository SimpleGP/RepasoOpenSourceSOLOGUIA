package com.caterpillar.platform.shared.interfaces.rest.resources;

import java.time.Instant;

/**
 * Provides a consistent JSON representation for API errors.
 *
 * @param timestamp time when the error was produced
 * @param status HTTP status code
 * @param error HTTP status description
 * @param message human-readable error message
 * @param path request path
 */
public record ErrorResource(Instant timestamp, int status, String error, String message, String path) {
}
