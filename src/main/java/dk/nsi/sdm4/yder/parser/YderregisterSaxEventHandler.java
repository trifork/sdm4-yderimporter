/**
 * The MIT License
 *
 * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
 * (http://www.nsi.dk)
 *
 * Copyright (C) 2011 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.nsi.sdm4.yder.parser;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import dk.nsi.sdm4.core.parser.ParserException;
import dk.nsi.sdm4.core.persistence.recordpersister.Record;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordBuilder;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordPersister;
import dk.nsi.sdm4.yder.recordspecs.YderregisterRecordSpecs;

import static java.lang.String.format;

public class YderregisterSaxEventHandler extends DefaultHandler {
    private static final Logger logger = Logger.getLogger(YderregisterSaxEventHandler.class);
    private static final String SUPPORTED_INTERFACE_VERSION = "S1040025";
    private static final String EXPECTED_RECIPIENT_ID = "F053";

    protected final DateFormat datoFormatter = new SimpleDateFormat("yyyyMMdd");

    private static final String ROOT_QNAME = "etds1040025XML";
    private static final String START_QNAME = "Start";
    private static final String END_QNAME = "Slut";
    private static final String YDER_QNAME = "Yder";
    private static final String PERSON_QNAME = "Person";

    private long yderRecordCount = 0;
    private long personRecordCount = 0;

    private String versionNumber = null;
    
    RecordPersister persister;

    public YderregisterSaxEventHandler(RecordPersister persister) {
        this.persister = persister;
        yderRecordCount = 0;
        personRecordCount = 0;
        versionNumber = null;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (START_QNAME.equals(qName)) {
            parseStartElement(attributes);
        } else if (YDER_QNAME.equals(qName)) {
            yderRecordCount += 1;
            parseYder(attributes);
        } else if (PERSON_QNAME.equals(qName)) {
            personRecordCount += 1;
            parsePerson(attributes);
        } else if (END_QNAME.equals(qName)) {
            parseEndRecord(attributes, yderRecordCount, personRecordCount);
        } else if (ROOT_QNAME.equals(qName)) {
            // ignore the root
        } else {
            throw new ParserException(format("Unknown xml element '%s' in yderregister.", qName));
        }
    }

    private void parseEndRecord(Attributes att, long yderRecordCount, long personRecordCount) {
        long expectedRecordCount = Long.parseLong(att.getValue("AntPost"));
        if (logger.isDebugEnabled()) {
            logger.debug(format("Found %s Yder records and %s Person records", yderRecordCount, personRecordCount));
        }

        long recordCount = yderRecordCount + personRecordCount;
        if (recordCount != expectedRecordCount) {
            throw new ParserException(format("The expected number of records '%d' did not match the actual '%d'.",
                    expectedRecordCount, recordCount));
        }
    }

    private void parsePerson(Attributes attributes) {
        Record record = new RecordBuilder(YderregisterRecordSpecs.PERSON_RECORD_TYPE).field("HistIdPerson",
                getValue(attributes, "HistIdPerson")).field("YdernrPerson",
                removeLeadingZeroes(attributes.getValue("YdernrPerson"))).field("CprNr", getValue(attributes, "CprNr"))
                .field("TilgDatoPerson", getValue(attributes, "TilgDatoPerson")).field("AfgDatoPerson",
                        getValue(attributes, "AfgDatoPerson")).field("PersonrolleKode",
                        getValue(attributes, "PersonrolleKode")).field("PersonrolleTxt",
                        getValue(attributes, "PersonrolleTxt")).build();

        try {
            persister.persist(record, YderregisterRecordSpecs.PERSON_RECORD_TYPE);
        } catch (SQLException e) {
            throw new ParserException(e);
        }
    }

    private void parseYder(Attributes attributes) {
        Record record = new RecordBuilder(YderregisterRecordSpecs.YDER_RECORD_TYPE).field("HistIdYder",
                getValue(attributes, "HistIdYder")).field("AmtKodeYder", getValue(attributes, "AmtKodeYder")).field(
                "AmtTxtYder", getValue(attributes, "AmtTxtYder")).field("YdernrYder",
                removeLeadingZeroes(attributes.getValue("YdernrYder"))).field("PrakBetegn",
                getValue(attributes, "PrakBetegn")).field("AdrYder", getValue(attributes, "AdrYder")).field(
                "PostnrYder", getValue(attributes, "PostnrYder")).field("PostdistYder",
                getValue(attributes, "PostdistYder")).field("AfgDatoYder", getValue(attributes, "AfgDatoYder")).field(
                "TilgDatoYder", getValue(attributes, "TilgDatoYder")).field("HvdSpecKode",
                getValue(attributes, "HvdSpecKode")).field("HvdSpecTxt", getValue(attributes, "HvdSpecTxt")).field(
                "HvdTlf", getValue(attributes, "HvdTlf")).field("EmailYder", getValue(attributes, "EmailYder")).field(
                "WWW", getValue(attributes, "WWW")).build();

        try {
            persister.persist(record, YderregisterRecordSpecs.YDER_RECORD_TYPE);
        } catch (SQLException e) {
            throw new ParserException(e);
        }
    }

    private String getValue(Attributes attributes, String name) {
        String value = attributes.getValue(name);
        return StringUtils.trimToNull(value);
    }

    private void parseStartElement(Attributes att) throws SAXException {
        String receiverId = getValue(att, "Modt");

        if (!EXPECTED_RECIPIENT_ID.equals(receiverId)) {
            throw new ParserException(format("The recipient id in the file '%s' did not match the expected '%s'.",
                    receiverId, EXPECTED_RECIPIENT_ID));
        }

        String interfaceId = getValue(att, "SnitfladeId");

        if (!SUPPORTED_INTERFACE_VERSION.equals(interfaceId)) {
            throw new ParserException(format("The interface id in the file '%s' did not match the expected '%s'.",
                    interfaceId, SUPPORTED_INTERFACE_VERSION));
        }

        // This number is used to check the sequence once the parser is done.
        //
        versionNumber = getValue(att, "OpgDato");
    }

    /**
     * Strips leading zeros but leaves one if the input is all zeros. E.g.
     * "0000" -> "0".
     */
    private String removeLeadingZeroes(String valueToStrip) {
        return valueToStrip.replaceFirst("^0+(?!$)", "");
    }

    public String GetVersionFromFileSet() {
        return versionNumber;
    }
}
