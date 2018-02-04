package com.challenge.olimpicgames.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.olimpicgames.enumerator.StageGame;
import com.challenge.olimpicgames.exception.CompetitionException;
import com.challenge.olimpicgames.exception.DurationCompetitionException;
import com.challenge.olimpicgames.exception.QuantityCompetitionException;
import com.challenge.olimpicgames.exception.SameTimeCompetitionException;
import com.challenge.olimpicgames.model.Competition;
import com.challenge.olimpicgames.repository.CompetitionRepository;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompetitionService {

	@Autowired
	private CompetitionRepository competitionRepository;

	/**
	 * Metodo que busca todas as competi��es para uma modalidade se usado o
	 * parametro modality, caso contrario, buscar� todas as competi��es.
	 * 
	 * @param modality
	 *            modalidade da competi��o.
	 * @return lista de competi��es.
	 */
	public List<Competition> findCompetitions(String modality) {
		if (modality == null || modality.trim().isEmpty()) {
			log.info("Modalidade vazia. Retornando todas as competi��es cadastradas.");
			return Lists.newArrayList(competitionRepository.findAll());
		}

		return competitionRepository.findByModalityIgnoreCaseContaining(modality);
	}

	/**
	 * Salva uma nova cometi��o de acordo com os criterios de cria��o.
	 * 
	 * @param newCompetition
	 *            Nova competi��o.
	 * @return competi��o salva.
	 * @throws CompetitionException
	 *             Erro ao cadastrar nova competi��o
	 */
	public Competition save(Competition newCompetition) throws CompetitionException {
		validateCompetition(newCompetition);
		Competition saved = competitionRepository.save(newCompetition);
		log.info("Competition saved: {}", saved.toString());

		return saved;
	}

	/**
	 * Metodo que contem os criterios para cria��o de uma nova competi��o.
	 * 
	 * @param newCompetition
	 *            nova competi��o.
	 * @throws CompetitionException
	 *             Erro ao cadastrar nova competi��o
	 */
	private void validateCompetition(Competition newCompetition) throws CompetitionException {
		checkDuration(newCompetition);
		checkSchedule(newCompetition);
		checkQuantityPerDay(newCompetition);
	}

	/**
	 * Metodo que verifica se uma competi��o, que n�o for da etapa final ou
	 * semi-final, n�o podera ocorrer no mesmo periodo local e modalidade.
	 * 
	 * @param newCompetition
	 *            nova competi��o.
	 * @throws SameTimeCompetitionException
	 *             Erro - duas competi��es n�o podera ocorrer no mesmo periodo local
	 *             e modalidade.
	 */
	private void checkSchedule(Competition newCompetition) throws SameTimeCompetitionException {
		if (!StageGame.FINALS.equals(newCompetition.getStage())
				&& !StageGame.SEMI_FINALS.equals(newCompetition.getStage())) {

			Integer numberOfCompetitionsInSamePeriod = competitionRepository
					.countByPeriod(newCompetition.getStartDate(), newCompetition.getEndDate());

			if (numberOfCompetitionsInSamePeriod > 0) {
				log.error("Existe uma competi��o no mesmo hor�rio.");
				throw new SameTimeCompetitionException("Existe uma competi��o no mesmo hor�rio.");
			}
		}
	}

	/**
	 * Metodo que verifica se a dura��o de uma competi��o � de no minimo de 30
	 * minutos.
	 * 
	 * @param newCompetition
	 *            nova competi��o.
	 * @throws DurationCompetitionException
	 *             Erro - A dura��o de uma competi��o � de no minimo de 30 minutos.
	 */
	private void checkDuration(Competition newCompetition) throws DurationCompetitionException {
		if (newCompetition.getStartDate().until(newCompetition.getEndDate(), ChronoUnit.MINUTES) < 30) {
			log.error("Uma competi��o deve ter um periodo m�nimo de 30 minutos.");
			throw new DurationCompetitionException("Uma competi��o deve ter um periodo minimo de 30 minutos.");
		}

	}

	/**
	 * Metodo que verifica a quantidade de competi��es em um mesmo dia. Numero n�o
	 * pode ser maior que 4.
	 * 
	 * @param newCompetition
	 *            nova competi��o
	 * @throws QuantityCompetitionException
	 *             Erro - A quantidade de competi��es em um mesmo dia, e no mesmo
	 *             local, n�o pode ser maior que 4.
	 */
	private void checkQuantityPerDay(Competition newCompetition) throws QuantityCompetitionException {
		LocalDateTime startDate = newCompetition.getStartDate();

		// gerando come�o e final do dia
		LocalDateTime beginOfDay = LocalDateTime.of(startDate.getYear(), startDate.getMonth(),
				startDate.getDayOfMonth(), 0, 0, 0);

		LocalDateTime endOfDay = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(),
				23, 59, 59);

		Integer quantityPerDay = competitionRepository.countByPeriodAndLocal(beginOfDay, endOfDay,
				newCompetition.getLocal());

		if (quantityPerDay >= 4) {
			log.error("N�o pode haver mais de 4 competi��es em um mesmo dia.");
			throw new QuantityCompetitionException("N�o pode haver mais de 4 competi��es em um mesmo dia.");
		}
	}
}
