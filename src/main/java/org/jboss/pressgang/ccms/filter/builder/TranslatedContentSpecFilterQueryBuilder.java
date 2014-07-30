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

package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.filter.TranslatedContentSpecFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedContentSpec;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslatedContentSpecFilterQueryBuilder extends BaseFilterQueryBuilder<TranslatedContentSpec> {
    private static final Logger LOG = LoggerFactory.getLogger(TranslatedContentSpecFilterQueryBuilder.class);

    public TranslatedContentSpecFilterQueryBuilder(final EntityManager entityManager) {
        super(TranslatedContentSpec.class, new TranslatedContentSpecFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.TRANSLATED_CONTENT_SPEC_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("translatedContentSpecId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.ZANATA_IDS_FILTER_VAR)) {
            final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
            final List<Predicate> conditions = new ArrayList<Predicate>();

            final List<String> zanataIds = (List<String>) field.getData();
            for (final String zanataId : zanataIds) {
                try {
                    conditions.add(getZanataIdCondition(zanataId));
                } catch (NumberFormatException ex) {
                    LOG.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, zanataId);
                }
            }

            // Only add the query if we found valid zanata ids
            if (conditions.size() > 1) {
                final Predicate[] predicates = conditions.toArray(new Predicate[conditions.size()]);
                addFieldCondition(criteriaBuilder.or(predicates));
            } else if (conditions.size() == 1) {
                addFieldCondition(conditions.get(0));
            }
        } else if (fieldName.equals(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR)) {
            final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
            final List<Predicate> conditions = new ArrayList<Predicate>();

            final List<String> zanataIds = (List<String>) field.getData();
            for (final String zanataId : zanataIds) {
                try {
                    conditions.add(getZanataIdCondition(zanataId));
                } catch (NumberFormatException ex) {
                    LOG.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, zanataId);
                }
            }

            // Only add the query if we found valid zanata ids
            if (conditions.size() > 1) {
                final Predicate[] predicates = conditions.toArray(new Predicate[conditions.size()]);
                addFieldCondition(criteriaBuilder.not(criteriaBuilder.or(predicates)));
            } else if (conditions.size() == 1) {
                addFieldCondition(conditions.get(0));
            }
        } else {
            super.processField(field);
        }
    }

    /**
     * Gets a JPA Predicate condition, to find TranslatedContentSpecs that match a Zanata Document ID in the form of
     * {@code "CS<ID>-<REVISION>"}.
     *
     * @param zanataId The Zanata Document ID.
     * @return A Predicate object that contains the SQL WHERE logic to find TranslatedContentSpecs that match the Zanata ID.
     * @throws NumberFormatException Thrown if the ZanataID, when broken down can't be transformed into an Integer.
     */
    protected Predicate getZanataIdCondition(final String zanataId) throws NumberFormatException {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        String[] zanataVars = zanataId.split("-");

        final Integer contentSpecId = Integer.parseInt(zanataVars[0].replaceAll("^CS", ""));
        final Integer contentSpecRevision = Integer.parseInt(zanataVars[1]);

        final Predicate contentSpecIdCondition = criteriaBuilder.equal(getRootPath().get("contentSpecId"), contentSpecId);
        final Predicate contentSpecRevisionCondition = criteriaBuilder.equal(getRootPath().get("contentSpecRevision"),
                contentSpecRevision);

        return criteriaBuilder.and(contentSpecIdCondition, contentSpecRevisionCondition);
    }
}
