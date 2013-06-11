package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.File;
import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class FileFilterQueryBuilder extends BaseFilterQueryBuilder<File> implements ILocaleFilterQueryBuilder {
    public FileFilterQueryBuilder(final EntityManager entityManager) {
        super(File.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.FILE_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches(ID_REGEX)) {
                addIdInCommaSeparatedListCondition("fileId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.FILE_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("description", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.FILE_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("fileName", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
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
