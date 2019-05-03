package br.com.lucasfaria.administrador.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import br.com.lucasfaria.administrador.R;
import br.com.lucasfaria.administrador.exception.ObrigatorioException;
import br.com.lucasfaria.administrador.fragment.MyDatePickerFragment;
import br.com.lucasfaria.administrador.config.ReferenceConfig;
import br.com.lucasfaria.comuns.enums.DiasDaSemana;
import br.com.lucasfaria.comuns.enums.Frequencia;
import br.com.lucasfaria.comuns.helper.DateHelper;
import br.com.lucasfaria.comuns.model.Missa;

public class CadastroMissaActivity extends AppCompatActivity {

    private TextInputEditText editTitulo, editHorario, editData, editObservacao;
    private Spinner spinnerFrequencia, spinnerDiasDaSemana;
    private TextView txDiasDaSemana;
    private RelativeLayout relativeDiasDaSemana, relativeData;
    private FloatingActionButton fab;
    private ProgressBar progressBar;

    private ReferenceConfig config;

    private ValueEventListener missaListener;
    private DatabaseReference missaReference;

    private Missa missa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_missa);

        config = new ReferenceConfig(CadastroMissaActivity.this);
        missaReference = config.recuperarReferenciaMissas();

        Intent intent = getIntent();
        if (intent != null && intent.getBundleExtra("missa") != null) {
            Bundle bundle = intent.getBundleExtra("missa");
            missa = (Missa) bundle.get("missa");
        }

        inicializar();
        tratarVisibilidade();
    }

    private void preencherObjetoMissa() {
        if (missa == null) {
            missa = new Missa();
        }
        Frequencia frequencia = Frequencia.getByName(spinnerFrequencia.getSelectedItem().toString());
        DiasDaSemana diasDaSemana = DiasDaSemana.getByName(spinnerDiasDaSemana.getSelectedItem().toString());
        missa.setFrequencia(frequencia);
        Date data = null;
        if (!editData.getText().toString().isEmpty()) {
            try {
                data = DateHelper.parseStringToDate(editData.getText().toString());
            } catch (ParseException e) {

            }
        }
        switch (frequencia) {
            case D:
                missa.setData(null);
                missa.setDiasDaSemana(null);
                break;
            case S:
                missa.setData(null);
                missa.setDiasDaSemana(diasDaSemana);
                break;
            default:
                missa.setData(data);
                missa.setDiasDaSemana(null);
                break;
        }
        missa.setTitulo(editTitulo.getText().toString());

        missa.setHorario(editHorario.getText().toString());
        missa.setObservacao(editObservacao.getText().toString());

    }


    private void preencherAtributosTela() {
        if (missa != null) {
            if (missa.getFrequencia() != null)
                spinnerFrequencia.setSelection(missa.getFrequencia().ordinal());
            if (missa.getDiasDaSemana() != null)
                spinnerDiasDaSemana.setSelection(missa.getDiasDaSemana().ordinal());

            editTitulo.setText(missa.getTitulo());
            editHorario.setText(missa.getHorario());
            editObservacao.setText(missa.getObservacao());
            if (missa.getData() != null) {
                String dataString = DateHelper.formatDateToString(missa.getData());
                editData.setText(dataString);
            }
        }
    }

    public void tratarVisibilidade() {

        Frequencia frequencia = Frequencia.getByName(spinnerFrequencia.getSelectedItem().toString());
        switch (frequencia) {
            case D:
                relativeData.setVisibility(View.GONE);
                relativeDiasDaSemana.setVisibility(View.GONE);
                txDiasDaSemana.setVisibility(View.GONE);

                break;
            case S:
                relativeDiasDaSemana.setVisibility(View.VISIBLE);
                txDiasDaSemana.setVisibility(View.VISIBLE);
                relativeData.setVisibility(View.GONE);
                break;
            case M:
                relativeDiasDaSemana.setVisibility(View.GONE);
                txDiasDaSemana.setVisibility(View.GONE);
                relativeData.setVisibility(View.VISIBLE);
                break;
            case A:
                relativeDiasDaSemana.setVisibility(View.GONE);
                txDiasDaSemana.setVisibility(View.GONE);
                relativeData.setVisibility(View.VISIBLE);
                break;

            case U:
                relativeDiasDaSemana.setVisibility(View.GONE);
                txDiasDaSemana.setVisibility(View.GONE);
                relativeData.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void inicializar() {
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);

        txDiasDaSemana = findViewById(R.id.txDiasDaSemana);
        spinnerFrequencia = findViewById(R.id.spinnerFrequencia);
        spinnerDiasDaSemana = findViewById(R.id.spinnerDiaDaSemana);
        editTitulo = findViewById(R.id.editTitulo);
        editHorario = findViewById(R.id.editHorario);
        editData = findViewById(R.id.editData);
        editObservacao = findViewById(R.id.editObservacao);

        relativeDiasDaSemana = findViewById(R.id.relativeDiasDaSemana);
        relativeData = findViewById(R.id.relativeData);

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDatePicker();
            }
        });
        editHorario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CadastroMissaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editHorario.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.show();
            }
        });

        spinnerFrequencia.setAdapter(new ArrayAdapter(CadastroMissaActivity.this, R.layout.my_spinner_layout, Frequencia.getNomes()));
        spinnerDiasDaSemana.setAdapter(new ArrayAdapter(CadastroMissaActivity.this, R.layout.my_spinner_layout, DiasDaSemana.getNomes()));

        spinnerFrequencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tratarVisibilidade();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    preencherObjetoMissa();
                    validar();
                    salvar(v);
                } catch (ObrigatorioException e) {
                    Snackbar.make(v, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validar() throws ObrigatorioException {
        if (missa.getFrequencia() == null) {
            throw new ObrigatorioException("frequencia");
        }

        if (missa.getTitulo() == null || missa.getTitulo().isEmpty()) {
            throw new ObrigatorioException("título");
        }

        if (missa.getHorario() == null || missa.getHorario().isEmpty()) {
            throw new ObrigatorioException("horário");
        }


    }

    private void abrirDatePicker() {
        MyDatePickerFragment datePicker = new MyDatePickerFragment();
        datePicker.setEditData(editData);
        datePicker.show(getSupportFragmentManager(), getString(R.string.data));
    }


    private void salvar(final View view) {

        progressBar.setVisibility(View.VISIBLE);

        if (missa.getId() == null || missa.getId().isEmpty()) {
            String key = missaReference.push().getKey();
            missa.setId(key);

        }
        missaReference.child(missa.getId()).setValue(missa).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(view, "Salvo", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStop() {
        if (missaListener != null && missaReference != null)
            missaReference.removeEventListener(missaListener);
        super.onStop();
    }

    @Override
    protected void onStart() {
        preencherAtributosTela();
        super.onStart();
    }
}
