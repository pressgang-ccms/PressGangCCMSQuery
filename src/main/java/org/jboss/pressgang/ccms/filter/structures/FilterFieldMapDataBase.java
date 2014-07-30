/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.filter.structures;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FilterFieldMapDataBase<U> extends FilterFieldDataBase<Map<Integer, U>> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldMapDataBase.class);

    /**
     * The data stored within this UIField
     */
    protected Map<Integer, U> data = null;
    /**
     * Whether or not this object has been "negated"
     */
    protected boolean negated = false;

    protected FilterFieldMapDataBase(final String name, final String description) {
        super(name, description);
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(final boolean negated) {
        this.negated = negated;
    }

    public abstract void put(Integer key, String value);

    @Override
    public void setData(String value) {
        if (isNullOrEmpty(value)) {
            data = null;
        } else {
            try {
                final List<String> vars = Arrays.asList(value.split(","));

                for (final String var : vars) {
                    String[] dataVars = var.split("=", 2);

                    put(Integer.parseInt(dataVars[0]), dataVars[1]);
                }
            } catch (final Exception ex) {
                // could not parse, so silently fail
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", name, value);
            }
        }
    }

    @Override
    public String getDataString() {
        if (data == null) return null;

        final StringBuilder retValue = new StringBuilder();

        for (final Map.Entry<Integer, U> entry : data.entrySet()) {
            if (retValue.length() > 0) retValue.append(",");

            retValue.append(entry.getKey() + "=" + entry.getValue());
        }

        return retValue.toString();
    }
}
