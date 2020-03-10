package com.pedroribeiro.intellicity.entities;

public class Nota {
    public String titulo;
    public String descricao;
    public String data;
    public String localizacao;

    public Nota(String titulo, String descricao, String data, String localizacao){
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getData() {
        return data;
    }

    public String getLocalizacao() {
        return localizacao;
    }
}
