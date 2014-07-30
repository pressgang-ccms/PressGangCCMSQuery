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

public abstract class FilterFieldDataBase<T> {
    /**
     * The data stored within this UIField
     */
    protected T data = null;
    /**
     * The name of the data field
     */
    protected String name = "";
    /**
     * The description
     */
    protected String description = "";

    protected FilterFieldDataBase(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getBaseName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public abstract T getData();

    public String getDataString() {
        return data.toString();
    }

    public abstract void setData(T data);

    public abstract void setData(String value);
}
