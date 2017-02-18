/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildDefinition;
import com.microsoft.tfs.core.clients.build.IBuildDetail;
import com.microsoft.tfs.core.clients.build.IBuildDetailSpec;
import com.microsoft.tfs.core.clients.build.IBuildQueryResult;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.clients.build.flags.BuildStatus;

public class FindBuilds
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();
        IBuildDefinition def =
            buildServer.getBuildDefinition(SnippetSettings.PROJECT_NAME, SnippetSettings.BUILD_DEFINITION_NAME);
        IBuildDetailSpec spec = buildServer.createBuildDetailSpec(def);
        spec.setStatus(BuildStatus.ALL);

        System.out.println("***");
        System.out.println("*** Query builds for build definition " + SnippetSettings.BUILD_DEFINITION_NAME);
        System.out.println("***");

        IBuildQueryResult buildsQueryResult = buildServer.queryBuilds(spec);

        System.out.println("Found " + buildsQueryResult.getBuilds().length + " build(s).");

        for (IBuildDetail build : buildsQueryResult.getBuilds())
        {
            displayBuildProperties(buildServer, build);
        }

    }

    private static void displayBuildProperties(final IBuildServer buildServer, final IBuildDetail build)
    {
        System.out.println("Build ");
        System.out.println("\tBuild Number: " + build.getBuildNumber());
        System.out.println("\tURI: " + build.getURI());
        System.out.println("\tStatus: " + buildServer.getDisplayText(build.getStatus()));
        System.out.println("\tRequested By: " + build.getRequestedBy());
        System.out.println("");
    }
}
