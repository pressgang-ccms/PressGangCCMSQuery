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

package org.jboss.pressgang.ccms.filter.constants;

import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class FilterConstants {
    /**
     * The default logic to be applied to tags within a category
     */
    public static final String DEFAULT_INTERNAL_LOGIC = CommonFilterConstants.OR_LOGIC;
    /**
     * The default logic to be applied between categories
     */
    public static final String DEFAULT_EXTERNAL_LOGIC = CommonFilterConstants.AND_LOGIC;
    /**
     * The default internal category logic state
     */
    public static final int CATEGORY_INTERNAL_DEFAULT_STATE = CommonFilterConstants.CATEGORY_INTERNAL_OR_STATE;
    /**
     * The default external category logic state
     */
    public static final int CATEGORY_EXTERNAL_DEFAULT_STATE = CommonFilterConstants.CATEGORY_EXTERNAL_AND_STATE;
    /**
     * The default logic to be applied to the search fields
     */
    public static final String LOGIC_FILTER_VAR_DEFAULT_VALUE = "and";
}
