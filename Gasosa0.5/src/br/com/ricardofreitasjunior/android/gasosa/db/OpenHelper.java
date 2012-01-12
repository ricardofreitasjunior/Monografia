package br.com.ricardofreitasjunior.android.gasosa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenHelper extends SQLiteOpenHelper{

	public OpenHelper(Context context) {
		super(context, DataHelper.getDatabaseName(), null, DataHelper.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(DataHelper.LOG_TAG, "Creating database");
		db.execSQL(DataHelper.DROP_SQL);
		db.execSQL(DataHelper.CREATE_SQL);
		db.setVersion(DataHelper.DATABASE_VERSION);
		populateDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(DataHelper.LOG_TAG, "Upgrading database, this will drop tables and recreate.");
		db.execSQL(DataHelper.DROP_SQL);

		Log.i(DataHelper.LOG_TAG, "Creating database");
		db.execSQL(DataHelper.CREATE_SQL);
		db.setVersion(newVersion);
		populateDatabase(db);
	}

	private void populateDatabase(SQLiteDatabase db) {
		db.beginTransaction();
		
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 1' ,'Rua 20','Centro'			,'Campo Grande','MS','15/06/2011', '-20.4579', '-54.6090', '2.333','1.989', 'G')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 2' ,'Rua 19','Carandá Bosque I'	,'Campo Grande','MS','15/06/2011', '-20.4578', '-54.6091', '2.334','1.988', 'E')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 3' ,'Rua 18','Jardim dos Estados','Campo Grande','MS','15/06/2011', '-20.4577', '-54.6092', '2.335','1.987', 'G')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 4' ,'Rua 17','Monte Castelo'	  	,'Campo Grande','MS','15/06/2011', '-20.4576', '-54.6093', '2.336','1.986', 'E')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 5' ,'Rua 16','Centro'			,'Campo Grande','MS','15/06/2011', '-20.4575', '-54.6094', '2.337','1.985', 'G')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 6' ,'Rua 15','Jardim Petropolis' ,'Campo Grande','MS','15/06/2011', '-20.4574', '-54.6095', '2.338','1.984', 'E')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 7' ,'Rua 14','Centro'			,'Campo Grande','MS','15/06/2011', '-20.4573', '-54.6096', '2.339','1.983', 'G')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 8' ,'Rua 13','Carandá'			,'Campo Grande','MS','15/06/2011', '-20.4572', '-54.6097', '2.340','1.982', 'E')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 9' ,'Rua 12','Centro'			,'Campo Grande','MS','15/06/2011', '-20.4571', '-54.6098', '2.341','1.981', 'G')");
		db.execSQL("INSERT INTO posto (nome, endereco, bairro, cidade, uf, data, latitude, longitude, vlgas, vleta, avaliacao) VALUES ('Posto 10','Rua 11','Centro'			,'Campo Grande','MS','15/06/2011', '-20.4570', '-54.6099', '2.342','1.980', 'E')");
		
		db.setTransactionSuccessful();
		db.endTransaction();
	}

}
