package com.airtribe.meditrack.interfaces;

/**
 * Generic Searchable interface.
 * Demonstrates generics, interface methods, and Java 8 default methods.
 *
 * @param <T> Entity type to be searched
 */
public interface Searchable<T> {

    /**
     * Search by a keyword or query string.
     *
     * @param query Search query string
     * @return true if entity matches the query
     */
    boolean matchesQuery(String query);

    /**
     * Default method demonstrating Java 8 default behavior in interfaces.
     *
     * @param query Search term
     * @return Formatted search result description
     */
    default String getSearchableSummary(String query) {
        return "Search match for '" + query + "': " + (matchesQuery(query) ? "FOUND" : "NOT FOUND");
    }
}
