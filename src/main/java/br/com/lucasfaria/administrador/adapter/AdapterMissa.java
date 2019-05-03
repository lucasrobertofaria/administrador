package br.com.lucasfaria.administrador.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.lucasfaria.administrador.R;
import br.com.lucasfaria.administrador.activity.CadastroMissaActivity;
import br.com.lucasfaria.comuns.helper.DateHelper;
import br.com.lucasfaria.comuns.model.Missa;

/**
 * Created by Jamilton Damasceno
 */

public class AdapterMissa extends RecyclerView.Adapter<AdapterMissa.MyViewHolder> {

    List<Missa> missas;
    Context context;

    public AdapterMissa(List<Missa> missas, Context context) {
        this.missas = missas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_missa, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Missa missa = missas.get(position);

        holder.titulo.setText(missa.getTitulo());
        holder.horario.setText(missa.getHorario());
        holder.frequencia.setText(missa.getFrequencia().getDescricao());

        if (missa.getDiasDaSemana() != null) {
            holder.diaDaSemana.setText(missa.getDiasDaSemana().getDescricao());
            holder.diaDaSemana.setVisibility(View.VISIBLE);
        } else {
            holder.diaDaSemana.setVisibility(View.GONE);
        }
        if (missa.getData() != null) {
            holder.data.setText(DateHelper.formatDateToString(missa.getData()));
            holder.data.setVisibility(View.VISIBLE);
        } else {
            holder.data.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CadastroMissaActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("missa", missa);
                intent.putExtra("missa", bundle);
                context.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return missas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, frequencia, horario, diaDaSemana, data;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.txTitulo);
            frequencia = itemView.findViewById(R.id.txFrequencia);
            horario = itemView.findViewById(R.id.txHorario);
            diaDaSemana = itemView.findViewById(R.id.txDiaDaSemana);
            data = itemView.findViewById(R.id.txData);
        }

    }

}
