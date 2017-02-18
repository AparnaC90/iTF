/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.console;

import java.net.URI;
import java.net.URISyntaxException;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.httpclient.Credentials;
import com.microsoft.tfs.core.httpclient.DefaultNTCredentials;
import com.microsoft.tfs.core.httpclient.UsernamePasswordCredentials;
import com.microsoft.tfs.core.util.CredentialsUtils;
import com.microsoft.tfs.core.util.URIUtils;

public class ConsoleSettings
{
    /**
     * The URL to a TFS project collection, including virtual directory and
     * collection name (like "http://server:8080/tfs/DefaultCollection").
     */
    public static String COLLECTION_URL = "";

    /*
     * Authentication information. HTTP_PROXY_URL should be set to null if none
     * is desired.
     */

    public static String USERNAME = "";
    public static String PASSWORD = "";
    public static String HTTP_PROXY_URL = null;
    public static String HTTP_PROXY_USERNAME = "";
    public static String HTTP_PROXY_PASSWORD = "";

    /**
     * A team project name (without the leading "$/") where files, work items,
     * and builds can be created and modified.
     */
    public static String PROJECT_NAME = "";

    /**
     * Set this to a writable TFS server folder (like "$/TeamProject/folder")
     * where you can create and modify files. To run {@link BuildSample} and
     * {@link GatedBuildSample}, this directory must contain a project that can
     * be built using the files in the {@link #BUILD_CONFIG_FOLDER_PATH} folder
     * path below.
     */
    public static String MAPPING_SERVER_PATH = "";

    /**
     * Set this to a writable local path (Windows or Unix style absolute path)
     * that will be mapped to {@link #MAPPING_SERVER_PATH}. Used for
     * {@link VersionControlSample}, {@link BuildSample}, and
     * {@link GatedBuildSample}.
     */
    public static String MAPPING_LOCAL_PATH = "";
    // public final static String MAPPING_LOCAL_PATH = "/tmp/consoletest";

    /**
     * Set this to a TFS server folder (like
     * "$/TeamProject/TeamBuildTypes/MyTypes") where you have created
     * upgrade-template compatible team build project files (TFSBuild.proj,
     * TFSBuild.rsp) for building the project in {@link #MAPPING_SERVER_PATH}.
     */
    public static String BUILD_CONFIG_FOLDER_PATH = "";

    /**
     * Set this to a writable UNC share (like "\\server\share\drops") where
     * build results can be copied.
     */
    public static String BUILD_DROP_LOCATION = "";

    /**
     * Connect to TFS using a set of credentials that uses the currently logged
     * user in case no user name was provided otherwise it uses the provided
     * credentials
     * 
     * @return The Team Project Collection connected to
     */
    public static TFSTeamProjectCollection connectToTFS()
    {
        TFSTeamProjectCollection tpc = null;
        Credentials credentials;

        // In case no username is provided and the current platform supports
        // default credentials, use default credentials
        if ((USERNAME == null || USERNAME.length() == 0) && CredentialsUtils.supportsDefaultCredentials())
        {
            credentials = new DefaultNTCredentials();
        }
        else
        {
            credentials = new UsernamePasswordCredentials(USERNAME, PASSWORD);
        }
        URI httpProxyURI = null;

        if (HTTP_PROXY_URL != null && HTTP_PROXY_URL.length() > 0)
        {
            try
            {
                httpProxyURI = new URI(HTTP_PROXY_URL);
            }
            catch (URISyntaxException e)
            {
                // Do Nothing
            }
        }

        ConsoleSamplesConnectionAdvisor connectionAdvisor = new ConsoleSamplesConnectionAdvisor(httpProxyURI);

        tpc = new TFSTeamProjectCollection(URIUtils.newURI(COLLECTION_URL), credentials, connectionAdvisor);

        return tpc;
    }

    /**
     * Checks if the build server version is older than TFS2010 and prints an
     * error message
     * 
     * @param buildServer
     *        The build server to check its version
     * 
     * @return boolean true if the build server is older than TFS2010, false
     *         otherwise
     */
    public static boolean isLessThanV3BuildServer(IBuildServer buildServer)
    {
        if (buildServer.getBuildServerVersion().isLessThanV3())
        {
            System.out.println("This sample does not support TFS servers older than TFS2010");
            return true;
        }
        return false;
    }
}
