
package org.omg.spec.bpmn._20100524.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for tDataStoreReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDataStoreReference">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tFlowElement">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}dataState" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="itemSubjectRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="dataStoreRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataStoreReference", propOrder = {
    "dataState"
})
public class TDataStoreReference
    extends TFlowElement
{

    protected DataState dataState;
    @XmlAttribute(name = "itemSubjectRef")
    protected QName itemSubjectRef;
    @XmlAttribute(name = "dataStoreRef")
    protected QName dataStoreRef;

    /**
     * Gets the value of the dataState property.
     * 
     * @return
     *     possible object is
     *     {@link DataState }
     *     
     */
    public DataState getDataState() {
        return dataState;
    }

    /**
     * Sets the value of the dataState property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataState }
     *     
     */
    public void setDataState(DataState value) {
        this.dataState = value;
    }

    /**
     * Gets the value of the itemSubjectRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getItemSubjectRef() {
        return itemSubjectRef;
    }

    /**
     * Sets the value of the itemSubjectRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setItemSubjectRef(QName value) {
        this.itemSubjectRef = value;
    }

    /**
     * Gets the value of the dataStoreRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getDataStoreRef() {
        return dataStoreRef;
    }

    /**
     * Sets the value of the dataStoreRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setDataStoreRef(QName value) {
        this.dataStoreRef = value;
    }

}
