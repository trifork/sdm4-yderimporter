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
package dk.nsi.sdm4.yder.recordspecs;

import dk.nsi.sdm4.core.persistence.recordpersister.RecordSpecification;

import static dk.nsi.sdm4.core.persistence.recordpersister.FieldSpecification.field;


public class YderregisterRecordSpecs
{
    public static final RecordSpecification START_RECORD_TYPE = RecordSpecification.createSpecification("DummyTable", "DummyKey",
            field("OpgDato", 8),
            field("Timestamp", 20),
            field("Modt", 6),
            field("SnitfladeId", 8)
    );

    public static final RecordSpecification YDER_RECORD_TYPE = RecordSpecification.createSpecification("Yderregister", "HistIdYder",
            field("HistIdYder", 16),
            field("AmtKodeYder", 2),
            field("AmtTxtYder", 60),
            field("YdernrYder", 6),
            field("PrakBetegn", 50),
            // Att
            field("AdrYder", 50),
            field("PostnrYder", 4),
            field("PostdistYder", 20),
            field("TilgDatoYder", 8),
            field("AfgDatoYder", 8),
            // OverensKode
            // OverenskomstTxt
            // LandsYdertypeKode
            // LandsYdertypeTxt
            field("HvdSpecKode", 2),
            field("HvdSpecTxt", 60),
            // IndberetFormKode
            // IndberetFormTxt
            // SelskFormKode
            // SelskFormTxt
            // SkatOpl
            // PrakFormKode
            // PrakFormTxt
            // PrakTypeKode
            // PrakTypeTxt
            // SamarbFormKode
            // SamarbFormTxt
            // PrakKomKode
            // PrakKomTxt
            field("HvdTlf", 8),
            // Fax
            field("EmailYder", 50),
            field("WWW", 78)
            // ...
    );

    public static final RecordSpecification PERSON_RECORD_TYPE = RecordSpecification.createSpecification("YderregisterPerson", "HistIdPerson",
            field("HistIdPerson", 16),
            field("YdernrPerson", 6),
            field("TilgDatoPerson", 8),
            field("AfgDatoPerson", 8),
            field("CprNr", 10),
            // Navn
            field("PersonrolleKode", 2),
            field("PersonrolleTxt", 60)
            // ...
    );
}
