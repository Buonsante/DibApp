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
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import io.opencensus.internal.StringUtils;
import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.services.BluetoothGattServerService;
import it.uniba.maw.dibapp.util.Util;

import static android.content.Context.MODE_PRIVATE;
import static it.uniba.maw.dibapp.model.Lezione.*;
import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;


public class DettagliFragment extends Fragment {

    private Button buttonRegister;
    private TextView textViewInsegnamento;
    private TextView textViewDocente;
    private TextView textViewOraInizio;
    private TextView textViewOraFine;
    private EditText editTextArgomento;
    private TextView textViewEmail;
    private Button buttonSalva;
    private Button btnBottomSheet;
    private TextView textViewCounter;

    //tipologia di utente (S, D)
    private String utente;
    private FirebaseUser user;

    //contiene il nome del server ble relativo alla lezione
    private String nameServerBle;
    private String linkLezione;
    private Lezione lezione;
    boolean activated = false;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dettagli, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        //recupera tipologia di utente
        utente = getContext().getSharedPreferences(Util.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString("tipo", "");

        btnBottomSheet = getActivity().findViewById(R.id.btn_bottom_sheet);

        buttonRegister = view.findViewById(R.id.buttonRegister);
        textViewInsegnamento = view.findViewById(R.id.text_view_insegnamento);
        textViewDocente = view.findViewById(R.id.text_view_docente);
        textViewOraInizio = view.findViewById(R.id.text_view_ora_inizio);
        textViewOraFine = view.findViewById(R.id.text_view_ora_fine);
        editTextArgomento = view.findViewById(R.id.edit_text_argomento);
        textViewEmail = view.findViewById(R.id.text_view_email);
        buttonSalva = view.findViewById(R.id.button_salva);
        textViewCounter = view.findViewById(R.id.count);

        if(utente.equals("D"))
            buttonRegister.setOnClickListener(buttonRegisterListener);
        buttonSalva.setOnClickListener(buttonSalvaListener);

        //recupera lezione da LessonActivity

        lezione = (Lezione) getArguments().getSerializable("lezione");
        linkLezione = lezione.getLinkLezione();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(linkLezione).addSnapshotListener(lezioneDbListener);

        if(utente.equals("D")){
            editTextArgomento.setEnabled(false);
            textViewEmail.setVisibility(View.GONE);
            buttonRegister.setText("Attiva lezione");

        } else {
            editTextArgomento.setEnabled(false);
            buttonSalva.setVisibility(View.GONE);
        }




        //TODO aggiungere il caricamento dell'argomento dalla editTextArgomento nel caso in cui l'utente è un docente


        return view;
    }

    EventListener<DocumentSnapshot> lezioneDbListener = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {
            editTextArgomento.setText(document.getString(ARGOMENTO));
            textViewInsegnamento.setText(document.getString(INSEGNAMENTO));
            textViewDocente.setText(document.getString(PROFESORE));
            textViewOraInizio.setText(formattaOra(document.getString(ORA_INIZIO)));
            textViewOraFine.setText(formattaOra(document.getString(ORA_FINE)));
            textViewCounter.setText(String.valueOf(document.getLong(NUM_PRESENZE)));
            if(utente.equals("S")) {
                //se l'utente è uno studente
                switch (document.getLong(STATO).intValue()) {
                    case LEZIONE_NON_INIZIATA:
                        buttonRegister.setOnClickListener(null);
                        getActivity().findViewById(R.id.btn_bottom_sheet).setVisibility(View.INVISIBLE);
                        break;
                    case LEZIONE_IN_REGISTRAZIONE:
                        String nameServeBleStringReceived = document.getString("nameServerBle");
                        if (document.get("utentiRegistrati") == null || !(((ArrayList<String>) document.get("utentiRegistrati")).contains(user.getUid()))) {
                            lezione.setNameServerBle(nameServeBleStringReceived);
                            buttonRegister.setText("Registrati");
                            buttonRegister.setOnClickListener(buttonRegisterListener);
                            btnBottomSheet.setVisibility(View.INVISIBLE);
                        } else {
                            buttonRegister.setText("Registrazione effetutata");
                            buttonRegister.setOnClickListener(null);
                            btnBottomSheet.setVisibility(View.VISIBLE);
                        }
                        break;
                    case LEZIONE_TERMINATA:
                        if (document.get("utentiRegistrati") == null || !(((ArrayList<String>) document.get("utentiRegistrati")).contains(user.getUid()))) {
                            buttonRegister.setText("Registrazioni interrotte");
                            buttonRegister.setOnClickListener(null);
                            btnBottomSheet.setVisibility(View.INVISIBLE);
                        } else {
                            buttonRegister.setText("Registrazione effettuata");
                            buttonRegister.setOnClickListener(null);
                            btnBottomSheet.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }else{
                //Se l'utente è un docente
                switch (document.getLong(STATO).intValue()) {
                    case LEZIONE_NON_INIZIATA:

                        break;
                    case LEZIONE_IN_REGISTRAZIONE:
                        activated = true;
                        buttonRegister.setText("Interrompi registrazione");
                        break;
                    case LEZIONE_TERMINATA:
                        buttonRegister.setText("Riapri Registrazione");
                        break;
                }
            }
        }
    };
    View.OnClickListener buttonRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(utente.equals("S")){
                registerStudent();
            }else{
                activateLesson();
            }
        }
    };

    View.OnClickListener buttonSalvaListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(editTextArgomento.isEnabled()){
                String argomento = editTextArgomento.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.document(linkLezione).update(ARGOMENTO, argomento);
                editTextArgomento.onEditorAction(EditorInfo.IME_ACTION_DONE);
                editTextArgomento.setEnabled(false);
            } else {
                editTextArgomento.setEnabled(true);
            }

        }
    };

    private String formattaOra(String ora){
        if(ora.length() == 1)
            return "0"+ora+":00";
        else
            return ora+":00";
    }

    private void activateLesson(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(!activated) {
            activated = true;
            String bleServerId = UUID.randomUUID().toString().substring(0, 7);

            Intent intent = new Intent(getContext(), BluetoothGattServerService.class);
            intent.putExtra("lezione", lezione);
            intent.putExtra("idServer", bleServerId);


            db.document(linkLezione).update(NAME_SERVER_BLE, bleServerId, STATO, LEZIONE_IN_REGISTRAZIONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getContext().startForegroundService(intent);
            } else {
                getContext().startService(intent);
            }
            buttonRegister.setText("Interrompi registrazione");
        }else{
            activated = false;
            Intent intent = new Intent(getContext(), BluetoothGattServerService.class);
            getContext().stopService(intent);
            buttonRegister.setText("Riapri registrazione");
            db.document(linkLezione).update(STATO, LEZIONE_TERMINATA);
        }
    }

    private void registerStudent() {

        // recuperiamo un riferimento al BluetoothManager
        BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
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
                    db.document(linkLezione).update(
                            "numPresenze", FieldValue.increment(1),
                            "utentiRegistrati",FieldValue.arrayUnion(user.getUid()));
                }
            }


        }
    };
}

