/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.filter.structures;

import java.util.Arrays;
import java.util.List;

import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFieldStringListData extends FilterFieldListDataBase<String> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldStringListData.class);

    public FilterFieldStringListData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public List<String> getData() {
        return data;
    }

    @Override
    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public void setData(String value) {
        try {
            data = Arrays.asList(value.split("\\s*,\\s*"));
        } catch (final Exception ex) {
            // could not parse, so silently fail
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", name, value);
        }
    }

    @Override
    public String getDataString() {
        return data == null ? null : CollectionUtilities.toSeperatedString(data, ",");
    }
}
