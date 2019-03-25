package com.cafeform.dndapp;

import java.util.ArrayList;
import java.util.List;
import static com.cafeform.dndapp.Constants.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.SortDirection;

@Controller
public class CharaController {

	private final DatastoreService _datastore;
	static final int PAGE_SIZE = 100;

	public CharaController() {
		_datastore = DatastoreServiceFactory.getDatastoreService();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list(Model model) {

		Query q = new Query("CharaData").addSort("name", SortDirection.ASCENDING);
		PreparedQuery pq = _datastore.prepare(q);

		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);

		QueryResultList<Entity> charaDatas;
		try {
			charaDatas = pq.asQueryResultList(fetchOptions);
		} catch (IllegalArgumentException e) {
			return "chara_list";
		}

		List<Chara> charas = new ArrayList<>();
		for (Entity charaData : charaDatas) {

			PlayerChara chara = new PlayerChara();
			updatePlayerChara(chara, charaData);
			charas.add(chara);
		}
		model.addAttribute("charas", charas);
		return "chara_list";
	}

	@RequestMapping(value = "/chara_view", method = RequestMethod.GET)
	public String view(Model model, PlayerChara chara, @ModelAttribute("charaKey") String charaKey)

	{
		Entity charaData;

		try {
			charaData = _datastore.get(KeyFactory.stringToKey(charaKey));
		} catch (EntityNotFoundException e) {
			// TODO print error on page
			return "chara_view";
		}

		updatePlayerChara(chara, charaData);

		return "chara_view";
	}

	Entity getCharaData(String charaKey) throws EntityNotFoundException {
		try {
			return _datastore.get(KeyFactory.stringToKey(charaKey));
		} catch (IllegalArgumentException e) {
			return new Entity("CharaData");
		}
	}

	@RequestMapping("/chara_create")
	public String create(Model model, PlayerChara chara) {
		return "chara_edit";
	}

	private void updatePlayerChara(PlayerChara chara, Entity charaData) {
		chara.setCharaKey(KeyFactory.keyToString(charaData.getKey()));
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
		
		chara.setNote(getStrProperty(charaData, NOTE));
	}

	@RequestMapping("/chara_edit")
	public String edit(Model model, PlayerChara chara, @ModelAttribute("charaKey") String charaKey) {
		Entity charaData;

		try {
			charaData = _datastore.get(KeyFactory.stringToKey(charaKey));
		} catch (EntityNotFoundException e) {
			// TODO print error on page
			return "chara_edit";
		}

		updatePlayerChara(chara, charaData);

		return "chara_edit";
	}

	private String getStrProperty(Entity entity, String property) {

		if (entity.getProperty(property) == null) {
			return "";
		}
		Object obj = entity.getProperty(property);
		if(Text.class.isInstance(obj)) {
			return ((Text)obj).getValue();			
		}
		return obj.toString();
	}

	private Integer getIntProperty(Entity entity, String property) {

		if (entity.getProperty(property) == null) {
			return 0;
		}
		return Integer.parseInt(entity.getProperty(property).toString());
	}

	private void updateCharaData(Entity charaData, PlayerChara chara) {
		charaData.setProperty(NAME, chara.getName());
		charaData.setProperty(KLASS, chara.getKlass());
		charaData.setProperty(LEVEL, chara.getLevel());
		charaData.setProperty(PLAYER, chara.getPlayer());
		charaData.setProperty(RACE, chara.getRace());
		charaData.setProperty(ALIGNMENT, chara.getAlignment());
		charaData.setProperty(EXPERIENCE, chara.getExperience());

		charaData.setProperty(STR, chara.getStrength());
		charaData.setProperty(DEX, chara.getDexterity());
		charaData.setProperty(CON, chara.getConstitution());
		charaData.setProperty(INT, chara.getIntelligence());
		charaData.setProperty(WIS, chara.getWisdom());
		charaData.setProperty(CHA, chara.getCharisma());

		charaData.setProperty(INSPIRATION, chara.getInspiration());

		charaData.setProperty(STR + SAVE, chara.getStrength_save());
		charaData.setProperty(DEX + SAVE, chara.getDexterity_save());
		charaData.setProperty(CON + SAVE, chara.getConstitution_save());
		charaData.setProperty(INT + SAVE, chara.getIntelligence_save());
		charaData.setProperty(WIS + SAVE, chara.getWisdom_save());
		charaData.setProperty(CHA + SAVE, chara.getCharisma_save());

		charaData.setProperty(ACROBATICS, chara.getAcrobatics());
		charaData.setProperty(ANIMAL, chara.getAnimal());
		charaData.setProperty(ARCANA, chara.getArcana());
		charaData.setProperty(ATHLETICS, chara.getAthletics());
		charaData.setProperty(DECEPTION, chara.getDeception());
		charaData.setProperty(HISTORY, chara.getHistory());
		charaData.setProperty(INTIMIDATION, chara.getIntimidation());
		charaData.setProperty(INSIGHT, chara.getInsight());
		charaData.setProperty(INVESTIGATION, chara.getInvestigation());
		charaData.setProperty(MEDICINE, chara.getMedicine());
		charaData.setProperty(NATURE, chara.getNature());
		charaData.setProperty(PERFORMANCE, chara.getPerformance());
		charaData.setProperty(PERCEPTION, chara.getPerception());
		charaData.setProperty(PERSUASION, chara.getPersuasion());
		charaData.setProperty(RELIGION, chara.getReligion());
		charaData.setProperty(SLEIGHT, chara.getSleight());
		charaData.setProperty(STEALTH, chara.getStealth());
		charaData.setProperty(SURVIVAL, chara.getSurvival());

		charaData.setProperty(PROFICIENCY_LANGUAGE, new Text(chara.getProficiency_language()));
		charaData.setProperty(AC, chara.getAc());
		charaData.setProperty(INITIATIVE, chara.getInitiative());
		charaData.setProperty(SPEED, chara.getSpeed());
		charaData.setProperty(HIT_DICE, chara.getHit_dice());
		charaData.setProperty(HIT_POINT, chara.getHit_point());
		charaData.setProperty(ATTACK_SPELLCASTING, new Text(chara.getAttack_spellcasting()));
		charaData.setProperty(EQUIPMENT, new Text(chara.getEquipment()));
		charaData.setProperty(PERSONALITY_TRAITS, new Text(chara.getPersonality_traits()));
		charaData.setProperty(IDEALS, new Text(chara.getIdeals()));
		charaData.setProperty(BONDS, new Text(chara.getBonds()));
		charaData.setProperty(FLAWS, new Text(chara.getFlaws()));
		charaData.setProperty(FEATURES_TRAITS, new Text(chara.getFeatures_traits()));
		charaData.setProperty(AGE, chara.getAge());
		charaData.setProperty(HEIGHT, chara.getHeight());
		charaData.setProperty(WEIGHT, chara.getWeight());
		charaData.setProperty(EYES, chara.getEyes());
		charaData.setProperty(SKIN, chara.getSkin());
		charaData.setProperty(HAIR, chara.getHair());
		charaData.setProperty(SPELLCASTING_ABILITY, chara.getSpellcasting_ability());
		charaData.setProperty(SPELLCASTING_CLASS, chara.getSpellcasting_class());
		
		charaData.setProperty(NOTE, new Text(chara.getNote()));
	}

	@RequestMapping("/chara_save")
	public String save(Model model, @ModelAttribute PlayerChara chara) {
		Entity charaData;

		try {
			charaData = getCharaData(chara.getCharaKey());
		} catch (EntityNotFoundException e) {
			// TODO print error
			return list(model);
		}

		updateCharaData(charaData, chara);

		_datastore.put(charaData);
		chara.setCharaKey(KeyFactory.keyToString(charaData.getKey()));

		return "chara_edit";
	}

	@RequestMapping("/chara_delete")
	public String delete(Model model, @ModelAttribute("charaKey") String charaKey) {
		_datastore.delete(KeyFactory.stringToKey(charaKey));

		return list(model);
	}
}
