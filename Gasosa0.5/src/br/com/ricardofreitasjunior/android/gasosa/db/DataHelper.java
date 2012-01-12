package br.com.ricardofreitasjunior.android.gasosa.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.util.Log;
import br.com.ricardofreitasjunior.android.gasosa.db.Posto.Postos;

public class DataHelper {

	public static final String TABLE_NAME = "posto";
	public static final int DATABASE_VERSION = 1;
	public static final String CREATE_SQL;
	public static final String DROP_SQL;
	static final String DATABASE_NAME = "GasosaDB.db";
	static final String DATABASE_TEST_NAME = "GasosaDB-test.db";
	
	private int RAIO_PROXIMIDADE = 3750;
	public Location localizacao;

	private Context context;
	public static SQLiteDatabase db;
	private static boolean testing;
	private static DataHelper dataHelperInstance;
	public static final String LOG_TAG = "Mensagem";

	static {
		StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sql.append(DataHelper.TABLE_NAME);
		sql.append(" (");
		sql.append("_id integer primary key autoincrement, ");
		sql.append("nome text, ");
		sql.append("endereco text, ");
		sql.append("bairro text, ");
		sql.append("cidade text, ");
		sql.append("uf text, ");
		sql.append("data text, ");
		sql.append("latitude text, ");
		sql.append("longitude text, ");
		sql.append("vlgas text not null, ");
		sql.append("vleta text not null, ");
		sql.append("avaliacao text");
		sql.append(")");

		CREATE_SQL = String.format(sql.toString(), TABLE_NAME);
		DROP_SQL = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
	}

	public static String getDatabaseName() {
		if (testing) {
			return DATABASE_TEST_NAME;
		} else {
			return DATABASE_NAME;
		}
	}

	public static DataHelper getDataHelper(Context context) {
		if (dataHelperInstance == null) {
			dataHelperInstance = new DataHelper(context);
		}
		dataHelperInstance.context = context;
		return dataHelperInstance;
	}

	private DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();

	}

	public Cursor getCursor() {
		try {
			return db.query(TABLE_NAME, Posto.colunas, null, null, null, null, null, null);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Erro ao buscar postos: " + e.toString());
			return null;
		}
	}

	public List<Posto> listarPostos() {
		Cursor c = getCursor();
		List<Posto> postos = new ArrayList<Posto>();
		
		if (c.moveToFirst()) {
			do {
				Posto posto = new Posto();

				posto.id = c.getLong(c.getColumnIndex(Postos._ID));
				posto.nome = c.getString(c.getColumnIndex(Postos.NOME));
				posto.endereco = c.getString(c.getColumnIndex(Postos.ENDERECO));
				posto.bairro = c.getString(c.getColumnIndex(Postos.BAIRRO));
				posto.cidade = c.getString(c.getColumnIndex(Postos.CIDADE));
				posto.uf = c.getString(c.getColumnIndex(Postos.UF));
				posto.data = c.getString(c.getColumnIndex(Postos.DATA));
				posto.latitude = c.getString(c.getColumnIndex(Postos.LATITUDE));
				posto.longitude = c.getString(c.getColumnIndex(Postos.LONGITUDE));
				posto.vlgas = c.getString(c.getColumnIndex(Postos.VLGASOLINA));
				posto.vleta = c.getString(c.getColumnIndex(Postos.VLETANOL));

				postos.add(posto);

			} while (c.moveToNext());
		}
		return postos;
	}
	
	public List<Posto> listarPostosProximos(Posto usuario) {
		Cursor c = getCursor();
		List<Posto> postos = new ArrayList<Posto>();
		
		if (c.moveToFirst()) {
			do {
				Posto posto = new Posto();

				posto.id = c.getLong(c.getColumnIndex(Postos._ID));
				posto.nome = c.getString(c.getColumnIndex(Postos.NOME));
				posto.endereco = c.getString(c.getColumnIndex(Postos.ENDERECO));
				posto.bairro = c.getString(c.getColumnIndex(Postos.BAIRRO));
				posto.cidade = c.getString(c.getColumnIndex(Postos.CIDADE));
				posto.uf = c.getString(c.getColumnIndex(Postos.UF));
				posto.data = c.getString(c.getColumnIndex(Postos.DATA));
				posto.latitude = c.getString(c.getColumnIndex(Postos.LATITUDE));
				posto.longitude = c.getString(c.getColumnIndex(Postos.LONGITUDE));
				posto.vlgas = c.getString(c.getColumnIndex(Postos.VLGASOLINA));
				posto.vleta = c.getString(c.getColumnIndex(Postos.VLETANOL));
				posto.vleta = c.getString(c.getColumnIndex(Postos.VLETANOL));
				posto.avaliacao = c.getString(c.getColumnIndex(Postos.AVALIACAO));

				double latP = Double.parseDouble(posto.latitude.toString());
				double lonP = Double.parseDouble(posto.longitude.toString());

				Location locPosto = new Location("reverseGeocoded");
				locPosto.setLatitude(latP); 
				locPosto.setLongitude(lonP); 
				
				double latU = Double.parseDouble(usuario.latitude);
				double lonU = Double.parseDouble(usuario.longitude);
				
				Location locUsuario = new Location("reverseGeocoded");
				locUsuario.setLatitude(latU);
				locUsuario.setLongitude(lonU);
				
//				locUsuario.setLatitude(ponto.getLatitudeE6() / 1E6); 
//				aLocation.setLongitude(ponto.getLongitudeE6() / 1E6); 
//				aLocation.set(aLocation); // Don't think I need this
//				bLocation.set(bLocation); // Don't think I need this either

				int distancia = (int) locUsuario.distanceTo(locPosto);

//				pegaConfiguracao();
//				
//				if (Raio != ""){
//					RAIO_PROXIMIDADE = Integer.valueOf(Raio)*1000;	
//				}
				
				if (distancia <= RAIO_PROXIMIDADE) {
					postos.add(posto);
				}

			} while (c.moveToNext());
		}
		return postos;
	}

	public int Atualizar(Posto posto) {
		ContentValues dados = new ContentValues();
		dados.put(Postos._ID, posto.id);
//		dados.put(Postos.NOME, posto.nome);		
//		dados.put(Postos.ENDERECO, posto.endereco);
//		dados.put(Postos.BAIRRO, posto.bairro);
//		dados.put(Postos.CIDADE, posto.cidade);
//		dados.put(Postos.UF, posto.uf);
//		dados.put(Postos.DATA, posto.data);
//		dados.put(Postos.LATITUDE, posto.latitude);
//		dados.put(Postos.LONGITUDE, posto.longitude);
		dados.put(Postos.VLGASOLINA, posto.vlgas);
		dados.put(Postos.VLETANOL, posto.vleta);
//		dados.put(Postos.VLDIESEL, posto.vldie);
//		dados.put(Postos.VLGNV, posto.vlgnv);
		Log.i(LOG_TAG, "Dados: id = " +posto.id + ", gas= " +posto.vlgas + ", alc = " +posto.vleta);
//		db.beginTransaction();
//		db.execSQL("UPDATE " + TABLE_NAME + " SET vlgas = '" + posto.vlgas + "', vlalc = '" + posto.vleta + "' WHERE _id = " + posto.id , null);
//		db.setTransactionSuccessful();
//		db.endTransaction();
		String _id = String.valueOf(posto.id);
		String where = Postos._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = Alterar(dados, where, whereArgs);
		return count;
//		return 1;
	}

	public int Alterar(ContentValues dados, String where, String[] whereArgs) {
		int count = db.update(TABLE_NAME, dados, where, whereArgs);
		Log.i(LOG_TAG, "Atualizou [" + count + "] registros.");
		return count;
	}
	
	public long Cadastrar(Posto posto) {
		ContentValues dados = new ContentValues();
		dados.put(Postos.NOME, posto.nome);
		dados.put(Postos.ENDERECO, posto.endereco);
		dados.put(Postos.BAIRRO, posto.bairro);
		dados.put(Postos.CIDADE, posto.cidade);
		dados.put(Postos.UF, posto.uf);
		dados.put(Postos.DATA, posto.data);
		dados.put(Postos.LATITUDE, posto.latitude);
		dados.put(Postos.LONGITUDE, posto.longitude);
		dados.put(Postos.VLGASOLINA, posto.vlgas);
		dados.put(Postos.VLETANOL, posto.vleta);
//		dados.put(Postos.VLDIESEL, posto.vldie);
//		dados.put(Postos.VLGNV, posto.vlgnv);

		long id = Inserir(dados);

		return id;
	}

	public long Inserir(ContentValues dados) {
		long id = db.insert(TABLE_NAME, "", dados);
		return id;
	}
	
	public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		Cursor c = queryBuilder.query(this.db, projection, selection, selectionArgs, groupBy, having, orderBy);
		return c;
	}

	public void Fechar() {
		 if(db != null){
		 db.close();
		 }
	}

}
