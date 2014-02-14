package org.jboss.pressgang.ccms.filter.base;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldBooleanData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDateTimeData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldMapDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.FilterField;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

/**
 * This class provides a mechanism to temporarily store and easily convert a set of fields for a filter until it needs to be
 * saved to a database entity. This is also used by the Seam GUI to store the data temporarily.
 */
public abstract class BaseTopicFieldFilter extends BaseFieldFilterWithProperties {
    /**
     * A map of the base filter field names that can not have multiple mappings
     */
    protected static final Map<String, String> singleFilterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = -6343139695468503659L;

        {
            put(CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR, CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_IDS_FILTER_VAR, CommonFilterConstants.TOPIC_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC, CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC_DESC);
            put(CommonFilterConstants.TOPIC_XML_FILTER_VAR, CommonFilterConstants.TOPIC_XML_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_TITLE_FILTER_VAR, CommonFilterConstants.TOPIC_TITLE_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR, CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.STARTDATE_FILTER_VAR, CommonFilterConstants.STARTDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.ENDDATE_FILTER_VAR, CommonFilterConstants.ENDDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.STARTEDITDATE_FILTER_VAR, CommonFilterConstants.STARTEDITDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.ENDEDITDATE_FILTER_VAR, CommonFilterConstants.ENDEDITDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS, CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS, CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS_DESC);
            put(CommonFilterConstants.TOPIC_RELATED_TO, CommonFilterConstants.TOPIC_RELATED_TO_DESC);
            put(CommonFilterConstants.TOPIC_RELATED_FROM, CommonFilterConstants.TOPIC_RELATED_FROM_DESC);
            put(CommonFilterConstants.TOPIC_HAS_XML_ERRORS, CommonFilterConstants.TOPIC_HAS_XML_ERRORS_DESC);
            put(CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS, CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS_DESC);
            put(CommonFilterConstants.TOPIC_EDITED_IN_LAST_MINUTES, CommonFilterConstants.TOPIC_EDITED_IN_LAST_MINUTES_DESC);
            put(CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS, CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS, CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS_DESC);

            put(CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR, CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC, CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC_DESC);
            put(CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR, CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR, CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR, CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_NOT_RELATED_TO, CommonFilterConstants.TOPIC_NOT_RELATED_TO_DESC);
            put(CommonFilterConstants.TOPIC_NOT_RELATED_FROM, CommonFilterConstants.TOPIC_NOT_RELATED_FROM_DESC);
            put(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS, CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS_DESC);
            put(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_MINUTES, CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_MINUTES_DESC);

            put(CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS, CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS, CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS, CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS, CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS,
                    CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS_DESC);
            put(CommonFilterConstants.TOPIC_FORMAT_VAR, CommonFilterConstants.TOPIC_FORMAT_VAR_DESC);
            put(CommonFilterConstants.TOPIC_NOT_FORMAT_VAR, CommonFilterConstants.TOPIC_NOT_FORMAT_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData topicIds;
    private FilterFieldIntegerListData notTopicIds;
    private FilterFieldIntegerData topicRelatedTo;
    private FilterFieldIntegerData notTopicRelatedTo;
    private FilterFieldIntegerData topicRelatedFrom;
    private FilterFieldIntegerData notTopicRelatedFrom;
    private FilterFieldStringData topicTitle;
    private FilterFieldStringData notTopicTitle;
    private FilterFieldStringData topicDescription;
    private FilterFieldStringData notTopicDescription;
    private FilterFieldDateTimeData startCreateDate;
    private FilterFieldDateTimeData endCreateDate;
    private FilterFieldStringData topicXML;
    private FilterFieldStringData notTopicXML;
    private FilterFieldBooleanData hasRelationships;
    private FilterFieldBooleanData hasIncomingRelationships;
    private FilterFieldStringData topicTextSearch;
    private FilterFieldBooleanData hasXMLErrors;
    private FilterFieldDateTimeData startEditDate;
    private FilterFieldDateTimeData endEditDate;
    private FilterFieldIntegerData editedInLastDays;
    private FilterFieldIntegerData notEditedInLastDays;
    private FilterFieldIntegerData editedInLastMins;
    private FilterFieldIntegerData notEditedInLastMins;
    private FilterFieldBooleanData hasOpenBugzillaBugs;
    private FilterFieldBooleanData hasBugzillaBugs;
    private FilterFieldIntegerListData topicIncludedInSpec;
    private FilterFieldIntegerListData notTopicIncludedInSpec;
    private FilterFieldIntegerData topicFormat;
    private FilterFieldIntegerData topicNotFormat;

    private FilterFieldBooleanData notHasXMLErrors;
    private FilterFieldBooleanData notHasRelationships;
    private FilterFieldBooleanData notHasIncomingRelationships;
    private FilterFieldBooleanData notHasOpenBugzillaBugs;
    private FilterFieldBooleanData notHasBugzillaBugs;

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        /* Topic ID's */
        topicIds = new FilterFieldIntegerListData(CommonFilterConstants.TOPIC_IDS_FILTER_VAR,
                CommonFilterConstants.TOPIC_IDS_FILTER_VAR_DESC);
        notTopicIds = new FilterFieldIntegerListData(CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR_DESC);

        /* Topic Related To */
        topicRelatedTo = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_RELATED_TO, CommonFilterConstants.TOPIC_RELATED_TO_DESC);
        notTopicRelatedTo = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_RELATED_TO,
                CommonFilterConstants.TOPIC_NOT_RELATED_TO_DESC);

        /* Topic Related From */
        topicRelatedFrom = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_RELATED_FROM,
                CommonFilterConstants.TOPIC_RELATED_FROM_DESC);
        notTopicRelatedFrom = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_RELATED_FROM,
                CommonFilterConstants.TOPIC_NOT_RELATED_FROM_DESC);

        /* Topic Title */
        topicTitle = new FilterFieldStringData(CommonFilterConstants.TOPIC_TITLE_FILTER_VAR,
                CommonFilterConstants.TOPIC_TITLE_FILTER_VAR_DESC);
        notTopicTitle = new FilterFieldStringData(CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR_DESC);

        /* Topic Description */
        topicDescription = new FilterFieldStringData(CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR_DESC);
        notTopicDescription = new FilterFieldStringData(CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR_DESC);

        /* Topic is included in content specification */
        topicIncludedInSpec = new FilterFieldIntegerListData(CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC,
                CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC_DESC);
        notTopicIncludedInSpec = new FilterFieldIntegerListData(CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC,
                CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC_DESC);

        /* Topic XML */
        topicXML = new FilterFieldStringData(CommonFilterConstants.TOPIC_XML_FILTER_VAR, CommonFilterConstants.TOPIC_XML_FILTER_VAR_DESC);
        notTopicXML = new FilterFieldStringData(CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR_DESC);

        /* Topic Edited in last days */
        editedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS_DESC);
        notEditedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS_DESC);

        /* Topic Edited in last minutes */
        editedInLastMins = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_EDITED_IN_LAST_MINUTES,
                CommonFilterConstants.TOPIC_EDITED_IN_LAST_MINUTES_DESC);
        notEditedInLastMins = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_MINUTES,
                CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_MINUTES_DESC);

        /* Has XML Errors */
        hasXMLErrors = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_XML_ERRORS,
                CommonFilterConstants.TOPIC_HAS_XML_ERRORS_DESC);
        notHasXMLErrors = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS,
                CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS_DESC);

        /* Has Relationships */
        hasRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS_DESC);
        notHasRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS_DESC);

        /* Has Incoming Relationships */
        hasIncomingRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS_DESC);
        notHasIncomingRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS_DESC);

        /* Has Open Bugzilla Bugs */
        hasOpenBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS_DESC);
        notHasOpenBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS_DESC);

        /* Has Bugzilla Bugs */
        hasBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS_DESC);
        notHasBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS_DESC);
        
        /* Format */
        topicFormat = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_FORMAT_VAR, CommonFilterConstants.TOPIC_FORMAT_VAR_DESC);
        topicNotFormat = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_FORMAT_VAR,
                CommonFilterConstants.TOPIC_NOT_FORMAT_VAR_DESC);

        topicTextSearch = new FilterFieldStringData(CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR,
                CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR_DESC);
        startCreateDate = new FilterFieldDateTimeData(CommonFilterConstants.STARTDATE_FILTER_VAR,
                CommonFilterConstants.STARTDATE_FILTER_VAR_DESC);
        endCreateDate = new FilterFieldDateTimeData(CommonFilterConstants.ENDDATE_FILTER_VAR,
                CommonFilterConstants.ENDDATE_FILTER_VAR_DESC);
        startEditDate = new FilterFieldDateTimeData(CommonFilterConstants.STARTEDITDATE_FILTER_VAR,
                CommonFilterConstants.STARTEDITDATE_FILTER_VAR_DESC);
        endEditDate = new FilterFieldDateTimeData(CommonFilterConstants.ENDEDITDATE_FILTER_VAR,
                CommonFilterConstants.ENDEDITDATE_FILTER_VAR_DESC);

        addFilterVar(topicTextSearch);
        addFilterVar(topicIds);
        addFilterVar(topicIncludedInSpec);
        addFilterVar(topicXML);
        addFilterVar(topicTitle);
        addFilterVar(topicDescription);
        addFilterVar(startCreateDate);
        addFilterVar(endCreateDate);
        addFilterVar(startEditDate);
        addFilterVar(endEditDate);
        addFilterVar(hasRelationships);
        addFilterVar(hasIncomingRelationships);
        addFilterVar(topicRelatedTo);
        addFilterVar(topicRelatedFrom);
        addFilterVar(hasXMLErrors);
        addFilterVar(editedInLastDays);
        addFilterVar(hasBugzillaBugs);
        addFilterVar(hasOpenBugzillaBugs);
        addFilterVar(topicFormat);

        addFilterVar(notTopicIds);
        addFilterVar(notTopicIncludedInSpec);
        addFilterVar(notTopicXML);
        addFilterVar(notTopicTitle);
        addFilterVar(notTopicDescription);
        addFilterVar(notTopicRelatedTo);
        addFilterVar(notTopicRelatedFrom);
        addFilterVar(topicNotFormat);

        addFilterVar(notHasXMLErrors);
        addFilterVar(notHasRelationships);
        addFilterVar(notHasIncomingRelationships);
        addFilterVar(notHasBugzillaBugs);
        addFilterVar(notHasOpenBugzillaBugs);
    }

    public Map<String, String> getFilterValues() {
        final Map<String, String> retValue = new HashMap<String, String>();

        // Add the single filters
        final List<FilterFieldDataBase<?>> filterVars = getFilterVars();
        for (final FilterFieldDataBase<?> uiField : filterVars) {
            retValue.put(uiField.getName(), uiField.getData().toString());
        }

        // Add the multi filters
        final List<FilterFieldMapDataBase<?>> multiFilterVars = getMultiFilterVars();
        for (final FilterFieldMapDataBase<?> uiField : multiFilterVars) {
            for (final Map.Entry<Integer, ?> entry : uiField.getData().entrySet()) {
                retValue.put(uiField.getName() + " " + entry.getKey(), entry.getValue().toString());
            }
        }

        return retValue;
    }

    /**
     * @return A map of the expanded filter field names (i.e. with regular expressions) mapped to their descriptions
     */
    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(singleFilterNames);

        return retValue;
    }

    /**
     * @return A map of the base filter field names (i.e. with no regular expressions) mapped to their descriptions
     */
    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getBaseFieldNames();
        retValue.putAll(singleFilterNames);

        return retValue;
    }

    public void loadFilterFields(final Filter filter) {
        resetAllValues();

        for (final FilterField filterField : filter.getFilterFields()) {
            final String field = filterField.getField();
            final String value = filterField.getValue();

            setFieldValue(field, value);
        }
    }

    public FilterFieldIntegerListData getTopicIds() {
        return topicIds;
    }

    public FilterFieldIntegerData getTopicRelatedTo() {
        return topicRelatedTo;
    }

    public FilterFieldIntegerData getTopicRelatedFrom() {
        return topicRelatedFrom;
    }

    public FilterFieldStringData getTopicTitle() {
        return topicTitle;
    }

    public FilterFieldStringData getTopicDescription() {
        return topicDescription;
    }

    public FilterFieldDateTimeData getStartCreateDate() {
        return startCreateDate;
    }

    public FilterFieldDateTimeData getEndCreateDate() {
        return endCreateDate;
    }

    public FilterFieldStringData getTopicXML() {
        return topicXML;
    }

    public FilterFieldBooleanData getHasRelationships() {
        return hasRelationships;
    }

    public FilterFieldBooleanData getHasIncomingRelationships() {
        return hasIncomingRelationships;
    }

    public FilterFieldStringData getTopicTextSearch() {
        return topicTextSearch;
    }

    public FilterFieldBooleanData getHasXMLErrors() {
        return hasXMLErrors;
    }

    public FilterFieldDateTimeData getStartEditDate() {
        return startEditDate;
    }

    public FilterFieldDateTimeData getEndEditDate() {
        return endEditDate;
    }

    public FilterFieldIntegerData getEditedInLastDays() {
        return editedInLastDays;
    }

    public FilterFieldBooleanData getHasOpenBugzillaBugs() {
        return hasOpenBugzillaBugs;
    }

    public FilterFieldBooleanData getHasBugzillaBugs() {
        return hasBugzillaBugs;
    }

    public FilterFieldIntegerListData getTopicIncludedInSpec() {
        return topicIncludedInSpec;
    }

    public FilterFieldIntegerListData getNotTopicIds() {
        return notTopicIds;
    }

    public FilterFieldIntegerData getNotTopicRelatedTo() {
        return notTopicRelatedTo;
    }

    public FilterFieldIntegerData getNotTopicRelatedFrom() {
        return notTopicRelatedFrom;
    }

    public FilterFieldStringData getNotTopicTitle() {
        return notTopicTitle;
    }

    public FilterFieldStringData getNotTopicDescription() {
        return notTopicDescription;
    }

    public FilterFieldStringData getNotTopicXML() {
        return notTopicXML;
    }

    public FilterFieldIntegerListData getNotTopicIncludedInSpec() {
        return notTopicIncludedInSpec;
    }

    public FilterFieldIntegerData getNotEditedInLastDays() {
        return notEditedInLastDays;
    }

    public void setStartCreateDatePlain(final Date startCreateDate) {
        this.startCreateDate.setData(startCreateDate);
    }

    public Date getStartCreateDatePlain() {
        return startCreateDate.getDateData();
    }

    public void setStartEditDatePlain(final Date startEditDate) {
        this.startEditDate.setData(startEditDate);
    }

    public Date getStartEditDatePlain() {
        return startEditDate.getDateData();
    }

    public void setEndCreateDatePlain(final Date endCreateDate) {
        this.endCreateDate.setData(endCreateDate);
    }

    public Date getEndCreateDatePlain() {
        return endCreateDate.getDateData();
    }

    public void setEndEditDatePlain(final Date endEditDate) {
        this.endEditDate.setData(endEditDate);
    }

    public Date getEndEditDatePlain() {
        return endEditDate.getDateData();
    }

    public FilterFieldBooleanData getNotHasXMLErrors() {
        return notHasXMLErrors;
    }

    public FilterFieldBooleanData getNotHasRelationships() {
        return notHasRelationships;
    }

    public FilterFieldBooleanData getNotHasIncomingRelationships() {
        return notHasIncomingRelationships;
    }

    public FilterFieldBooleanData getNotHasOpenBugzillaBugs() {
        return notHasOpenBugzillaBugs;
    }

    public FilterFieldBooleanData getNotHasBugzillaBugs() {
        return notHasBugzillaBugs;
    }

    public FilterFieldIntegerData getNotEditedInLastMins() {
        return notEditedInLastMins;
    }

    public void setNotEditedInLastMins(final FilterFieldIntegerData notEditedInLastMins) {
        this.notEditedInLastMins = notEditedInLastMins;
    }

    public FilterFieldIntegerData getEditedInLastMins() {
        return editedInLastMins;
    }

    public void setEditedInLastMins(final FilterFieldIntegerData editedInLastMins) {
        this.editedInLastMins = editedInLastMins;
    }
}
