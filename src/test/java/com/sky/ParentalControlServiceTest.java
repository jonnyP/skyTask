package com.sky;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ParentalControlServiceTest {

	@InjectMocks
	ParentalControlService testObj;

	@Mock
	MovieService movieServiceMock;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void returnsFalseWhenParentalLevelBelowMovieLevel() throws TitleNotFoundException, TechnicalFailureException, ControlLevelNotFoundException {
		doReturn("18").when(movieServiceMock).getParentalControlLevel("18CertMovieId");
		assertFalse(testObj.movieAccessAllowed("PG", "18CertMovieId"));
	}

	@Test
	public void returnsTrueWhenParentalLevelAboveMovieLevel() throws TitleNotFoundException, TechnicalFailureException, ControlLevelNotFoundException {
		doReturn("U").when(movieServiceMock).getParentalControlLevel("UCertMovieId");
		assertTrue(testObj.movieAccessAllowed("18", "UCertMovieId"));
	}

	@Test
	public void returnsTrueWhenParentalLevelEqualMovieLevel() throws TitleNotFoundException, TechnicalFailureException, ControlLevelNotFoundException {
		doReturn("12").when(movieServiceMock).getParentalControlLevel("12CertMovieId");
		assertTrue(testObj.movieAccessAllowed("12", "12CertMovieId"));
	}

	@Test(expected = ControlLevelNotFoundException.class)
	public void throwsExceptionWhenControlLevelNotFound() throws TitleNotFoundException, TechnicalFailureException, ControlLevelNotFoundException {
		testObj.movieAccessAllowed("21", "12CertMovieId");
	}

	@Test(expected = TitleNotFoundException.class)
	public void throwsExceptionWhenMovieNotAvailable() throws TitleNotFoundException, TechnicalFailureException, ControlLevelNotFoundException {
		doThrow(new TitleNotFoundException()).when(movieServiceMock).getParentalControlLevel("MovieNotAvailableId");
		testObj.movieAccessAllowed("18", "MovieNotAvailableId");
	}

	@Test
	public void returnsFalseWhenTechnicalExceptionThrown() throws TitleNotFoundException, TechnicalFailureException, ControlLevelNotFoundException {
		doThrow(new TechnicalFailureException()).when(movieServiceMock).getParentalControlLevel("MovieTechnicalFailureId");
		assertFalse(testObj.movieAccessAllowed("18", "MovieTechnicalFailureId"));
	}

}
