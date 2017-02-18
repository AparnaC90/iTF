/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSConfigurationServer;
import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.framework.catalog.CatalogQueryOptions;
import com.microsoft.tfs.core.clients.framework.catalog.CatalogResource;
import com.microsoft.tfs.core.clients.framework.catalog.CatalogResourceTypes;
import com.microsoft.tfs.util.GUID;

public class EnumerateTeamProjectCollections
{
    public static void main(final String[] args)
    {
        TFSConfigurationServer configurationServer = SnippetSettings.connectToTFS().getConfigurationServer();

        GUID[] resourceTypes = new GUID[]
        {
            CatalogResourceTypes.PROJECT_COLLECTION
        };

        CatalogResource[] resources =
            configurationServer.getCatalogService().queryResourcesByType(resourceTypes, CatalogQueryOptions.NONE);

        if (resources != null)
        {
            for (CatalogResource resource : resources)
            {
                String instanceId = resource.getProperties().get("InstanceId");
                TFSTeamProjectCollection tpc = configurationServer.getTeamProjectCollection(new GUID(instanceId));

                System.out.println("TFSTeamProjectCollection");
                System.out.println("\tName: " + tpc.getName().toString());
                System.out.println("\tURI: " + tpc.getBaseURI());
            }
        }
    }
}
