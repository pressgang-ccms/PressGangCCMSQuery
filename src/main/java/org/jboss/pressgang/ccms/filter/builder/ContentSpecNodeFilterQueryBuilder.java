package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ContentSpecNodeFilterQueryBuilder extends BaseFilterQueryBuilder<CSNode> {
    public ContentSpecNodeFilterQueryBuilder(final EntityManager entityManager) {
        super(CSNode.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches(ID_REGEX)) {
                addIdInCommaSeparatedListCondition("CSNodeId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("CSNodeTitle", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR)) {
            addFieldCondition(getCriteriaBuilder().equal(getRootPath().get("CSNodeType").as(Integer.class), Integer.parseInt(fieldValue)));
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}