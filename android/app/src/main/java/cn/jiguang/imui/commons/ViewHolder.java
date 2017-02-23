package cn.jiguang.imui.commons;


import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ViewHolder<DATA> extends RecyclerView.ViewHolder {

    public abstract void onBind(DATA data);

    public ViewHolder(View itemView) {
        super(itemView);
    }
}
