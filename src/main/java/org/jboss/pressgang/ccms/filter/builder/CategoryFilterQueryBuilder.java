package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.CategoryFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class CategoryFilterQueryBuilder extends BaseFilterQueryBuilder<Category> {

    public CategoryFilterQueryBuilder(final EntityManager entityManager) {
        super(Category.class, new CategoryFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.CATEGORY_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("categoryId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "categoryName");
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "categoryDescription");
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR)) {
            final Boolean fieldValueBoolean = (Boolean) field.getData();
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().isTrue(getRootPath().get("mutuallyExclusive").as(Boolean.class)));
            }
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR)) {
            final Boolean fieldValueBoolean = (Boolean) field.getData();
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().isFalse(getRootPath().get("mutuallyExclusive").as(Boolean.class)));
            }
        } else {
            super.processField(field);
        }
    }

}
