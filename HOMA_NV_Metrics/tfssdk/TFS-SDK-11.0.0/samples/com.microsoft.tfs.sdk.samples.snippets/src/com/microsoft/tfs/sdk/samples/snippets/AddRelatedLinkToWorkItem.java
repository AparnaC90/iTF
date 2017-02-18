/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.link.ExternalLink;
import com.microsoft.tfs.core.clients.workitem.link.Hyperlink;
import com.microsoft.tfs.core.clients.workitem.link.Link;
import com.microsoft.tfs.core.clients.workitem.link.LinkFactory;
import com.microsoft.tfs.core.clients.workitem.link.RelatedLink;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;

public class AddRelatedLinkToWorkItem
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        Project project = tpc.getWorkItemClient().getProjects().get(SnippetSettings.PROJECT_NAME);
        WorkItemClient workItemClient = project.getWorkItemClient();

        // Find the work item type matching the specified name.
        WorkItemType bugWorkItemType = project.getWorkItemTypes().get("Bug");

        // Create two new work items of type bug.
        WorkItem firstNewWorkItem = workItemClient.newWorkItem(bugWorkItemType);
        WorkItem secondNewWorkItem = workItemClient.newWorkItem(bugWorkItemType);

        firstNewWorkItem.setTitle("First work item -- created by sample");
        secondNewWorkItem.setTitle("Second work item -- created by sample");

        firstNewWorkItem.save();
        secondNewWorkItem.save();
        System.out.println("Work item " + firstNewWorkItem.getID() + " successfully created");
        System.out.println("Work item " + secondNewWorkItem.getID() + " successfully created");

        // Create a related link between the work items.
        String linkComment = "Link created by sample";
        RelatedLink newRelatedLink =
            LinkFactory.newRelatedLink(firstNewWorkItem, secondNewWorkItem, linkComment, false);

        // Add the link to the first new work item.
        firstNewWorkItem.getLinks().add(newRelatedLink);
        firstNewWorkItem.save();
        System.out.println("Added a link to work item " + firstNewWorkItem.getID());

        // Reopen the work items and display the links.
        WorkItem firstWorkItem = workItemClient.getWorkItemByID(firstNewWorkItem.getID());
        showLinks(firstWorkItem);

        WorkItem secondWorkItem = workItemClient.getWorkItemByID(secondNewWorkItem.getID());
        showLinks(secondWorkItem);
    }

    private static void showLinks(final WorkItem workItem)
    {
        System.out.println();
        System.out.println("Links for work item " + workItem.getID());
        System.out.println("Work item has " + workItem.getLinks().size() + " link(s)");
        System.out.println();

        for (Link link : workItem.getLinks())
        {

            System.out.println("Link Type: " + link.getLinkType().getName());
            System.out.println("\tComment: " + link.getComment());
            System.out.println("\tDescription: " + link.getDescription());

            if (link instanceof Hyperlink)
            {
                Hyperlink hyperlink = (Hyperlink) link;
                System.out.println("\tLocation: " + hyperlink.getLocation());
            }
            else if (link instanceof RelatedLink)
            {
                RelatedLink relatedLink = (RelatedLink) link;
                System.out.println("\tTarget work item ID: " + relatedLink.getTargetWorkItemID());
            }
            else if (link instanceof ExternalLink)
            {
                ExternalLink externalLink = (ExternalLink) link;
                System.out.println("\tArtifact ID: " + externalLink.getArtifactID());
                System.out.println("\tUri: " + externalLink.getURI());
            }
        }
    }
}
