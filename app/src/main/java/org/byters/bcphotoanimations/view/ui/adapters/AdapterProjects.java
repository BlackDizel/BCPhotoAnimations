package org.byters.bcphotoanimations.view.ui.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterProjectsCallback;

import javax.inject.Inject;

public class AdapterProjects extends AdapterBase {

    @Inject
    IPresenterAdapterProjects presenterAdapterProjects;
    private PresenterCallback presenterCallback;

    public AdapterProjects() {
        ApplicationStopMotion.getComponent().inject(this);
        presenterAdapterProjects.setCallback(presenterCallback = new PresenterCallback());
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        if (presenterAdapterProjects.isTypeProject(viewType))
            return new ViewHolderProject(parent);

        if (presenterAdapterProjects.isTypeProjectNew(viewType))
            return new ViewHolderProjectNew(parent);

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return presenterAdapterProjects.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return presenterAdapterProjects.getItemsSize();
    }

    private class ViewHolderProject extends ViewHolderItem implements View.OnLongClickListener {

        private TextView tvTitle;

        ViewHolderProject(ViewGroup parent) {
            super(parent, R.layout.view_item_project);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.findViewById(R.id.ivSettings).setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        void setData(int position) {
            String title = presenterAdapterProjects.getItemTitle(position);
            tvTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        }

        @Override
        public boolean onLongClick(View v) {
            presenterAdapterProjects.onClickLong(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ivSettings)
                presenterAdapterProjects.onClickSettings(getAdapterPosition());
            else
                super.onClick(v);
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
            presenterAdapterProjects.onClickItem(getAdapterPosition());
        }
    }
}
