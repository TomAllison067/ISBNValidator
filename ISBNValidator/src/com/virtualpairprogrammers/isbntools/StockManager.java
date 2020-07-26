package com.virtualpairprogrammers.isbntools;

/**
 * Class that allows us to get stock locator codes from a database service or a
 * web service given a book's ISBN.
 * 
 * @author tom
 *
 */
public class StockManager {

	private ExternalISBNDataService webService;
	private ExternalISBNDataService databaseService;

	public void setWebService(ExternalISBNDataService service) {
		this.webService = service;
	}

	public void setDatabaseService(ExternalISBNDataService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * Returns a stock locator code given a book's ISBN. A locator code consists of
	 * the last 4 digits of the ISBN, the initial of the author, and a digit
	 * corresponding to the number of words in the book's title.
	 * 
	 * Queries the database service first - and if the result is null, queries the web service.
	 * @param isbn
	 * @return a locator code.
	 */
	public String getLocatorCode(String isbn) {
		Book book = databaseService.lookup(isbn);
		if (book == null)
			book = webService.lookup(isbn);
		StringBuilder locator = new StringBuilder();
		locator.append(isbn.substring(isbn.length() - 4));
		locator.append(book.getAuthor().substring(0, 1));
		locator.append(book.getTitle().split(" ").length);
		return locator.toString();
	}

}
