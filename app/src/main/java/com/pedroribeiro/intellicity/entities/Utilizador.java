package com.pedroribeiro.intellicity.entities;

import java.io.Serializable;

public class Utilizador implements Serializable  {
    public int id;
    public String nome;
    public String username;
    public String password;
    public String data_nasc;
    public String morada;

    public Utilizador(int id, String nome, String username, String password, String data_nasc, String morada) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.password = password;
        this.data_nasc = data_nasc;
        this.morada = morada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }
}

