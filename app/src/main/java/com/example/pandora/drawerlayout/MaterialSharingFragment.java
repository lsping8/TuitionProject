package com.example.pandora.drawerlayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pandora.drawerlayout.DataForServer.ForumData;
import com.example.pandora.drawerlayout.DataForServer.MaterialData;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/9/2016.
 */
public class MaterialSharingFragment extends Fragment {

    View view;
    float height,width;
    BitmapConfig bitmapConfig;
    ImageView shareBtn;
    ArrayList<MaterialData> materialData;
    RecyclerView recyclerView;
    MaterialSharingRecycleView adapter;

    public MaterialSharingFragment(ForumData forumData){
        materialData = forumData.getMaterialData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_material,container,false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        shareBtn = (ImageView)view.findViewById(R.id.postBtn);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FileDialog fileDialog = new FileDialog(materialData.get(position));
                fileDialog.show(getActivity().getFragmentManager(),null);
            }

            @Override
            public void onLongClick(View view, int position) {
                adapter.remove(position);
            }
        }));

        float scale = getContext().getResources().getDisplayMetrics().density;
        adapter = new MaterialSharingRecycleView();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        shareBtn.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(),R.drawable.post_image,(int)(width/6),(int)(width/6)),0));
        shareBtn.setY(height - 650);
        shareBtn.setX(width - width/6 - width/21.6f);
        shareBtn.setOnClickListener(onClickListener);

        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            UniversalDialog universalDialog = new UniversalDialog(adapter,materialData,"materialSharing");
            universalDialog.show(getActivity().getFragmentManager(),null);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public class MaterialSharingRecycleView extends RecyclerView.Adapter<MaterialFolderViewHolder>{

        @Override
        public MaterialFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_folder_material, parent, false);
            return new MaterialFolderViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MaterialFolderViewHolder holder, int position) {
            holder.folderIcon.setImageBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(), R.drawable.folder_icon, (int) (width / 3), (int) (width / 3)));
            holder.folderName.setText(materialData.get(position).getFolderName());
        }

        @Override
        public int getItemCount() {
            Log.d("getCount",materialData.size()+"");
            return materialData.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public void insert(String folderName) {
            materialData.add(new MaterialData(folderName));
            notifyItemInserted(getItemCount());
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(int position) {
            materialData.remove(position);
            notifyItemRemoved(position);
        }
    }

    private class MaterialFolderViewHolder extends RecyclerView.ViewHolder {

        ImageView folderIcon;
        TextView folderName;

        private MaterialFolderViewHolder(View itemView) {
            super(itemView);
            folderIcon = (ImageView)itemView.findViewById(R.id.folderIcon);
            folderName = (TextView)itemView.findViewById(R.id.folderName);
        }
    }

}
