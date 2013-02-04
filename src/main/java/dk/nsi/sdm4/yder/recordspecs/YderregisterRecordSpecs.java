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
            field("OpgDato", 8, false),
            field("Timestamp", 20, false),
            field("Modt", 6, false),
            field("SnitfladeId", 8, false)
    );

    public static final RecordSpecification YDER_RECORD_TYPE = RecordSpecification.createSpecification("Yderregister", "Id",
            // Id is a MD5 of the following fields HistIdYder + YdernrYder + TilgDatoYder, seperated by -
            field("Id", 32, false),
            field("HistIdYder", 16, false),
            field("AmtKodeYder", 2, false),
            field("AmtTxtYder", 60, false),
            field("YdernrYder", 6, false),
            field("PrakBetegn", 50, false),
            // Att
            field("AdrYder", 50, false),
            field("PostnrYder", 4, false),
            field("PostdistYder", 20, false),
            field("TilgDatoYder", 8, false),
            field("AfgDatoYder", 8, false),
            // OverensKode
            // OverenskomstTxt
            // LandsYdertypeKode
            // LandsYdertypeTxt
            field("HvdSpecKode", 2, false),
            field("HvdSpecTxt", 60, false),
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
            field("HvdTlf", 8, false),
            // Fax
            field("EmailYder", 50, false),
            field("WWW", 78, false)
            // ...
    );

    public static final RecordSpecification PERSON_RECORD_TYPE = RecordSpecification.createSpecification("YderregisterPerson", "Id",
            // Id is a MD5 of the following fields HistIdPerson + CprNr + YdernrPerson + TilgDatoPerson, seperated by -
            field("Id", 32, false),
            field("HistIdPerson", 16, false),
            field("YdernrPerson", 6, false),
            field("TilgDatoPerson", 8, false),
            field("AfgDatoPerson", 8, false),
            field("CprNr", 10, false),
            // Navn
            field("PersonrolleKode", 2, false),
            field("PersonrolleTxt", 60, false)
            // ...
    );
}
