package babybed.hang.efan.babybed.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import babybed.hang.efan.babybed.R;

/**
 * Created by Efan on 2018/3/13.
 */

public class DrawerLayoutFooterRegridAdapter extends RecyclerView.Adapter<DrawerLayoutFooterRegridAdapter.ViewHolder> {
    private List<Integer> mData;

    private DrawerLayoutFooterRegridAdapter.OnItemClickListener onItemClickListener;

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(DrawerLayoutFooterRegridAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public DrawerLayoutFooterRegridAdapter(List<Integer> data) {
        this.mData = data;
    }

    public void updateData(List<Integer> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_regrid_drawerlayout_navigation_footer, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        holder.mImageView.setImageResource(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.img_drawerlayout_footer_item);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}

