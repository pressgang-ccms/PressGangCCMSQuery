package org.jboss.pressgang.ccms.filter.structures;

import java.util.HashMap;
import java.util.Map;

public class FilterFieldStringMapData extends FilterFieldMapDataBase<String> {
    public FilterFieldStringMapData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public void put(Integer key, String value) {
        if (data == null) {
            data = new HashMap<Integer, String>();
        }

        data.put(key, value);
    }

    @Override
    public Map<Integer, String> getData() {
        return data;
    }

    @Override
    public void setData(Map<Integer, String> data) {
        this.data = data;
    }
}
