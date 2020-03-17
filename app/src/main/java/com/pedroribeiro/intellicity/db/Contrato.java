package com.pedroribeiro.intellicity.db;

import android.provider.BaseColumns;

public class Contrato {
    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";

    public Contrato(){

    }

    public static abstract class Utilizador implements BaseColumns {
        public static final String TABLE_NAME = "utilizador";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_DTNASC = "dtnasc";
        public static final String COLUMN_MORADA = "morada";


        public static final String[] PROJECTION = {
                Utilizador._ID,
                Utilizador.COLUMN_NOME,
                Utilizador.COLUMN_USERNAME,
                Utilizador.COLUMN_PASSWORD,
                Utilizador.COLUMN_DTNASC,
                Utilizador.COLUMN_MORADA
        };

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Utilizador.TABLE_NAME + "("  +
                        Utilizador._ID             + INT_TYPE  + "PRIMARY KEY," +
                        Utilizador.COLUMN_NOME     + TEXT_TYPE + ","            +
                        Utilizador.COLUMN_USERNAME + TEXT_TYPE + ","            +
                        Utilizador.COLUMN_PASSWORD + TEXT_TYPE + ","            +
                        Utilizador.COLUMN_DTNASC   + TEXT_TYPE + ","            +
                        Utilizador.COLUMN_MORADA   + TEXT_TYPE + ");" ;

        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + Utilizador.TABLE_NAME +";";
    }

    public static abstract class Notas implements BaseColumns {
        public static final String TABLE_NAME = "notas";
        public static final String COLUMN_ID_UTILIZADOR = "id_utilizador";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_LOCALIZACAO = "localizacao";


        public static final String[] PROJECTION = {
                Notas._ID,
                Notas.COLUMN_ID_UTILIZADOR,
                Notas.COLUMN_TITULO,
                Notas.COLUMN_DESCRICAO,
                Notas.COLUMN_DATA,
                Notas.COLUMN_LOCALIZACAO
        };

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Notas.TABLE_NAME + "("  +
                        Notas._ID                  + INT_TYPE  + "PRIMARY KEY," +
                        Notas.COLUMN_TITULO        + TEXT_TYPE + ","            +
                        Notas.COLUMN_DESCRICAO     + TEXT_TYPE + ","            +
                        Notas.COLUMN_DATA          + TEXT_TYPE + ","            +
                        Notas.COLUMN_LOCALIZACAO   + TEXT_TYPE + ","            +
                        Notas.COLUMN_ID_UTILIZADOR + INT_TYPE  + " REFERENCES " +
                        Utilizador.TABLE_NAME + "(" + Utilizador._ID + "));" ;

        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + Notas.TABLE_NAME +";";
    }

}