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

import java.util.List;

public abstract class FilterFieldListDataBase<T> extends FilterFieldDataBase<List<T>> {

    /**
     * The data stored within this UIField
     */
    protected List<T> data = null;
    /**
     * Whether or not this object has been "negated"
     */
    protected boolean negated = false;

    protected FilterFieldListDataBase(final String name, final String description) {
        super(name, description);
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(final boolean negated) {
        this.negated = negated;
    }
}
