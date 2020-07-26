package com.virtualpairprogrammers.isbntools;

/**
 * Interface for an external data service which would look up a Book given its ISBN number.
 * 
 * This service could interact with a web service, or a database (or anything else appropriate).
 * @author tom
 *
 */
public interface ExternalISBNDataService {
	public Book lookup(String isbn);
}
