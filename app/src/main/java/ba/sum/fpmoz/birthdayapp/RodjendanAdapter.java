package ba.sum.fpmoz.birthdayapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import ba.sum.fpmoz.birthdayapp.model.Rodjendan;

public class RodjendanAdapter extends FirebaseRecyclerAdapter<Rodjendan, RodjendanAdapter.RodjendanViewHolder> {

    public RodjendanAdapter(@NonNull FirebaseRecyclerOptions<Rodjendan> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RodjendanViewHolder holder, int position, @NonNull Rodjendan model) {
        holder.rodjendan_naziv.setText(model.getNaziv());
        holder.rodjendan_datum.setText(model.getDatum());
        holder.rodjendan_poklon.setText(model.getPoklon());
        Glide.with(holder.rodjendanImg.getContext()).load(model.getSlika()).into(holder.rodjendanImg);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                Intent i = new Intent(v.getContext(), RodjendanActivity.class);
                i.putExtra("RodjendanKey", key);
                v.getContext().startActivity(i);

            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence[] delete = {"Izbriši", "Uredi"};
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setItems(delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition()).getRef().removeValue();
                        } else if (which == 1) {
                            String key = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                            Intent i = new Intent(v.getContext(), EditRodjendanActivity.class);
                            i.putExtra("RodjendanKey", key);
                            v.getContext().startActivity(i);
                        }
                    }
                });
                alert.create().show();
                return false;
            }

        });

    }

    @NonNull
    @Override
    public RodjendanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rodjendan_item_view, parent, false);
        return new RodjendanViewHolder(view);
    }

    static class RodjendanViewHolder extends RecyclerView.ViewHolder {

        TextView rodjendan_naziv;
        TextView rodjendan_datum;
        TextView rodjendan_poklon;
        ImageView rodjendanImg;
        View view;

        public RodjendanViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.rodjendan_naziv = itemView.findViewById(R.id.rodjendan_item_naziv);
            this.rodjendan_datum = itemView.findViewById(R.id.rodjendan_item_datum);
            this.rodjendan_poklon = itemView.findViewById(R.id.rodjendan_item_poklon);
            this.rodjendanImg = itemView.findViewById(R.id.rodjendan_item_imageView);
        }
    }
}
