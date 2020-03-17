package com.pedroribeiro.intellicity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "report.db";

    public DB(Context context){
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contrato.Utilizador.SQL_CREATE_ENTRIES);
        db.execSQL(Contrato.Notas.SQL_CREATE_ENTRIES);

        db.execSQL("insert into " + Contrato.Utilizador.TABLE_NAME + " values(1,'Pedro','pedro','pedro','29.03.1998','Alvaraes');");
        db.execSQL("insert into " + Contrato.Utilizador.TABLE_NAME + " values(2,'Raul','raul','raul','17.07.1993','Alvaraes');");

        db.execSQL("insert into " + Contrato.Notas.TABLE_NAME + " values(1,'Buraco no chão','buraco grande no estrada nacional','10.03.2020','Barroselas',1);");
        db.execSQL("insert into " + Contrato.Notas.TABLE_NAME + " values(2,'Piso irregular','Piso gasto e o carro escorrega','02.03.2020','Chafe',2);");
        db.execSQL("insert into " + Contrato.Notas.TABLE_NAME + " values(3,'Parti uma jante','buraco enorme, impossível desviar e estraguei o carro','10.02.2020','Vila de Punhe',1);");
        db.execSQL("insert into " + Contrato.Notas.TABLE_NAME + " values(4,'Estrada sem condições','Caminho em calceta romana, já devia ter levado alcatrão','20.02.2020','Alvaraes',2);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contrato.Utilizador.SQL_DROP_ENTRIES);
        db.execSQL(Contrato.Notas.SQL_DROP_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
