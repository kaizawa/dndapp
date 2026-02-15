package com.cafeform.dndapp;

import static com.cafeform.dndapp.Constants.*;

public abstract class CharaBase implements Chara {

	@Override
	public Integer getProficiency_bonus() {
		int level = getLevel();

		level = level == 1 ? 1 : level - 1;

		return level / 4 + 2;
	}

	@Override
	public Integer getStrength_save_bonus() {

		int bonus = getStrength_bonus();

		if (getStrength_save() != null && getStrength_save().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getDexterity_save_bonus() {

		int bonus = getDexterity_bonus();
		if (getDexterity_save() != null && getDexterity_save().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getConstitution_save_bonus() {

		int bonus = getConstitution_bonus();
		if (getConstitution_save() != null && getConstitution_save().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getIntelligence_save_bonus() {

		int bonus = getIntelligence_bonus();
		if (getIntelligence_save() != null && getIntelligence_save().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getWisdom_save_bonus() {

		int bonus = getWisdom_bonus();
		if (getWisdom_save() != null && getWisdom_save().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getCharisma_save_bonus() {

		int bonus = getCharisma_bonus();
		if (getCharisma_save() != null && getCharisma_save().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	public Integer getAbilityBonus(Integer ability) {

		ability = ability % 2 == 0 ? ability : ability - 1;
		return (ability - 10) / 2;
	}

	@Override
	public Integer getStrength_bonus() {
		return getAbilityBonus(getStrength());
	}

	@Override
	public Integer getDexterity_bonus() {
		return getAbilityBonus(getDexterity());

	}

	@Override
	public Integer getConstitution_bonus() {
		return getAbilityBonus(getConstitution());

	}

	@Override
	public Integer getIntelligence_bonus() {
		return getAbilityBonus(getIntelligence());

	}

	@Override
	public Integer getWisdom_bonus() {
		return getAbilityBonus(getWisdom());

	}

	@Override
	public Integer getCharisma_bonus() {
		return getAbilityBonus(getCharisma());
	}

	@Override
	public Integer getAcrobatics_bonus() {

		int bonus = getCharisma_bonus();

		if (getAcrobatics() != null && getAcrobatics().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getAnimal_bonus() {

		int bonus = getWisdom_bonus();

		if (getAnimal() != null && getAnimal().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getArcana_bonus() {

		int bonus = getIntelligence_bonus();

		if (getArcana() != null && getArcana().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getAthletics_bonus() {

		int bonus = getStrength_bonus();

		if (getAthletics() != null && getAthletics().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getDeception_bonus() {

		int bonus = getCharisma_bonus();

		if (getDeception() != null && getDeception().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getHistory_bonus() {

		int bonus = getIntelligence_bonus();

		if (getHistory() != null && getHistory().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getIntimidation_bonus() {

		int bonus = getCharisma_bonus();

		if (getIntimidation() != null && getIntimidation().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getInsight_bonus() {

		int bonus = getWisdom_bonus();

		if (getInsight() != null && getInsight().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getInvestigation_bonus() {

		int bonus = getWisdom_bonus();

		if (getInvestigation() != null && getInvestigation().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getMedicine_bonus() {

		int bonus = getWisdom_bonus();

		if (getMedicine() != null && getMedicine().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getNature_bonus() {

		int bonus = getIntelligence_bonus();

		if (getNature() != null && getNature().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getPerformance_bonus() {

		int bonus = getCharisma_bonus();

		if (getPerformance() != null && getPerformance().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getPerception_bonus() {

		int bonus = getWisdom_bonus();

		if (getPerception() != null && getPerception().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getPersuasion_bonus() {

		int bonus = getCharisma_bonus();

		if (getPersuasion() != null && getPersuasion().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getReligion_bonus() {

		int bonus = getIntelligence_bonus();

		if (getReligion() != null && getReligion().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getSleight_bonus() {

		int bonus = getDexterity_bonus();

		if (getSleight() != null && getSleight().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getStealth_bonus() {

		int bonus = getDexterity_bonus();

		if (getStealth() != null && getStealth().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	@Override
	public Integer getSurvival_bonus() {

		int bonus = getWisdom_bonus();

		if (getSurvival() != null && getSurvival().equals("on")) {
			return bonus + getProficiency_bonus();
		} else {
			return bonus;
		}
	}

	public Integer getPassive_wisdom_perception() {

		return 10 + getInvestigation_bonus();
	}

	public Integer getSpell_save_dc() {

		switch (getSpellcasting_ability()) {
		case "知力":
		case INT:
			return 10 + getIntelligence_bonus() + getSpell_save_dc_modifier();
		case "判断力":
		case WIS:
			return 10 + getWisdom_bonus() + getSpell_save_dc_modifier();
		case "魅力":
		case CHA:
			return 10 + getCharisma_bonus() + getSpell_save_dc_modifier();
		default:
			return 10  + getSpell_attack_bonus_modifier();
		}
	}

	public Integer getSpell_attack_bonus() {

		switch (getSpellcasting_ability()) {
		case "知力":
		case INT:
			return getProficiency_bonus() + getIntelligence_bonus() + getSpell_attack_bonus_modifier();
		case "判断力":
		case WIS:
			return getProficiency_bonus() + getWisdom_bonus() + getSpell_attack_bonus_modifier();
		case "魅力":
		case CHA:
			return getProficiency_bonus() + getCharisma_bonus() + getSpell_attack_bonus_modifier();
		default:
			return getProficiency_bonus() + getSpell_attack_bonus_modifier();
		}
	}
}
