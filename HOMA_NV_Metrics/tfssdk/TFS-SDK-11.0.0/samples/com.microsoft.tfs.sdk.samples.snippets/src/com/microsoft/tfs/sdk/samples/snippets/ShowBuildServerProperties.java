/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.clients.build.flags.BuildServerVersion;

public class ShowBuildServerProperties
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();

        // Show the build server version number.
        BuildServerVersion version = buildServer.getBuildServerVersion();
        System.out.println("Build server version: " + version.getVersion());

        // Show all localized display strings for BuildServerVersion.
        showDisplayValues(buildServer);
    }

    private static void showDisplayValues(final IBuildServer buildServer)
    {
        System.out.println("Display values for BuildServerVersion");
        for (String displayValue : buildServer.getDisplayTextValues(BuildServerVersion.class))
        {
            System.out.println(displayValue);
        }
    }
}
