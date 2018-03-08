package org.byters.bcphotoanimations.view.ui.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterProjectsCallback;

import java.lang.ref.WeakReference;

public class AdapterProjects extends AdapterBase {

    private PresenterCallback presenterCallback;
    private WeakReference<IPresenterAdapterProjects> refPresenterAdapterProjects;

    public AdapterProjects(IPresenterAdapterProjects presenterAdapterProjects) {
        this.refPresenterAdapterProjects = new WeakReference<>(presenterAdapterProjects);
        this.refPresenterAdapterProjects.get().setCallback(presenterCallback = new PresenterCallback());
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        if (refPresenterAdapterProjects.get().isTypeProject(viewType))
            return new ViewHolderProject(parent);

        if (refPresenterAdapterProjects.get().isTypeProjectNew(viewType))
            return new ViewHolderProjectNew(parent);

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return refPresenterAdapterProjects.get().getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return refPresenterAdapterProjects.get().getItemsSize();
    }

    private class ViewHolderProject extends ViewHolderItem implements View.OnLongClickListener {

        private TextView tvTitle;

        ViewHolderProject(ViewGroup parent) {
            super(parent, R.layout.view_item_project);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnLongClickListener(this);
        }

        @Override
        void setData(int position) {
            String title = refPresenterAdapterProjects.get().getItemTitle(position);
            tvTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        }

        @Override
        public boolean onLongClick(View v) {
            refPresenterAdapterProjects.get().onClickLong(getAdapterPosition());
            return true;
        }
    }

    private class ViewHolderProjectNew extends ViewHolderItem {
        ViewHolderProjectNew(ViewGroup parent) {
            super(parent, R.layout.view_item_project_new);
        }

        @Override
        void setData(int position) {

        }
    }

    private class PresenterCallback implements IPresenterAdapterProjectsCallback {
        @Override
        public void onUpdate() {
            notifyDataSetChanged();
        }
    }

    private abstract class ViewHolderItem extends ViewHolderBase implements View.OnClickListener {

        ViewHolderItem(ViewGroup container, int layoutRes) {
            super(container, layoutRes);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            refPresenterAdapterProjects.get().onClickItem(getAdapterPosition());
        }
    }
}
