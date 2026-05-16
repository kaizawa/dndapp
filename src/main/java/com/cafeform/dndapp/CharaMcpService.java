package com.cafeform.dndapp;

import static com.cafeform.dndapp.Constants.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

@Service
public class CharaMcpService {

    private static final Logger log = LoggerFactory.getLogger(CharaMcpService.class);

    private final Datastore datastore;

    public CharaMcpService(Datastore datastore) {
        this.datastore = datastore;
    }

    // --- Tool 1: Get character by name ---

    @Tool(description = "Look up a D&D character by name and return their full character sheet. "
            + "The name can be in Japanese (e.g. セルシウス) or English. "
            + "Returns all stats: abilities, combat, saving throws, skills, spellcasting, equipment, personality, and physical description.",
            resultConverter = PlainStringConverter.class)
    public String getCharacterByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Error: character name must not be empty.";
        }
        List<Entity> all = queryAllCharacters();
        List<Entity> candidates = new ArrayList<>();
        for (Entity e : all) {
            String charName = getStr(e, NAME);
            if (name.equals(charName)) return formatSheet(e);
            if (name.equalsIgnoreCase(charName)) candidates.add(e);
        }
        if (!candidates.isEmpty()) return formatSheet(candidates.get(0));
        return "Character not found: \"" + name + "\". Use listCharacters to see available names.";
    }

    // --- Tool 2: List all characters ---

    @Tool(description = "List all D&D characters with a brief summary (name, class, level, race, player). "
            + "Use this to discover available character names before calling getCharacterByName.",
            resultConverter = PlainStringConverter.class)
    public String listCharacters() {
        List<Entity> all = queryAllCharacters();
        if (all.isEmpty()) return "No characters found.";
        all.sort(Comparator.comparing(e -> getStr(e, NAME)));
        StringBuilder sb = new StringBuilder("Characters:\n");
        for (Entity e : all) {
            sb.append(String.format("  %-20s  %s Lv%d  %s  (Player: %s)%n",
                    getStr(e, NAME), getStr(e, KLASS), getInt(e, LEVEL),
                    getStr(e, RACE), getStr(e, PLAYER)));
        }
        return sb.toString();
    }

    // --- Tool 3: Search characters ---

    @Tool(description = "Search D&D characters by class, race, and/or level range. "
            + "All parameters are optional — omit or pass empty string / 0 to skip that filter. "
            + "Returns a summary list of matching characters.",
            resultConverter = PlainStringConverter.class)
    public String searchCharacters(String className, String race, int minLevel, int maxLevel) {
        List<Entity> all = queryAllCharacters();
        List<Entity> matched = new ArrayList<>();
        for (Entity e : all) {
            if (!isEmpty(className) && !getStr(e, KLASS).equalsIgnoreCase(className)) continue;
            if (!isEmpty(race) && !getStr(e, RACE).equalsIgnoreCase(race)) continue;
            int level = getInt(e, LEVEL);
            if (minLevel > 0 && level < minLevel) continue;
            if (maxLevel > 0 && level > maxLevel) continue;
            matched.add(e);
        }
        if (matched.isEmpty()) return "No characters matched the search criteria.";
        matched.sort(Comparator.comparing(e -> getStr(e, NAME)));
        StringBuilder sb = new StringBuilder("Matching characters:\n");
        for (Entity e : matched) {
            sb.append(String.format("  %-20s  %s Lv%d  %s  (Player: %s)%n",
                    getStr(e, NAME), getStr(e, KLASS), getInt(e, LEVEL),
                    getStr(e, RACE), getStr(e, PLAYER)));
        }
        return sb.toString();
    }

    // --- Tool 4: List campaign notes ---

    @Tool(description = "List all campaign notes with their campaign name and title. "
            + "Use this to discover note titles before calling getCampaignNoteByTitle.",
            resultConverter = PlainStringConverter.class)
    public String listCampaignNotes() {
        List<Entity> all = queryAllNotes();
        if (all.isEmpty()) return "No campaign notes found.";
        all.sort(Comparator.comparing((Entity e) -> getStr(e, CAMPAIGN_NAME))
                .thenComparing(e -> getStr(e, CAMPAIGN_NOTE_TITLE)));
        StringBuilder sb = new StringBuilder("Campaign notes:\n");
        for (Entity e : all) {
            sb.append(String.format("  [%s]  %s%n",
                    getStr(e, CAMPAIGN_NAME), getStr(e, CAMPAIGN_NOTE_TITLE)));
        }
        return sb.toString();
    }

    // --- Tool 5: Get campaign note by title ---

    @Tool(description = "Retrieve the full content of a campaign note by its title. "
            + "Use listCampaignNotes first to find available titles.",
            resultConverter = PlainStringConverter.class)
    public String getCampaignNoteByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "Error: title must not be empty.";
        }
        List<Entity> all = queryAllNotes();
        List<Entity> candidates = new ArrayList<>();
        for (Entity e : all) {
            String t = getStr(e, CAMPAIGN_NOTE_TITLE);
            if (title.equals(t)) return formatNote(e);
            if (title.equalsIgnoreCase(t)) candidates.add(e);
        }
        if (!candidates.isEmpty()) return formatNote(candidates.get(0));
        return "Campaign note not found: \"" + title + "\". Use listCampaignNotes to see available titles.";
    }

    // --- Datastore queries ---

    private List<Entity> queryAllCharacters() {
        QueryResults<Entity> results = datastore.run(
                Query.newEntityQueryBuilder().setKind("CharaData").setLimit(100).build());
        List<Entity> list = new ArrayList<>();
        while (results.hasNext()) {
            Entity e = results.next();
            if (!getBool(e, DELETED)) list.add(e);
        }
        return list;
    }

    private List<Entity> queryAllNotes() {
        QueryResults<Entity> results = datastore.run(
                Query.newEntityQueryBuilder().setKind("CampaignNote").setLimit(500).build());
        List<Entity> list = new ArrayList<>();
        while (results.hasNext()) {
            Entity e = results.next();
            if (!getBool(e, DELETED)) list.add(e);
        }
        return list;
    }

    // --- Formatters ---

    private String formatSheet(Entity e) {
        PlayerChara c = new PlayerChara();
        c.setName(getStr(e, NAME));
        c.setKlass(getStr(e, KLASS));
        c.setLevel(getInt(e, LEVEL));
        c.setPlayer(getStr(e, PLAYER));
        c.setRace(getStr(e, RACE));
        c.setAlignment(getStr(e, ALIGNMENT));
        c.setExperience(getInt(e, EXPERIENCE));
        c.setStrength(getInt(e, STR));
        c.setDexterity(getInt(e, DEX));
        c.setConstitution(getInt(e, CON));
        c.setIntelligence(getInt(e, INT));
        c.setWisdom(getInt(e, WIS));
        c.setCharisma(getInt(e, CHA));
        c.setStrength_save(getStr(e, STR + SAVE));
        c.setDexterity_save(getStr(e, DEX + SAVE));
        c.setConstitution_save(getStr(e, CON + SAVE));
        c.setIntelligence_save(getStr(e, INT + SAVE));
        c.setWisdom_save(getStr(e, WIS + SAVE));
        c.setCharisma_save(getStr(e, CHA + SAVE));
        c.setAcrobatics(getStr(e, ACROBATICS));
        c.setAnimal(getStr(e, ANIMAL));
        c.setArcana(getStr(e, ARCANA));
        c.setAthletics(getStr(e, ATHLETICS));
        c.setDeception(getStr(e, DECEPTION));
        c.setHistory(getStr(e, HISTORY));
        c.setIntimidation(getStr(e, INTIMIDATION));
        c.setInsight(getStr(e, INSIGHT));
        c.setInvestigation(getStr(e, INVESTIGATION));
        c.setMedicine(getStr(e, MEDICINE));
        c.setNature(getStr(e, NATURE));
        c.setPerformance(getStr(e, PERFORMANCE));
        c.setPerception(getStr(e, PERCEPTION));
        c.setPersuasion(getStr(e, PERSUASION));
        c.setReligion(getStr(e, RELIGION));
        c.setSleight(getStr(e, SLEIGHT));
        c.setStealth(getStr(e, STEALTH));
        c.setSurvival(getStr(e, SURVIVAL));
        c.setProficiency_language(getStr(e, PROFICIENCY_LANGUAGE));
        c.setAc(getInt(e, AC));
        c.setInitiative(getInt(e, INITIATIVE));
        c.setSpeed(getInt(e, SPEED));
        c.setHit_dice(getStr(e, HIT_DICE));
        c.setHit_point(getInt(e, HIT_POINT));
        c.setAttack_spellcasting(getStr(e, ATTACK_SPELLCASTING));
        c.setEquipment(getStr(e, EQUIPMENT));
        c.setPersonality_traits(getStr(e, PERSONALITY_TRAITS));
        c.setIdeals(getStr(e, IDEALS));
        c.setBonds(getStr(e, BONDS));
        c.setFlaws(getStr(e, FLAWS));
        c.setFeatures_traits(getStr(e, FEATURES_TRAITS));
        c.setAge(getStr(e, AGE));
        c.setHeight(getStr(e, HEIGHT));
        c.setWeight(getStr(e, WEIGHT));
        c.setEyes(getStr(e, EYES));
        c.setSkin(getStr(e, SKIN));
        c.setHair(getStr(e, HAIR));
        c.setSpellcasting_ability(getStr(e, SPELLCASTING_ABILITY));
        c.setSpellcasting_class(getStr(e, SPELLCASTING_CLASS));
        c.setSpell_save_dc_modifier(getInt(e, SPELL_SAVE_DC_MODIFIER));
        c.setSpell_attack_bonus_modifier(getInt(e, SPELL_ATTACK_BONUS_MODIFIER));

        StringBuilder sb = new StringBuilder();
        sb.append("=== CHARACTER SHEET ===\n");
        sb.append("Name: ").append(c.getName()).append("\n");
        sb.append("Class: ").append(c.getKlass()).append("  Level: ").append(c.getLevel()).append("\n");
        sb.append("Player: ").append(c.getPlayer()).append("\n");
        sb.append("Race: ").append(c.getRace()).append("  Alignment: ").append(c.getAlignment()).append("\n");
        sb.append("Experience: ").append(c.getExperience()).append("\n");

        sb.append("\n--- ABILITY SCORES ---\n");
        sb.append(String.format("STR: %d (%+d)  DEX: %d (%+d)  CON: %d (%+d)%n",
                c.getStrength(), c.getStrength_bonus(),
                c.getDexterity(), c.getDexterity_bonus(),
                c.getConstitution(), c.getConstitution_bonus()));
        sb.append(String.format("INT: %d (%+d)  WIS: %d (%+d)  CHA: %d (%+d)%n",
                c.getIntelligence(), c.getIntelligence_bonus(),
                c.getWisdom(), c.getWisdom_bonus(),
                c.getCharisma(), c.getCharisma_bonus()));

        sb.append("\n--- COMBAT ---\n");
        sb.append("AC: ").append(c.getAc())
          .append("  Initiative: ").append(fmt(c.getInitiative()))
          .append("  Speed: ").append(c.getSpeed()).append("ft\n");
        sb.append("HP: ").append(c.getHit_point())
          .append("  Hit Dice: ").append(c.getHit_dice()).append("\n");
        sb.append("Proficiency Bonus: ").append(fmt(c.getProficiency_bonus())).append("\n");
        sb.append("Passive Perception: ").append(c.getPassive_wisdom_perception()).append("\n");

        sb.append("\n--- SAVING THROWS ---\n");
        sb.append(String.format("STR: %+d  DEX: %+d  CON: %+d  INT: %+d  WIS: %+d  CHA: %+d%n",
                c.getStrength_save_bonus(), c.getDexterity_save_bonus(),
                c.getConstitution_save_bonus(), c.getIntelligence_save_bonus(),
                c.getWisdom_save_bonus(), c.getCharisma_save_bonus()));

        sb.append("\n--- SKILLS ---\n");
        skill(sb, "Acrobatics (DEX)", c.getAcrobatics_bonus());
        skill(sb, "Animal Handling (WIS)", c.getAnimal_bonus());
        skill(sb, "Arcana (INT)", c.getArcana_bonus());
        skill(sb, "Athletics (STR)", c.getAthletics_bonus());
        skill(sb, "Deception (CHA)", c.getDeception_bonus());
        skill(sb, "History (INT)", c.getHistory_bonus());
        skill(sb, "Insight (WIS)", c.getInsight_bonus());
        skill(sb, "Intimidation (CHA)", c.getIntimidation_bonus());
        skill(sb, "Investigation (INT)", c.getInvestigation_bonus());
        skill(sb, "Medicine (WIS)", c.getMedicine_bonus());
        skill(sb, "Nature (INT)", c.getNature_bonus());
        skill(sb, "Perception (WIS)", c.getPerception_bonus());
        skill(sb, "Performance (CHA)", c.getPerformance_bonus());
        skill(sb, "Persuasion (CHA)", c.getPersuasion_bonus());
        skill(sb, "Religion (INT)", c.getReligion_bonus());
        skill(sb, "Sleight of Hand (DEX)", c.getSleight_bonus());
        skill(sb, "Stealth (DEX)", c.getStealth_bonus());
        skill(sb, "Survival (WIS)", c.getSurvival_bonus());

        if (!c.getSpellcasting_class().isEmpty() || !c.getSpellcasting_ability().isEmpty()) {
            sb.append("\n--- SPELLCASTING ---\n");
            sb.append("Class: ").append(c.getSpellcasting_class()).append("\n");
            sb.append("Ability: ").append(c.getSpellcasting_ability()).append("\n");
            sb.append("Spell Save DC: ").append(c.getSpell_save_dc()).append("\n");
            sb.append("Spell Attack Bonus: ").append(fmt(c.getSpell_attack_bonus())).append("\n");
        }
        if (!c.getAttack_spellcasting().isEmpty())
            sb.append("\n--- ATTACKS ---\n").append(c.getAttack_spellcasting()).append("\n");
        if (!c.getEquipment().isEmpty())
            sb.append("\n--- EQUIPMENT ---\n").append(c.getEquipment()).append("\n");
        if (!c.getPersonality_traits().isEmpty() || !c.getIdeals().isEmpty()
                || !c.getBonds().isEmpty() || !c.getFlaws().isEmpty()) {
            sb.append("\n--- PERSONALITY ---\n");
            if (!c.getPersonality_traits().isEmpty()) sb.append("Traits: ").append(c.getPersonality_traits()).append("\n");
            if (!c.getIdeals().isEmpty()) sb.append("Ideals: ").append(c.getIdeals()).append("\n");
            if (!c.getBonds().isEmpty()) sb.append("Bonds: ").append(c.getBonds()).append("\n");
            if (!c.getFlaws().isEmpty()) sb.append("Flaws: ").append(c.getFlaws()).append("\n");
        }
        if (!c.getAge().isEmpty() || !c.getHeight().isEmpty() || !c.getWeight().isEmpty()) {
            sb.append("\n--- PHYSICAL ---\n");
            sb.append("Age: ").append(c.getAge()).append("  Height: ").append(c.getHeight())
              .append("  Weight: ").append(c.getWeight()).append("\n");
            sb.append("Eyes: ").append(c.getEyes()).append("  Skin: ").append(c.getSkin())
              .append("  Hair: ").append(c.getHair()).append("\n");
        }
        if (!c.getFeatures_traits().isEmpty())
            sb.append("\n--- FEATURES & TRAITS ---\n").append(c.getFeatures_traits()).append("\n");
        if (!c.getProficiency_language().isEmpty())
            sb.append("\n--- PROFICIENCIES & LANGUAGES ---\n").append(c.getProficiency_language()).append("\n");
        return sb.toString();
    }

    private String formatNote(Entity e) {
        return "Campaign: " + getStr(e, CAMPAIGN_NAME) + "\n"
                + "Title: " + getStr(e, CAMPAIGN_NOTE_TITLE) + "\n\n"
                + getStr(e, CAMPAIGN_NOTE);
    }

    // --- Helpers ---

    private void skill(StringBuilder sb, String label, int bonus) {
        sb.append(String.format("  %-28s %+d%n", label + ":", bonus));
    }

    private String fmt(int v) { return v >= 0 ? "+" + v : String.valueOf(v); }

    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }

    private String getStr(Entity entity, String prop) {
        String blobProp = prop + "_BLOB";
        if (entity.contains(blobProp)) {
            try {
                Blob b = entity.getBlob(blobProp);
                if (b != null) {
                    String v = new String(b.toByteArray(), StandardCharsets.UTF_8);
                    if (!v.isEmpty()) return v;
                }
            } catch (Exception ex) {
                log.warn("Failed to read BLOB {}: {}", blobProp, ex.getMessage());
            }
        }
        if (!entity.contains(prop)) return "";
        try { String v = entity.getString(prop); return v != null ? v : ""; }
        catch (Exception ex) { return ""; }
    }

    private Integer getInt(Entity entity, String prop) {
        if (!entity.contains(prop)) return 0;
        try { Long v = entity.getLong(prop); return v != null ? v.intValue() : 0; }
        catch (Exception ex) { return 0; }
    }

    private Boolean getBool(Entity entity, String prop) {
        if (!entity.contains(prop)) return false;
        try { Boolean v = entity.getBoolean(prop); return v != null ? v : false; }
        catch (Exception ex) { return false; }
    }
}
