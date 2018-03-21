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

import javax.inject.Inject;


public class AdapterFrames extends AdapterBase {

    @Inject
    IPresenterAdapterFrames presenterAdapterFrames;

    public AdapterFrames() {
        ApplicationStopMotion.getComponent().inject(this);
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
            implements View.OnClickListener {

        private ImageView ivItem;
        private TextView tvTitle;

        ViewHolderItem(ViewGroup parent) {
            super(parent, R.layout.view_frame);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivItem = itemView.findViewById(R.id.ivItem);
            itemView.setOnClickListener(this);
        }

        @Override
        void setData(int position) {
            //todo set state setlected

            String imageUri = presenterAdapterFrames.getItemImageUri(position);
            if (TextUtils.isEmpty(imageUri))
                ivItem.setImageDrawable(null);
            else Glide.with(itemView.getContext())
                    .load(imageUri)
                    .into(ivItem);

            tvTitle.setText(getTextPosition(position));
        }

        private String getTextPosition(int position) {
            return String.valueOf(position + 1);
        }

        @Override
        public void onClick(View v) {

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
}
