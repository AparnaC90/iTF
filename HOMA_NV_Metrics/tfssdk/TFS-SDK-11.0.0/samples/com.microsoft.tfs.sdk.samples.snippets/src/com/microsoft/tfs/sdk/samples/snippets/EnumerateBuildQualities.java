/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildServer;

public class EnumerateBuildQualities
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();
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
