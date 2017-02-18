/*
 * Copyright (C) Microsoft Corporation. All Rights Reserved. This code released
 * under the terms of the Microsoft Public License (MS-PL,
 * http://opensource.org/licenses/ms-pl.html.)
 */

package com.microsoft.tfs.sdk.samples.witcontrols;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.microsoft.tfs.client.common.ui.framework.helper.UIHelpers;
import com.microsoft.tfs.client.common.ui.wit.form.controls.LabelableControl;
import com.microsoft.tfs.core.clients.workitem.fields.Field;
import com.microsoft.tfs.core.clients.workitem.fields.FieldChangeEvent;
import com.microsoft.tfs.core.clients.workitem.fields.FieldChangeListener;

/**
 * Custom control which displays optional values in the form of radio buttons
 * and stores the selected value in a work item field
 * 
 * @author nickkirc
 */
public class RadioButtonsControl
    extends LabelableControl
{
    private String[] optionValues;
    private Button[] radioButtons;

    @Override
    protected int getControlColumns()
    {
        return 1;
    }

    protected String[] getOptionValues()
    {
        if (optionValues == null)
        {
            /*
             * Read options as a semi-colon separated list from the Control
             * element's 'OptionValues' attribute in the work item type
             * definition
             */
            String values = getControlDescription().getAttribute("OptionValues");
            if (values == null)
            {
                values = "Must specify 'OptionValues' attribute in work item type definition Control.";
            }
            optionValues = values.split(";");
        }
        return optionValues;
    }

    @Override
    protected void createControl(Composite parent, int numColumns)
    {
        /*
         * Don't show anything if this control is not backed by a work item
         * field
         */
        if (getControlDescription().getFieldName() == null)
        {
            return;
        }

        String[] optionValues = getOptionValues();

        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, numColumns, 1));
        composite.setLayout(new GridLayout(optionValues.length, true));

        final Field workItemField = getWorkItem().getFields().getField(getControlDescription().getFieldName());

        radioButtons = new Button[optionValues.length];
        for (int i = 0; i < optionValues.length; i++)
        {
            final String optionValue = optionValues[i];

            radioButtons[i] = new Button(composite, SWT.RADIO);
            radioButtons[i].setText(optionValue);
            radioButtons[i].addSelectionListener(new SelectionAdapter()
            {
                public void widgetSelected(SelectionEvent e)
                {
                    /*
                     * Update the work item field's value when a radio button is
                     * selected
                     */
                    workItemField.setValue(optionValue);
                }
            });
        }

        composite.setEnabled(!isFormReadonly() && workItemField.isEditable());

        /*
         * Select the appropriate radio button when the field's value changes
         */
        final FieldChangeListener fieldChangeListener = new FieldChangeListener()
        {
            public void fieldChanged(final FieldChangeEvent event)
            {
                UIHelpers.runOnUIThread(composite.getDisplay(), true, new Runnable()
                {
                    public void run()
                    {
                        handleFieldChanged(event);
                    }
                });
            }
        };
        workItemField.addFieldChangeListener(fieldChangeListener);

        parent.addDisposeListener(new DisposeListener()
        {
            public void widgetDisposed(DisposeEvent e)
            {
                workItemField.removeFieldChangeListener(fieldChangeListener);
            }
        });

        /*
         * fire a "fake" field change event to set the appropriate radio button
         */
        FieldChangeEvent fieldChangeEvent = new FieldChangeEvent();
        fieldChangeEvent.field = workItemField;
        fieldChangeListener.fieldChanged(fieldChangeEvent);
    }

    /**
     * Select the appropriate radio button when the work item field changes
     * 
     * @param event
     */
    private void handleFieldChanged(FieldChangeEvent event)
    {
        Field field = event.field;
        String newValue = field.getValue() == null ? "" : field.getValue().toString();

        for (Button radioButton : radioButtons)
        {
            if (radioButton.getText().equals(newValue))
            {
                radioButton.setSelection(true);
                break;
            }
        }
    }
}
