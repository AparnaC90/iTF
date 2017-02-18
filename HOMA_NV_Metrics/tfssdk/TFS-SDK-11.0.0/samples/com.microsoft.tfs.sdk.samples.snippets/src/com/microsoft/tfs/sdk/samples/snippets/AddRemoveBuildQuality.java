/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildServer;

public class AddRemoveBuildQuality
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();
        System.out.println("Before change");
        showBuildQualities(buildServer, SnippetSettings.PROJECT_NAME);

        // Define a couple new build qualities.
        String quality1 = "My Build Quality 1";
        String quality2 = "My Build Quality 2";

        // Add a new build quality to the project.
        buildServer.addBuildQuality(SnippetSettings.PROJECT_NAME, quality1);
        System.out.println();
        System.out.println("After adding build quality");
        showBuildQualities(buildServer, SnippetSettings.PROJECT_NAME);

        // Remove the new build quality from the project
        buildServer.deleteBuildQuality(SnippetSettings.PROJECT_NAME, quality1);
        System.out.println();
        System.out.println("After removing build quality");
        showBuildQualities(buildServer, SnippetSettings.PROJECT_NAME);

        // Add two new build qualities to the project.
        buildServer.addBuildQuality(SnippetSettings.PROJECT_NAME, new String[]
        {
            quality1, quality2
        });
        System.out.println();
        System.out.println("After adding 2 build qualities");
        showBuildQualities(buildServer, SnippetSettings.PROJECT_NAME);

        // Remove the two new build qualities from the project.
        buildServer.deleteBuildQuality(SnippetSettings.PROJECT_NAME, new String[]
        {
            quality1, quality2
        });
        System.out.println();
        System.out.println("After removing 2 build qualities");
        showBuildQualities(buildServer, SnippetSettings.PROJECT_NAME);
    }

    private static void showBuildQualities(final IBuildServer buildServer, final String projectName)
    {
        String[] buildQualities = buildServer.getBuildQualities(projectName);
        System.out.println("Build Qualities:");

        int count = 1;
        for (String buildQuality : buildQualities)
        {
            System.out.println("\t[" + count++ + "]" + buildQuality);
        }
    }
}
