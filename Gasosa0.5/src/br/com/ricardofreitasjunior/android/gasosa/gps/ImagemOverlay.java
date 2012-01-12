package br.com.ricardofreitasjunior.android.gasosa.gps;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ricardofreitasjunior.android.gasosa.R;
import br.com.ricardofreitasjunior.android.gasosa.db.DataHelper;
import br.com.ricardofreitasjunior.android.gasosa.db.Posto;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ImagemOverlay extends Overlay {

	private GeoPoint geoPoint;
	private Paint paint = new Paint();
	private int imagemId;
	private Posto posto;
	private Ponto ponto;
	private Context context;
	private TextView avaliacao;
	private EditText nome;
	private EditText endereco;
	private EditText bairro;
	private EditText cidade;
	private EditText estado;
	private EditText vlgas;
	private EditText vleta;
	private static DataHelper repositorio;
	protected static final String LOG_TAG = "Mensagem";

	public ImagemOverlay(Posto posto, int resId, Context context) {
		this.posto = posto;
		this.imagemId = resId;
		this.ponto = new Ponto(Double.parseDouble(posto.latitude),Double.parseDouble(posto.longitude));
		this.context = context;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);

		// if(geoPoint != null){
		Point p = mapView.getProjection().toPixels(ponto, null);
		Bitmap btm = BitmapFactory.decodeResource(mapView.getResources(), this.imagemId);
		RectF r = new RectF(p.x, p.y, p.x + btm.getWidth(), p.y
				+ btm.getHeight());
		canvas.drawBitmap(btm, null, r, paint);
		// }
	}

	// public void setGeoPoint(GeoPoint p) {
	// this.geoPoint = p;
	//
	// }

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		Point ponto = mapView.getProjection().toPixels(this.ponto, null);
		// Cria o ret�ngulo
		RectF recf = new RectF(ponto.x - 5, ponto.y - 5, ponto.x + 5, ponto.y + 5);
		// Converte para ponto em pixels
		Point newPoint = mapView.getProjection().toPixels(geoPoint, null);
		// Verifica se o ponto est� contido no ret�ngulo
		boolean ok = recf.contains(newPoint.x, newPoint.y);
		if (ok) {
			// Toast.makeText(mapView.getContext(), "Nome do Posto: " +
			// posto.nome + " - Endereço: " + posto.endereco,
			// Toast.LENGTH_SHORT).show();
			// return true;
			DialogInfo();
			Log.i(LOG_TAG, "AVALIACAO: " + posto.avaliacao);
		}
		return super.onTap(geoPoint, mapView);
	}

	private void DialogInfo() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialoginfo);
		dialog.setTitle("Informaçoes do Ponto");
		dialog.setCancelable(true);

		nome = (EditText) dialog.findViewById(R.id.nome);
		endereco = (EditText) dialog.findViewById(R.id.endereco);
		bairro = (EditText) dialog.findViewById(R.id.bairro);
		cidade = (EditText) dialog.findViewById(R.id.cidade);
		estado = (EditText) dialog.findViewById(R.id.estado);
		vlgas = (EditText) dialog.findViewById(R.id.vlgas);
		vleta = (EditText) dialog.findViewById(R.id.vleta);
		avaliacao = (TextView) dialog.findViewById(R.id.avaliacao);

		nome.setText(posto.nome);
		endereco.setText(posto.endereco);
		vlgas.setText(posto.vlgas);
		vleta.setText(posto.vleta);
		estado.setText(posto.uf);
		cidade.setText(posto.cidade);
		bairro.setText(posto.bairro);

		Button calcular = (Button) dialog.findViewById(R.id.btnCalcular);
		Button salvar = (Button) dialog.findViewById(R.id.btnSalvar);

		calcular.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AvaliaCombustivel();
			}
		});

		salvar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void AvaliaCombustivel() {
		Double gasolina = 0.0;
		Double etanol = 0.0;

		gasolina = Double.parseDouble(vlgas.getText().toString());
		etanol = Double.parseDouble(vleta.getText().toString());

		if (gasolina / 100 * 70 <= etanol) {
			posto.avaliacao = "G";
			avaliacao.setText("Melhor abastecer com GASOLINA");
		} else {
			posto.avaliacao = "E";
			avaliacao.setText("Melhor abastecer com ETANOL");
		}
	}
	
	private void atualiza() {
		Posto novoposto = new Posto();
		novoposto.id = posto.id;
		novoposto.nome = nome.getText().toString();
		novoposto.endereco = endereco.getText().toString();
		novoposto.bairro = bairro.getText().toString();
		novoposto.cidade = cidade.getText().toString();
		novoposto.uf = estado.getText().toString();
		novoposto.vlgas = vlgas.getText().toString();
		novoposto.vleta = vleta.getText().toString();
		novoposto.latitude = posto.latitude;
		novoposto.longitude = posto.longitude;
		
		repositorio.getDataHelper(context).Atualizar(novoposto);
	}
}
