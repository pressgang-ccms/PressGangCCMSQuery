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

import java.util.List;

import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;

public class FilterFieldStringData extends FilterFieldDataBase<String> {
    private FilterStringLogic searchLogic = FilterStringLogic.CONTAINS;
    private boolean caseInsensitive = true;

    public FilterFieldStringData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public String getName() {
        if (searchLogic == FilterStringLogic.CONTAINS) {
            return super.getName();
        } else {
            return super.getName() + searchLogic.toString();
        }
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(final String data) {
        this.data = data;
    }

    public <T> void setData(final List<T> data) {
        this.data = CollectionUtilities.toSeperatedString(data, ",");
    }

    @Override
    public String toString() {
        return data == null ? null : data.toString();
    }

    public FilterStringLogic getSearchLogic() {
        return searchLogic;
    }

    public void setSearchLogic(FilterStringLogic searchLogic) {
        this.searchLogic = searchLogic;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }
}
