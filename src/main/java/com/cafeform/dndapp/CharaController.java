package com.cafeform.dndapp;

import java.util.ArrayList;
import java.util.List;

import static com.cafeform.dndapp.Constants.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

@Controller
public class CharaController {

	private final Datastore _datastore;
	private final KeyFactory _charaKeyFactory;
	private final KeyFactory _noteKeyFactory;
	static final int PAGE_SIZE = 100;

	public CharaController(Datastore datastore) {
		_datastore = datastore;
		_charaKeyFactory = datastore.newKeyFactory().setKind("CharaData");
		_noteKeyFactory = datastore.newKeyFactory().setKind("CampaignNote");
	}

	@RequestMapping(value = "/deleted_chara", method = RequestMethod.GET)
	public String listDeletedChara(Model model) {
		return listChara(model, true);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list(Model model) {
		return listChara(model, false);
	}

	public String listChara(Model model, boolean showDeleted) {
		Query<Entity> q = Query.newEntityQueryBuilder()
				.setKind("CharaData")
				.addOrderBy(OrderBy.asc("name"))
				.setLimit(PAGE_SIZE)
				.build();

		QueryResults<Entity> results = _datastore.run(q);
		List<Chara> charas = new ArrayList<>();

		while (results.hasNext()) {
			Entity charaData = results.next();
			Boolean deleted = getBooleanProperty(charaData, DELETED);
			if ((showDeleted && !deleted) || (!showDeleted && deleted)) {
				continue;
			}
			PlayerChara chara = new PlayerChara();
			updatePlayerChara(chara, charaData);
			charas.add(chara);
		}

		model.addAttribute("charas", charas);
		model.addAttribute("showDeleted", showDeleted);
		return "chara_list";
	}

	@RequestMapping(value = "/chara_view", method = RequestMethod.GET)
	public String viewChara(Model model, PlayerChara chara, @ModelAttribute("charaKey") String charaKey) {
		Entity charaData = getCharaData(charaKey);
		if (charaData == null) {
			return "chara_view";
		}
		updatePlayerChara(chara, charaData);
		return "chara_view";
	}

	Entity getCharaData(String charaKey) {
		if (charaKey == null || charaKey.trim().isEmpty()) {
			return null;
		}
		try {
			Key key = Key.fromUrlSafe(charaKey);
			return _datastore.get(key);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@RequestMapping("/chara_create")
	public String create(Model model, PlayerChara chara) {
		return "chara_edit";
	}

	private void updatePlayerChara(PlayerChara chara, Entity charaData) {
		chara.setCharaKey(charaData.getKey().toUrlSafe());
		chara.setName(getStrProperty(charaData, NAME));
		chara.setKlass(getStrProperty(charaData, KLASS));
		chara.setLevel(getIntProperty(charaData, LEVEL));
		chara.setPlayer(getStrProperty(charaData, PLAYER));
		chara.setRace(getStrProperty(charaData, RACE));
		chara.setAlignment(getStrProperty(charaData, ALIGNMENT));
		chara.setExperience(getIntProperty(charaData, EXPERIENCE));
		chara.setStrength(getIntProperty(charaData, STR));
		chara.setDexterity(getIntProperty(charaData, DEX));
		chara.setConstitution(getIntProperty(charaData, CON));
		chara.setIntelligence(getIntProperty(charaData, INT));
		chara.setWisdom(getIntProperty(charaData, WIS));
		chara.setCharisma(getIntProperty(charaData, CHA));
		chara.setInspiration(getStrProperty(charaData, INSPIRATION));
		chara.setStrength_save(getStrProperty(charaData, STR + SAVE));
		chara.setDexterity_save(getStrProperty(charaData, DEX + SAVE));
		chara.setConstitution_save(getStrProperty(charaData, CON + SAVE));
		chara.setIntelligence_save(getStrProperty(charaData, INT + SAVE));
		chara.setWisdom_save(getStrProperty(charaData, WIS + SAVE));
		chara.setCharisma_save(getStrProperty(charaData, CHA + SAVE));
		chara.setAcrobatics(getStrProperty(charaData, ACROBATICS));
		chara.setAnimal(getStrProperty(charaData, ANIMAL));
		chara.setArcana(getStrProperty(charaData, ARCANA));
		chara.setAthletics(getStrProperty(charaData, ATHLETICS));
		chara.setDeception(getStrProperty(charaData, DECEPTION));
		chara.setHistory(getStrProperty(charaData, HISTORY));
		chara.setIntimidation(getStrProperty(charaData, INTIMIDATION));
		chara.setInsight(getStrProperty(charaData, INSIGHT));
		chara.setInvestigation(getStrProperty(charaData, INVESTIGATION));
		chara.setMedicine(getStrProperty(charaData, MEDICINE));
		chara.setNature(getStrProperty(charaData, NATURE));
		chara.setPerformance(getStrProperty(charaData, PERFORMANCE));
		chara.setPerception(getStrProperty(charaData, PERCEPTION));
		chara.setPersuasion(getStrProperty(charaData, PERSUASION));
		chara.setReligion(getStrProperty(charaData, RELIGION));
		chara.setSleight(getStrProperty(charaData, SLEIGHT));
		chara.setStealth(getStrProperty(charaData, STEALTH));
		chara.setSurvival(getStrProperty(charaData, SURVIVAL));
		chara.setProficiency_language(getStrProperty(charaData, PROFICIENCY_LANGUAGE));
		chara.setAc(getIntProperty(charaData, AC));
		chara.setInitiative(getIntProperty(charaData, INITIATIVE));
		chara.setSpeed(getIntProperty(charaData, SPEED));
		chara.setHit_dice(getStrProperty(charaData, HIT_DICE));
		chara.setHit_point(getIntProperty(charaData, HIT_POINT));
		chara.setAttack_spellcasting(getStrProperty(charaData, ATTACK_SPELLCASTING));
		chara.setEquipment(getStrProperty(charaData, EQUIPMENT));
		chara.setPersonality_traits(getStrProperty(charaData, PERSONALITY_TRAITS));
		chara.setIdeals(getStrProperty(charaData, IDEALS));
		chara.setBonds(getStrProperty(charaData, BONDS));
		chara.setFlaws(getStrProperty(charaData, FLAWS));
		chara.setFeatures_traits(getStrProperty(charaData, FEATURES_TRAITS));
		chara.setAge(getStrProperty(charaData, AGE));
		chara.setHeight(getStrProperty(charaData, HEIGHT));
		chara.setWeight(getStrProperty(charaData, WEIGHT));
		chara.setEyes(getStrProperty(charaData, EYES));
		chara.setSkin(getStrProperty(charaData, SKIN));
		chara.setHair(getStrProperty(charaData, HAIR));
		chara.setSpellcasting_ability(getStrProperty(charaData, SPELLCASTING_ABILITY));
		chara.setSpellcasting_class(getStrProperty(charaData, SPELLCASTING_CLASS));
		chara.setSpell_note(getStrProperty(charaData, SPELL_NOTE));
		chara.setCache(getIntProperty(charaData, CACHE));
		chara.setDefeats(getIntProperty(charaData, DEFEATS));
		chara.setNpp(getIntProperty(charaData, NPP));
		chara.setNote(getStrProperty(charaData, NOTE));
		chara.setDeleted(getBooleanProperty(charaData, DELETED));
		chara.setSpell_save_dc_modifier(getIntProperty(charaData, SPELL_SAVE_DC_MODIFIER));
		chara.setSpell_attack_bonus_modifier(getIntProperty(charaData, SPELL_ATTACK_BONUS_MODIFIER));
	}

	private String getStrProperty(Entity entity, String property) {
		if (!entity.contains(property)) {
			return "";
		}
		String v = entity.getString(property);
		return v != null ? v : "";
	}

	private Integer getIntProperty(Entity entity, String property) {
		if (!entity.contains(property)) {
			return 0;
		}
		Long v = entity.getLong(property);
		return v != null ? v.intValue() : 0;
	}

	private Boolean getBooleanProperty(Entity entity, String property) {
		if (!entity.contains(property)) {
			return false;
		}
		Boolean v = entity.getBoolean(property);
		return v != null ? v : false;
	}

	@RequestMapping("/chara_edit")
	public String editChara(Model model, PlayerChara chara, @ModelAttribute("charaKey") String charaKey) {
		Entity charaData = getCharaData(charaKey);
		if (charaData == null) {
			return "chara_edit";
		}
		updatePlayerChara(chara, charaData);
		return "chara_edit";
	}

	private Entity buildCharaEntity(Key key, PlayerChara chara) {
		Entity.Builder builder = Entity.newBuilder(key);
		builder.set(NAME, chara.getName() != null ? chara.getName() : "")
				.set(KLASS, chara.getKlass() != null ? chara.getKlass() : "")
				.set(LEVEL, chara.getLevel() != null ? chara.getLevel() : 0)
				.set(PLAYER, chara.getPlayer() != null ? chara.getPlayer() : "")
				.set(RACE, chara.getRace() != null ? chara.getRace() : "")
				.set(ALIGNMENT, chara.getAlignment() != null ? chara.getAlignment() : "")
				.set(EXPERIENCE, chara.getExperience() != null ? chara.getExperience() : 0)
				.set(STR, chara.getStrength() != null ? chara.getStrength() : 0)
				.set(DEX, chara.getDexterity() != null ? chara.getDexterity() : 0)
				.set(CON, chara.getConstitution() != null ? chara.getConstitution() : 0)
				.set(INT, chara.getIntelligence() != null ? chara.getIntelligence() : 0)
				.set(WIS, chara.getWisdom() != null ? chara.getWisdom() : 0)
				.set(CHA, chara.getCharisma() != null ? chara.getCharisma() : 0)
				.set(INSPIRATION, chara.getInspiration() != null ? chara.getInspiration() : "")
				.set(STR + SAVE, chara.getStrength_save() != null ? chara.getStrength_save() : "")
				.set(DEX + SAVE, chara.getDexterity_save() != null ? chara.getDexterity_save() : "")
				.set(CON + SAVE, chara.getConstitution_save() != null ? chara.getConstitution_save() : "")
				.set(INT + SAVE, chara.getIntelligence_save() != null ? chara.getIntelligence_save() : "")
				.set(WIS + SAVE, chara.getWisdom_save() != null ? chara.getWisdom_save() : "")
				.set(CHA + SAVE, chara.getCharisma_save() != null ? chara.getCharisma_save() : "")
				.set(ACROBATICS, chara.getAcrobatics() != null ? chara.getAcrobatics() : "")
				.set(ANIMAL, chara.getAnimal() != null ? chara.getAnimal() : "")
				.set(ARCANA, chara.getArcana() != null ? chara.getArcana() : "")
				.set(ATHLETICS, chara.getAthletics() != null ? chara.getAthletics() : "")
				.set(DECEPTION, chara.getDeception() != null ? chara.getDeception() : "")
				.set(HISTORY, chara.getHistory() != null ? chara.getHistory() : "")
				.set(INTIMIDATION, chara.getIntimidation() != null ? chara.getIntimidation() : "")
				.set(INSIGHT, chara.getInsight() != null ? chara.getInsight() : "")
				.set(INVESTIGATION, chara.getInvestigation() != null ? chara.getInvestigation() : "")
				.set(MEDICINE, chara.getMedicine() != null ? chara.getMedicine() : "")
				.set(NATURE, chara.getNature() != null ? chara.getNature() : "")
				.set(PERFORMANCE, chara.getPerformance() != null ? chara.getPerformance() : "")
				.set(PERCEPTION, chara.getPerception() != null ? chara.getPerception() : "")
				.set(PERSUASION, chara.getPersuasion() != null ? chara.getPersuasion() : "")
				.set(RELIGION, chara.getReligion() != null ? chara.getReligion() : "")
				.set(SLEIGHT, chara.getSleight() != null ? chara.getSleight() : "")
				.set(STEALTH, chara.getStealth() != null ? chara.getStealth() : "")
				.set(SURVIVAL, chara.getSurvival() != null ? chara.getSurvival() : "")
				.set(PROFICIENCY_LANGUAGE, chara.getProficiency_language() != null ? chara.getProficiency_language() : "")
				.set(AC, chara.getAc() != null ? chara.getAc() : 0)
				.set(INITIATIVE, chara.getInitiative() != null ? chara.getInitiative() : 0)
				.set(SPEED, chara.getSpeed() != null ? chara.getSpeed() : 0)
				.set(HIT_DICE, chara.getHit_dice() != null ? chara.getHit_dice() : "")
				.set(HIT_POINT, chara.getHit_point() != null ? chara.getHit_point() : 0)
				.set(ATTACK_SPELLCASTING, chara.getAttack_spellcasting() != null ? chara.getAttack_spellcasting() : "")
				.set(EQUIPMENT, chara.getEquipment() != null ? chara.getEquipment() : "")
				.set(PERSONALITY_TRAITS, chara.getPersonality_traits() != null ? chara.getPersonality_traits() : "")
				.set(IDEALS, chara.getIdeals() != null ? chara.getIdeals() : "")
				.set(BONDS, chara.getBonds() != null ? chara.getBonds() : "")
				.set(FLAWS, chara.getFlaws() != null ? chara.getFlaws() : "")
				.set(FEATURES_TRAITS, chara.getFeatures_traits() != null ? chara.getFeatures_traits() : "")
				.set(AGE, chara.getAge() != null ? chara.getAge() : "")
				.set(HEIGHT, chara.getHeight() != null ? chara.getHeight() : "")
				.set(WEIGHT, chara.getWeight() != null ? chara.getWeight() : "")
				.set(EYES, chara.getEyes() != null ? chara.getEyes() : "")
				.set(SKIN, chara.getSkin() != null ? chara.getSkin() : "")
				.set(HAIR, chara.getHair() != null ? chara.getHair() : "")
				.set(SPELLCASTING_ABILITY, chara.getSpellcasting_ability() != null ? chara.getSpellcasting_ability() : "")
				.set(SPELLCASTING_CLASS, chara.getSpellcasting_class() != null ? chara.getSpellcasting_class() : "")
				.set(SPELL_NOTE, chara.getSpell_note() != null ? chara.getSpell_note() : "")
				.set(CACHE, chara.getCache() != null ? chara.getCache() : 0)
				.set(DEFEATS, chara.getDefeats() != null ? chara.getDefeats() : 0)
				.set(NPP, chara.getNpp() != null ? chara.getNpp() : 0)
				.set(NOTE, chara.getNote() != null ? chara.getNote() : "")
				.set(DELETED, chara.isDeleted() != null ? chara.isDeleted() : false)
				.set(SPELL_SAVE_DC_MODIFIER, chara.getSpell_save_dc_modifier() != null ? chara.getSpell_save_dc_modifier() : 0)
				.set(SPELL_ATTACK_BONUS_MODIFIER, chara.getSpell_attack_bonus_modifier() != null ? chara.getSpell_attack_bonus_modifier() : 0);
		return builder.build();
	}

	@RequestMapping("/chara_save")
	public String saveChara(Model model, @ModelAttribute PlayerChara chara) {
		String keyStr = chara.getCharaKey();
		boolean isNew = keyStr == null || keyStr.trim().isEmpty();

		if (isNew) {
			Key key = _datastore.allocateId(_charaKeyFactory.newKey());
			Entity entity = buildCharaEntity(key, chara);
			_datastore.put(entity);
			chara.setCharaKey(entity.getKey().toUrlSafe());
		} else {
			Entity charaData = getCharaData(keyStr);
			if (charaData == null) {
				return list(model);
			}
			Entity entity = buildCharaEntity(charaData.getKey(), chara);
			_datastore.put(entity);
			chara.setCharaKey(entity.getKey().toUrlSafe());
		}
		return "chara_edit";
	}

	@RequestMapping("/chara_delete")
	public String deleteChara(Model model, @ModelAttribute("charaKey") String charaKey) {
		Entity charaData = getCharaData(charaKey);
		if (charaData == null) {
			return list(model);
		}
		Entity updated = Entity.newBuilder(charaData).set(DELETED, true).build();
		_datastore.put(updated);
		return list(model);
	}

	@RequestMapping("/chara_restore")
	public String restoreChara(Model model, @ModelAttribute("charaKey") String charaKey) {
		Entity charaData = getCharaData(charaKey);
		if (charaData == null) {
			return list(model);
		}
		Entity updated = Entity.newBuilder(charaData).set(DELETED, false).build();
		_datastore.put(updated);
		return list(model);
	}

	@RequestMapping("/campaign_note_restore")
	public String campaignNoteRestore(Model model, @ModelAttribute("key") String key) {
		Entity campaignNote = getCampaignNote(key);
		if (campaignNote == null) {
			return list(model);
		}
		Entity updated = Entity.newBuilder(campaignNote).set(DELETED, false).build();
		_datastore.put(updated);
		return campaignNoteList(model);
	}

	@RequestMapping(value = "/campaign_note_list", method = RequestMethod.GET)
	public String campaignNoteList(Model model) {
		return campaignNoteList(model, false);
	}

	@RequestMapping(value = "/deleted_campaign_note_list", method = RequestMethod.GET)
	public String deletedCampaignNoteList(Model model) {
		return campaignNoteList(model, true);
	}

	public String campaignNoteList(Model model, boolean showDeleted) {
		Query<Entity> q = Query.newEntityQueryBuilder()
				.setKind("CampaignNote")
				.addOrderBy(OrderBy.asc("campaign_name"))
				.setLimit(PAGE_SIZE)
				.build();

		QueryResults<Entity> results = _datastore.run(q);
		List<CampaignNote> notes = new ArrayList<>();

		while (results.hasNext()) {
			Entity entity = results.next();
			Boolean deleted = getBooleanProperty(entity, DELETED);
			if ((showDeleted && !deleted) || (!showDeleted && deleted)) {
				continue;
			}
			CampaignNote note = new CampaignNote();
			note.setKey(entity.getKey().toUrlSafe());
			note.setCampaign_name(getStrProperty(entity, CAMPAIGN_NAME));
			note.setTitle(getStrProperty(entity, CAMPAIGN_NOTE_TITLE));
			note.setNote(getStrProperty(entity, CAMPAIGN_NOTE));
			note.setDeleted(getBooleanProperty(entity, DELETED));
			notes.add(note);
		}

		model.addAttribute("notes", notes);
		model.addAttribute("showDeleted", showDeleted);
		return "campaign_note_list_page";
	}

	Entity getCampaignNote(String keyStr) {
		if (keyStr == null || keyStr.trim().isEmpty()) {
			return null;
		}
		try {
			Key key = Key.fromUrlSafe(keyStr);
			return _datastore.get(key);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@RequestMapping("/campaign_note_save")
	public String saveCampaignNote(Model model, @ModelAttribute CampaignNote note) {
		String keyStr = note.getKey();
		boolean isNew = keyStr == null || keyStr.trim().isEmpty();

		if (isNew) {
			Key key = _datastore.allocateId(_noteKeyFactory.newKey());
			Entity entity = Entity.newBuilder(key)
					.set(CAMPAIGN_NAME, note.getCampaign_name() != null ? note.getCampaign_name() : "")
					.set(CAMPAIGN_NOTE_TITLE, note.getTitle() != null ? note.getTitle() : "")
					.set(CAMPAIGN_NOTE, note.getNote() != null ? note.getNote() : "")
					.set(DELETED, note.isDeleted() != null ? note.isDeleted() : false)
					.build();
			_datastore.put(entity);
			note.setKey(entity.getKey().toUrlSafe());
		} else {
			Entity entity = getCampaignNote(keyStr);
			if (entity == null) {
				return campaignNoteList(model);
			}
			Entity updated = Entity.newBuilder(entity)
					.set(CAMPAIGN_NAME, note.getCampaign_name() != null ? note.getCampaign_name() : "")
					.set(CAMPAIGN_NOTE_TITLE, note.getTitle() != null ? note.getTitle() : "")
					.set(CAMPAIGN_NOTE, note.getNote() != null ? note.getNote() : "")
					.set(DELETED, note.isDeleted() != null ? note.isDeleted() : false)
					.build();
			_datastore.put(updated);
			note.setKey(updated.getKey().toUrlSafe());
		}
		return "campaign_note_edit";
	}

	@RequestMapping("/campaign_note_delete")
	public String deleteCampaignNote(Model model, @ModelAttribute("key") String key) {
		Entity campaignNote = getCampaignNote(key);
		if (campaignNote == null) {
			return list(model);
		}
		Entity updated = Entity.newBuilder(campaignNote).set(DELETED, true).build();
		_datastore.put(updated);
		return campaignNoteList(model);
	}

	@RequestMapping("/campaign_note_edit")
	public String editCampaignNote(Model model, CampaignNote note, @ModelAttribute("key") String key) {
		Entity entity = getCampaignNote(key);
		if (entity == null) {
			return "campaign_note_edit";
		}
		note.setKey(entity.getKey().toUrlSafe());
		note.setCampaign_name(getStrProperty(entity, CAMPAIGN_NAME));
		note.setTitle(getStrProperty(entity, CAMPAIGN_NOTE_TITLE));
		note.setNote(getStrProperty(entity, CAMPAIGN_NOTE));
		note.setDeleted(getBooleanProperty(entity, DELETED));
		return "campaign_note_edit";
	}

	@RequestMapping("/campaign_note_create")
	public String createCampaignNote(Model model, CampaignNote note) {
		return "campaign_note_edit";
	}
}
