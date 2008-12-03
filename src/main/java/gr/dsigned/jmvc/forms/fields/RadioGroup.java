/*
 *  RadioGroup.java
 *
 *  Copyright (C) 2008 Vas Chryssikou <vchrys@gmail.com>
 *
 *  This module is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version. See http://www.gnu.org/licenses/lgpl.html.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package gr.dsigned.jmvc.forms.fields;

import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.types.Tuple2;
import java.util.ArrayList;

/**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class RadioGroup extends Field {

    private ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();

    /**
     * This class represents a radio button group.
     * This constructor creates a group with no initially selected button
     * @param labelName The label displayed by the button group not the buttons
     * @param fieldName The name of the group (all buttons will have the same name 
     * @param inputValues Map of (value of radiobutton, label of radiobutton)
     * @param rules
     */
    public RadioGroup(String labelName, String fieldName, Hmap inputValues, Tuple2<Rule, String>... rules) {
        super(fieldName, rules);
        super.setLabelName(labelName);
        for (String k : inputValues.keySet()) {
            addRadio(new RadioButton(fieldName, inputValues.get(k), k));
        }
    }

    /**
     * This class represents a radio button group.
     * This constructor creates a group with an initially selected button 
     * @param labelName The label displayed by the button group not the buttons
     * @param fieldName The name of the group (all buttons will have the same name 
     * @param inputValues Map of (value of radiobutton, label of radiobutton)
     * @param initiallyCheckedByValue If there is a button with this value it wiil be set as selected
     * @param rules
     */
    public RadioGroup(String labelName, String fieldName, Hmap inputValues, String initiallyCheckedByValue, Tuple2<Rule, String>... rules) {
        super(fieldName, rules);
        super.setLabelName(labelName);
        for (String k : inputValues.keySet()) {
            addRadio(new RadioButton(fieldName, inputValues.get(k), k));
        }
        this.setCheckedByValue(initiallyCheckedByValue);
    }

    /**
     * This class represents a radio button group.
     * This constructor creates a group with an initially selected button 
     * @param labelName The label displayed by the button group not the buttons
     * @param fieldName The name of the group (all buttons will have the same name 
     * @param inputValues Map of (value of radiobutton, label of radiobutton)
     * @param initiallyCheckedByIndex If there is a button at this position it wiil be set as selected
     * @param rules
     */
    public RadioGroup(String labelName, String fieldName, Hmap inputValues, int initiallyCheckedByIndex, Tuple2<Rule, String>... rules) {
        super(fieldName, rules);
        super.setLabelName(labelName);
        for (String k : inputValues.keySet()) {
            addRadio(new RadioButton(fieldName, inputValues.get(k), k));
        }
        setCheckedIndex(initiallyCheckedByIndex);
    }

    @Override
    public String renderField() {
        return String.format("%1$s %n", renderRadioGroup(), getErrors());
    }

    private RadioButton addRadio(RadioButton radioInput) {
        radioButtons.add(radioInput);
        return radioInput;
    }

    private void setRadioButtons(ArrayList<RadioButton> radioInputs) {
        for (RadioButton radioInput : radioInputs) {
            radioInputs.add(radioInput);
        }
    }

    /**
     * (Re)creates the radio buttons
     * @param radioValues Map of (value of radiobutton, label of radiobutton)
     */
    public void setRadioButtons(Hmap radioValues) {
        for (String o : radioValues.keySet()) {
            addRadio(new RadioButton(getFieldName(), o, radioValues.get(o)));
        }
    }

    /**
     * Renders the group to HTML
     * @return Html group of radio buttons
     */
    public String renderRadioGroup() {
        StringBuilder sb = new StringBuilder();
        for (RadioButton radioInput : radioButtons) {
            sb.append(radioInput.render());
        }
        return sb.toString();
    }

    /**
     * Sets the checked attribute for the group of radiobuttons 
     * according to an integer which corrensponds 
     * to the the order inputs are rendered
     * @param i
     */
    public void setCheckedIndex(int index) {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (i == index) {
                radioButtons.get(i).checked();
            } else {
                radioButtons.get(i).unchecked();
            }
        }
    }

    @Override
    public String getValue() {
        String out = "";
        for (RadioButton radioInput : radioButtons) {
            if (radioInput.isChecked()) {
                out = radioInput.getValue();
            }
        }
        return out;
    }

    /**
     * Sets the checked attribute for the group of radiobuttons according to a specific value attribute
     * @param valueOfInput
     */
    public void setCheckedByValue(String valueOfInput) {
        for (RadioButton radioInput : radioButtons) {
            if (radioInput.getValue().equalsIgnoreCase(valueOfInput)) {
                radioInput.checked();
            }
        }
    }

    /**
     * Deselects all buttons
     */
    public void reset() {
        setCheckedIndex(-1);
    }
}
