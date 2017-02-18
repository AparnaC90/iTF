/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;

public class CreateWorkItem
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        Project project = tpc.getWorkItemClient().getProjects().get(SnippetSettings.PROJECT_NAME);

        // Find the work item type matching the specified name.
        WorkItemType bugWorkItemType = project.getWorkItemTypes().get("Bug");

        // Create a new work item of the specified type.
        WorkItem newWorkItem = project.getWorkItemClient().newWorkItem(bugWorkItemType);

        // Set the title on the work item.
        newWorkItem.setTitle("Example Work Item");

        // Add a comment as part of the change
        newWorkItem.getFields().getField(CoreFieldReferenceNames.HISTORY).setValue(
            "<p>Created automatically by a sample</p>");

        // Save the new work item to the server.
        newWorkItem.save();

        System.out.println("Work item " + newWorkItem.getID() + " successfully created");
    }
}
