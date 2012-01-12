package br.com.ricardofreitasjunior.android.gasosa.db;

import java.io.Serializable;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Posto implements Serializable {

	public static String[] colunas = new String[] { Postos._ID, Postos.NOME,
			Postos.ENDERECO, Postos.BAIRRO, Postos.CIDADE, Postos.UF,
			Postos.DATA, Postos.LATITUDE, Postos.LONGITUDE, Postos.VLGASOLINA,
			Postos.VLETANOL, Postos.AVALIACAO };

	public static final String AUTHORITY = "com.android.mapa.provider.posto";

	public long id;
	public String nome;
	public String endereco;
	public String bairro;
	public String cidade;
	public String uf;
	public String data;
	public String latitude;
	public String longitude;
	public String vlgas;
	public String vleta;
	public String avaliacao;
	public boolean selecionado;

	public Posto() {
	}

	protected Posto(long id, String nome, String endereco, String bairro,
			String cidade, String uf, String data, String latitude,
			String longitude, String vlGas, String vlEta, String avaliacao) {
		super();
		this.id = id;
		this.nome = nome;
		this.endereco = endereco;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
		this.data = data;
		this.latitude = latitude;
		this.longitude = longitude;
		this.vlgas = vlGas;
		this.vleta = vlEta;
		this.avaliacao = avaliacao;

	}

	protected Posto(String nome, String endereco, String bairro, String cidade,
			String uf, String data, String latitude, String longitude,
			String vlGas, String vlEta, String avaliacao) {
		super();
		this.nome = nome;
		this.endereco = endereco;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
		this.data = data;
		this.latitude = latitude;
		this.longitude = longitude;
		this.vlgas = vlGas;
		this.vleta = vlEta;
		this.avaliacao = avaliacao;

	}

	public static final class Postos implements BaseColumns {
		private Postos() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/postos");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.postos";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.postos";
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		public static final String NOME = "nome";
		public static final String ENDERECO = "endereco";
		public static final String BAIRRO = "bairro";
		public static final String CIDADE = "cidade";
		public static final String UF = "uf";
		public static final String DATA = "data";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String VLGASOLINA = "vlgas";
		public static final String VLETANOL = "vleta";
		public static final String AVALIACAO = "avaliacao";

		public static Uri getUriID(long id) {
			Uri uriPosto = ContentUris.withAppendedId(CONTENT_URI, id);
			return uriPosto;
		}
	}

	@Override
	public String toString() {
		return "Nome: " + nome + ", Endereço: " + endereco + ", Bairro: "
				+ bairro + ", Cidade: " + cidade + ", UF: " + uf
				+ ", Atualizado em: " + data + ", Gasolina: " + vlgas
				+ ", Etanol: " + vleta+ ", Avaliacao: " + avaliacao;

	}

}
