package com.example.ld.model;

public class User {

    int id;

    String name, email, level, web, kelas_id;

    Kelas kelas;

    public User() {
    }

    public User(int id, String name, String email, String level, String web, String kelas_id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
        this.web = web;
        this.kelas_id = kelas_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getKelas_id() {
        return kelas_id;
    }

    public void setKelas_id(String kelas_id) {
        this.kelas_id = kelas_id;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public class Kelas {
        int id;
        String kelas;

        public Kelas() {
        }

        public Kelas(int id, String kelas) {
            this.id = id;
            this.kelas = kelas;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKelas() {
            return kelas;
        }

        public void setKelas(String kelas) {
            this.kelas = kelas;
        }
    }
}
