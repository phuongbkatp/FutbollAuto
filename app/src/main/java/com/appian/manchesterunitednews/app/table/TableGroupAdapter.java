package com.appian.manchesterunitednews.app.table;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appnet.android.football.sofa.helper.SofaImageHelper;
import com.appian.manchesterunitednews.app.widget.SectionRecyclerViewAdapter;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appnet.android.football.sofa.data.Promotion;
import com.appnet.android.football.sofa.data.TableField;
import com.appnet.android.football.sofa.data.TableRow;
import com.appnet.android.football.sofa.data.TableRowsData;

import java.util.List;

class TableGroupAdapter extends SectionRecyclerViewAdapter<TableRowsSection,
        TableRow,
        GroupHeaderHolder,
        TableHolder> {
    private static final int[] COLOR_PROMOTIONS = { R.drawable.circle_green, R.drawable.circle_blue, R.drawable.circle_gray};
    private Context mContext;

    TableGroupAdapter(Context context, List<TableRowsSection> data) {
        super(data);
        mContext = context;
    }


    @Override
    public void onBindSectionViewHolder(GroupHeaderHolder sectionViewHolder, int sectionPosition, TableRowsSection section) {
        TableRowsData data = section.getData();
        if(data != null) {
            sectionViewHolder.TvGroup.setText(data.getName());
        }
    }

    @Override
    public void onBindChildViewHolder(TableHolder childViewHolder, int sectionPosition, int childPosition, TableRow child) {
        if (child.getTeam() != null) {
            childViewHolder.TvPos.setText(child.getPosition());
            childViewHolder.TvTeam.setText(child.getTeam().getShortName());
            ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(child.getTeam().getId()), childViewHolder.ImgLogo);
        }

        childViewHolder.TvPoint.setText(child.getPoints());
        TableField tableField = child.getTotalFields();
        if (tableField != null) {
            childViewHolder.TvGoals.setText(tableField.getGoalsTotal());
            childViewHolder.TvPlayed.setText(tableField.getMatchesTotal());
            childViewHolder.TvWin.setText(tableField.getWinTotal());
            childViewHolder.TvDraw.setText(tableField.getDrawTotal());
            childViewHolder.TvLoss.setText(tableField.getLossTotal());
        }
        Resources res = mContext.getResources();
        int defColor = R.color.color_promotion_none;
        Promotion promotion = child.getPromotion();
        if(promotion != null) {
            if(Constant.SOFA_STANDING_PROMOTION_RELEGATION.equalsIgnoreCase(promotion.getName())) {
                defColor = R.drawable.circle_red;
            } else {
                TableRowsData group = getSectionItem(sectionPosition).getData();
                if(group != null) {
                    List<Integer> promotions = group.getPromotionKeys();
                    int index = promotions.indexOf(promotion.getId());
                    if (index >= 0 && index < 3) {
                        defColor = COLOR_PROMOTIONS[index];
                    } else if (index >= 3) {
                        defColor = R.drawable.circle_black;
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            childViewHolder.TvPos.setBackground(res.getDrawable(defColor));
        } else {
            childViewHolder.TvPos.setBackgroundDrawable(res.getDrawable(defColor));
        }
        if(defColor != R.color.color_promotion_none) {
            childViewHolder.TvPos.setTextColor(res.getColor(R.color.globalWhite));
        } else {
            childViewHolder.TvPos.setTextColor(res.getColor(R.color.lightBlack));
        }
    }

    @Override
    public GroupHeaderHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.item_table_header, sectionViewGroup, false);
        return new GroupHeaderHolder(header);
    }

    @Override
    public TableHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_table_row, childViewGroup, false);
        return new TableHolder(row);
    }
}
