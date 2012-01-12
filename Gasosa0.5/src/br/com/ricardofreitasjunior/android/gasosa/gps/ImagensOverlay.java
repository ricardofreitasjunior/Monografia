package br.com.ricardofreitasjunior.android.gasosa.gps;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.ricardofreitasjunior.android.gasosa.R;
import br.com.ricardofreitasjunior.android.gasosa.db.Posto;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ImagensOverlay extends ItemizedOverlay {

	private final List<OverlayItem> imagens;
	private final Context context;
	private Posto posto;
	
	private TextView avaliacao;
	private EditText nome;
	private EditText endereco;
	private EditText bairro;
	private EditText cidade;
	private EditText estado;
	private EditText vlgas;
	private EditText vleta;
	
	protected static final String LOG_TAG = "Mensagem";
	
	public ImagensOverlay(Context context, List<OverlayItem> imagens, Drawable drawable, Posto posto) {
		super(drawable);
		this.imagens = imagens;
		this.context = context;
		this.posto = posto;
		
		boundCenterBottom(drawable);
		populate();
	}
	
	protected boolean onTap(int i){
		OverlayItem overlayItem = imagens.get(i);
		String texto = overlayItem.getTitle() + " - " + overlayItem.getSnippet();
		Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
		DialogInfo(overlayItem);
		
		return(true);
	}

	private void DialogInfo(OverlayItem overlayItem) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialoginfo);
		dialog.setTitle("Informaçoes do Ponto");
		dialog.setCancelable(true);

		Log.i(LOG_TAG, "AVALIACAO: " + posto.avaliacao);
		
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
	
	@Override
	protected OverlayItem createItem(int i) {
		return imagens.get(i);
	}

	@Override
	public int size() {
		return imagens.size();
	}

}
