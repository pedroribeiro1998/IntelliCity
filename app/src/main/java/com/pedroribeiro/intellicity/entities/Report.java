package com.pedroribeiro.intellicity.entities;

import java.io.Serializable;
import java.util.Date;

public class Report implements Serializable {
    public int id;
    public String nome;
    public int utilizador_id;
    public String titulo;
    public String descricao;
    public String data;
    public String localizacao;
    public String fotografia;
    public String latitude;
    public String longitude;

    public Report(int id, String nome, int utilizador_id, String titulo, String descricao, String data, String localizacao, String fotografia, String latitude, String longitude){
        this.id = id;
        this.nome = nome;
        this.utilizador_id = utilizador_id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.fotografia = fotografia;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public int getUtilizador_id() {
        return utilizador_id;
    }

    public void setUtilizador_id(int utilizador_id) {
        this.utilizador_id = utilizador_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

