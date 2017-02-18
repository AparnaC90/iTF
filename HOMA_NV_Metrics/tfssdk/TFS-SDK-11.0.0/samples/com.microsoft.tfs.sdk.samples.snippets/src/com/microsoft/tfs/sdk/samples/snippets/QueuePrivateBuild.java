/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildDefinition;
import com.microsoft.tfs.core.clients.build.IBuildDetail;
import com.microsoft.tfs.core.clients.build.IBuildRequest;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.clients.build.IQueuedBuild;
import com.microsoft.tfs.core.clients.build.flags.BuildReason;
import com.microsoft.tfs.core.clients.build.flags.BuildStatus;
import com.microsoft.tfs.core.clients.build.flags.QueryOptions;
import com.microsoft.tfs.core.clients.build.flags.QueueStatus;

public class QueuePrivateBuild
{
    public static void main(final String[] args)
        throws InterruptedException
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();
        IBuildDefinition buildDefinition =
            buildServer.getBuildDefinition(SnippetSettings.PROJECT_NAME, SnippetSettings.BUILD_DEFINITION_NAME);

        // Create a build request for a private build based on a shelveset.
        IBuildRequest buildRequest = buildDefinition.createBuildRequest();
        buildRequest.setShelvesetName("MyShelvesetName;"
            + tpc.getAuthorizedDomainName()
            + "\\"
            + tpc.getAuthorizedAccountName());
        buildRequest.setReason(BuildReason.VALIDATE_SHELVESET);

        IQueuedBuild queuedBuild = buildServer.queueBuild(buildRequest);
        waitForQueuedBuildToFinish(queuedBuild);

        if (queuedBuild.getStatus().contains(QueueStatus.COMPLETED))
        {
            // Display the status of the completed build.
            IBuildDetail buildDetail = queuedBuild.getBuild();
            BuildStatus buildStatus = buildDetail.getStatus();

            System.out.println("Build "
                + buildDetail.getBuildNumber()
                + " completed with status "
                + buildServer.getDisplayText(buildStatus));
        }
        else
        {
            System.out.println("Build canceled or did not finish in time.");
        }
    }

    private static void waitForQueuedBuildToFinish(final IQueuedBuild queuedBuild)
        throws InterruptedException
    {
        // Wait for the build to finish.
        System.out.print("Waiting for build to finish");
        do
        {
            Thread.sleep(2000);
            System.out.print(".");
            queuedBuild.refresh(QueryOptions.ALL);
        }
        while (queuedBuild.getBuild() == null || !queuedBuild.getBuild().isBuildFinished());

        System.out.println();
    }
}
