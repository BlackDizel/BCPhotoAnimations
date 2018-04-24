package org.byters.bcphotoanimations.view.ui.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterAdapterFrames;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterFramesCallback;

import javax.inject.Inject;

public class AdapterFrames extends AdapterBase {

    private final PresenterCallback presenterCallback;

    @Inject
    IPresenterAdapterFrames presenterAdapterFrames;

    public AdapterFrames() {
        ApplicationStopMotion.getComponent().inject(this);
        presenterAdapterFrames.setCallback(presenterCallback = new PresenterCallback());
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        if (presenterAdapterFrames.isTypeFrame(viewType))
            return new ViewHolderItem(parent);
        if (presenterAdapterFrames.isTypeFrameAdd(viewType))
            return new ViewHolderFrameAdd(parent);

        return null;
    }

    @Override
    public int getItemCount() {
        return presenterAdapterFrames.getItemsNum();
    }

    @Override
    public int getItemViewType(int position) {
        return presenterAdapterFrames.getItemViewType(position);
    }

    private class ViewHolderItem extends ViewHolderBase
            implements View.OnClickListener, View.OnLongClickListener {

        private ImageView ivItem;
        private TextView tvTitle;
        private View vSelected;

        ViewHolderItem(ViewGroup parent) {
            super(parent, R.layout.view_frame);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivItem = itemView.findViewById(R.id.ivItem);
            vSelected = itemView.findViewById(R.id.flSelected);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        void setData(int position) {

            String imageUri = presenterAdapterFrames.getItemImageUri(position);
            if (TextUtils.isEmpty(imageUri))
                ivItem.setImageDrawable(null);
            else Glide.with(itemView.getContext())
                    .load(imageUri)
                    .into(ivItem);

            tvTitle.setText(getTextPosition(position));

            vSelected.setVisibility(presenterAdapterFrames.isSelected(position) ? View.VISIBLE : View.GONE);
        }

        private String getTextPosition(int position) {
            return String.valueOf(position + 1);
        }

        @Override
        public void onClick(View v) {
            presenterAdapterFrames.onClickItem(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            presenterAdapterFrames.onLongClickItem(getAdapterPosition());
            return true;
        }
    }

    private class ViewHolderFrameAdd extends ViewHolderBase implements View.OnClickListener {

        ViewHolderFrameAdd(ViewGroup parent) {
            super(parent, R.layout.view_frame_add);
            itemView.setOnClickListener(this);
        }

        @Override
        void setData(int position) {

        }

        @Override
        public void onClick(View v) {
            presenterAdapterFrames.onClickFrameAdd();
        }
    }

    private class PresenterCallback implements IPresenterAdapterFramesCallback {
        @Override
        public void onUpdate() {
            notifyDataSetChanged();
        }
    }
}
