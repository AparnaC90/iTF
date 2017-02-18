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

public class AddExternalLinkToWorkItem
{
    public static void main(final String[] args)
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        Project project = tpc.getWorkItemClient().getProjects().get(SnippetSettings.PROJECT_NAME);
        WorkItemClient workItemClient = project.getWorkItemClient();

        // Find the work item type matching the specified name.
        WorkItemType bugWorkItemType = project.getWorkItemTypes().get("Bug");

        // Create a new work item.
        WorkItem newWorkItem = workItemClient.newWorkItem(bugWorkItemType);
        newWorkItem.setTitle("Created by sample");

        // Add an external link of type 'hyperlink' to the work item.
        String hyperlinkLocation = "www.microsoft.com";
        String hyperlinkComment = "Microsoft web location";
        Hyperlink newHyperlink = LinkFactory.newHyperlink(hyperlinkLocation, hyperlinkComment, false);
        newWorkItem.getLinks().add(newHyperlink);

        // Save the new work item to the server.
        newWorkItem.save();
        System.out.println("Work item " + newWorkItem.getID() + " successfully created with hyperlink");

        // Reopen the new work item and display the links.
        WorkItem workItem = workItemClient.getWorkItemByID(newWorkItem.getID());
        System.out.println("Open work item " + workItem.getID());
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
