package com.appian.manchesterunitednews.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appnet.android.football.fbvn.data.Cell;
import com.appnet.android.football.fbvn.data.ColumnHeader;
import com.evrencoskun.tableview.TableView;

import java.util.List;

/**
 * Created by phuongbkatp on 10/14/2018.
 */

public class CustomTableLayout extends LinearLayout {
    Context mContext;
    public CustomTableLayout(Context context,String title, List<ColumnHeader> columnList, List<List<Cell>> cellList ) {
        super(context);
        mContext = context;
        setOrientation(VERTICAL);
        initTable(title, columnList, cellList);
    }
    private void initTable (String title,List<ColumnHeader> columnList, List<List<Cell>> cellList) {
        View view = inflate(mContext, R.layout.custom_table_layout, null);

        TextView tit = view.findViewById(R.id.txt_caption);
        tit.setText(title);

        TableView tableView = view.findViewById(R.id.table_layout);
        // Create our custom TableView Adapter
        MyTableViewAdapter adapter = new MyTableViewAdapter(mContext);

        // Set this adapter to the our TableView
        tableView.setAdapter(adapter);

        // Let's set datas of the TableView on the Adapter
        adapter.setAllItems(columnList, null, cellList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
        addView(view, params);
    }
}
