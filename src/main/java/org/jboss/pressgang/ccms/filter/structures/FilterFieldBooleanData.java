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

import static com.google.common.base.Strings.isNullOrEmpty;

public class FilterFieldBooleanData extends FilterFieldDataBase<Boolean> {

    public FilterFieldBooleanData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public Boolean getData() {
        return data;
    }

    @Override
    public void setData(final Boolean data) {
        this.data = data ? true : null;
    }

    @Override
    public void setData(final String value) {
        data = isNullOrEmpty(value) ? null : (Boolean.parseBoolean(value) ? true : null);
    }

    @Override
    public String toString() {
        return data == null ? "" : data.toString();
    }
}
