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

package org.jboss.pressgang.ccms.filter.base;

import javax.persistence.criteria.Predicate;

/**
 * An Interface that defines that a Query Builder can search against matching tags.
 *
 * @author lnewson
 */
public interface ITagFilterQueryBuilder {
    /**
     * Create a Filter Query Condition that will ensure the entity has a matching specified tag.
     *
     * @param tagId The ID of the tag that should exist for the entity.
     * @return A Predicate object that holds the Query Condition for the match.
     */
    Predicate getMatchTagString(Integer tagId);

    /**
     * Create a Filter Query Condition that will ensure the entity doesn't have a matching specified tag.
     *
     * @param tagId The ID of the tag that shouldn't exist for the entity.
     * @return A Predicate object that holds the Query Condition for the match.
     */
    Predicate getNotMatchTagString(Integer tagId);
}
