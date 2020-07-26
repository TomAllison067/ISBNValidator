package com.virtualpairprogrammers.isbntools;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockManagementTests {
	ExternalISBNDataService webService;
	ExternalISBNDataService databaseService;
	StockManager stockManager = new StockManager();

	// J Steinbeck's Of Mice and Men
	final static String CORRECT_ISBN = "0140177396";
	final static String CORRECT_LOCATOR_CODE = "7396J4";
	final static Book OF_MICE_AND_MEN = new Book(CORRECT_ISBN, "Of Mice And Men", "J. Steinbeck");

	@BeforeEach
	public void setup() {
		webService = mock(ExternalISBNDataService.class);
		databaseService = mock(ExternalISBNDataService.class);

		stockManager = new StockManager();
		stockManager.setWebService(webService);
		stockManager.setDatabaseService(databaseService);
	}

	/**
	 * Tests whether, given any ISBN and returning a specific book (Of Mice and
	 * Men), StockManager can get the correct locator code for that book.
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

		when(webService.lookup(anyString())).thenReturn(OF_MICE_AND_MEN);
		when(databaseService.lookup(anyString())).thenReturn(null);
		String locatorCode = stockManager.getLocatorCode(CORRECT_ISBN);
		assertEquals(CORRECT_LOCATOR_CODE, locatorCode);
	}

	/**
	 * Tests if the database service is called and the web service is not called,
	 * provided the database service exists. For myself: This is testing the
	 * behaviour (which methods are called) rather than the logic of the lookup
	 * method.
	 */
	@Test
	public void databaseIsUsedIfDataIsPresent() {
		when(databaseService.lookup(CORRECT_ISBN)).thenReturn(OF_MICE_AND_MEN);

		// same as verify(databaseService, times(1)).lookup("0140177396");
		verify(databaseService).lookup(CORRECT_ISBN);
		verify(webService, never()).lookup(anyString());
	}

	/**
	 * Tests that the web service is used if the database service returns null. In
	 * this case, lookup should be called once by each service.
	 */
	@Test
	public void webServiceIsUsedIfDataIsNotPresentInDatabase() {
		when(databaseService.lookup(anyString())).thenReturn(null);
		when(webService.lookup(CORRECT_ISBN)).thenReturn(OF_MICE_AND_MEN);

		verify(databaseService).lookup(CORRECT_ISBN);
		verify(webService).lookup(CORRECT_ISBN);
	}

}
