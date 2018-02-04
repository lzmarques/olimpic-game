package com.challenge.olimpicgames.enumerator;

public enum StageGame {

	GROUP_PHASE("eliminatorias"), //
	ROUNDE_SIXTEEN("oitavas de final"), //
	QUARTER_FINALS("quartas de final"), //
	SEMI_FINALS("semi finais"), //
	FINALS("finais");

	private String stageName;

	StageGame(String stageName) {
		this.stageName = stageName;
	}

	public String getStageName() {
		return this.stageName;
	}

}
