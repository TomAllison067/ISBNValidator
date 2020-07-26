package com.virtualpairprogrammers.isbntools;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class StockManagementTests {

	@Test
	void testCanGetACorrectLocatorCode() {
		/*
		 * In production, we would of course give stockManager a proper implementation
		 *  of ExternalISBNDataService that calls a third party web service.
		 *  In this test, we can override that with our own implementation of
		 *  the interface.
		 *  
		 *  So, testService is a simulation of an external 3rd party web service
		 *  would be doing - because we are testing the business logic of calculating
		 *  the correct locator code, NOT the 3rd party web service.
		 */
		
		/*
		 * So, we have created a test stub. A test stub is a replacement for an object
		 * that the class we are testing has a dependency on.
		 */
		ExternalISBNDataService testWebService = new ExternalISBNDataService() {

			@Override
			public Book lookup(String isbn) {
				return new Book(isbn, "Of Mice And Men", "J. Steinbeck");
			}
			
		};
		
		ExternalISBNDataService testDatabaseService = new ExternalISBNDataService() {

			@Override
			public Book lookup(String isbn) {
				return null;
			}
			
		};
		StockManager stockManager = new StockManager();
		stockManager.setWebService(testWebService);
		stockManager.setDatabaseService(testDatabaseService);
		
		// J Steinbeck's Of Mice and Men
		String isbn = "0140177396";
		
		String locatorCode = stockManager.getLocatorCode(isbn);
		/* Last four digits of ISBN, the initial of the author
		 * and number of words in the title. */
		assertEquals("7396J4", locatorCode);
	}
	
	/**
	 * Tests if the database service is called and the web service is not called, provided the database service exists.
	 * For myself: This is testing the behaviour (which methods are called) rather than the logic of the lookup method.
	 */
	@Test
	public void databaseIsUsedIfDataIsPresent() {
		ExternalISBNDataService databaseService = mock(ExternalISBNDataService.class);
		ExternalISBNDataService webService = mock(ExternalISBNDataService.class);
		
		when(databaseService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));
		
		StockManager stockManager = new StockManager();
		stockManager.setWebService(webService);
		stockManager.setDatabaseService(databaseService);
		
		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		
		// same as verify(databaseService, times(1)).lookup("0140177396");
		verify(databaseService).lookup("0140177396");
		verify(webService, never()).lookup(anyString());
	}
	
	/**
	 * Tests that the web service is used if the database service is not present.
	 */
	@Test
	public void webServiceIsUsedIfDataIsNotPresentInDatabase() {
		ExternalISBNDataService databaseService = mock(ExternalISBNDataService.class);
		ExternalISBNDataService webService = mock(ExternalISBNDataService.class);
		
		when(databaseService.lookup("0140177396")).thenReturn(null);
		when(webService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));
		
		StockManager stockManager = new StockManager();
		stockManager.setWebService(webService);
		stockManager.setDatabaseService(databaseService);
		
		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		
		verify(databaseService).lookup("0140177396");
		verify(webService).lookup("0140177396");
	}

}
