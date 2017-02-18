/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.project.Project;

public class EnumerateWorkItemTeamProjects
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        // Enumerate the Team Projects.
        for (Project project : tpc.getWorkItemClient().getProjects())
        {
            System.out.println(project.getName());
        }
    }
}
