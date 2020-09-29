/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.matcalc.groups;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.text.RegexUtils;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The class KeyXmlHandler.
 */
public class XYSeriesXmlHandler extends DefaultHandler {

  /** The m groups. */
  private Collection<XYSeries> mGroups;

  /** The m regex mode. */
  private boolean mRegexMode;

  /** The m name. */
  private String mName;

  /** The m color. */
  private Color mColor;

  /** The m regexes. */
  private List<String> mRegexes;

  /**
   * Instantiates a new key xml handler.
   *
   * @param groups the groups
   */
  public XYSeriesXmlHandler(Collection<XYSeries> groups) {
    mGroups = groups;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   * java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    if (qName.equals("group")) {
      mName = attributes.getValue("name");
      mColor = ColorUtils.decodeHtmlColor(attributes.getValue("color"));
      mRegexes = new ArrayList<String>();

    } else if (qName.equals("search")) {
      mRegexMode = true;
    } else {

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName.equals("group")) {
      mGroups.add(new XYSeries(mName, RegexUtils.compile(mRegexes), mColor));
    } else if (qName.equals("search")) {
      mRegexMode = false;
    } else {
      // Do nothing
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
   */
  @Override
  public void characters(char ch[], int start, int length) throws SAXException {
    if (mRegexMode) {
      mRegexes.add(new String(ch, start, length));
    }
  }
}
