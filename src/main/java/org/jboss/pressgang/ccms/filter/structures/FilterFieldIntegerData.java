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
            data = (value == null ? null : Integer.parseInt(value));
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
