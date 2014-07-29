/*
  Copyright 2011-2014 Red Hat

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
