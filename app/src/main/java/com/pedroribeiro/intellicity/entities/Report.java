package com.pedroribeiro.intellicity.entities;

import java.io.Serializable;
import java.util.Date;

public class Report implements Serializable {
    public int id;
    public int utilizador_id;
    public String titulo;
    public String descricao;
    public String data;
    public String localizacao;
    public String fotografia;
    public double latitude;
    public double longitude;

    public Report(int id, int utilizador_id, String titulo, String descricao, String data, String localizacao, String fotografia, double latitude, double longitude){
        this.id = id;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

