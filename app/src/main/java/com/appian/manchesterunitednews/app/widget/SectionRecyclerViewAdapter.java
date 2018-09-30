package com.appian.manchesterunitednews.app.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class SectionRecyclerViewAdapter<S extends Section<C>, C,
        SVH extends RecyclerView.ViewHolder,
        CVH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SECTION_HEADER = -1;
    private static final int TYPE_SECTION_ITEM = -2;

    private List<SectionWrapper<S, C>> mFlatItems;
    private List<S> mData;

    private SectionRecyclerViewAdapter() {
    }

    public SectionRecyclerViewAdapter(List<S> data) {
        mData = data;
        generateSectionWrapper(mData);
    }

    private void generateSectionWrapper(final List<S> sections) {
        mFlatItems = new ArrayList<>();
        if(sections == null) {
            return;
        }
        int sizeSection = sections.size();
        for(int sectionPosition = 0; sectionPosition < sizeSection; sectionPosition++) {
            Section section = sections.get(sectionPosition);
            SectionWrapper wrapper = new SectionWrapper(section, sectionPosition);
            mFlatItems.add(wrapper);
            int size = section.getChildItem().size();
            for(int i = 0; i < size; i++) {
                SectionWrapper wrapperChild = new SectionWrapper(section.getChildItem().get(i), sectionPosition, i);
                mFlatItems.add(wrapperChild);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (isSectionViewType(viewType)) {
            return onCreateSectionViewHolder(viewGroup, viewType);
        } else {
            return onCreateChildViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int flatPosition) {
        if (flatPosition > mFlatItems.size()) {
            throw new IllegalStateException("Trying to bind item out of bounds, size " + mFlatItems.size()
                    + " flatPosition " + flatPosition + ". Was the data changed without a call to notify...()?");
        }
        SectionWrapper<S, C> sectionWrapper = mFlatItems.get(flatPosition);
        if (sectionWrapper.isSection()) {
            SVH sectionViewHolder = (SVH) holder;
            onBindSectionViewHolder(sectionViewHolder, sectionWrapper.getSectionPosition(), sectionWrapper.getSection());
        } else {
            CVH childViewHolder = (CVH) holder;
            onBindChildViewHolder(childViewHolder, sectionWrapper.getSectionPosition(), sectionWrapper.getChildPosition(), sectionWrapper.getChild());
        }
    }

    public abstract void onBindSectionViewHolder(SVH sectionViewHolder, int sectionPosition, S section);

    public abstract void onBindChildViewHolder(CVH childViewHolder, int sectionPosition, int childPosition, C child);

    @Override
    public int getItemCount() {
        return mFlatItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mFlatItems.get(position).isSection() ? TYPE_SECTION_HEADER : TYPE_SECTION_ITEM;
    }

    public void notifyDataChanged() {
        generateSectionWrapper(mData);
        notifyDataSetChanged();
    }

    public abstract SVH onCreateSectionViewHolder(final ViewGroup sectionViewGroup, int viewType);

    public abstract CVH onCreateChildViewHolder(final ViewGroup childViewGroup, int viewType);

    public boolean isSectionViewType(int viewType) {
        return viewType == TYPE_SECTION_HEADER;
    }

    public S getSectionItem(int sectionPosition) {
        if(mData == null) {
            return null;
        }
        return  mData.get(sectionPosition);
    }

    public SectionWrapper<S, C> getItem(int position) {
        return mFlatItems.get(position);
    }
}
