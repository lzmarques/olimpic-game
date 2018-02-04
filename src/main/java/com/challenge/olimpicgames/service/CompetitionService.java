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
	 * Metodo que busca todas as competições para uma modalidade se usado o
	 * parametro modality, caso contrario, buscará todas as competições.
	 * 
	 * @param modality
	 *            modalidade da competição.
	 * @return lista de competições.
	 */
	public List<Competition> findCompetitions(String modality) {
		if (modality == null || modality.trim().isEmpty()) {
			log.info("Modalidade vazia. Retornando todas as competições cadastradas.");
			return Lists.newArrayList(competitionRepository.findAll());
		}

		return competitionRepository.findByModalityIgnoreCaseContaining(modality);
	}

	/**
	 * Salva uma nova cometição de acordo com os criterios de criação.
	 * 
	 * @param newCompetition
	 *            Nova competição.
	 * @return competição salva.
	 * @throws CompetitionException
	 *             Erro ao cadastrar nova competição
	 */
	public Competition save(Competition newCompetition) throws CompetitionException {
		validateCompetition(newCompetition);
		Competition saved = competitionRepository.save(newCompetition);
		log.info("Competition saved: {}", saved.toString());

		return saved;
	}

	/**
	 * Metodo que contem os criterios para criação de uma nova competição.
	 * 
	 * @param newCompetition
	 *            nova competição.
	 * @throws CompetitionException
	 *             Erro ao cadastrar nova competição
	 */
	private void validateCompetition(Competition newCompetition) throws CompetitionException {
		checkDuration(newCompetition);
		checkSchedule(newCompetition);
		checkQuantityPerDay(newCompetition);
	}

	/**
	 * Metodo que verifica se uma competição, que não for da etapa final ou
	 * semi-final, não podera ocorrer no mesmo periodo local e modalidade.
	 * 
	 * @param newCompetition
	 *            nova competição.
	 * @throws SameTimeCompetitionException
	 *             Erro - duas competições não podera ocorrer no mesmo periodo local
	 *             e modalidade.
	 */
	private void checkSchedule(Competition newCompetition) throws SameTimeCompetitionException {
		if (!StageGame.FINALS.equals(newCompetition.getStage())
				&& !StageGame.SEMI_FINALS.equals(newCompetition.getStage())) {

			Integer numberOfCompetitionsInSamePeriod = competitionRepository
					.countByPeriod(newCompetition.getStartDate(), newCompetition.getEndDate());

			if (numberOfCompetitionsInSamePeriod > 0) {
				log.error("Existe uma competição no mesmo horário.");
				throw new SameTimeCompetitionException("Existe uma competição no mesmo horário.");
			}
		}
	}

	/**
	 * Metodo que verifica se a duração de uma competição é de no minimo de 30
	 * minutos.
	 * 
	 * @param newCompetition
	 *            nova competição.
	 * @throws DurationCompetitionException
	 *             Erro - A duração de uma competição é de no minimo de 30 minutos.
	 */
	private void checkDuration(Competition newCompetition) throws DurationCompetitionException {
		if (newCompetition.getStartDate().until(newCompetition.getEndDate(), ChronoUnit.MINUTES) < 30) {
			log.error("Uma competição deve ter um periodo mínimo de 30 minutos.");
			throw new DurationCompetitionException("Uma competição deve ter um periodo minimo de 30 minutos.");
		}

	}

	/**
	 * Metodo que verifica a quantidade de competições em um mesmo dia. Numero não
	 * pode ser maior que 4.
	 * 
	 * @param newCompetition
	 *            nova competição
	 * @throws QuantityCompetitionException
	 *             Erro - A quantidade de competições em um mesmo dia, e no mesmo
	 *             local, não pode ser maior que 4.
	 */
	private void checkQuantityPerDay(Competition newCompetition) throws QuantityCompetitionException {
		LocalDateTime startDate = newCompetition.getStartDate();

		// gerando começo e final do dia
		LocalDateTime beginOfDay = LocalDateTime.of(startDate.getYear(), startDate.getMonth(),
				startDate.getDayOfMonth(), 0, 0, 0);

		LocalDateTime endOfDay = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(),
				23, 59, 59);

		Integer quantityPerDay = competitionRepository.countByPeriodAndLocal(beginOfDay, endOfDay,
				newCompetition.getLocal());

		if (quantityPerDay >= 4) {
			log.error("Não pode haver mais de 4 competições em um mesmo dia.");
			throw new QuantityCompetitionException("Não pode haver mais de 4 competições em um mesmo dia.");
		}
	}
}
