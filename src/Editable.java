import java.awt.Color;

import SwingMan.SwingThing;


/**
 * @author Stephen Vorwerk + Max Kurtz
 * @version 0.9
 * <b>Editable<b> <code>extends</code> <b>SwingThing</b>
 * <p>
 * Editable is an interface extension of SwingThing. All of the
 * JComponents represented in SwingUIEditor implement this interface.
 * <p>
 * Extending SwingThing allows the BasicGUITool paradigm of an ArrayList{SwingThing} figures
 * to be implemented while at the same time offering a more customized control of 
 * the objects interactivity with the software. 
 * <p>
 * Editable has 10 static final integer variables, which are used by the 
 * properties panel when generating the correct JComponent to use for the variable type.
 * These static integers are:
 * <ul>
 * <li>	<code>OPTION_IS_TEXT</code>=0;</li>
 * <li><code>OPTION_IS_INT</code>=1;</li>
 * <li><code>OPTION_IS_BOOLEAN</code>=2;</li>
 * <li><code>OPTION_IS_CHAR</code>=3;</li>
 * <li><code>OPTION_IS_COLOR</code>=4;</li>
 * <li><code>OPTION_IS_FONT</code>=5;</li>
 * <li><code>OPTION_IS_EH</code>=6;</li>
 * <li><code>OPTION_IS_SOURCE</code>=7;</li>
 * <li><code>OPTION_IS_SCOPE</code>=8;</li>
 * <li><code>OPTION_IS_COMBOBOX</code>=9;</li>
 * <ul>
 */
public interface Editable extends SwingThing {
	public static final int OPTION_IS_TEXT=0;
	public static final int OPTION_IS_INT=1;
	public static final int OPTION_IS_BOOLEAN=2;
	public static final int OPTION_IS_CHAR=3;
	public static final int OPTION_IS_COLOR=4;
	public static final int OPTION_IS_FONT=5;
	public static final int OPTION_IS_EH=6;
	public static final int OPTION_IS_SOURCE=7;
	public static final int OPTION_IS_SCOPE=8;
	public static final int OPTION_IS_COMBOBOX=9;

	/**
	 * Sets the appended source of the Editable Object.
	 * @param newSource	The String value of the new source.
	 */
	public void setSource(String newSource);
	/**
	 * Moves the object's x,y values by dx and dy.
	 * @param dx	Change in X
	 * @param dy	Change in Y
	 */
	public void moveBy(int dx, int dy);
	/**
	 * Returns the X value of the object as it exists on the SwingUIEditor's myDesignFrame.
	 * @return	<code>int</code>	X-Value
	 */
	public int getXPos();
	/**
	 * Returns the Y value of the object as it exists on the SwingUIEditor's myDesignFrame.
	 * @return	<code>int</code>	Y-Value
	 */
	public int getYPos();
	/**
	 * Sets the X value of the object as it exists on the SwingUIEditor's myDesignFrame.
	 * @param	<code>int</code>	X-Value
	 */
	public void setXPos(int x);
	/**
	 * Sets the Y value of the object as it exists on the SwingUIEditor's myDesignFrame.
	 * @param	<code>int</code>	Y-Value
	 */
	public void setYPos(int y);
	/**
	 * Returns the width value of the object as it exists on the SwingUIEditor's myDesignFrame.
	 * @return	<code>int</code>	Width-Value
	 */
	public int getWDimension();
	/**
	 * Returns the height value of the object as it exists on the SwingUIEditor's myDesignFrame.
	 * @return	<code>int</code>	Height-Value
	 */
	public int getHDimension();
	/**
	 * Loads and reloads the properties of the Editable object, as they change throughout program use.
	 */
	public void loadProperties();
	/**
	 * Sets the property value at propertyValues[index] to the String value.
	 * @param index	The index of the property being set.
	 * @param value	The value to set the property to.
	 */
	public void setProperties(int index, String value);
	/**
	 * Returns the property names followed by a comma, followed by the property value,
	 * another comma, and the type of Editable OPTION the property is.
	 * 
	 * This function is used by the properties panel to load the property editing panel.
	 * @return <code>String[]</code>	Returns an array of corresponding Property Names and Property Values
	 */
	public String[] getPropertyNamesAndValues();
	/**
	 * Returns the header info (the import statements) of the object.
	 * @return	<code>String</code>	Header Info
	 */
	public String getHeaderInfo();
	/**
	 * Returns the appended source of the object.
	 * @return	<code>String</code>	The appended source
	 */
	public String getSource();
	/**
	 * This function translates the color properties from their textual representation
	 * to the corresponding Color object.
	 * @param colorString	The String to be translated (in the form: rgb(RED,GREEN,BLUE) )
	 * @return	<code>Color</code>	The Color object represented by the paramater <code>colorString</code>
	 */
	public Color getColorFromProperties(String colorString);
	/**
	 * Returns just the property <i>Values</i>, separated by commas
	 * @return	<code>String</code>	Object's Property Values separated by commas
	 */
	public String getPropertyValues();
	/**
	 * Returns the variables scope {<code>local</code>, <code>public</code>, or <code>private</code>}
	 * @return	<code>String</code>	The Object's Scope property value
	 */
	public String getScope();
	/**
	 * Sets the Scope property value of an Editable Object
	 * @param scope	The <code>String</code> representing the object's scope
	 */
	public void setScope(String scope);
	/**
	 * Returns a <code>String</code> corresponding the type of JComponent the object represents.
	 * @return	<code>String</code>	The Editable Object's JComponent counterpart 
	 */
	public String getType();
}
