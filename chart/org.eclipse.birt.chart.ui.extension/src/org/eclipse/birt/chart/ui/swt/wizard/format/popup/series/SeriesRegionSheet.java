/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class SeriesRegionSheet extends AbstractPopupSheet implements
		SelectionListener, Listener {

	private transient Composite cmpContent;

	private transient Composite cmpList = null;

	private transient Button btnAddRange = null;

	private transient Button btnRemove = null;

	private transient List lstMarkers = null;

	private transient Group grpGeneral = null;

	private transient Composite cmpRange = null;

	private transient Group grpMarkerRange = null;

	private transient Label lblStartValue = null;

	private transient TextEditorComposite txtStartValue = null;

	private transient Button btnStartFormatSpecifier = null;

	private transient Label lblEndValue = null;

	private transient TextEditorComposite txtEndValue = null;

	private transient Button btnEndFormatSpecifier = null;

	private transient Label lblInnerRadius = null;

	private transient TextEditorComposite txtInnerRadius = null;

	private transient Label lblOuterRadius = null;

	private transient TextEditorComposite txtOuterRadius = null;

	private transient Label lblRangeFill = null;

	private transient FillChooserComposite fccRange = null;

	private transient LineAttributesComposite liacMarkerRange = null;

	private transient LabelAttributesComposite lacLabel = null;

	private transient int iRangeCount = 0;

	private transient SeriesDefinition seriesDefn;

	public SeriesRegionSheet(Composite parent, Chart chart,
			SeriesDefinition seriesDefn) {
		super(parent, chart, false);
		this.seriesDefn = seriesDefn;
		cmpTop = getComponent(parent);
	}

	protected Composite getComponent(Composite parent) {
		// Layout for the main composite
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);

		// Layout for the List composite
		GridLayout glList = new GridLayout();
		glList.numColumns = 3;
		glList.horizontalSpacing = 5;
		glList.verticalSpacing = 5;
		glList.marginHeight = 0;
		glList.marginWidth = 0;

		cmpList = new Composite(cmpContent, SWT.NONE);
		GridData gdCMPList = new GridData(GridData.FILL_BOTH);
		gdCMPList.horizontalSpan = 2;
		cmpList.setLayoutData(gdCMPList);
		cmpList.setLayout(glList);

		// Layout for the buttons composite
		GridLayout glButtons = new GridLayout();
		glButtons.numColumns = 3;
		glButtons.horizontalSpacing = 5;
		glButtons.verticalSpacing = 5;
		glButtons.marginHeight = 5;
		glButtons.marginWidth = 0;

		Composite cmpButtons = new Composite(cmpList, SWT.NONE);
		GridData gdCMPButtons = new GridData(GridData.FILL_HORIZONTAL);
		gdCMPButtons.horizontalSpan = 3;
		cmpButtons.setLayoutData(gdCMPButtons);
		cmpButtons.setLayout(glButtons);

		btnAddRange = new Button(cmpButtons, SWT.PUSH);
		GridData gdBTNAddRange = new GridData(GridData.FILL_HORIZONTAL);
		btnAddRange.setLayoutData(gdBTNAddRange);
		btnAddRange.setText(Messages
				.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.AddRegion")); //$NON-NLS-1$
		btnAddRange.addSelectionListener(this);

		btnRemove = new Button(cmpButtons, SWT.PUSH);
		GridData gdBTNRemove = new GridData(GridData.FILL_HORIZONTAL);
		btnRemove.setLayoutData(gdBTNRemove);
		btnRemove.setText(Messages
				.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.RemoveEntry")); //$NON-NLS-1$
		btnRemove.addSelectionListener(this);

		lstMarkers = new List(cmpList, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		GridData gdLSTMarkers = new GridData(GridData.FILL_BOTH);
		gdLSTMarkers.horizontalSpan = 3;
		lstMarkers.setLayoutData(gdLSTMarkers);
		lstMarkers.addSelectionListener(this);

		grpGeneral = new Group(cmpContent, SWT.NONE);
		GridData gdCMPGeneral = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gdCMPGeneral.widthHint = 180;
		grpGeneral.setLayoutData(gdCMPGeneral);
		grpGeneral.setLayout(new GridLayout());
		grpGeneral
				.setText(Messages
						.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.DialProperties")); //$NON-NLS-1$

		// Layout for the Marker Range composite
		GridLayout glMarkerRange = new GridLayout();
		glMarkerRange.numColumns = 3;
		glMarkerRange.horizontalSpacing = 5;
		glMarkerRange.verticalSpacing = 5;
		glMarkerRange.marginHeight = 7;
		glMarkerRange.marginWidth = 7;

		cmpRange = new Composite(grpGeneral, SWT.NONE);
		GridData gdGRPRange = new GridData(GridData.FILL_HORIZONTAL);
		cmpRange.setLayoutData(gdGRPRange);
		cmpRange.setLayout(glMarkerRange);

		// Layout for Value composite
		GridLayout glRangeValue = new GridLayout();
		glRangeValue.numColumns = 3;
		glRangeValue.horizontalSpacing = 2;
		glRangeValue.verticalSpacing = 5;
		glRangeValue.marginHeight = 0;
		glRangeValue.marginWidth = 0;

		Composite cmpRangeValue = new Composite(cmpRange, SWT.NONE);
		GridData gdCMPRangeValue = new GridData(GridData.FILL_HORIZONTAL);
		gdCMPRangeValue.horizontalSpan = 3;
		cmpRangeValue.setLayoutData(gdCMPRangeValue);
		cmpRangeValue.setLayout(glRangeValue);

		lblStartValue = new Label(cmpRangeValue, SWT.NONE);
		GridData gdLBLStartValue = new GridData();
		gdLBLStartValue.horizontalIndent = 5;
		lblStartValue.setLayoutData(gdLBLStartValue);
		lblStartValue.setText(Messages
				.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.StartValue")); //$NON-NLS-1$

		txtStartValue = new TextEditorComposite(cmpRangeValue, SWT.BORDER
				| SWT.SINGLE);
		GridData gdTXTStartValue = new GridData(GridData.FILL_HORIZONTAL);
		txtStartValue.setLayoutData(gdTXTStartValue);
		txtStartValue.addListener(this);

		btnStartFormatSpecifier = new Button(cmpRangeValue, SWT.PUSH);
		GridData gdBTNStartFormatSpecifier = new GridData();
		gdBTNStartFormatSpecifier.heightHint = 18;
		gdBTNStartFormatSpecifier.widthHint = 18;
		btnStartFormatSpecifier.setLayoutData(gdBTNStartFormatSpecifier);
		btnStartFormatSpecifier.setImage(UIHelper
				.getImage("icons/obj16/formatbuilder.gif")); //$NON-NLS-1$
		btnStartFormatSpecifier.addSelectionListener(this);
		btnStartFormatSpecifier.getImage().setBackground(
				btnStartFormatSpecifier.getBackground());

		lblEndValue = new Label(cmpRangeValue, SWT.NONE);
		GridData gdLBLEndValue = new GridData();
		gdLBLEndValue.horizontalIndent = 5;
		lblEndValue.setLayoutData(gdLBLEndValue);
		lblEndValue.setText(Messages
				.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.EndValue")); //$NON-NLS-1$

		txtEndValue = new TextEditorComposite(cmpRangeValue, SWT.BORDER
				| SWT.SINGLE);
		GridData gdTXTEndValue = new GridData(GridData.FILL_HORIZONTAL);
		txtEndValue.setLayoutData(gdTXTEndValue);
		txtEndValue.addListener(this);

		btnEndFormatSpecifier = new Button(cmpRangeValue, SWT.PUSH);
		GridData gdBTNEndFormatSpecifier = new GridData();
		gdBTNEndFormatSpecifier.heightHint = 18;
		gdBTNEndFormatSpecifier.widthHint = 18;
		btnEndFormatSpecifier.setLayoutData(gdBTNEndFormatSpecifier);
		btnEndFormatSpecifier.setImage(UIHelper
				.getImage("icons/obj16/formatbuilder.gif")); //$NON-NLS-1$
		btnEndFormatSpecifier.addSelectionListener(this);
		btnEndFormatSpecifier.getImage().setBackground(
				btnEndFormatSpecifier.getBackground());

		// Radius
		lblInnerRadius = new Label(cmpRangeValue, SWT.NONE);
		GridData gdLBLInnerRadius = new GridData();
		gdLBLInnerRadius.horizontalIndent = 5;
		lblInnerRadius.setLayoutData(gdLBLInnerRadius);
		lblInnerRadius.setText(Messages
				.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.InnerRadius")); //$NON-NLS-1$

		txtInnerRadius = new TextEditorComposite(cmpRangeValue, SWT.BORDER
				| SWT.SINGLE);
		GridData gdTXTInnerRadius = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTInnerRadius.horizontalSpan = 2;
		txtInnerRadius.setLayoutData(gdTXTInnerRadius);
		txtInnerRadius.addListener(this);

		lblOuterRadius = new Label(cmpRangeValue, SWT.NONE);
		GridData gdLBLOuterRadius = new GridData();
		gdLBLOuterRadius.horizontalIndent = 5;
		lblOuterRadius.setLayoutData(gdLBLOuterRadius);
		lblOuterRadius.setText(Messages
				.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.OuterRadius")); //$NON-NLS-1$

		txtOuterRadius = new TextEditorComposite(cmpRangeValue, SWT.BORDER
				| SWT.SINGLE);
		GridData gdTXTOuterRadius = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTOuterRadius.horizontalSpan = 2;
		txtOuterRadius.setLayoutData(gdTXTOuterRadius);
		txtOuterRadius.addListener(this);

		// Fill
		lblRangeFill = new Label(cmpRange, SWT.NONE);
		GridData gdLBLRangeFill = new GridData();
		gdLBLRangeFill.horizontalIndent = 5;
		lblRangeFill.setLayoutData(gdLBLRangeFill);
		lblRangeFill.setText(Messages
				.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.Fill")); //$NON-NLS-1$

		fccRange = new FillChooserComposite(cmpRange, SWT.NONE, null, true,
				true);
		GridData gdFCCRange = new GridData(GridData.FILL_HORIZONTAL);
		gdFCCRange.horizontalSpan = 2;
		fccRange.setLayoutData(gdFCCRange);
		fccRange.addListener(this);

		// Range Outline
		grpMarkerRange = new Group(cmpRange, SWT.NONE);
		GridData gdGRPMarkerRange = new GridData(GridData.FILL_HORIZONTAL);
		gdGRPMarkerRange.horizontalSpan = 3;
		grpMarkerRange.setLayoutData(gdGRPMarkerRange);
		grpMarkerRange.setLayout(new FillLayout());
		grpMarkerRange
				.setText(Messages
						.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.RangeOutline")); //$NON-NLS-1$

		liacMarkerRange = new LineAttributesComposite(grpMarkerRange, SWT.NONE,
				null, true, true, true);
		liacMarkerRange.addListener(this);

		// Label Properties
		lacLabel = new LabelAttributesComposite( cmpContent,
				SWT.NONE,
				Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.DialLabelProperties" ), //$NON-NLS-1$
				LabelImpl.create( ),
				chart.getUnits( ),
				false,
				false,
				serviceprovider,
				false,
				false);
		GridData gdLACLabel = new GridData(GridData.FILL_BOTH);
		lacLabel.setLayoutData(gdLACLabel);
		lacLabel.addListener(this);

		populateLists();

		refreshButtons();

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(lacLabel)) 
		{
			if (this.lstMarkers.getSelection().length != 0) 
			{
				switch (event.type) 
				{
					case LabelAttributesComposite.FONT_CHANGED_EVENT:
						getSelectedMarkerLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
						getSelectedMarkerLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
						break;
					case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT:
						getSelectedMarkerLabel().setBackground((Fill) event.data);
						break;
					case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT:
						getSelectedMarkerLabel().getOutline().setStyle((LineStyle) event.data);
						break;
					case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT:
						getSelectedMarkerLabel().getOutline().setThickness(
							((Integer) event.data).intValue());
						break;
					case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT:
						getSelectedMarkerLabel().getOutline().setColor(
							(ColorDefinition) event.data);
						break;
					case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT:
						getSelectedMarkerLabel().getOutline().setVisible(
							((Boolean) event.data).booleanValue());
						break;
				}
			}
		} else if (event.widget.equals(fccRange)) {
			((DialRegion) (getDialForProcessing().getDialRegions())
					.get(getMarkerIndex())).setFill((Fill) event.data);
		} else if (event.widget.equals(txtStartValue)) {
			int iMarkerIndex = getMarkerIndex();
				((DialRegion) (getDialForProcessing().getDialRegions())
						.get(iMarkerIndex)).setStartValue(this
						.getTypedDataElement(txtStartValue.getText()));
		} else if (event.widget.equals(txtEndValue)) {
			int iMarkerIndex = getMarkerIndex();
				((DialRegion) (getDialForProcessing().getDialRegions())
						.get(iMarkerIndex)).setEndValue(this
						.getTypedDataElement(txtEndValue.getText()));
		} else if (event.widget.equals(txtInnerRadius)) {
			((DialRegion) (getDialForProcessing().getDialRegions())
					.get(getMarkerIndex())).setInnerRadius(Double
					.parseDouble( trimString(txtInnerRadius.getText())));
		} else if (event.widget.equals(txtOuterRadius)) {
			((DialRegion) (getDialForProcessing().getDialRegions())
					.get(getMarkerIndex())).setOuterRadius(Double
					.parseDouble( trimString( txtOuterRadius.getText())));
		} else if (event.widget.equals(liacMarkerRange)) {
			if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				((DialRegion) getDialForProcessing().getDialRegions().get(
						getMarkerIndex())).getOutline().setStyle(
						(LineStyle) event.data);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				((DialRegion) getDialForProcessing().getDialRegions().get(
						getMarkerIndex())).getOutline().setThickness(
						((Integer) event.data).intValue());
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				((DialRegion) getDialForProcessing().getDialRegions().get(
						getMarkerIndex())).getOutline().setColor(
						(ColorDefinition) event.data);
			} else {
				((DialRegion) getDialForProcessing().getDialRegions().get(
						getMarkerIndex())).getOutline().setVisible(
						((Boolean) event.data).booleanValue());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(btnAddRange)) {
			DialRegion range = DialRegionImpl.create(); //$NON-NLS-1$
			range.setStartValue(getTypedDataElement( "" )); //$NON-NLS-1$
			range.setEndValue(getTypedDataElement( "" )); //$NON-NLS-1$
			getDialForProcessing().getDialRegions().add(range);
			range.eAdapters().addAll(getDialForProcessing().eAdapters());
			iRangeCount++;
			buildList();
			lstMarkers.select(lstMarkers.getItemCount() - 1);
			updateUIForSelection();
			if (lstMarkers.getItemCount() == 1) {
				// Enable UI elements
				setState(true);
			}

			refreshButtons();
		} else if (e.getSource().equals(btnRemove)) {
			if (lstMarkers.getSelection().length == 0) {
				return;
			}
			int iMarkerIndex = getMarkerIndex();

			getDialForProcessing().getDialRegions().remove(iMarkerIndex);
			iRangeCount--;

			buildList();
			if (lstMarkers.getItemCount() > 0) {
				lstMarkers.select(0);
				updateUIForSelection();
			} else {
				setState(false);
				resetUI();
			}

			refreshButtons();
		} else if (e.getSource().equals(lstMarkers)) {
			updateUIForSelection();
			refreshButtons();
		} else if (e.getSource().equals(btnStartFormatSpecifier)
				|| e.getSource().equals(btnEndFormatSpecifier)) {
			String sAxisTitle = ""; //$NON-NLS-1$
			try {
				String sTitleString = getDialForProcessing().getLabel()
						.getCaption().getValue();
				int iSeparatorIndex = sTitleString
						.indexOf(ExternalizedTextEditorComposite.SEPARATOR);
				if (iSeparatorIndex > 0) {
					sTitleString = sTitleString.substring(iSeparatorIndex);
				} else if (iSeparatorIndex == 0) {
					sTitleString = sTitleString
							.substring(ExternalizedTextEditorComposite.SEPARATOR
									.length());
				}
				sAxisTitle = new MessageFormat(
						Messages
								.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.ForAxis")).format(new Object[] { sTitleString }); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (NullPointerException e1) {
			}

			FormatSpecifier formatspecifier = null;
			if (((DialRegion) getDialForProcessing().getDialRegions().get(
					getMarkerIndex())).getFormatSpecifier() != null) {
				formatspecifier = ((DialRegion) getDialForProcessing()
						.getDialRegions().get(getMarkerIndex()))
						.getFormatSpecifier();
			}
			FormatSpecifierDialog editor = new FormatSpecifierDialog(
					cmpContent.getShell(),
					formatspecifier,
					new MessageFormat(
							Messages
									.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerRange")).format(new Object[] { new Integer(getMarkerIndex() + 1), sAxisTitle })); //$NON-NLS-1$
			if (!editor.wasCancelled()) {
				if (editor.getFormatSpecifier() == null) {
					((DialRegion) getDialForProcessing().getDialRegions().get(
							getMarkerIndex()))
							.eUnset(ComponentPackage.eINSTANCE
									.getMarkerRange_FormatSpecifier());
					return;
				}
				((DialRegion) getDialForProcessing().getDialRegions().get(
						getMarkerIndex())).setFormatSpecifier(editor
						.getFormatSpecifier());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private Dial getDialForProcessing() {
		return ((DialSeries) seriesDefn.getDesignTimeSeries()).getDial();
	}

	private String getValueAsString(DataElement de) {
		String sValue = ""; //$NON-NLS-1$
			sValue = String.valueOf(((NumberDataElement) de).getValue());
		return sValue;
	}

	private int getMarkerIndex() {
		int iSelectionIndex = lstMarkers.getSelectionIndex();
		return iSelectionIndex;
	}

	private void buildList() {
		// Clear any existing contents
		lstMarkers.removeAll();

		iRangeCount = getDialForProcessing( ).getDialRegions( ).size( );
		for (int iRanges = 0; iRanges < iRangeCount; iRanges++) {
			lstMarkers.add("DialRegion - " + (iRanges + 1)); //$NON-NLS-1$
		}
	}

	private void refreshButtons() {
		btnRemove.setEnabled(lstMarkers.getSelectionIndex() != -1);
	}

	private void updateUIForSelection() {
		grpGeneral.layout();

		int iRangeIndex = getMarkerIndex();
		DialRegion range = (DialRegion) getDialForProcessing().getDialRegions()
				.get(iRangeIndex);

		// Update the value fields
		txtStartValue.setText(getValueAsString(range.getStartValue()));
		txtEndValue.setText(getValueAsString(range.getEndValue()));

		// Update the radius fields
		txtInnerRadius.setText(String.valueOf(range.getInnerRadius()));
		txtOuterRadius.setText(String.valueOf(range.getOuterRadius()));

		// Update the fill
		fccRange.setFill(range.getFill());

		// Update the Line attribute fields
		liacMarkerRange.setLineAttributes(range.getOutline());

		// Update the Label attribute fields
		lacLabel.setLabel(range.getLabel(), chart.getUnits());
	}

	private void populateLists() {
		buildList();

		if (lstMarkers.getItemCount() > 0) {
			lstMarkers.select(0);
			updateUIForSelection();
		} else {
			setState(false);
		}
	}

	private void setState(boolean bState) {
		btnStartFormatSpecifier.setEnabled(bState);
		btnEndFormatSpecifier.setEnabled(bState);
		lblStartValue.setEnabled(bState);
		txtStartValue.setEnabled(bState);
		lblEndValue.setEnabled(bState);
		txtEndValue.setEnabled(bState);
		lblInnerRadius.setEnabled(bState);
		txtInnerRadius.setEnabled(bState);
		lblOuterRadius.setEnabled(bState);
		txtOuterRadius.setEnabled(bState);
		liacMarkerRange.setEnabled(bState);
		lacLabel.setEnabled(bState);
		lblRangeFill.setEnabled(bState);
		fccRange.setEnabled(bState);

		this.grpGeneral.setEnabled(bState);
		this.grpMarkerRange.setEnabled(bState);
	}

	private void resetUI() {
		txtStartValue.setText(""); //$NON-NLS-1$
		txtEndValue.setText(""); //$NON-NLS-1$
		txtInnerRadius.setText("0.0"); //$NON-NLS-1$
		txtOuterRadius.setText("0.0"); //$NON-NLS-1$
		fccRange.setFill(null);
		liacMarkerRange.setLineAttributes(null);
		liacMarkerRange.layout();
		lacLabel.setLabel(LabelImpl.create(), chart.getUnits());
		lacLabel.layout();
	}

	private org.eclipse.birt.chart.model.component.Label getSelectedMarkerLabel() {
		int iMarkerIndex = getMarkerIndex();
		return ((DialRegion) (getDialForProcessing().getDialRegions().get(
				iMarkerIndex))).getLabel();
	}

	private DataElement getTypedDataElement(String strDataElement) {
		if ( strDataElement.trim( ).length( ) == 0 )
		{
			return NumberDataElementImpl.create( 0.0 );
		}
		NumberFormat nf = NumberFormat.getNumberInstance( Locale.getDefault( ) );

		try
		{
			Number numberElement = nf.parse( strDataElement );
			return NumberDataElementImpl.create( numberElement.doubleValue( ) );
		}
		catch ( ParseException e1 )
		{
			return NumberDataElementImpl.create( 0.0 );
		}
	}

	private String trimString( String input )
	{
		if ( input.trim( ).length( ) == 0 )
		{
			return "0.0"; //$NON-NLS-1$
		}
		else
		{
			return input.trim( );
		}
	}
}
