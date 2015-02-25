package com.cuber.cuberlovematerial;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cuber on 2015/2/14.
 */
public class List1Activity extends ActionBarActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_1);
        recyclerView = (RecyclerView)findViewById(R.id.list1);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ListViewAdapter());
    }

    private class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            if (viewType == TYPE_ITEM) {
                LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View item = inflater.inflate(R.layout.item_list_1, viewGroup, false);
                return new ViewHolderItem(item);

            } else if (viewType == TYPE_HEADER) {
                LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View subhead = inflater.inflate(R.layout.item_list_subhead_1, viewGroup, false);
                return new ViewHolderHeader(subhead);
            }

            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof ViewHolderHeader) {
                //cast holder to VHItem and set data
            } else if (viewHolder instanceof ViewHolderItem) {
                //cast holder to VHHeader and set data for header.
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;

            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        public class ViewHolderHeader extends RecyclerView.ViewHolder {
            TextView Subhead;

            public ViewHolderHeader(View itemView) {
                super(itemView);
                Subhead = (TextView) itemView.findViewById(R.id.list_subhead);
            }
        }

        public class ViewHolderItem extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView body2;
            TextView body1;
            TextView caption;

            public ViewHolderItem(View itemView) {
                super(itemView);
                imageView = (ImageView)itemView.findViewById(R.id.list_imageView);
                body2 = (TextView) itemView.findViewById(R.id.list_body2);
                body1 = (TextView) itemView.findViewById(R.id.list_body1);
                caption = (TextView) itemView.findViewById(R.id.list_caption);
            }
        }
    }
}
