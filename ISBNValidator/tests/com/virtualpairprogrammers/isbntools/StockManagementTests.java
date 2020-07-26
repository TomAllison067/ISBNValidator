package com.virtualpairprogrammers.isbntools;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class StockManagementTests {
	// J Steinbeck's Of Mice and Men
	final static String CORRECT_ISBN = "0140177396";
	final static String CORRECT_LOCATOR_CODE = "7396J4";
	final static Book OF_MICE_AND_MEN = new Book(CORRECT_ISBN, "Of Mice And Men", "J. Steinbeck");
	
	/**
	 * Tests whether, given any ISBN and returning a specific book, StockManager can
	 * get the correct locator code.
	 */
	@Test
	void testCanGetACorrectLocatorCode() {

//		ExternalISBNDataService testWebService = new ExternalISBNDataService() {
//
//			@Override
//			public Book lookup(String isbn) {
//				return new Book(isbn, "Of Mice And Men", "J. Steinbeck");
//			}
//			
//		};
//		
//		ExternalISBNDataService testDatabaseService = new ExternalISBNDataService() {
//
//			@Override
//			public Book lookup(String isbn) {
//				return null;
//			}
//			
//		};
		
		ExternalISBNDataService webService = mock(ExternalISBNDataService.class);
		when(webService.lookup(anyString())).thenReturn(OF_MICE_AND_MEN);
		
		ExternalISBNDataService databaseService = mock(ExternalISBNDataService.class);
		when(databaseService.lookup(anyString())).thenReturn(null);
		
		
		StockManager stockManager = new StockManager();
		stockManager.setWebService(webService);
		stockManager.setDatabaseService(databaseService);
		
		
		String locatorCode = stockManager.getLocatorCode(CORRECT_ISBN);
		assertEquals(CORRECT_LOCATOR_CODE ,locatorCode);
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
