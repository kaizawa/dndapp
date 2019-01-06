package com.cafeform.dndapp;

import java.io.Serializable;

public class PlayerChara extends CharaBase implements Serializable {

    private static final long serialVersionUID = -248233926411329328L;

    private String _charaKey = "";
    private String _name = "NO NAME";
    private String _klass = "";

    private Integer _level = 1;
    
    private String _player = "";
    private String _race = "";
    private String _alignment = "";
    private Integer _experience = 0;

    private Integer _strength = 10;
    private Integer _dexterity = 10;
    private Integer _constitution = 10;
    private Integer _intelligence = 10;
    private Integer _wisdom = 10;
    private Integer _charisma = 10;
    
    private String _inspiration = "";

    private String _strength_save = "";
    private String _dexterity_save = "";
    private String _constitution_save = "";
    private String _intelligence_save = "";
    private String _wisdom_save = "";
    private String _charisma_save = "";

    private String _acrobatics = "";
    private String _animal = "";
    private String _arcana = "";
    private String _athletics = "";
    private String _deception = "";
    private String _history = "";
    private String _intimidation = "";
    private String _insight = "";
    private String _investigation = "";
    private String _medicine = "";
    private String _nature = "";
    private String _performance = "";
    private String _perception = "";
    private String _persuasion = "";
    private String _religion = "";
    private String _sleight = "";
    private String _stealth = "";
    private String _survival = "";
    
    private String _proficiency_language = "";
    private Integer _ac = 10;
    private Integer _initiative = 0;
    private Integer _speed = 0;
    private String _hit_dice = "";
    private String _attach_spellcasting = "";
    private String _equipment = "";
    private String _personality_traits = "";
    private String _ideals = "";
    private String _bonds = "";
    private String _flaws = "";
    private String _features_traits = "";
    private String _age = "";
    private String _height = "";
    private String _weight = "";
    private String _eyes = "";
    private String _skin = "";
    private String _hair = "";
    private String _spellcasting_ability = "";
    private String _spellcasting_class = "";

    public PlayerChara() {
    }

    public String getCharaKey() {
	return _charaKey;
    }

    public void setCharaKey(String charaKey) {
	_charaKey = charaKey;
    }

    public String getName() {
	return _name;
    }

    public void setName(String name) {
	_name = name;
    }

    public String getKlass() {
	return _klass;
    }

    public void setKlass(String klass) {
	_klass = klass;
    }

 
    public String getAcrobatics() {
	return _acrobatics;
    }

    public void setAcrobatics(String acrobatics) {
	_acrobatics = acrobatics;
    }

    public String getAnimal() {
	return _animal;
    }

    public void setAnimal(String animal) {
	_animal = animal;
    }

    public String getArcana() {
	return _arcana;
    }

    public void setArcana(String arcana) {
	_arcana = arcana;
    }

    public String getAthletics() {
	return _athletics;
    }

    public void setAthletics(String athletics) {
	_athletics = athletics;
    }

    public String getDeception() {
	return _deception;
    }

    public void setDeception(String deception) {
	_deception = deception;
    }

    public String getHistory() {
	return _history;
    }

    public void setHistory(String history) {
	_history = history;
    }

    public String getIntimidation() {
	return _intimidation;
    }

    public void setIntimidation(String intimidation) {
	_intimidation = intimidation;
    }

    public String getInsight() {
	return _insight;
    }

    public void setInsight(String insight) {
	_insight = insight;
    }

    public String getInvestigation() {
	return _investigation;
    }

    public void setInvestigation(String investigation) {
	_investigation = investigation;
    }

    public String getMedicine() {
	return _medicine;
    }

    public void setMedicine(String medicine) {
	_medicine = medicine;
    }

    public String getNature() {
	return _nature;
    }

    public void setNature(String nature) {
	_nature = nature;
    }

    public String getPerformance() {
	return _performance;
    }

    public void setPerformance(String performance) {
	_performance = performance;
    }

    public String getPerception() {
	return _perception;
    }

    public void setPerception(String perception) {
	_perception = perception;
    }

    public String getPersuasion() {
	return _persuasion;
    }

    public void setPersuasion(String persuasion) {
	_persuasion = persuasion;
    }

    public String getReligion() {
	return _religion;
    }

    public void setReligion(String religion) {
	_religion = religion;
    }

    public String getSleight() {
	return _sleight;
    }

    public void setSleight(String sleight) {
	_sleight = sleight;
    }

    public String getStealth() {
	return _stealth;
    }

    public void setStealth(String stealth) {
	_stealth = stealth;
    }

    public String getSurvival() {
	return _survival;
    }

    public void setSurvival(String survival) {
	_survival = survival;
    }

    public String getStrength_save() {
	return _strength_save;
    }

    public void setStrength_save(String strength_save) {
	_strength_save = strength_save;
    }

    public String getDexterity_save() {
	return _dexterity_save;
    }

    public void setDexterity_save(String dexterity_save) {
	_dexterity_save = dexterity_save;
    }

    public String getConstitution_save() {
	return _constitution_save;
    }

    public void setConstitution_save(String constitution_save) {
	_constitution_save = constitution_save;
    }

    public String getIntelligence_save() {
	return _intelligence_save;
    }

    public void setIntelligence_save(String intelligence_save) {
	_intelligence_save = intelligence_save;
    }

    public String getWisdom_save() {
	return _wisdom_save;
    }

    public void setWisdom_save(String wisdom_save) {
	_wisdom_save = wisdom_save;
    }

    public String getCharisma_save() {
	return _charisma_save;
    }

    public void setCharisma_save(String charisma_save) {
	_charisma_save = charisma_save;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    public Integer getLevel() {
        return _level;
    }

    public void setLevel(Integer level) {
        _level = level;
    }

    public String getPlayer() {
        return _player;
    }

    public void setPlayer(String player) {
        _player = player;
    }

    public String getRace() {
        return _race;
    }

    public void setRace(String race) {
        _race = race;
    }

    public String getAlignment() {
        return _alignment;
    }

    public void setAlignment(String alignment) {
        _alignment = alignment;
    }

    public Integer getExperience() {
        return _experience;
    }

    public void setExperience(Integer experience) {
        _experience = experience;
    }

    public String getInspiration() {
        return _inspiration;
    }

    public void setInspiration(String inspiration) {
        _inspiration = inspiration;
    }

    public Integer getStrength() {
        return _strength;
    }

    public void setStrength(Integer strength) {
        _strength = strength;
    }

    public Integer getDexterity() {
        return _dexterity;
    }

    public void setDexterity(Integer dexterity) {
        _dexterity = dexterity;
    }

    public Integer getConstitution() {
        return _constitution;
    }

    public void setConstitution(Integer constitution) {
        _constitution = constitution;
    }

    public Integer getIntelligence() {
        return _intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        _intelligence = intelligence;
    }

    public Integer getWisdom() {
        return _wisdom;
    }

    public void setWisdom(Integer wisdom) {
        _wisdom = wisdom;
    }

    public Integer getCharisma() {
        return _charisma;
    }

    public void setCharisma(Integer charisma) {
        _charisma = charisma;
    }

    public String getProficiency_language() {
        return _proficiency_language;
    }
    
    public String getProficiency_language_view() {
        return newLineToBr(_proficiency_language);
    }

    public void setProficiency_language(String proficiency_language) {
        _proficiency_language = proficiency_language;
    }

    public Integer getAc() {
        return _ac;
    }

    public void setAc(Integer ac) {
        _ac = ac;
    }

    public Integer getInitiative() {
        return _initiative;
    }

    public void setInitiative(Integer initiative) {
        _initiative = initiative;
    }

    public Integer getSpeed() {
        return _speed;
    }

    public void setSpeed(Integer speed) {
        _speed = speed;
    }

    public String getHit_dice() {
        return _hit_dice;
    }

    public void setHit_dice(String hit_dice) {
        _hit_dice = hit_dice;
    }

    public String getAttack_spellcasting() {
        return _attach_spellcasting;
    }
    
    public String getAttack_spellcasting_view() {
        return newLineToBr(_attach_spellcasting);
    }

    public void setAttack_spellcasting(String attach_spellcasting) {
        _attach_spellcasting = attach_spellcasting;
    }

    public String getEquipment() {
        return _equipment;
    }
    
    public String getEquipment_view() {
        return newLineToBr(_equipment);
    }

    public void setEquipment(String equipment) {
        _equipment = equipment;
    }

    public String getPersonality_traits() {
        return _personality_traits;
    }
    
    public String getPersonality_traits_view() {
        return newLineToBr(_personality_traits);
    }

    public void setPersonality_traits(String personality_traits) {
        _personality_traits = personality_traits;
    }

    public String getIdeals() {
        return _ideals;
    }
    
    public String getIdeals_view() {
        return newLineToBr(_ideals);
    }

    public void setIdeals(String ideals) {
        _ideals = ideals;
    }

    public String getBonds() {
        return _bonds;
    }
    
    public String getBonds_view() {
        return newLineToBr(_bonds);
    }
    
    public void setBonds(String bonds) {
        _bonds = bonds;
    }

    public String getFlaws() {
        return _flaws;
    }
    
    public String getFlaws_view() {
        return newLineToBr(_flaws);
    }

    public void setFlaws(String flaws) {
        _flaws = flaws;
    }

    public String getFeatures_traits() {
        return _features_traits;
    }
    
    public String getFeatures_traits_view() {
        return newLineToBr(_features_traits);
    }

    public void setFeatures_traits(String features_traits) {
        _features_traits = features_traits;
    }

    public String getAge() {
        return _age;
    }

    public void setAge(String age) {
        _age = age;
    }

    public String getHeight() {
        return _height;
    }

    public void setHeight(String height) {
        _height = height;
    }

    public String getWeight() {
        return _weight;
    }

    public void setWeight(String weight) {
        _weight = weight;
    }

    public String getEyes() {
        return _eyes;
    }

    public void setEyes(String eyes) {
        _eyes = eyes;
    }

    public String getSkin() {
        return _skin;
    }

    public void setSkin(String skin) {
        _skin = skin;
    }

    public String getHair() {
        return _hair;
    }

    public void setHair(String hair) {
        _hair = hair;
    }

    public String getSpellcasting_ability() {
        return _spellcasting_ability;
    }

    public void setSpellcasting_ability(String spellcasting_ability) {
        _spellcasting_ability = spellcasting_ability;
    }

    public String getSpellcasting_class() {
        return _spellcasting_class;
    }

    public void setSpellcasting_class(String spellcasting_class) {
        _spellcasting_class = spellcasting_class;
    }
    
    private String newLineToBr(String text) 
    {
	return text.replaceAll("\n", "<br/>");	
    }
}
