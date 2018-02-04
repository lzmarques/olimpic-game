package com.challenge.olimpicgames.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.challenge.olimpicgames.model.Competition;

public interface CompetitionRepository extends CrudRepository<Competition, Long> {

	public List<Competition> findByModalityIgnoreCaseContaining(String modality);

	@Query(value = "SELECT COUNT(c) FROM Competition c WHERE (c.startDate BETWEEN :startDate AND :endDate) OR (c.endDate BETWEEN :startDate AND :endDate) ")
	public Integer countByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query(value = "SELECT COUNT(c) FROM Competition c WHERE (c.startDate BETWEEN :beginOfDay AND :endOfDay) AND c.local LIKE :local")
	public Integer countByPeriodAndLocal(@Param("beginOfDay") LocalDateTime beginOfDay,
			@Param("endOfDay") LocalDateTime endOfDay, @Param("local") String local);
}
