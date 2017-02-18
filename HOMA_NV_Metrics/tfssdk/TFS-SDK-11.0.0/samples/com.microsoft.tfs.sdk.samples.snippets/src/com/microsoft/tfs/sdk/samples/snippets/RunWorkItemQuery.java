/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;

public class RunWorkItemQuery
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        Project project = tpc.getWorkItemClient().getProjects().get(SnippetSettings.PROJECT_NAME);
        WorkItemClient workItemClient = project.getWorkItemClient();

        // Define the WIQL query.
        String wiqlQuery = "Select ID, Title from WorkItems where (State = 'Active') order by Title";

        // Run the query and get the results.
        WorkItemCollection workItems = workItemClient.query(wiqlQuery);
        System.out.println("Found " + workItems.size() + " work items.");
        System.out.println();

        // Write out the heading.
        System.out.println("Query: " + wiqlQuery);
        System.out.println();
        System.out.println("ID\tTitle");

        // Output the results of the query.
        final int maxToPrint = 20;
        for (int i = 0; i < workItems.size(); i++)
        {
            if (i >= maxToPrint)
            {
                System.out.println("[...]");
                break;
            }

            WorkItem workItem = workItems.getWorkItem(i);
            System.out.println(workItem.getID() + "\t" + workItem.getTitle());
        }
    }
}
