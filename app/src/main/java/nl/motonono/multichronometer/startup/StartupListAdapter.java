package nl.motonono.multichronometer.startup;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.motonono.multichronometer.R;
import nl.motonono.multichronometer.model.ChronoManager;
import nl.motonono.multichronometer.model.Chronometer;

public class StartupListAdapter extends RecyclerView.Adapter<StartupListAdapter.ViewHolder> {

//    private List<Chronometer> listdata;
    ChronoManager chronoManager;
    boolean isOnTextChanged = false; // prevent double events lon edit changes

    // RecyclerView recyclerView;
    public StartupListAdapter(ChronoManager chronoManager) {
        this.chronoManager = chronoManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.startup_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem, new MyCustomEditTextListener());
        return viewHolder;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull StartupListAdapter.ViewHolder holder) {
        holder.enableTextWatcher();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull StartupListAdapter.ViewHolder holder) {
        holder.disableTextWatcher();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Chronometer myListData = chronoManager.getChronos().get(position);
        holder.mEditText.setText(chronoManager.getChronos().get(position).getName());
        holder.imageView.setImageResource(android.R.drawable.ic_delete);
        holder.myCustomEditTextListener.updatePosition(holder.getBindingAdapterPosition());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoManager.getChronos().remove(holder.getBindingAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chronoManager.getChronos().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView mEditText;
        public ConstraintLayout constraintLayout;
        public MyCustomEditTextListener myCustomEditTextListener;


        public ViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.mEditText = (TextView) itemView.findViewById(R.id.txPersonName);
            this.constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout);
            this.myCustomEditTextListener = myCustomEditTextListener;
        }

        void enableTextWatcher() {
            mEditText.addTextChangedListener(myCustomEditTextListener);
        }
        void disableTextWatcher() {
            mEditText.removeTextChangedListener(myCustomEditTextListener);
        }
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            chronoManager.getChronos().get(position).setName(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
