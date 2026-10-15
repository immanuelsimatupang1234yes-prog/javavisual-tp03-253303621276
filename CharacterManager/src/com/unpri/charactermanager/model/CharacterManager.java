package com.unpri.charactermanager.model;

import java.util.ArrayList;

/**
 * Mengelola koleksi Character: tambah, cari, hapus, dan statistik dasar.
 * Menerapkan Composition: CharacterManager "has-a" ArrayList<Character>.
 */
public class CharacterManager {
    private ArrayList<Character> characterList;

    public CharacterManager() {
        this.characterList = new ArrayList<>();
    }

    /**
     * Menambahkan karakter baru ke dalam list.
     * Menolak jika sudah ada karakter dengan nama yang sama.
     * @return true jika berhasil ditambahkan, false jika ditolak karena duplikat
     */
    public boolean addCharacter(Character character) {
        if (findByName(character.getName()) != null) {
            System.out.println("❌ Gagal: karakter dengan nama '" + character.getName() + "' sudah ada!");
            return false;
        }
        characterList.add(character);
        System.out.println("✅ Karakter '" + character.getName() + "' berhasil ditambahkan.");
        return true;
    }

    /**
     * Mencari karakter berdasarkan nama.
     * @return object Character jika ditemukan, null jika tidak ada
     */
    public Character findByName(String name) {
        for (Character c : characterList) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Menghapus karakter dari list berdasarkan nama.
     * @return true jika berhasil dihapus, false jika tidak ditemukan
     */
    public boolean removeCharacter(String name) {
        Character target = findByName(name);
        if (target == null) {
            System.out.println("❌ Gagal hapus: karakter '" + name + "' tidak ditemukan.");
            return false;
        }
        characterList.remove(target);
        System.out.println("🗑️ Karakter '" + name + "' berhasil dihapus.");
        return true;
    }

    public int getTotalCharacters() {
        return characterList.size();
    }

    /**
     * Mengembalikan SALINAN list, bukan referensi asli,
     * agar pemanggil tidak bisa mengubah data internal manager secara langsung.
     */
    public ArrayList<Character> getAllCharacters() {
        return new ArrayList<>(characterList);
    }
}