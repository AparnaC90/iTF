/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;

public class EnumerateWorkItemTypes
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        Project project = tpc.getWorkItemClient().getProjects().get(SnippetSettings.PROJECT_NAME);

        // Enumerate the work item types for this project.
        for (WorkItemType workItemType : project.getWorkItemTypes())
        {
            System.out.println(workItemType.getName());
        }
    }
}
