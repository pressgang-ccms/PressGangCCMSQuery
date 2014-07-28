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

package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.List;

import org.jboss.pressgang.ccms.filter.ImageFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.filter.structures.FilterStringLogic;
import org.jboss.pressgang.ccms.model.ImageFile;
import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ImageFilterQueryBuilder extends BaseFilterQueryBuilder<ImageFile> implements ILocaleFilterQueryBuilder {
    public ImageFilterQueryBuilder(final EntityManager entityManager) {
        super(ImageFile.class, new ImageFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.IMAGE_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("imageFileId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "description");
        } else if (fieldName.equals(CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getImagesWithFileNameSubquery(stringField.getData(), stringField.getSearchLogic()));
        } else {
            super.processField(field);
        }
    }

    @Override
    public Predicate getMatchingLocaleString(final String locale) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<LanguageImage> subquery = getCriteriaQuery().subquery(LanguageImage.class);
        final Root<LanguageImage> from = subquery.from(LanguageImage.class);
        final Predicate languageImageEqual = criteriaBuilder.equal(getRootPath(), from.get("imageFile"));
        final Predicate localeEqual = criteriaBuilder.equal(from.get("locale"), locale);
        subquery.where(criteriaBuilder.and(languageImageEqual, localeEqual));

        return criteriaBuilder.exists(subquery);
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<LanguageImage> subquery = getCriteriaQuery().subquery(LanguageImage.class);
        final Root<LanguageImage> from = subquery.from(LanguageImage.class);
        final Predicate languageImageEqual = criteriaBuilder.equal(getRootPath(), from.get("imageFile"));
        final Predicate localeEqual = criteriaBuilder.equal(from.get("locale"), locale);
        subquery.where(criteriaBuilder.and(languageImageEqual, localeEqual));

        return criteriaBuilder.not(criteriaBuilder.exists(subquery));
    }

    /**
     * Create a Subquery to check if a image has a specific filename.
     *
     * @param filename The Value that the property tag should have.
     * @return A subquery that can be used in an exists statement to see if a topic has a property tag with the specified value.
     */
    private Subquery<ImageFile> getImagesWithFileNameSubquery(final String filename, final FilterStringLogic searchLogic) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<ImageFile> subQuery = getCriteriaQuery().subquery(ImageFile.class);
        final Root<LanguageImage> root = subQuery.from(LanguageImage.class);
        subQuery.select(root.get("imageFile").as(ImageFile.class));

        // Create the Condition for the subquery
        final Predicate imageIdMatch = criteriaBuilder.equal(getRootPath().get("imageFileId"), root.get("imageFile").get("imageFileId"));
        final Predicate filenameMatch;
        if (searchLogic == FilterStringLogic.MATCHES) {
            filenameMatch = criteriaBuilder.equal(root.get("originalFileName"), filename);
        } else {
            filenameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("originalFileName").as(String.class)),
                    '%' + cleanLikeCondition(filename).toLowerCase() + '%');
        }
        subQuery.where(criteriaBuilder.and(imageIdMatch, filenameMatch));

        return subQuery;
    }
}
