package de.hgs.android;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;
import java.math.MathContext;

public class MainActivity extends AppCompatActivity {

    //Objekte der View deklarieren
    EditText etKommazahl;
    TextView tvVorkommateil;
    TextView tvVorkommateilBinaer;
    TextView tvNachkommateil;
    TextView tvNachkommateilBinaer;
    TextView tvFKZ;
    TextView tvFKZN;
    TextView tvExcess;
    TextView tvExpBin;
    TextView tvGKZ;
    Button btBerechne;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Objekte erstellen und verknüpfen
        etKommazahl = findViewById(R.id.etKommazahl);
        tvVorkommateil = findViewById(R.id.tvVorkommateil);
        tvVorkommateilBinaer = findViewById(R.id.tvVorkommateilBinaer);
        tvNachkommateil = findViewById(R.id.tvNachkommateil);
        tvNachkommateilBinaer=findViewById(R.id.tvNachkommateilBinaer);
        tvFKZ = findViewById(R.id.tvFestkommazahl);
        tvFKZN = findViewById(R.id.tvFestkommazahlNormiert);
        tvExcess = findViewById(R.id.tvExcess);
        tvExpBin = findViewById(R.id.tvExPBin);
        tvGKZ = findViewById(R.id.tvGleitkommazahl);


        btBerechne = findViewById(R.id.btBerechne);



        //Listener für Button
        btBerechne.setOnClickListener(View -> clickedButton());

    }

    private void clickedButton() {
        //Kontrollausgabe für den Klick
        Toast.makeText(this,"Berechne geklickt",Toast.LENGTH_LONG).show();

        //Kommazahl einlesen
        String strKommazahl = etKommazahl.getText().toString();
        double kommazahl=0;
        if(!strKommazahl.equals("")) {
            kommazahl = Double.parseDouble(strKommazahl);
        }


        //Vorkommateil bestimmen und ausgeben
        int vkDez = (int) kommazahl;
        tvVorkommateil.setText(""+vkDez);


        //Vorkommateil in Binaerzahl umwandeln und ausgeben
        String strVkBin = Integer.toBinaryString(vkDez);
        tvVorkommateilBinaer.setText(strVkBin);

        //Nachkommateil bestimmen und ausgeben
        int posKomma = strKommazahl.indexOf(".");
        String strNkDez = "0"+strKommazahl.substring(posKomma);
        tvNachkommateil.setText(strNkDez);

        //Nachkommateil in Binaerwert umrechnen
        double nk = Double.parseDouble(strNkDez);
        double produkt = 0;
        String strNkBin = "";


        for (int i=0;i<23;i++){
            produkt = 2*nk;
            if(produkt>=1){
                strNkBin = strNkBin+"1";
                nk = produkt-1;
            }
            else{
                strNkBin = strNkBin+"0";
                nk = produkt;
            }
        }

        tvNachkommateilBinaer.setText(strNkBin);

        //Festkommazahl betimmen und anzeigen
        String strFKZ = strVkBin+","+strNkBin;
        tvFKZ.setText(strFKZ);

        //Festkommazahl normieren aus 1011,1011101 wird  1,0111011 *2^3
        //                                 0,001011     wird  1,011*2^-3

        //Position der ersten 1 von links nach rechts in strFKZ
        int ersteEinsFKZ = strFKZ.indexOf("1");

        //Position des KOmmas bestimmen
        int posKommaFKZ = strFKZ.indexOf(",");

        //dualen exponenten bestimmen hier: +3 oder -3
        int expDual;
        if(posKommaFKZ<ersteEinsFKZ){
            expDual =  (posKommaFKZ) -ersteEinsFKZ;
        }
        else{
          expDual  = (posKommaFKZ-1) -ersteEinsFKZ;
        }


        //Komma entfernen
        String strFKZN = strFKZ.replace(",","");

        //Komma an neue Stelle setzen nach der ersten 1
        int ersteEinsFKZN = strFKZN.indexOf("1");
        strFKZN = "1,"+strFKZN.substring(ersteEinsFKZN+1);

        //Anzeigen normierte Festkommazahl
        tvFKZN.setText(strFKZN+" 2^"+expDual);

        //Binaeren exponenten Bestimmen
        //Dualer exp + excess = 3+127 = 130 -> binaer darstellen
        int anzBitsExpo = 8;
        int excess = (int) Math.pow(2,anzBitsExpo-1)-1;
        String expBin = Integer.toBinaryString(expDual+excess);
        tvExcess.setText("excess=2^(n-1)-1="+excess);
        tvExpBin.setText(expBin);

        //Mantisse bestimmen
        String mantisse = strFKZN.substring(2);

        //Vorzeichen bestimmen
        String vz;
        if(kommazahl>=0){
            vz = "0";
        }
        else{
            vz="1";
        }

        //Gleitkommazahl bestimmen und ausgeben
        String strGkz = vz+"|"+expBin+"|"+mantisse;
        tvGKZ.setText(strGkz);






    }


}