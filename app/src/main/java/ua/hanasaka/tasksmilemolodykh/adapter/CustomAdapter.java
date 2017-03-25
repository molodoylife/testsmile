package ua.hanasaka.tasksmilemolodykh.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.hanasaka.tasksmilemolodykh.R;
import ua.hanasaka.tasksmilemolodykh.textmanager.TextTwitManager;

/**
 * custom adapter to bind data from Cursor to item in recyclerview
 *
 * Created by Oleksandr Molodykh on 24.03.2017.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    Context ctx;
    private Cursor cursor;

    /**
     * initializing cursor and context
     *
     * @param ctx transferred context
     * @param c   transferred cursor
     */
    public CustomAdapter(Context ctx, Cursor c) {
        this.ctx = ctx;
        this.cursor = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        cursor.moveToPosition(position);
        long id = cursor.getLong(cursor.getColumnIndex("name"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String nick = cursor.getString(cursor.getColumnIndex("nick"));
        String body = cursor.getString(cursor.getColumnIndex("body"));
        switch ((int)id){
            case 1:
                holder.getImageView().setImageResource(R.drawable.user_photo_default);
                break;
            case 2:
                holder.getImageView().setImageResource(R.drawable.sayhello);
                break;
            case 3:
                holder.getImageView().setImageResource(R.drawable.smile);
                break;
        }
        holder.getNameView().setText(name);
        holder.getNickView().setText(nick);
        holder.getTwitView().setText(TextTwitManager.changeNamesInText(body, ctx));
        holder.getTwitView().setMovementMethod(LinkMovementMethod.getInstance());
        holder.getTwitView().setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void updateRecyclerView(Context context, Cursor cursor, RecyclerView recyclerView){
        CustomAdapter newCustomAdapter = new CustomAdapter(context, cursor);
        recyclerView.setAdapter(newCustomAdapter);
        newCustomAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView twitText;
        private final TextView nameUser;
        private final TextView nickUser;
        private final ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            nameUser = (TextView) v.findViewById(R.id.name);
            nickUser = (TextView) v.findViewById(R.id.nick);
            twitText = (TextView) v.findViewById(R.id.textViewBody);
            imageView = (ImageView) v.findViewById(R.id.imageView);
        }
        public TextView getTwitView() {
            return twitText;
        }
        public TextView getNameView() {
            return nameUser;
        }
        public TextView getNickView() {
            return nickUser;
        }
        public ImageView getImageView() {
            return imageView;
        }
    }
}
