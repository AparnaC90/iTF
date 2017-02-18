/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.snippets;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildAgent;
import com.microsoft.tfs.core.clients.build.IBuildAgentQueryResult;
import com.microsoft.tfs.core.clients.build.IBuildAgentSpec;
import com.microsoft.tfs.core.clients.build.IBuildController;
import com.microsoft.tfs.core.clients.build.IBuildControllerQueryResult;
import com.microsoft.tfs.core.clients.build.IBuildControllerSpec;
import com.microsoft.tfs.core.clients.build.IBuildServer;

public class FindBuildControllersAndAgents
{
    public static void main(final String[] args)
        throws Exception
    {
        TFSTeamProjectCollection tpc = SnippetSettings.connectToTFS();

        IBuildServer buildServer = tpc.getBuildServer();

        // Check if the build server version is supported
        if (SnippetSettings.isLessThanV3BuildServer(buildServer))
        {
            return;
        }

        //
        // Scenario 1: Retrieve all controllers with all agent information
        // included.
        //
        System.out.println("***");
        System.out.println("*** Retrieve all build controllers and agents");
        IBuildController[] buildControllers = buildServer.queryBuildControllers(true);
        if (buildControllers.length == 0)
        {
            throw new Exception("no build controllers found");
        }

        displayBuildControllers(buildControllers);

        //
        // Scenario 2: Retrieve an individual controller by name.
        //
        System.out.println("***");
        System.out.println("*** Retrieve a build controller by name");
        String aBuildControllerName = buildControllers[0].getName();
        IBuildController buildController = buildServer.getBuildController(aBuildControllerName);
        if (buildController == null)
        {
            throw new Exception("build controller not found");
        }

        displayBuildControllerProperties(buildController);

        //
        // Scenario 3: Retrieve an individual controller by URI.
        //
        System.out.println("***");
        System.out.println("*** Retrieve a build controller by URI");
        String aBuildControllerUri = buildControllers[0].getURI();
        buildController = buildServer.getBuildController(aBuildControllerUri, false);
        if (buildController == null)
        {
            throw new Exception("Build controller not found");
        }

        displayBuildControllerProperties(buildController);

        //
        // Scenario 4: Retrieve an individual build agent by URI.
        //
        System.out.println("***");
        System.out.println("*** Retrieving a build agent by URI");
        buildController = buildControllers[0];
        IBuildAgent[] buildAgents = buildController.getAgents();

        if (buildAgents == null)
        {
            throw new Exception("build agents not found");
        }

        String anAgentUri = buildController.getAgents()[0].getURI();
        IBuildAgent buildAgent = buildServer.getBuildAgent(anAgentUri);
        if (buildAgent == null)
        {
            throw new Exception("build agent not found");
        }

        System.out.println("Build agent URI: " + anAgentUri);
        displayBuildAgentProperties(buildAgent);

        //
        // Scenario 5: Query build controllers using a BuildControllerSpec.
        //
        System.out.println("***");
        System.out.println("*** Query build controllers using a BuildControllerSpec");
        IBuildControllerSpec buildControllerSpec = buildServer.createBuildControllerSpec();
        buildControllerSpec.setServiceHostName("*"); // all controllers
        buildControllerSpec.setName("*"); // any controller name
        buildControllerSpec.setIncludeAgents(false); // just controller data
        IBuildControllerQueryResult controllerResult = buildServer.queryBuildControllers(buildControllerSpec);

        displayBuildControllers(controllerResult.getControllers());

        //
        // Scenario 6: Query build agents using a BuildAgentSpec.
        //
        System.out.println("***");
        System.out.println("*** Query build agents using a BuildAgentSpec");
        IBuildAgentSpec buildAgentSpec = buildServer.createBuildAgentSpec();
        buildAgentSpec.setServiceHostName("*"); // all services
        buildAgentSpec.setName("*"); // any agent name
        IBuildAgentQueryResult agentResult = buildServer.queryBuildAgents(buildAgentSpec);

        displayBuildAgents(agentResult.getAgents());
    }

    private static void displayBuildControllerProperties(final IBuildController buildController)
    {
        System.out.println("Build Controller");
        System.out.println("\tName: " + buildController.getName());
        System.out.println("\tURI: " + buildController.getURI());
        System.out.println("\tDescription: " + buildController.getDescription());
        System.out.println("\tEnabled: " + buildController.isEnabled());
        System.out.println("\tStatus: " + buildController.getStatus().toString());
        System.out.println();
    }

    private static void displayBuildAgentProperties(final IBuildAgent buildAgent)
    {
        System.out.println("\tBuild Agent");
        System.out.println("\t\tName: " + buildAgent.getName());
        System.out.println("\t\tURI: " + buildAgent.getURI());
        System.out.println("\t\tDescription: " + buildAgent.getDescription());
        System.out.println("\t\tBuild Directory: " + buildAgent.getBuildDirectory());
        System.out.println("\t\tEnabled: " + buildAgent.isEnabled());
        System.out.println("\t\tReserved: " + buildAgent.isReserved());
        System.out.println("\t\tStatus: " + buildAgent.getStatus().toString());

        System.out.print("\t\tTags: ");
        for (String tag : buildAgent.getTags())
        {
            System.out.print(tag + ", ");
        }
        System.out.println();
        System.out.println();
    }

    private static void displayBuildControllers(final IBuildController[] buildControllers)
    {
        for (IBuildController buildController : buildControllers)
        {
            displayBuildControllerProperties(buildController);
            displayBuildAgents(buildController.getAgents());
        }
    }

    private static void displayBuildAgents(final IBuildAgent[] buildAgents)
    {
        for (IBuildAgent buildAgent : buildAgents)
        {
            displayBuildAgentProperties(buildAgent);
        }
    }
}
