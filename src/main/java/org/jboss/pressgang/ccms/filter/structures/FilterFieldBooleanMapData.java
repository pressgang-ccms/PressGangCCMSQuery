package org.jboss.pressgang.ccms.filter.structures;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFieldBooleanMapData extends FilterFieldMapDataBase<Boolean> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldBooleanMapData.class);

    public FilterFieldBooleanMapData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public void put(Integer key, String value) {
        if (data == null) {
            data = new HashMap<Integer, Boolean>();
        }

        if (Boolean.parseBoolean(value)) {
            data.put(key, true);
        } else {
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", name, value);
        }
    }

    @Override
    public Map<Integer, Boolean> getData() {
        return data;
    }

    @Override
    public void setData(Map<Integer, Boolean> data) {
        this.data = data;
    }
}
