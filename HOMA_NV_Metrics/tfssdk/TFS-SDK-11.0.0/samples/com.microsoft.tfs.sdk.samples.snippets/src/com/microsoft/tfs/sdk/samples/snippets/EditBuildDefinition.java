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

public class EditBuildDefinition
{
    public static void main(final String[] args)
        throws Exception
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();

        IBuildDefinitionSpec buildDefinitionSpec = buildServer.createBuildDefinitionSpec(SnippetSettings.PROJECT_NAME);
        buildDefinitionSpec.setName(SnippetSettings.BUILD_DEFINITION_NAME);

        IBuildDefinitionQueryResult queryResult = buildServer.queryBuildDefinitions(buildDefinitionSpec);
        IBuildDefinition[] buildDefinitions = queryResult.getDefinitions();
        if (buildDefinitions.length == 0)
        {
            throw new Exception("Build definition was not found");
        }

        // Toggle the enabled flag and save the definition.
        IBuildDefinition buildDefinition = buildDefinitions[0];
        buildDefinition.setEnabled(!buildDefinition.isEnabled());
        buildDefinition.save();

        // Set it back
        buildDefinition.setEnabled(!buildDefinition.isEnabled());
        buildDefinition.save();

        System.out.print("Build definition '" + buildDefinition.getName() + "' was edited.");
    }
}
