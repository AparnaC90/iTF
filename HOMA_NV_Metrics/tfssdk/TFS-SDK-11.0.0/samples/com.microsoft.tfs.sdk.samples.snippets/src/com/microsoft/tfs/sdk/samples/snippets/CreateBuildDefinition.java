/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildController;
import com.microsoft.tfs.core.clients.build.IBuildDefinition;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.clients.build.IProcessTemplate;
import com.microsoft.tfs.core.clients.build.soapextensions.ContinuousIntegrationType;

public class CreateBuildDefinition
{
    public static void main(final String[] args)
        throws Exception
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();

        // Check if the build server version is supported
        if (SnippetSettings.isLessThanV3BuildServer(buildServer))
        {
            return;
        }

        // Find a build controller.
        IBuildController[] buildControllers = buildServer.queryBuildControllers();
        if (buildControllers.length == 0)
        {
            throw new Exception("no build controllers");
        }

        // Find a process template.
        IProcessTemplate[] processTemplates = buildServer.queryProcessTemplates(SnippetSettings.PROJECT_NAME);
        if (processTemplates.length == 0)
        {
            throw new Exception("no process templates");
        }

        IBuildDefinition buildDefinition = buildServer.createBuildDefinition(SnippetSettings.PROJECT_NAME);
        buildDefinition.setName("Created by "
            + CreateBuildDefinition.class.getSimpleName()
            + " ("
            + System.currentTimeMillis()
            + ")");
        buildDefinition.setDescription("description of build definition");
        buildDefinition.setContinuousIntegrationType(ContinuousIntegrationType.NONE);
        buildDefinition.setBuildController(buildControllers[0]);
        buildDefinition.setDefaultDropLocation(SnippetSettings.BUILD_DROP_LOCATION);
        buildDefinition.setEnabled(true);
        buildDefinition.setProcess(processTemplates[0]);

        buildDefinition.save();
        System.out.println("Created build definition " + buildDefinition.getURI());

        buildServer.deleteBuildDefinitions(new IBuildDefinition[]
        {
            buildDefinition
        });
        System.out.println("Deleted build definition " + buildDefinition.getURI());
    }
}
