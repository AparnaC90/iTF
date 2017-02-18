/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildDefinition;
import com.microsoft.tfs.core.clients.build.IBuildDefinitionQueryResult;
import com.microsoft.tfs.core.clients.build.IBuildDefinitionSpec;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.clients.build.flags.QueryOptions;
import com.microsoft.tfs.core.clients.build.soapextensions.ContinuousIntegrationType;

public class FindBuildDefinitions
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();

        //
        // Scenario 1: Query all definitions in the specified project.
        //
        System.out.println("***");
        System.out.println("*** Query for build definitions by project name");
        System.out.println("***");

        IBuildDefinition[] buildDefinitions = buildServer.queryBuildDefinitions(SnippetSettings.PROJECT_NAME);
        System.out.println("Found " + buildDefinitions.length + " build definition(s).");
        for (IBuildDefinition buildDefinition : buildDefinitions)
        {
            displayBuildDefinitionProperties(buildDefinition);
        }

        //
        // Scenario 2: Query definitions in a specified project by type.
        //
        System.out.println("***");
        System.out.println("*** Query build definitions of type GATED");
        System.out.println("***");

        IBuildDefinitionSpec buildDefinitionSpec = buildServer.createBuildDefinitionSpec(SnippetSettings.PROJECT_NAME);
        buildDefinitionSpec.setContinuousIntegrationType(ContinuousIntegrationType.GATED);
        IBuildDefinitionQueryResult queryResults = buildServer.queryBuildDefinitions(buildDefinitionSpec);
        buildDefinitions = queryResults.getDefinitions();

        System.out.println("Found " + buildDefinitions.length + " gated build definition(s).");
        for (IBuildDefinition buildDefinition : buildDefinitions)
        {
            displayBuildDefinitionProperties(buildDefinition);
        }

        //
        // Scenario 3: Get an individual build definition by name.
        //
        System.out.println("***");
        System.out.println("*** Get build definition by name");
        System.out.println("***");

        IBuildDefinition buildDefinition;
        buildDefinition =
            buildServer.getBuildDefinition(SnippetSettings.PROJECT_NAME, SnippetSettings.BUILD_DEFINITION_NAME);
        displayBuildDefinitionProperties(buildDefinition);

        //
        // Scenario 4: Get an individual build definition by URI
        //
        System.out.println("***");
        System.out.println("*** Get build definition by URI");
        System.out.println("***");

        if (buildDefinitions.length > 0)
        {
            String aBuildDefinitionUri = buildDefinitions[0].getURI();
            buildDefinition = buildServer.getBuildDefinition(aBuildDefinitionUri);
            displayBuildDefinitionProperties(buildDefinition);
        }

        //
        // Scenario 5: Get an individual build definition by name with
        // options to control the amount of data retrieved for the
        // definition.
        //
        System.out.println("***");
        System.out.println("*** Get build definition by name with options");
        System.out.println("***");

        buildDefinition =
            buildServer.getBuildDefinition(
                SnippetSettings.PROJECT_NAME,
                SnippetSettings.BUILD_DEFINITION_NAME,
                QueryOptions.DEFINITIONS);

        displayBuildDefinitionProperties(buildDefinition);

        //
        // Scenario 6: Get an individual build definition by URI with
        // options to control the amount of data retrieved for the
        // definition.
        //
        System.out.println("***");
        System.out.println("*** Get build definition by URI with options");
        System.out.println("***");

        if (buildDefinitions.length > 0)
        {
            String aBuildDefinitionURI = buildDefinitions[0].getURI();
            buildDefinition = buildServer.getBuildDefinition(aBuildDefinitionURI, QueryOptions.DEFINITIONS);
            displayBuildDefinitionProperties(buildDefinition);
        }
    }

    private static void displayBuildDefinitionProperties(final IBuildDefinition buildDefinition)
    {
        System.out.println("Build Definition");
        System.out.println("\tName: " + buildDefinition.getName());
        System.out.println("\tURI: " + buildDefinition.getURI());
        System.out.println("\tDescription: " + buildDefinition.getDescription());
        System.out.println("\tEnabled: " + buildDefinition.isEnabled());
        System.out.println("\tDrop Folder: " + buildDefinition.getDefaultDropLocation());
        System.out.println("");
    }
}
