/*
 * Copyright (c) Microsoft Corporation. All rights reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.console;

import com.microsoft.tfs.core.clients.versioncontrol.events.GetEvent;
import com.microsoft.tfs.core.clients.versioncontrol.events.GetListener;

public class SampleGetEventListener
    implements GetListener
{
    public void onGet(final GetEvent e)
    {
        String item = e.getTargetLocalItem() != null ? e.getTargetLocalItem() : e.getServerItem();

        System.out.println("getting: " + item);
    }
}
