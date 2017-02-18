/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.console;

import java.util.Calendar;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildController;
import com.microsoft.tfs.core.clients.build.IBuildDefinition;
import com.microsoft.tfs.core.clients.build.IBuildDetail;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.clients.build.IProcessTemplate;
import com.microsoft.tfs.core.clients.build.IQueuedBuild;
import com.microsoft.tfs.core.clients.build.flags.BuildStatus;
import com.microsoft.tfs.core.clients.build.flags.QueryOptions;
import com.microsoft.tfs.core.clients.build.flags.QueueStatus;
import com.microsoft.tfs.core.clients.build.flags.WorkspaceMappingDepth;
import com.microsoft.tfs.core.clients.build.internal.TeamBuildCache;
import com.microsoft.tfs.core.clients.build.soapextensions.ProcessTemplateType;
import com.microsoft.tfs.core.clients.build.soapextensions.WorkspaceMappingType;

/**
 * This sample demonstrates creating a build definition, queuing a build for
 * that definition, waiting on build completion, printing completed builds, and
 * deleting builds and definitions.
 */
public class BuildSample
{
    public static int TIME_OUT_COUNTER = 200;

    public static void main(final String[] args)
        throws InterruptedException
    {
        /*
         * NOTE: This sample omits some recommended SDK initialization steps for
         * clarity. See ConnectionAdvisorSample and LogConfigurationSample for
         * important information on initializing the TFS SDK for Java before
         * usage.
         */

        TFSTeamProjectCollection tpc = null;
        IBuildDefinition buildDefinition = null;

        try
        {
            // Connect to TFS
            tpc = ConsoleSettings.connectToTFS();

            // Verify that the tfs server has a build service
            if (!isBuildServiceConfigured(tpc.getBuildServer()))
            {
                return;
            }

            // Check if the build server version is supported
            if (ConsoleSettings.isLessThanV3BuildServer(tpc.getBuildServer()))
            {
                return;
            }

            // Create a new build definition
            buildDefinition =
                createBuildDefinition(tpc, BuildSample.class.getSimpleName()
                    + "_"
                    + Calendar.getInstance().getTimeInMillis());

            // Queue a new build
            IQueuedBuild queuedBuild = queueBuild(tpc, buildDefinition);

            // wait for the build to finish
            waitForBuildToFinish(tpc, queuedBuild);

            // Query builds
            IBuildDetail[] builds = queryBuilds(buildDefinition);

            // Print builds
            printBuilds(builds);

        }
        finally
        {
            // Delete the build definition for clean up
            if (tpc != null && buildDefinition != null)
            {
                deleteBuildDefinition(tpc.getBuildServer(), buildDefinition);
            }
        }
    }

    public static IBuildDetail[] queryBuilds(final IBuildDefinition buildDefinition)
    {
        return buildDefinition.queryBuilds();
    }

    public static IBuildDefinition createBuildDefinition(final TFSTeamProjectCollection tpc, final String buildName)
    {
        // Create a new build definition
        IBuildDefinition buildDefinition = tpc.getBuildServer().createBuildDefinition(ConsoleSettings.PROJECT_NAME);

        // Set the name of the build definition
        buildDefinition.setName(buildName);

        // Enable the build definition
        buildDefinition.setEnabled(true);

        // Set the workspace mapping
        buildDefinition.getWorkspace().addMapping(
            ConsoleSettings.MAPPING_SERVER_PATH,
            "$(SourceDir)",
            WorkspaceMappingType.MAP,
            WorkspaceMappingDepth.FULL);

        // Set the build controller
        buildDefinition.setBuildController(getAvailableBuildController(tpc.getBuildServer()));

        // Set the drop location
        buildDefinition.setDefaultDropLocation(ConsoleSettings.BUILD_DROP_LOCATION);

        // Set the process template
        IProcessTemplate processTemplate = getUpgradeProcessTemplate(tpc.getBuildServer());
        if (processTemplate != null)
        {
            buildDefinition.setProcess(processTemplate);
        }

        // Set the build config file
        buildDefinition.setConfigurationFolderPath(ConsoleSettings.BUILD_CONFIG_FOLDER_PATH);

        // Save it
        buildDefinition.save();

        System.out.println("Created build " + buildName);

        return buildDefinition;
    }

    public static IQueuedBuild queueBuild(final TFSTeamProjectCollection tpc, final IBuildDefinition buildDefinition)
    {
        IQueuedBuild queuedBuild = tpc.getBuildServer().queueBuild(buildDefinition);
        System.out.println("Queued a new build for build definition " + buildDefinition.getName());
        return queuedBuild;
    }

    public static void waitForBuildToFinish(final TFSTeamProjectCollection tpc, final IQueuedBuild queuedBuild)
        throws InterruptedException
    {
        System.out.println("Waiting for build to finish");
        int timeOutIndex = 0;
        do
        {
            Thread.sleep(2000);
            queuedBuild.refresh(QueryOptions.NONE);
            System.out.println("Build status is " + queuedBuild.getStatus().toString());
            timeOutIndex++;
        }
        while (!queuedBuild.getStatus().contains(QueueStatus.COMPLETED) && timeOutIndex < TIME_OUT_COUNTER);

        // Check if the build wait timed out
        if (!queuedBuild.getStatus().contains(QueueStatus.COMPLETED))
        {
            System.out.println("The build wait timed out");
        }
        else
        {

            // Display the status of the completed build.
            IBuildDetail buildDetail = queuedBuild.getBuild();
            BuildStatus buildStatus = buildDetail.getStatus();

            System.out.println("Build "
                + buildDetail.getBuildNumber()
                + " completed with status "
                + queuedBuild.getBuildServer().getDisplayText(buildStatus));
        }
    }

    public static boolean isBuildServiceConfigured(final IBuildServer buildServer)
    {
        IBuildController[] controllers =
            TeamBuildCache.getInstance(buildServer, ConsoleSettings.PROJECT_NAME).getBuildControllers(false);
        if (controllers == null || controllers.length == 0)
        {
            System.out.println("This server does not have a build service configured");
            return false;
        }

        return true;
    }

    public static void printBuilds(final IBuildDetail[] builds)
    {
        for (IBuildDetail build : builds)
        {
            System.out.println("BuildNumber: " + build.getBuildNumber() + ", Status: " + build.getStatus().toString());
        }
    }

    public static void deleteBuildDefinition(final IBuildServer buildServer, final IBuildDefinition definition)
    {
        // Delete existing builds
        deleteBuilds(buildServer, definition);

        // Delete build definition
        definition.delete();

        System.out.println("Deleted build definition: " + definition.getName());
    }

    /**
     * Deletes all build of the specified build definition
     * 
     * @param buildDefinition
     *        The build definition whose builds should be deleted
     */
    public static void deleteBuilds(final IBuildServer buildServer, final IBuildDefinition buildDefinition)
    {
        /*
         * Try to stop any builds in progress
         */
        IBuildDetail[] details = buildDefinition.queryBuilds();
        for (IBuildDetail detail : details)
        {
            if (!detail.isBuildFinished())
            {
                try
                {
                    detail.stop();
                }
                catch (Throwable t)
                {
                    System.out.println("Failed to stop in-progress build: " + t.getMessage());
                }
            }
        }

        /*
         * Delete the builds
         */
        buildServer.deleteBuilds(details);

        System.out.println("Deleted all builds of build definition : " + buildDefinition.getName());
    }

    /**
     * Queries for build controllers and returns the first available one
     * 
     * @return IBuildController
     */
    public static IBuildController getAvailableBuildController(final IBuildServer buildServer)
    {
        // Retrieve all the build controllers
        IBuildController[] buildControllers = buildServer.queryBuildControllers();

        // Return the first available build controller
        for (IBuildController buildController : buildControllers)
        {
            if (buildController.isEnabled())
            {
                return buildController;
            }
        }

        return null;
    }

    public static IProcessTemplate getUpgradeProcessTemplate(final IBuildServer buildServer)
    {
        // Find the upgrade template
        IProcessTemplate[] templates =
            buildServer.queryProcessTemplates(ConsoleSettings.PROJECT_NAME, new ProcessTemplateType[]
            {
                ProcessTemplateType.UPGRADE
            });

        if (templates.length == 0)
        {

            return null;
        }

        return templates[0];
    }
}
