package br.com.lucasfaria.administrador.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.lucasfaria.administrador.R;
import br.com.lucasfaria.administrador.activity.CadastroMissaActivity;
import br.com.lucasfaria.administrador.adapter.AdapterMissa;
import br.com.lucasfaria.administrador.config.ReferenceConfig;
import br.com.lucasfaria.comuns.model.Missa;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissaFragment extends AppBaseFragment {

    private FloatingActionButton fabAdd;
    private ValueEventListener missaListener;
    private DatabaseReference missaReference;
    private ReferenceConfig config;
    private RecyclerView recyclerView;
    private List<Missa> missas = new ArrayList<>();
    private AdapterMissa adapterMissa;
    private ProgressBar progressBar;

    public MissaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missa, container, false);

        inicializar(view);
        swipe();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        configurarRecyclerView();
        pesquisarMissas();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (missaListener != null && missaReference != null)
            missaReference.removeEventListener(missaListener);
    }

    private void inicializar(View view) {
        config = new ReferenceConfig(getContext());
        missaReference = config.recuperarReferenciaMissas();

        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerMissas);
        fabAdd = view.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CadastroMissaActivity.class));
            }
        });
    }

    public void configurarRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapterMissa = new AdapterMissa(missas, getContext());
        recyclerView.setAdapter(adapterMissa);


    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                abrirAlertDialog(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }
    public void abrirAlertDialog(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Excluir Movimentação da Conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                Missa m = missas.get(position);
//                m.excluir();

                adapterMissa.notifyItemRemoved(position);
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                adapterMissa.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    private void pesquisarMissas() {

        progressBar.setVisibility(View.VISIBLE);
        missaListener = missaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                missas = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Missa missa = dados.getValue(Missa.class);
                        missa.setId(dados.getKey());
                        missas.add(missa);
                    }
                }
                configurarRecyclerView();
                adapterMissa.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
