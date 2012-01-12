package br.com.ricardofreitasjunior.android.gasosa;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ZoomControls;

import br.com.ricardofreitasjunior.android.gasosa.db.DataHelper;
import br.com.ricardofreitasjunior.android.gasosa.db.Posto;
import br.com.ricardofreitasjunior.android.gasosa.gps.ImagemOverlay;
import br.com.ricardofreitasjunior.android.gasosa.gps.Ponto;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Principal extends MapActivity implements LocationListener {

	private MapView mapa;
	private MapController controlador;
	protected static final String LOG_TAG = "Mensagem";

	private Posto novoposto = new Posto();

	private static DataHelper repositorio;
	private List<Posto> postos;
	// private Posicao posicao = new Posicao();
//	public String endereco;

	// private ImagemOverlay imagem;
	private GeoPoint ponto;
	private Location loc;
	private MyLocationOverlay usuario;
	private ImagemOverlay gasolina;
	private ImagemOverlay etanol;
	private ImagemOverlay semavaliacao;
	private ImagemOverlay user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ConfiguraMapa();

		PosicaoUsuario();

		PegaDadosPosicao();

		ListaProximos();
		
//		usuario = new MyLocationOverlay(this, mapa);
//		mapa.getOverlays().add(usuario);
	}

	private void ConfiguraMapa() {
		mapa = (MapView) findViewById(R.id.mapa);
		mapa.setClickable(true);
		controlador = mapa.getController();

		controlador.setZoom(20);
		ZoomControls zoom = (ZoomControls) mapa.getZoomControls();
		zoom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		zoom.setGravity(Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);
		mapa.addView(zoom);
		mapa.displayZoomControls(true);
		
	}

	private void PosicaoUsuario() {
		loc = getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (loc != null) {
			ponto = new Ponto(loc);
			Log.i(LOG_TAG, "Localização: " + loc.getLatitude() + ", " + loc.getLongitude());
			controlador.setCenter(ponto);
		}

		 
		// usuario.onTap(ponto, mapa);

		getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

	}

	private void PegaDadosPosicao() {

		Geocoder gc = new Geocoder(this, Locale.getDefault());

		try {
			List<Address> addresses = gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

			if (addresses.size() > 0) {
				Address address = addresses.get(0);

				// endereco = address.getAddressLine(0);

				novoposto.endereco = address.getAddressLine(0).substring(0, address.getAddressLine(0).lastIndexOf("-"));
				novoposto.bairro = address.getAddressLine(0).substring(address.getAddressLine(0).lastIndexOf("-") + 1);
				novoposto.cidade = address.getLocality().toString();
				novoposto.uf = address.getAdminArea().toString();
				novoposto.latitude = String.valueOf(loc.getLatitude());
				novoposto.longitude = String.valueOf(loc.getLongitude());
				novoposto.avaliacao = "U";

				Log.i(LOG_TAG, "Endereço: " + novoposto.endereco);
			}
		} catch (Exception e) {
			Log.i(LOG_TAG,
					"Não foi possível encontrar dados da localização do usuário." + e);
		}
	}

	private void ListaProximos() {
		if (loc != null) {
			postos = repositorio.getDataHelper(this).listarPostosProximos(novoposto);

			AdicionaPonto();

			AvaliaPontos();
		}
	}

	private void AdicionaPonto() {
		postos.add(novoposto);
	}

	private void AvaliaPontos(){
		
		for (int i = 0; i < postos.size(); i++){
//			Log.i(LOG_TAG, "AVALIACAO: " + i + " - " + postos.get(i).avaliacao);
			ponto = new Ponto(Double.parseDouble(postos.get(i).latitude), Double.parseDouble(postos.get(i).longitude));

			if (postos.get(i).avaliacao.equals("G")){
				gasolina = new ImagemOverlay(postos.get(i), R.drawable.gasolina, this);
				mapa.getOverlays().add(gasolina);
				mapa.getController().setCenter(ponto);
//				MostraNoMapa(gasolina);
				
			} else if(postos.get(i).avaliacao.equals("E")){
				etanol = new ImagemOverlay(postos.get(i), R.drawable.etanol, this);
				mapa.getOverlays().add(etanol);
//				MostraNoMapa(etanol);
				
			} else if(postos.get(i).avaliacao.equals("U")){
				user = new ImagemOverlay(postos.get(i), R.drawable.usuario, this);
				mapa.getOverlays().add(user);
//				MostraNoMapa(user);
//				usuario = new MyLocationOverlay(this, mapa);
//				mapa.getOverlays().add(usuario);
//				mapa.getController().setCenter(ponto);
				
			} else {
				semavaliacao = new ImagemOverlay(postos.get(i), R.drawable.semavaliacao, this);
				mapa.getOverlays().add(semavaliacao);
				
//				MostraNoMapa(semavaliacao);
			}
			
		}
	}

	private void MostraNoMapa(ImagemOverlay imagem) {
		mapa.getOverlays().add(imagem);
	}

	private void InfoPonto() {

	}

	private void ManipulaPonto() {

	}

	// //////////////////////////////////////////////////////////////////////////////////////////

	private LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(LOG_TAG, "Posição atualizada: " + location.getLatitude() + ", " + location.getLongitude());
		GeoPoint geoPoint = new Ponto(location);
		controlador.animateTo(geoPoint);
		mapa.invalidate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getLocationManager().removeUpdates(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		usuario.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		usuario.enableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
