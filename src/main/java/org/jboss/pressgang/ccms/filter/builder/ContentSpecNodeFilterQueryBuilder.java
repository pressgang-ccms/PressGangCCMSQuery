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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

import org.jboss.pressgang.ccms.filter.ContentSpecNodeFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.contentspec.CSInfoNode;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ContentSpecNodeFilterQueryBuilder extends BaseFilterQueryBuilder<CSNode> {
    private final Join<CSInfoNode, CSNode> csInfoNode;

    public ContentSpecNodeFilterQueryBuilder(final EntityManager entityManager) {
        super(CSNode.class, new ContentSpecNodeFieldFilter(), entityManager);
        csInfoNode = ((Root< CSNode >) getRootPath()).join("CSInfoNode", JoinType.LEFT);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("CSNodeId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "CSNodeTitle");
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR)) {
            addIdInCollectionCondition("CSNodeType", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_ID_FILTER_VAR)) {
            addEqualsCondition("entityId", (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_REVISION_FILTER_VAR)) {
            addEqualsCondition("entityRevision", (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_ID_FILTER_VAR)) {
            addEqualsCondition(csInfoNode.get("topicId").as(Integer.class), (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_REVISION_FILTER_VAR)) {
            addEqualsCondition(csInfoNode.get("topicRevision").as(Integer.class), (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR)) {
            addIdInCollectionCondition(getRootPath().get("contentSpec").get("contentSpecId"), (List<Integer>) field.getData());
        } else {
            super.processField(field);
        }
    }
}
