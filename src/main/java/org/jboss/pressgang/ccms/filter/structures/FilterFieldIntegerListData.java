package org.jboss.pressgang.ccms.filter.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFieldIntegerListData extends FilterFieldListDataBase<Integer> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldIntegerListData.class);
    private static final Pattern ID_REGEX = Pattern.compile("^(\\s*\\-?\\d+\\s*,?)*(\\s*\\-?\\d+\\s*)$");

    public FilterFieldIntegerListData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public List<Integer> getData() {
        return data;
    }

    @Override
    public void setData(List<Integer> data) {
        this.data = data;
    }

    @Override
    public void setData(String value) {
        if (ID_REGEX.matcher(value).matches()) {
            final List<Integer> idValues = new ArrayList<Integer>();
            for (final String id : value.split("\\s*,\\s*")) {
                idValues.add(Integer.parseInt(id.trim()));
            }
            data = idValues;
        } else {
            // could not parse, so silently fail
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", name, value);
        }
    }

    @Override
    public String getDataString() {
        return data == null ? null : CollectionUtilities.toSeperatedString(data, ",");
    }
}
