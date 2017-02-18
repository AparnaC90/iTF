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
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;

public class EditWorkItemByID
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        Project project = tpc.getWorkItemClient().getProjects().get(SnippetSettings.PROJECT_NAME);
        WorkItemClient workItemClient = project.getWorkItemClient();

        // Create a new work item, save it, and get its ID.
        WorkItemType type = project.getWorkItemTypes().get("Bug");
        WorkItem newWorkItem = workItemClient.newWorkItem(type);
        newWorkItem.setTitle("Created by sample");
        newWorkItem.save();
        int newWorkItemId = newWorkItem.getID();
        System.out.println("Created work item " + newWorkItemId + " and title '" + newWorkItem.getTitle() + "'");

        // Open a new instance of the work item we just saved and change it.
        WorkItem workItem = workItemClient.getWorkItemByID(newWorkItemId);
        workItem.setTitle("Edited by sample");
        workItem.save();
        System.out.println("Edited work item " + workItem.getID() + " and title '" + workItem.getTitle() + "'");
    }
}
