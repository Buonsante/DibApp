package it.uniba.maw.dibapp.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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


public class DettagliFragment extends Fragment implements SensorEventListener {

    private ProgressBar progressBar;
    ProgressDialog dialog;

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

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */
    private static final float SHAKE_THRESHOLD_GRAVITY = 3F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;


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
        progressBar = view.findViewById(R.id.progressBarLesson);

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
        db.document(lezione.getProfessoreLink()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textViewEmail.setText(documentSnapshot.getString("mail"));
            }
        });
        if(utente.equals("D")){
            editTextArgomento.setEnabled(false);
            textViewEmail.setVisibility(View.GONE);
            buttonRegister.setText(R.string.activate_lesson);

        } else {
            editTextArgomento.setEnabled(false);
            buttonSalva.setVisibility(View.GONE);

        }

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    private EventListener<DocumentSnapshot> lezioneDbListener = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {

            editTextArgomento.setText(document.getString(ARGOMENTO));
            textViewInsegnamento.setText(document.getString(INSEGNAMENTO));
            textViewDocente.setText(document.getString(PROFESSORE));
            textViewOraInizio.setText(formattaOra(document.getString(ORA_INIZIO)));
            textViewOraFine.setText(formattaOra(document.getString(ORA_FINE)));
            textViewCounter.setText(String.valueOf(document.getLong(NUM_PRESENZE)));

            if(utente.equals("S")) {
                //se l'utente è uno studente
                ConstraintLayout.LayoutParams layoutParams;
                switch (document.getLong(STATO).intValue()) {
                    case LEZIONE_NON_INIZIATA:
                        buttonRegister.setOnClickListener(null);
                        getActivity().findViewById(R.id.btn_bottom_sheet).setVisibility(View.INVISIBLE);
                         layoutParams = (ConstraintLayout.LayoutParams) buttonRegister.getLayoutParams();
                        layoutParams.setMargins(0,0,0,0);
                        buttonRegister.setLayoutParams(layoutParams);
                        break;
                    case LEZIONE_IN_REGISTRAZIONE:
                        String nameServeBleStringReceived = document.getString("nameServerBle");
                        if (document.get("utentiRegistrati") == null || !(((ArrayList<String>) document.get("utentiRegistrati")).contains(user.getUid()))) {
                            lezione.setNameServerBle(nameServeBleStringReceived);
                            buttonRegister.setText(R.string.register);
                            buttonRegister.setOnClickListener(buttonRegisterListener);
                            btnBottomSheet.setVisibility(View.INVISIBLE);
                            layoutParams = (ConstraintLayout.LayoutParams) buttonRegister.getLayoutParams();
                            layoutParams.setMargins(0,0,0,0);
                            buttonRegister.setLayoutParams(layoutParams);
                        } else {
                            buttonRegister.setText(R.string.registration_done);
                            buttonRegister.setOnClickListener(null);

                            if(((document.get("hadCommented")) != null) && !(((ArrayList<String>) document.get("hadCommented")).contains(user.getUid()))){
                                btnBottomSheet.setVisibility(View.VISIBLE);
                                layoutParams = (ConstraintLayout.LayoutParams) buttonRegister.getLayoutParams();
                                layoutParams.setMargins(0,0,0,130);
                                buttonRegister.setLayoutParams(layoutParams);
                            }else{
                                btnBottomSheet.setVisibility(View.INVISIBLE);
                                layoutParams = (ConstraintLayout.LayoutParams) buttonRegister.getLayoutParams();
                                layoutParams.setMargins(0,0,0,0);
                                buttonRegister.setLayoutParams(layoutParams);
                            }
                        }
                        break;
                    case LEZIONE_TERMINATA:
                        if (document.get("utentiRegistrati") == null || !(((ArrayList<String>) document.get("utentiRegistrati")).contains(user.getUid()))) {
                            buttonRegister.setText(R.string.registration_stopped);
                            buttonRegister.setOnClickListener(null);
                            btnBottomSheet.setVisibility(View.INVISIBLE);
                            layoutParams = (ConstraintLayout.LayoutParams) buttonRegister.getLayoutParams();
                            layoutParams.setMargins(0,0,0,0);
                            buttonRegister.setLayoutParams(layoutParams);
                        } else {
                            buttonRegister.setText(R.string.registration_done);
                            buttonRegister.setOnClickListener(null);
                            if(((document.get("hadCommented")) != null) && !(((ArrayList<String>) document.get("hadCommented")).contains(user.getUid()))){
                                btnBottomSheet.setVisibility(View.VISIBLE);
                                layoutParams = (ConstraintLayout.LayoutParams) buttonRegister.getLayoutParams();
                                layoutParams.setMargins(0,0,0,130);
                                buttonRegister.setLayoutParams(layoutParams);
                            }else{
                                btnBottomSheet.setVisibility(View.INVISIBLE);
                                layoutParams = (ConstraintLayout.LayoutParams) buttonRegister.getLayoutParams();
                                layoutParams.setMargins(0,0,0,0);
                                buttonRegister.setLayoutParams(layoutParams);
                            }
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
                        buttonRegister.setText(R.string.stop_registration);
                        break;
                    case LEZIONE_TERMINATA:
                        buttonRegister.setText(R.string.open_registration);
                        break;
                }
            }
        }
    };
    View.OnClickListener buttonRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(utente.equals("S")){
                buttonRegister.setOnClickListener(null);
                progressShow();
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
            buttonRegister.setText(R.string.stop_registration);
        }else{
            activated = false;
            Intent intent = new Intent(getContext(), BluetoothGattServerService.class);
            getContext().stopService(intent);
            buttonRegister.setText(R.string.open_registration);
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
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Log.w(DEBUG_TAG+"ii", "Registrazione effettuata");
                    Toast.makeText(getContext(), R.string.registration_done, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //Log.i(DEBUG_TAG+"/sensor",x+"   "+y+"   "+z);
        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();
            // ignore shake events too close to each other (500ms)

            long mShakeTimestamp = 0;

            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {

                return;
            }

            mShakeTimestamp = now;

            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                buttonRegister.performClick();
            } else {
                //deprecated in API 26
                v.vibrate(300);
                buttonRegister.performClick();
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void progressShow() {
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setTitle("Registrazione in corso");
        dialog.show();
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}

