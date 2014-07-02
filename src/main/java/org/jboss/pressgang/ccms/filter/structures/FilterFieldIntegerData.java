package org.jboss.pressgang.ccms.filter.structures;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFieldIntegerData extends FilterFieldDataBase<Integer> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldIntegerData.class);

    public FilterFieldIntegerData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public Integer getData() {
        return data;
    }

    @Override
    public void setData(final Integer data) {
        this.data = data;
    }

    @Override
    public void setData(final String value) {
        try {
            data = isNullOrEmpty(value) ? null : Integer.parseInt(value);
        } catch (final NumberFormatException ex) {
            // could not parse integer, so silently fail
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", name, value);
        }
    }

    @Override
    public String toString() {
        return data == null ? null : data.toString();
    }
}
