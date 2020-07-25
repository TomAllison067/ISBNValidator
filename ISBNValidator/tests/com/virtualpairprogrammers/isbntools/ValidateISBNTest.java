package com.virtualpairprogrammers.isbntools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValidateISBNTest {

	@Test
	void checkValid10DigitISBN() {
		ValidateISBN validator = new ValidateISBN();
		boolean result = validator.checkISBN("1853262420");
		assertTrue(result, "First value");
		result = validator.checkISBN("1853260258");
		assertTrue(result, "Second value");
	}

	@Test
	void checkValid13DigitISBN() {
		ValidateISBN validator = new ValidateISBN();
		boolean result = validator.checkISBN("9780140390261");
		assertTrue(result, "First value");
		result = validator.checkISBN("9781514812105");
		assertTrue(result, "Second value");
	}
	
	@Test
	void checkInvalid13DigitISBN() {
		ValidateISBN validator = new ValidateISBN();
		boolean result = validator.checkISBN("9881514812105");
		assertFalse(result);
	}

	@Test
	void tenDigitISBNNumbersEndingInXAreValid() {
		ValidateISBN validator = new ValidateISBN();
		boolean result = validator.checkISBN("012000030X");
		assertTrue(result);
	}
	
	@Test
	void checkInvalid10DigitISBN() {
		ValidateISBN validator = new ValidateISBN();
		boolean result = validator.checkISBN("1853262421");
		assertFalse(result);
	}
	
	@Test
	void checkInvalid10DigitISBNEndingInX() {
		ValidateISBN validator = new ValidateISBN();
		boolean result = validator.checkISBN("013000030X");
		assertFalse(result);
	}

	@Test
	void nineDigitISBNsAreNotAllowed() {
		ValidateISBN validator = new ValidateISBN();
		assertThrows(NumberFormatException.class, () -> {
			validator.checkISBN("123456789");
		});
	}

	@Test
	void nonNumericISBNsAreNotAllowed() {
		ValidateISBN validator = new ValidateISBN();
		assertThrows(NumberFormatException.class, () -> {
			validator.checkISBN("helloworld");
		});
		assertThrows(NumberFormatException.class, () -> {
			validator.checkISBN("helloworldfoo");
		});
	}
}
