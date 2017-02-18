/*
 * Copyright (C) Microsoft Corporation. All Rights Reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.witcontrols;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.microsoft.tfs.client.common.ui.wit.form.FormContext;
import com.microsoft.tfs.client.common.ui.wit.form.controls.IWorkItemControl;
import com.microsoft.tfs.core.clients.workitem.form.WIFormElement;

/**
 * Simple custom control that displays a button and raises a message box that
 * displays the current work item id when clicked
 * 
 * @author nickkirc
 */
public class SimpleButtonControl
    implements IWorkItemControl
{
    private FormContext formContext;

    public final void init(WIFormElement formElement, FormContext formContext)
    {
        this.formContext = formContext;
    }

    public int getMinimumRequiredColumnCount()
    {
        return 1;
    }

    public boolean wantsVerticalFill()
    {
        return false;
    }

    public void addToComposite(final Composite parent)
    {
        Button button = new Button(parent, SWT.NONE);
        button.setText("Click Me!");
        button.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                String messageTitle = "Message from your work item";
                String message = "Hello, I am work item #" + formContext.getWorkItem().getID();
                MessageDialog.openInformation(parent.getShell(), messageTitle, message);
            }
        });
    }
}
