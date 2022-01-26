//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine.
// Generato il: 2021.09.26 alle 06:01:44 PM CEST
//


package it.unina.recoverminer.dto;

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * <p>Classe Java per anonymous complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="testcase">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="error">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="system-out" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="classname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="tests" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="errors" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="skipped" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="failures" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "testcase"
})
@XmlRootElement(name = "testsuite")
public class Testsuite {

    @XmlElement(required = true)
    protected List<Testcase> testcase;
    @XmlAttribute(name = "name")
    protected String name;
    //if needed, use String as type
    /*@XmlAttribute(name = "time")
    protected Float time;*/
    @XmlAttribute(name = "tests")
    protected Byte tests;
    @XmlAttribute(name = "errors")
    protected Byte errors;
    @XmlAttribute(name = "skipped")
    protected Byte skipped;
    @XmlAttribute(name = "failures")
    protected Byte failures;

    /**
     * Recupera il valore della proprietà testcase.
     *
     * @return
     *     possible object is
     *     {@link Testsuite.Testcase }
     *
     */
    public List<Testsuite.Testcase> getTestcase() {
        return testcase;
    }

    /**
     * Imposta il valore della proprietà testcase.
     *
     * @param value
     *     allowed object is
     *     {@link Testsuite.Testcase }
     *
     */
    public void setTestcase(List<Testsuite.Testcase> value) {
        this.testcase = value;
    }

    /**
     * Recupera il valore della proprietà name.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il valore della proprietà name.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Recupera il valore della proprietà time.
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
   /* public Float getTime() {
        return time;
    }*/

    /**
     * Imposta il valore della proprietà time.
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    /*public void setTime(Float value) {
        this.time = value;
    }*/

    /**
     * Recupera il valore della proprietà tests.
     *
     * @return
     *     possible object is
     *     {@link Byte }
     *
     */
    public Byte getTests() {
        return tests;
    }

    /**
     * Imposta il valore della proprietà tests.
     *
     * @param value
     *     allowed object is
     *     {@link Byte }
     *
     */
    public void setTests(Byte value) {
        this.tests = value;
    }

    /**
     * Recupera il valore della proprietà errors.
     *
     * @return
     *     possible object is
     *     {@link Byte }
     *
     */
    public Byte getErrors() {
        return errors;
    }

    /**
     * Imposta il valore della proprietà errors.
     *
     * @param value
     *     allowed object is
     *     {@link Byte }
     *
     */
    public void setErrors(Byte value) {
        this.errors = value;
    }

    /**
     * Recupera il valore della proprietà skipped.
     *
     * @return
     *     possible object is
     *     {@link Byte }
     *
     */
    public Byte getSkipped() {
        return skipped;
    }

    /**
     * Imposta il valore della proprietà skipped.
     *
     * @param value
     *     allowed object is
     *     {@link Byte }
     *
     */
    public void setSkipped(Byte value) {
        this.skipped = value;
    }

    /**
     * Recupera il valore della proprietà failures.
     *
     * @return
     *     possible object is
     *     {@link Byte }
     *
     */
    public Byte getFailures() {
        return failures;
    }

    /**
     * Imposta il valore della proprietà failures.
     *
     * @param value
     *     allowed object is
     *     {@link Byte }
     *
     */
    public void setFailures(Byte value) {
        this.failures = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     *
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="error">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="system-out" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="classname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}float" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "error",
            "failure",
            "systemOut"
    })
    public static class Testcase {

        @XmlElement(required = true)
        protected Testsuite.Testcase.Error error;
        @XmlElement(required = true)
        protected Testsuite.Testcase.Failure failure;
        @XmlElement(name = "system-out", required = true)
        protected String systemOut;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "classname")
        protected String classname;
        //if needed, use String as type
        /*@XmlAttribute(name = "time")
        protected Float time;*/

        /**
         * Recupera il valore della proprietà error.
         *
         * @return
         *     possible object is
         *     {@link Testsuite.Testcase.Error }
         *
         */
        public Testsuite.Testcase.Error getError() {
            return error;
        }

        /**
         * Imposta il valore della proprietà error.
         *
         * @param value
         *     allowed object is
         *     {@link Testsuite.Testcase.Error }
         *
         */
        public void setError(Testsuite.Testcase.Error value) {
            this.error = value;
        }

        /**
         * Recupera il valore della proprietà failure.
         *
         * @return
         *     possible object is
         *     {@link Testsuite.Testcase.Failure }
         *
         */
        public Testsuite.Testcase.Failure getFailure() {
            return failure;
        }

        /**
         * Imposta il valore della proprietà failure.
         *
         * @param value
         *     allowed object is
         *     {@link Testsuite.Testcase.Failure }
         *
         */
        public void setError(Testsuite.Testcase.Failure value) {
            this.failure = value;
        }

        /**
         * Recupera il valore della proprietà systemOut.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getSystemOut() {
            return systemOut;
        }

        /**
         * Imposta il valore della proprietà systemOut.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setSystemOut(String value) {
            this.systemOut = value;
        }

        /**
         * Recupera il valore della proprietà name.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getName() {
            return name;
        }

        /**
         * Imposta il valore della proprietà name.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Recupera il valore della proprietà classname.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getClassname() {
            return classname;
        }

        /**
         * Imposta il valore della proprietà classname.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setClassname(String value) {
            this.classname = value;
        }

        /**
         * Recupera il valore della proprietà time.
         *
         * @return
         *     possible object is
         *     {@link Float }
         *
         */
        /*public Float getTime() {
            return time;
        }*/

        /**
         * Imposta il valore della proprietà time.
         *
         * @param value
         *     allowed object is
         *     {@link Float }
         *
         */
        /*public void setTime(Float value) {
            this.time = value;
        }*/


        /**
         * <p>Classe Java per anonymous complex type.
         *
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "value"
        })
        public static class Error {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "type")
            protected String type;

            /**
             * Recupera il valore della proprietà value.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getValue() {
                return value;
            }

            /**
             * Imposta il valore della proprietà value.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Recupera il valore della proprietà type.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getType() {
                return type;
            }

            /**
             * Imposta il valore della proprietà type.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setType(String value) {
                this.type = value;
            }

        }
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "value"
        })
        public static class Failure {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "type")
            protected String type;
            @XmlAttribute(name = "message")
            protected String message;

            /**
             * Recupera il valore della proprietà value.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getValue() {
                return value;
            }

            /**
             * Imposta il valore della proprietà value.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Recupera il valore della proprietà type.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getType() {
                return type;
            }

            /**
             * Imposta il valore della proprietà type.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Recupera il valore della proprietà message.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getMessage() {
                return message;
            }

            /**
             * Imposta il valore della proprietà type.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setMessage(String value) {
                this.message = value;
            }

        }

    }

}
