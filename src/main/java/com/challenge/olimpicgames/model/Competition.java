package com.challenge.olimpicgames.model;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.challenge.olimpicgames.converter.LocalDateTimeConverter;
import com.challenge.olimpicgames.enumerator.StageGame;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter
	@Setter
	private String modality;

	@Getter
	@Setter
	private String local;

	@Getter
	@Setter
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime startDate;

	@Getter
	@Setter
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime endDate;

	@Getter
	@Setter
	private StageGame stage;

	@Getter
	@Setter
	private String firstCountry;

	@Getter
	@Setter
	private String secondeCountry;

	@Override
	public String toString() {
		return "Competition [id= " + id + ", modality= " + modality + ", local= " + local + ", startDate= " + startDate
				+ ", endDate= " + endDate + ", stage= " + stage + "]";
	}
}
