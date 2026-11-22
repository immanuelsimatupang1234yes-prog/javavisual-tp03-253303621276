package com.unpri.charactermanager.model;

public class Character {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 99;
    public static final int MAX_MAGE_LEVEL = 60;

    private int id;
    private String name;
    private CharacterType type;
    private int level;
    private int hitPoints;
    private int attackPower;
    private int mana;

    // STATIC: dimiliki bersama oleh semua object Character
    private static int totalCharacterCreated = 0;

    // Constructor 1: karakter BARU (statistik dihitung otomatis, id dari counter)
    public Character(String name, CharacterType type, int level) {
        validate(name, type, level);   // menolak input tidak sah SEBELUM objek terbentuk
        this.name = name;
        this.type = type;
        this.level = level;
        this.hitPoints = calculateBaseHP();
        this.attackPower = calculateBaseAttack();
        totalCharacterCreated++;
        this.id = totalCharacterCreated;
    }

    // Constructor 2: MEMUAT karakter dari database (id dan statistik sudah ada)
    public Character(int id, String name, CharacterType type, int level, int hitPoints, int attackPower) {
        validate(name, type, level);
        this.id = id;
        this.name = name;
        this.type = type;
        this.level = level;
        this.hitPoints = hitPoints;
        this.attackPower = attackPower;
    }

    /** Aturan data di Model: berlaku untuk siapa pun yang membuat Character (form, file, kode lain). */
    private static void validate(String name, CharacterType type, int level) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nama wajib diisi");
        }
        if (type == null) {
            throw new IllegalArgumentException("Role wajib dipilih");
        }
        if (level < MIN_LEVEL || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Level harus " + MIN_LEVEL + "-" + MAX_LEVEL);
        }
        if (type == CharacterType.MAGE && level > MAX_MAGE_LEVEL) {
    throw new IllegalArgumentException("MAGE tidak boleh di atas level " + MAX_MAGE_LEVEL);
        }
    }

    private int calculateBaseHP() {
        return 100 + (level * 10);
    }

    private int calculateBaseAttack() {
        return 10 + (level * 5);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public CharacterType getType() { return type; }
    public int getLevel() { return level; }
    public int getHitPoints() { return hitPoints; }
    public int getAttackPower() { return attackPower; }
    public int getMana() { return mana; }

    public void setId(int id) { this.id = id; }

    public void setLevel(int level) {
        validate(this.name, this.type, level);   // aturan yang sama dengan konstruktor
        this.level = level;
        this.hitPoints = calculateBaseHP();
        this.attackPower = calculateBaseAttack();
    }

    public void setMana(int mana) {
        if (mana < 0) {
            throw new IllegalArgumentException("Mana tidak boleh negatif");
        }
        this.mana = mana;
    }

    public boolean isAlive() {
        return hitPoints > 0;
    }

    public static int getTotalCharacterCreated() {
        return totalCharacterCreated;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Character)) return false;
        Character other = (Character) obj;
        return this.name.equals(other.name);   // dianggap sama jika namanya identik
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Character{id=" + id +
               ", name='" + name + '\'' +
               ", type=" + type +
               ", level=" + level +
               ", hp=" + hitPoints +
               ", atk=" + attackPower +
               ", mana=" + mana + '}';
    }
}