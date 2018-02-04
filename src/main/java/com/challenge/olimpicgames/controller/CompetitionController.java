package com.challenge.olimpicgames.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.olimpicgames.exception.CompetitionException;
import com.challenge.olimpicgames.model.Competition;
import com.challenge.olimpicgames.service.CompetitionService;

@RestController
public class CompetitionController extends BaseController {

	@Autowired
	private CompetitionService competitionService;

	@RequestMapping(value = "/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Competition>> findCompetitions(@RequestParam(required = false) final String modality) {
		return ResponseEntity.ok().body(competitionService.findCompetitions(modality));
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Competition> save(@RequestBody final Competition competition)
			throws CompetitionException {
		return ResponseEntity.ok().body(competitionService.save(competition));
	}
}
