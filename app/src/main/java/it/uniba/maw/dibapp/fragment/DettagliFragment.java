package it.uniba.maw.dibapp.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static android.content.Context.MODE_PRIVATE;
import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;


public class DettagliFragment extends Fragment {

    private Button buttonRegister;
    private TextView textViewInsegnamento;
    private TextView textViewDocente;
    private TextView textViewOraInizio;
    private TextView textViewOraFine;
    private TextView textViewArgomento;
    private EditText editTextArgomento;

    //contiene il nome del server ble relativo alla lezione
    private String nameServerBle;
    private Lezione lezione;

    //BLE
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler = new Handler();
    private final static int SCAN_PERIOD = 20000;

    public DettagliFragment() {
        // Required empty public constructor
    }

    public static DettagliFragment newInstance(String param1, String param2) {
        DettagliFragment fragment = new DettagliFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dettagli, container, false);

        buttonRegister = view.findViewById(R.id.buttonRegister);
        textViewInsegnamento = view.findViewById(R.id.text_view_insegnamento);
        textViewDocente = view.findViewById(R.id.text_view_docente);
        textViewOraInizio = view.findViewById(R.id.text_view_ora_inizio);
        textViewOraFine = view.findViewById(R.id.text_view_ora_fine);
        textViewArgomento = view.findViewById(R.id.text_view_argomento);
        editTextArgomento = view.findViewById(R.id.edit_text_argomento);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerStudent(view);
            }
        });

        if(getContext().getSharedPreferences(Util.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString("tipo", "").equals("D")){
            textViewArgomento.setVisibility(View.INVISIBLE);
            editTextArgomento.setVisibility(View.VISIBLE);
        } else {
            editTextArgomento.setVisibility(View.INVISIBLE);
            textViewArgomento.setVisibility(View.VISIBLE);
        }

        //recupera lezione da LessonActivity
        lezione = (Lezione) getArguments().getSerializable("lezione");

        textViewInsegnamento.setText(lezione.getInsegnamento());
        textViewDocente.setText(lezione.getProfessore());
        textViewOraInizio.setText(lezione.getOraInizio());
        textViewOraFine.setText(lezione.getOraFine());
        textViewArgomento.setText(lezione.getArgomento());

        //TODO aggiungere il caricamento dell'argomento dalla editTextArgomento nel caso in cui l'utente è un docente

        return view;
    }

    private void registerStudent(View view) {

        // recuperiamo un riferimento al BluetoothManager
        BluetoothManager bluetoothManager = (BluetoothManager) view.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        // recuperiamo un riferimento all'adapter Bluetooth
        bluetoothAdapter = bluetoothManager.getAdapter();

        //richiede permesso per accesso alla posizione (senza tale permesso non è in grado di rilevare dispositivi ble)
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 456);

        // verifichiamo che Bluetooth sia attivo nel dispositivo
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        } else{
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            startBleScan();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        startBleScan();
    }

    private void startBleScan() {
        // scanning a true significa "scansione in corso"
        scanning = true;
        // avviamo la scansione da un thread secondario
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // avvio della scansione
                bluetoothLeScanner.startScan(scanCallback);
                Log.w(DEBUG_TAG+"ii", "StartScan");
            }
        });
        // l'oggetto Handler viene utilizzato per impostare un timeout
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // tempo scaduto per la scansione
                // scansione interrotta
                bluetoothLeScanner.stopScan(scanCallback);
                Log.w(DEBUG_TAG+"ii", "StopScan");
                // scanning=false significa "Nessuna scansione in corso"
                scanning = false;
            }
        }, SCAN_PERIOD);
        // SCAN_PERIOD indica una durata in millisecondi
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            Log.w(DEBUG_TAG+"ii", "OnScanResult");

            nameServerBle = lezione.getNameServerBle();

            //questo if è provvisorio per il test 
            if(nameServerBle == null) {
                nameServerBle = "1234";
            }
            if(result.getDevice().getName() != null) {
                if(result.getDevice().getName().equals(nameServerBle)) {
                    Log.w(DEBUG_TAG+"ii", "Registrazione effettuata");
                    Toast.makeText(getContext(), "Regitrazione effettuata!", Toast.LENGTH_SHORT).show();
                    bluetoothLeScanner.stopScan(scanCallback);
                    Log.w(DEBUG_TAG+"ii", "StopScan");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.document(lezione.getLinkLezione())
                            .update("numPresenze", FieldValue.increment(1));
                }
            }


        }
    };
}

