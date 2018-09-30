package com.appian.manchesterunitednews.app.table;

import com.appian.manchesterunitednews.app.widget.Section;
import com.appnet.android.football.sofa.data.TableRow;
import com.appnet.android.football.sofa.data.TableRowsData;

import java.util.ArrayList;
import java.util.List;

public class TableRowsSection implements Section<TableRow> {
    private final TableRowsData data;

    public TableRowsSection(TableRowsData data) {
        this.data = data;
    }

    @Override
    public List<TableRow> getChildItem() {
        return data.getTableRows();
    }

    public static List<TableRowsSection> valueOf(List<TableRowsData> tables) {
        List<TableRowsSection> data = new ArrayList<>();
        if(tables == null) {
            return data;
        }
        for(TableRowsData item : tables) {
            data.add(new TableRowsSection(item));
        }
        return data;
    }

    public TableRowsData getData() {
        return data;
    }
}
