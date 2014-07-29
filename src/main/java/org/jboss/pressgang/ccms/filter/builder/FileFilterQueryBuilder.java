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

package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.List;

import org.jboss.pressgang.ccms.filter.FileFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.File;
import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class FileFilterQueryBuilder extends BaseFilterQueryBuilder<File> implements ILocaleFilterQueryBuilder {
    public FileFilterQueryBuilder(final EntityManager entityManager) {
        super(File.class, new FileFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.FILE_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("fileId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.FILE_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "description");
        } else if (fieldName.equals(CommonFilterConstants.FILE_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "fileName");
        } else {
            super.processField(field);
        }
    }

    @Override
    public Predicate getMatchingLocaleString(final String locale) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<LanguageFile> subquery = getCriteriaQuery().subquery(LanguageFile.class);
        final Root<LanguageFile> from = subquery.from(LanguageFile.class);
        final Predicate languageFileEqual = criteriaBuilder.equal(getRootPath(), from.get("file"));
        final Predicate localeEqual = criteriaBuilder.equal(from.get("locale"), locale);
        subquery.where(criteriaBuilder.and(languageFileEqual, localeEqual));

        return criteriaBuilder.exists(subquery);
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<LanguageFile> subquery = getCriteriaQuery().subquery(LanguageFile.class);
        final Root<LanguageFile> from = subquery.from(LanguageFile.class);
        final Predicate languageFileEqual = criteriaBuilder.equal(getRootPath(), from.get("file"));
        final Predicate localeEqual = criteriaBuilder.equal(from.get("locale"), locale);
        subquery.where(criteriaBuilder.and(languageFileEqual, localeEqual));

        return criteriaBuilder.not(criteriaBuilder.exists(subquery));
    }
}
