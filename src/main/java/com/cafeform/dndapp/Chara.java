package com.cafeform.dndapp;

public interface Chara {
    
    // basic character info    

    public String getName();

    public String getKlass();

    public String getCharaKey();
    
    public Integer getLevel();

    public String getPlayer();

    public String getRace();

    public String getAlignment();

    public Integer getExperience();

    // Ability
    
    public Integer getStrength();

    public Integer getDexterity();

    public Integer getConstitution();

    public Integer getIntelligence();

    public Integer getWisdom();

    public Integer getCharisma();

    public Integer getStrength_bonus();

    public Integer getDexterity_bonus();

    public Integer getConstitution_bonus();

    public Integer getIntelligence_bonus();

    public Integer getWisdom_bonus();

    public Integer getCharisma_bonus();

    public Integer getStrength_save_bonus();

    public Integer getDexterity_save_bonus();

    public Integer getConstitution_save_bonus();

    public Integer getIntelligence_save_bonus();

    public Integer getWisdom_save_bonus();

    public Integer getCharisma_save_bonus();

    // Skill

    public String getAcrobatics();

    public String getAnimal();

    public String getArcana();

    public String getAthletics();

    public String getDeception();

    public String getHistory();

    public String getIntimidation();

    public String getInsight();

    public String getInvestigation();

    public String getMedicine();

    public String getNature();

    public String getPerformance();

    public String getPerception();

    public String getPersuasion();

    public String getReligion();

    public String getSleight();

    public String getStealth();

    public String getSurvival();

    public Integer getAcrobatics_bonus();

    public Integer getAnimal_bonus();

    public Integer getArcana_bonus();

    public Integer getAthletics_bonus();

    public Integer getDeception_bonus();

    public Integer getHistory_bonus();

    public Integer getIntimidation_bonus();

    public Integer getInsight_bonus();

    public Integer getInvestigation_bonus();

    public Integer getMedicine_bonus();

    public Integer getNature_bonus();

    public Integer getPerformance_bonus();

    public Integer getPerception_bonus();

    public Integer getPersuasion_bonus();

    public Integer getReligion_bonus();

    public Integer getSleight_bonus();

    public Integer getStealth_bonus();

    public Integer getSurvival_bonus();

    // Saving throw

    public String getStrength_save();

    public String getDexterity_save();

    public String getConstitution_save();

    public String getIntelligence_save();

    public String getWisdom_save();

    public String getCharisma_save();
    
    // other characteristics 

    public String getInspiration();

    public Integer getProficiency_bonus();

    public Integer getPassive_wisdom_perception();

    public String getProficiency_language();

    public Integer getAc();

    public Integer getInitiative();

    public Integer getSpeed();

    public String getHit_dice();
    
    public Integer getHit_point();

    public String getAttack_spellcasting();

    public String getEquipment();

    public String getPersonality_traits();

    public String getIdeals();

    public String getBonds();

    public String getFlaws();

    public String getFeatures_traits();

    public String getAge();

    public String getHeight();

    public String getWeight();

    public String getEyes();

    public String getSkin();

    public String getHair();

    public String getSpellcasting_ability();

    public Integer getSpell_save_dc();

    public Integer getSpell_attack_bonus();

    public String getSpellcasting_class();
    
    public String getSpell_note();
}
