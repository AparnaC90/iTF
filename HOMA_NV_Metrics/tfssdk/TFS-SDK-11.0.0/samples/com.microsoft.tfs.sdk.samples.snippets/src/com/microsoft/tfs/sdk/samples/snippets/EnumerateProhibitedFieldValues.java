/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.fields.Field;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;

public class EnumerateProhibitedFieldValues
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        Project project = tpc.getWorkItemClient().getProjects().get(SnippetSettings.PROJECT_NAME);
        WorkItemClient client = project.getWorkItemClient();

        int prohibitedCount = 0;

        for (WorkItemType workItemType : project.getWorkItemTypes())
        {
            WorkItem workItem = client.newWorkItem(workItemType);
            for (Field field : workItem.getFields())
            {
                for (String prohibitedValue : field.getProhibitedValues())
                {
                    prohibitedCount++;
                    System.out.println("Type='"
                        + workItemType.getName()
                        + "' Field='"
                        + field.getName()
                        + "' Prohibited='"
                        + prohibitedValue
                        + "'");
                }
            }
        }

        System.out.println(prohibitedCount
            + " prohibited values for "
            + project.getWorkItemTypes().size()
            + " work item types.");
    }
}
