package com.cafeform.dndapp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.cafeform.dndapp.Constants.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

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
		// Do not orderBy("name"): entities with only name_BLOB (no "name") would be excluded from index.
		Query<Entity> q = Query.newEntityQueryBuilder()
				.setKind("CharaData")
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
		charas.sort(java.util.Comparator.comparing(Chara::getName, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder())));

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
		chara.setName(getStringFromBlobOrLegacy(charaData, NAME));
		chara.setKlass(getStringFromBlobOrLegacy(charaData, KLASS));
		chara.setLevel(getIntProperty(charaData, LEVEL));
		chara.setPlayer(getStringFromBlobOrLegacy(charaData, PLAYER));
		chara.setRace(getStringFromBlobOrLegacy(charaData, RACE));
		chara.setAlignment(getStringFromBlobOrLegacy(charaData, ALIGNMENT));
		chara.setExperience(getIntProperty(charaData, EXPERIENCE));
		chara.setStrength(getIntProperty(charaData, STR));
		chara.setDexterity(getIntProperty(charaData, DEX));
		chara.setConstitution(getIntProperty(charaData, CON));
		chara.setIntelligence(getIntProperty(charaData, INT));
		chara.setWisdom(getIntProperty(charaData, WIS));
		chara.setCharisma(getIntProperty(charaData, CHA));
		chara.setInspiration(getStringFromBlobOrLegacy(charaData, INSPIRATION));
		chara.setStrength_save(getStringFromBlobOrLegacy(charaData, STR + SAVE));
		chara.setDexterity_save(getStringFromBlobOrLegacy(charaData, DEX + SAVE));
		chara.setConstitution_save(getStringFromBlobOrLegacy(charaData, CON + SAVE));
		chara.setIntelligence_save(getStringFromBlobOrLegacy(charaData, INT + SAVE));
		chara.setWisdom_save(getStringFromBlobOrLegacy(charaData, WIS + SAVE));
		chara.setCharisma_save(getStringFromBlobOrLegacy(charaData, CHA + SAVE));
		chara.setAcrobatics(getStringFromBlobOrLegacy(charaData, ACROBATICS));
		chara.setAnimal(getStringFromBlobOrLegacy(charaData, ANIMAL));
		chara.setArcana(getStringFromBlobOrLegacy(charaData, ARCANA));
		chara.setAthletics(getStringFromBlobOrLegacy(charaData, ATHLETICS));
		chara.setDeception(getStringFromBlobOrLegacy(charaData, DECEPTION));
		chara.setHistory(getStringFromBlobOrLegacy(charaData, HISTORY));
		chara.setIntimidation(getStringFromBlobOrLegacy(charaData, INTIMIDATION));
		chara.setInsight(getStringFromBlobOrLegacy(charaData, INSIGHT));
		chara.setInvestigation(getStringFromBlobOrLegacy(charaData, INVESTIGATION));
		chara.setMedicine(getStringFromBlobOrLegacy(charaData, MEDICINE));
		chara.setNature(getStringFromBlobOrLegacy(charaData, NATURE));
		chara.setPerformance(getStringFromBlobOrLegacy(charaData, PERFORMANCE));
		chara.setPerception(getStringFromBlobOrLegacy(charaData, PERCEPTION));
		chara.setPersuasion(getStringFromBlobOrLegacy(charaData, PERSUASION));
		chara.setReligion(getStringFromBlobOrLegacy(charaData, RELIGION));
		chara.setSleight(getStringFromBlobOrLegacy(charaData, SLEIGHT));
		chara.setStealth(getStringFromBlobOrLegacy(charaData, STEALTH));
		chara.setSurvival(getStringFromBlobOrLegacy(charaData, SURVIVAL));
		chara.setProficiency_language(getStringFromBlobOrLegacy(charaData, PROFICIENCY_LANGUAGE));
		chara.setAc(getIntProperty(charaData, AC));
		chara.setInitiative(getIntProperty(charaData, INITIATIVE));
		chara.setSpeed(getIntProperty(charaData, SPEED));
		chara.setHit_dice(getStringFromBlobOrLegacy(charaData, HIT_DICE));
		chara.setHit_point(getIntProperty(charaData, HIT_POINT));
		chara.setAttack_spellcasting(getStringFromBlobOrLegacy(charaData, ATTACK_SPELLCASTING));
		chara.setEquipment(getStringFromBlobOrLegacy(charaData, EQUIPMENT));
		chara.setPersonality_traits(getStringFromBlobOrLegacy(charaData, PERSONALITY_TRAITS));
		chara.setIdeals(getStringFromBlobOrLegacy(charaData, IDEALS));
		chara.setBonds(getStringFromBlobOrLegacy(charaData, BONDS));
		chara.setFlaws(getStringFromBlobOrLegacy(charaData, FLAWS));
		chara.setFeatures_traits(getStringFromBlobOrLegacy(charaData, FEATURES_TRAITS));
		chara.setAge(getStringFromBlobOrLegacy(charaData, AGE));
		chara.setHeight(getStringFromBlobOrLegacy(charaData, HEIGHT));
		chara.setWeight(getStringFromBlobOrLegacy(charaData, WEIGHT));
		chara.setEyes(getStringFromBlobOrLegacy(charaData, EYES));
		chara.setSkin(getStringFromBlobOrLegacy(charaData, SKIN));
		chara.setHair(getStringFromBlobOrLegacy(charaData, HAIR));
		chara.setSpellcasting_ability(getStringFromBlobOrLegacy(charaData, SPELLCASTING_ABILITY));
		chara.setSpellcasting_class(getStringFromBlobOrLegacy(charaData, SPELLCASTING_CLASS));
		chara.setSpell_note(getStringFromBlobOrLegacy(charaData, SPELL_NOTE));
		chara.setCache(getIntProperty(charaData, CACHE));
		chara.setDefeats(getIntProperty(charaData, DEFEATS));
		chara.setNpp(getIntProperty(charaData, NPP));
		chara.setNote(getStringFromBlobOrLegacy(charaData, NOTE));
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

	/** Read string: prefer prop_BLOB (Blob); if missing/empty or error, use legacy prop (String). */
	private String getStringFromBlobOrLegacy(Entity entity, String prop) {
		String blobProp = prop + "_BLOB";
		if (entity.contains(blobProp)) {
			try {
				Blob b = entity.getBlob(blobProp);
				if (b != null) {
					String decoded = new String(b.toByteArray(), StandardCharsets.UTF_8);
					if (!decoded.isEmpty()) return decoded;
				}
			} catch (Exception e) {
				// fall through to legacy
			}
		}
		return getStrProperty(entity, prop);
	}

	/** Write string to prop_BLOB only (Blob type, unindexed so >1500 bytes allowed); do not set legacy prop. */
	private static void setStringToBlob(Entity.Builder builder, String prop, String s) {
		if (s == null) s = "";
		Blob blob = Blob.copyFrom(s.getBytes(StandardCharsets.UTF_8));
		BlobValue value = BlobValue.newBuilder(blob).setExcludeFromIndexes(true).build();
		builder.set(prop + "_BLOB", value);
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
		setStringToBlob(builder, NAME, chara.getName());
		setStringToBlob(builder, KLASS, chara.getKlass());
		setStringToBlob(builder, PLAYER, chara.getPlayer());
		setStringToBlob(builder, RACE, chara.getRace());
		setStringToBlob(builder, ALIGNMENT, chara.getAlignment());
		setStringToBlob(builder, INSPIRATION, chara.getInspiration());
		setStringToBlob(builder, STR + SAVE, chara.getStrength_save());
		setStringToBlob(builder, DEX + SAVE, chara.getDexterity_save());
		setStringToBlob(builder, CON + SAVE, chara.getConstitution_save());
		setStringToBlob(builder, INT + SAVE, chara.getIntelligence_save());
		setStringToBlob(builder, WIS + SAVE, chara.getWisdom_save());
		setStringToBlob(builder, CHA + SAVE, chara.getCharisma_save());
		setStringToBlob(builder, ACROBATICS, chara.getAcrobatics());
		setStringToBlob(builder, ANIMAL, chara.getAnimal());
		setStringToBlob(builder, ARCANA, chara.getArcana());
		setStringToBlob(builder, ATHLETICS, chara.getAthletics());
		setStringToBlob(builder, DECEPTION, chara.getDeception());
		setStringToBlob(builder, HISTORY, chara.getHistory());
		setStringToBlob(builder, INTIMIDATION, chara.getIntimidation());
		setStringToBlob(builder, INSIGHT, chara.getInsight());
		setStringToBlob(builder, INVESTIGATION, chara.getInvestigation());
		setStringToBlob(builder, MEDICINE, chara.getMedicine());
		setStringToBlob(builder, NATURE, chara.getNature());
		setStringToBlob(builder, PERFORMANCE, chara.getPerformance());
		setStringToBlob(builder, PERCEPTION, chara.getPerception());
		setStringToBlob(builder, PERSUASION, chara.getPersuasion());
		setStringToBlob(builder, RELIGION, chara.getReligion());
		setStringToBlob(builder, SLEIGHT, chara.getSleight());
		setStringToBlob(builder, STEALTH, chara.getStealth());
		setStringToBlob(builder, SURVIVAL, chara.getSurvival());
		setStringToBlob(builder, PROFICIENCY_LANGUAGE, chara.getProficiency_language());
		setStringToBlob(builder, HIT_DICE, chara.getHit_dice());
		setStringToBlob(builder, ATTACK_SPELLCASTING, chara.getAttack_spellcasting());
		setStringToBlob(builder, EQUIPMENT, chara.getEquipment());
		setStringToBlob(builder, PERSONALITY_TRAITS, chara.getPersonality_traits());
		setStringToBlob(builder, IDEALS, chara.getIdeals());
		setStringToBlob(builder, BONDS, chara.getBonds());
		setStringToBlob(builder, FLAWS, chara.getFlaws());
		setStringToBlob(builder, FEATURES_TRAITS, chara.getFeatures_traits());
		setStringToBlob(builder, AGE, chara.getAge());
		setStringToBlob(builder, HEIGHT, chara.getHeight());
		setStringToBlob(builder, WEIGHT, chara.getWeight());
		setStringToBlob(builder, EYES, chara.getEyes());
		setStringToBlob(builder, SKIN, chara.getSkin());
		setStringToBlob(builder, HAIR, chara.getHair());
		setStringToBlob(builder, SPELLCASTING_ABILITY, chara.getSpellcasting_ability());
		setStringToBlob(builder, SPELLCASTING_CLASS, chara.getSpellcasting_class());
		setStringToBlob(builder, SPELL_NOTE, chara.getSpell_note());
		setStringToBlob(builder, NOTE, chara.getNote());
		builder.set(LEVEL, chara.getLevel() != null ? chara.getLevel() : 0)
				.set(EXPERIENCE, chara.getExperience() != null ? chara.getExperience() : 0)
				.set(STR, chara.getStrength() != null ? chara.getStrength() : 0)
				.set(DEX, chara.getDexterity() != null ? chara.getDexterity() : 0)
				.set(CON, chara.getConstitution() != null ? chara.getConstitution() : 0)
				.set(INT, chara.getIntelligence() != null ? chara.getIntelligence() : 0)
				.set(WIS, chara.getWisdom() != null ? chara.getWisdom() : 0)
				.set(CHA, chara.getCharisma() != null ? chara.getCharisma() : 0)
				.set(AC, chara.getAc() != null ? chara.getAc() : 0)
				.set(INITIATIVE, chara.getInitiative() != null ? chara.getInitiative() : 0)
				.set(SPEED, chara.getSpeed() != null ? chara.getSpeed() : 0)
				.set(HIT_POINT, chara.getHit_point() != null ? chara.getHit_point() : 0)
				.set(CACHE, chara.getCache() != null ? chara.getCache() : 0)
				.set(DEFEATS, chara.getDefeats() != null ? chara.getDefeats() : 0)
				.set(NPP, chara.getNpp() != null ? chara.getNpp() : 0)
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
		// Do not orderBy("campaign_name"): entities with only campaign_name_BLOB would be excluded from index.
		Query<Entity> q = Query.newEntityQueryBuilder()
				.setKind("CampaignNote")
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
			note.setCampaign_name(getStringFromBlobOrLegacy(entity, CAMPAIGN_NAME));
			note.setTitle(getStringFromBlobOrLegacy(entity, CAMPAIGN_NOTE_TITLE));
			note.setNote(getStringFromBlobOrLegacy(entity, CAMPAIGN_NOTE));
			note.setDeleted(getBooleanProperty(entity, DELETED));
			notes.add(note);
		}
		notes.sort(java.util.Comparator
				.comparing(CampaignNote::getCampaign_name, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()))
				.thenComparing(CampaignNote::getTitle, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder())));

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
			Entity.Builder b = Entity.newBuilder(key);
			setStringToBlob(b, CAMPAIGN_NAME, note.getCampaign_name());
			setStringToBlob(b, CAMPAIGN_NOTE_TITLE, note.getTitle());
			setStringToBlob(b, CAMPAIGN_NOTE, note.getNote());
			b.set(DELETED, note.isDeleted() != null ? note.isDeleted() : false);
			Entity entity = b.build();
			_datastore.put(entity);
			note.setKey(entity.getKey().toUrlSafe());
		} else {
			Entity entity = getCampaignNote(keyStr);
			if (entity == null) {
				return campaignNoteList(model);
			}
			Entity.Builder b = Entity.newBuilder(entity.getKey());
			setStringToBlob(b, CAMPAIGN_NAME, note.getCampaign_name());
			setStringToBlob(b, CAMPAIGN_NOTE_TITLE, note.getTitle());
			setStringToBlob(b, CAMPAIGN_NOTE, note.getNote());
			b.set(DELETED, note.isDeleted() != null ? note.isDeleted() : false);
			Entity updated = b.build();
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
		note.setCampaign_name(getStringFromBlobOrLegacy(entity, CAMPAIGN_NAME));
		note.setTitle(getStringFromBlobOrLegacy(entity, CAMPAIGN_NOTE_TITLE));
		note.setNote(getStringFromBlobOrLegacy(entity, CAMPAIGN_NOTE));
		note.setDeleted(getBooleanProperty(entity, DELETED));
		return "campaign_note_edit";
	}

	@RequestMapping("/campaign_note_create")
	public String createCampaignNote(Model model, CampaignNote note) {
		return "campaign_note_edit";
	}
}
