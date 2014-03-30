package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.ContentSpecNodeFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ContentSpecNodeFilterQueryBuilder extends BaseFilterQueryBuilder<CSNode> {
    public ContentSpecNodeFilterQueryBuilder(final EntityManager entityManager) {
        super(CSNode.class, new ContentSpecNodeFieldFilter(), entityManager);
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
            addEqualsCondition(getRootPath().get("CSInfoNode").get("topicId").as(Integer.class), (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_REVISION_FILTER_VAR)) {
            addEqualsCondition(getRootPath().get("CSInfoNode").get("topicRevision").as(Integer.class), (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR)) {
            addIdInCollectionCondition(getRootPath().get("contentSpec").get("contentSpecId"), (List<Integer>) field.getData());
        } else {
            super.processField(field);
        }
    }
}
