package com.challenge.olimpicgames.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.challenge.olimpicgames.exception.CompetitionException;
import com.challenge.olimpicgames.exception.DurationCompetitionException;
import com.challenge.olimpicgames.exception.QuantityCompetitionException;
import com.challenge.olimpicgames.exception.SameTimeCompetitionException;
import com.challenge.olimpicgames.model.Competition;
import com.challenge.olimpicgames.repository.CompetitionRepository;
import com.challenge.olimpicgames.service.CompetitionService;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompetitionServiceTest {

	@InjectMocks
	private CompetitionService service;

	@Mock
	private CompetitionRepository repository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = SameTimeCompetitionException.class)
	public void twoCompetitionsInSameTimeTest() throws CompetitionException {
		// given
		Competition newCompetition = Competition.builder().startDate(LocalDateTime.of(2018, 8, 01, 13, 00, 00))
				.endDate(LocalDateTime.of(2018, 8, 01, 15, 00, 00)).build();

		when(repository.countByPeriod(same(newCompetition.getStartDate()), same(newCompetition.getEndDate())))
				.thenReturn(1);

		// when
		service.save(newCompetition);
	}

	@Test(expected = DurationCompetitionException.class)
	public void durationLessThan30MinutesTest() throws CompetitionException {
		// given
		Competition newCompetition = Competition.builder().startDate(LocalDateTime.of(2018, 8, 01, 13, 00, 00))
				.endDate(LocalDateTime.of(2018, 8, 01, 13, 20, 00)).build();

		when(repository.countByPeriod(same(newCompetition.getStartDate()), same(newCompetition.getEndDate())))
				.thenReturn(0);

		// when
		service.save(newCompetition);
	}

	@Test(expected = QuantityCompetitionException.class)
	public void quantityPerDayGreaterThan4Test() throws CompetitionException {
		// given
		Competition newCompetition = Competition.builder().startDate(LocalDateTime.of(2018, 8, 01, 13, 00, 00))
				.endDate(LocalDateTime.of(2018, 8, 01, 15, 00, 00)).build();

		when(repository.countByPeriod(same(newCompetition.getStartDate()), same(newCompetition.getEndDate())))
				.thenReturn(0);
		when(repository.countByPeriodAndLocal(any(LocalDateTime.class), any(LocalDateTime.class), any(String.class)))
				.thenReturn(4);

		// when
		service.save(newCompetition);
	}

	@Test
	public void insertNewCompetitionWithSuccessTest() throws CompetitionException {
		// given
		Competition newCompetition = Competition.builder().startDate(LocalDateTime.of(2018, 8, 01, 13, 00, 00))
				.endDate(LocalDateTime.of(2018, 8, 01, 15, 00, 00)).build();

		when(repository.countByPeriod(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0);
		when(repository.countByPeriodAndLocal(any(LocalDateTime.class), any(LocalDateTime.class), any(String.class)))
				.thenReturn(2);
		when(repository.save(newCompetition)).thenReturn(newCompetition);

		// when
		service.save(newCompetition);

		// then
		verify(repository, times(1)).save(newCompetition);
	}
	
	@Test
	public void findCompetitionByModalityTest() {
		// given
		Competition newCompetition = Competition.builder().modality("futebol").build();
		
		when(repository.findByModalityIgnoreCaseContaining("futebol")).thenReturn(Arrays.asList(newCompetition));

		// when
		service.findCompetitions("futebol");
		
		// then
		verify(repository, times(1)).findByModalityIgnoreCaseContaining("futebol");
		verify(repository, never()).findAll();
	}
	
	@Test
	public void findCompetitionByEmptyModalityTest() {
		// given
		Competition newCompetition = Competition.builder().modality("futebol").build();
		
		when(repository.findAll()).thenReturn(Arrays.asList(newCompetition));

		// when
		service.findCompetitions("");
		
		// then
		verify(repository, never()).findByModalityIgnoreCaseContaining(anyString());
		verify(repository, times(1)).findAll();
	}

	@Test
	public void findCompetitionByNullModalityTest() {
		// given
		Competition newCompetition = Competition.builder().modality("futebol").build();
		
		when(repository.findAll()).thenReturn(Arrays.asList(newCompetition));

		// when
		service.findCompetitions(null);
		
		// then
		verify(repository, never()).findByModalityIgnoreCaseContaining(anyString());
		verify(repository, times(1)).findAll();
	}
}
