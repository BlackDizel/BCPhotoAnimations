package org.byters.bcphotoanimations.view.ui.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterAdapterFrames;

import java.lang.ref.WeakReference;


public class AdapterFrames extends AdapterBase {

    private WeakReference<IPresenterAdapterFrames> refPresenterAdapterFrames;

    public AdapterFrames(IPresenterAdapterFrames presenterAdapterFrames) {
        this.refPresenterAdapterFrames = new WeakReference<>(presenterAdapterFrames);
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        if (refPresenterAdapterFrames.get().isTypeFrame(viewType))
            return new ViewHolderItem(parent);
        if (refPresenterAdapterFrames.get().isTypeFrameAdd(viewType))
            return new ViewHolderFrameAdd(parent);

        return null;
    }

    @Override
    public int getItemCount() {
        return refPresenterAdapterFrames.get().getItemsNum();
    }

    @Override
    public int getItemViewType(int position) {
        return refPresenterAdapterFrames.get().getItemViewType(position);
    }

    private class ViewHolderItem extends ViewHolderBase {

        private ImageView ivItem;
        private TextView tvTitle;

        ViewHolderItem(ViewGroup parent) {
            super(parent, R.layout.view_frame);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivItem = itemView.findViewById(R.id.ivItem);
        }

        @Override
        void setData(int position) {
            //todo set state setlected

            String imageUri = refPresenterAdapterFrames.get().getItemImageUri(position);
            if (TextUtils.isEmpty(imageUri))
                ivItem.setImageDrawable(null);
            else Glide.with(itemView.getContext())
                    .load(imageUri)
                    .into(ivItem);

            tvTitle.setText(String.valueOf(position));
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
            refPresenterAdapterFrames.get().onClickFrameAdd();
        }
    }
}
