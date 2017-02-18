/*
 * Copyright (C) Microsoft Corporation. All Rights Reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.witcontrols;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto-complete combo custom control which gets dropdown values from a file
 * 
 * @author nickkirc
 */
public class FileSourceDropdown
    extends ExternalSourceDropdown
{
    private String[] dropdownItems;

    /**
     * Read the dropdown items from file - each line is a dropdown item
     * {@inheritDoc}
     */
    @Override
    protected String[] getDropdownItems()
    {
        if (dropdownItems == null)
        {
            List<String> items = new ArrayList<String>();

            String filePath = getControlDescription().getAttribute("FilePath");
            if (filePath == null || filePath.length() == 0)
            {
                items.add("Must specify the 'FilePath' attribute in this work item type definition Control");
            }
            else
            {
                try
                {
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));

                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        items.add(line);
                    }
                }
                catch (Exception e)
                {
                    items.clear();
                    items.add("Failed to read file: " + filePath + ". Error: " + e.getLocalizedMessage());
                }
            }

            dropdownItems = items.toArray(new String[items.size()]);
        }

        return dropdownItems;
    }
}
