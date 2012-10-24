package com.deepshiftlabs.nerrvana;

import hudson.model.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.*;

/**
 * Jenkins uses this class and accompanying file
 * com\deepshiftlabs\nerrvana\ReportAction\index.jelly to create report link and
 * report page
 *
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class ReportActionAggregated implements Action, Serializable {

    private static final long serialVersionUID = 1L;
    private AbstractBuild<?, ?> build;
    private String executionResultsFilename;
    private Document results;
    private ArrayList<NerrvanaExecution> list;
    private String reportError;
    private NerrvanaPluginSettings settings;

    /**
     * Loads file with results of execution of all Nerrvana plugins in this
     * build
     *
     * @param build
     * @param executionResultsFilename
     * @param settings plugin settings
     */
    public ReportActionAggregated(AbstractBuild<?, ?> build, String executionResultsFilename, NerrvanaPluginSettings settings) {
        this.build = build;
        this.executionResultsFilename = executionResultsFilename;
        this.settings = settings;
        File buildDir = build.getRootDir();
        File executionResultsFile = new File(buildDir, executionResultsFilename);
        if (executionResultsFile.exists() && executionResultsFile.isFile()) {
            try {
                results = Utils.file2xml(executionResultsFile.getAbsolutePath());
                list = NerrvanaExecution.xml2list(results);
            } catch (Exception e) {
                reportError = e.getMessage();
            }

        } else {
            reportError = "Nerrvana execution results file '" + executionResultsFile.getAbsolutePath() + "' not found.";
        }
    }

    public String getExecutionResultsFilename() {
        return executionResultsFilename;
    }

    public String getIconFileName() {
        return "/plugin/NerrvanaPlugin/icons/nerrvana.png";
    }

    public String getDisplayName() {
        return "Nerrvana Report";
    }

    public AbstractBuild<?, ?> getOwner() {
        return build;
    }

    @Override
    public String getUrlName() {
        // TODO Auto-generated method stub
        return "nerrvana";
    }

    /**
     * Checks if error text exists
     *
     * @return true if error text is not null
     */
    public boolean hasError() {
        return reportError != null;
    }

    /**
     * Returns error text
     *
     * @return error text
     */
    public String getErrorText() {
        return reportError;
    }

    /**
     * Returns list of NerrvanaExecution object. These objects contain results
     * of test executions in Nerrvana for all Nerrvana plugins in this build
     *
     * @return list of NerrvanaExecutions
     */
    public ArrayList<NerrvanaExecution> getList() {
        return list;
    }

    /**
     * Returns status label
     *
     * @return SUCCESS or FAILURE
     */
    public String getExecutionStatus() {
        return NerrvanaExecution.testExecutionResults(build, settings) ? "SUCCESS"
                : "FAILURE";
    }

    /**
     * Returns name of the UserMessageLevel which was set in the plugin settings
     * as message threshold.
     *
     * @return
     */
    public String getMessageThreshold() {
        return this.settings.messageThreshold.name();
    }
}