package br.com.ricardofreitasjunior.android.gasosa.gps;

import android.location.Location;
import com.google.android.maps.GeoPoint;

public class Ponto extends GeoPoint{
	
	//valores em graus
	public Ponto(int latitudeE6, int longitudeE6) {
		super(latitudeE6, longitudeE6);
	}
	
	//converte valores para graus
	public Ponto(double latitude, double longitude){
		this((int)(latitude * 1E6), (int)(longitude * 1E6));
	}
	
	//recebe valores direto do object 'Location'
	public Ponto(Location location){
		this(location.getLatitude(), location.getLongitude());
	}
}