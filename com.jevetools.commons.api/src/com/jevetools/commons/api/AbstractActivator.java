/*
 * Copyright (c) 2013, jEVETools
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the names of the contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.jevetools.commons.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;

/**
 * Copyright (c) 2013, jEVETools.
 * 
 * All rights reserved.
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class AbstractActivator
    extends AbstractUtility
    implements BundleActivator
{
  /**
   * @since 0.0.1
   */
  private static final String PROPERTIES = "OSGI-INF/component.properties";

  /**
   * @param aBundle
   *            {@link Bundle}
   * 
   * @return {@link Dictionary}
   * 
   * @throws IOException
   *             on error
   * 
   * @since 0.0.1
   */
  protected final Dictionary<String, Object> getProperties(final Bundle aBundle)
      throws IOException
  {
    final Properties properties = loadProperties(aBundle, PROPERTIES);

    final Dictionary<String, Object> dict = convertProperties(properties);

    return dict;
  }

  /**
   * Returns an {@link URL} for a path in a given {@link Bundle}.
   * 
   * @param aBundle
   *            {@link Bundle} used to get an URL for a path
   * @param aPath
   *            path
   * 
   * @return {@link URL} for a path in a given {@link Bundle}
   * 
   * @since 0.0.1
   */
  protected final URL getEntry(final Bundle aBundle, final String aPath)
  {
    return aBundle.getEntry(aPath);
  }

  /**
   * Loads properties from a file in a given {@link Bundle}.
   * 
   * @param aBundle
   *            {@link Bundle} to load properties from
   * @param aPath
   *            file to load properties from
   * 
   * @return {@link Properties} loaded from a path in a given {@link Bundle}
   * 
   * @throws IOException
   *             on error
   * 
   * @since 0.0.1
   */
  protected final Properties loadProperties(final Bundle aBundle,
      final String aPath) throws IOException
  {
    final URL url = getEntry(aBundle, aPath);

    final InputStream stream = getStream(url);

    final Properties properties = loadProperties(stream);

    closeStream(stream);

    return properties;
  }

  /**
   * @param stream {@link InputStream}
   * 
   * @throws IOException on error
   */
  private void closeStream(final InputStream stream) throws IOException
  {
    stream.close();
  }

  /**
   * Loads properties from {@link InputStream}.
   * 
   * @param aInputStream
   *            {@link InputStream} to load properties from
   * 
   * @return {@link Properties} loaded from {@link InputStream}
   * 
   * @throws IOException
   *             on error
   * 
   * @since 0.0.1
   */
  protected final Properties loadProperties(final InputStream aInputStream)
      throws IOException
  {
    final Properties properties = new Properties();

    properties.load(aInputStream);

    return properties;
  }

  /**
   * @param aUrl {@link URL}
   * 
   * @return {@link InputStream}
   * 
   * @throws IOException on error
   */
  private InputStream getStream(final URL aUrl) throws IOException
  {
    return aUrl.openStream();
  }

  /**
   * @param aProperties
   *            properties
   * 
   * @return {@link Dictionary}
   * 
   * @since 0.0.1
   */
  private Dictionary<String, Object> convertProperties(
      final Properties aProperties)
  {
    final Iterator<Entry<Object, Object>> itr = getIterator(aProperties);

    return convertProperties(itr);
  }

  /**
   * @param aProperties
   *            properties
   * 
   * @return {@link Iterator}
   * 
   * @since 0.0.1
   */
  private Iterator<Entry<Object, Object>> getIterator(
      final Properties aProperties)
  {
    final Set<Entry<Object, Object>> set = getPropertiesSet(aProperties);

    final Iterator<Entry<Object, Object>> itr = getSetIterator(set);

    return itr;
  }

  /**
   * @param set
   *            {@link Set}
   * 
   * @return {@link Iterator}
   * 
   * @since 0.0.1
   */
  private Iterator<Entry<Object, Object>> getSetIterator(
      final Set<Entry<Object, Object>> set)
  {
    return set.iterator();
  }

  /**
   * @param aProperties
   *            properties
   * 
   * @return {@link Set}
   * 
   * @since 0.0.1
   */
  private Set<Entry<Object, Object>> getPropertiesSet(
      final Properties aProperties)
  {
    return aProperties.entrySet();
  }

  /**
   * @param aIterator
   *            {@link Iterator}
   * 
   * @return {@link Dictionary}
   * 
   * @since 0.0.1
   */
  private Dictionary<String, Object> convertProperties(
      final Iterator<Entry<Object, Object>> aIterator)
  {
    final Dictionary<String, Object> dict = new Hashtable<String, Object>();

    while (aIterator.hasNext())
    {
      final Map.Entry<Object, Object> entry = getNextProperty(aIterator);

      dict.put(getKey(entry), getValue(entry));
    }

    return dict;
  }

  /**
   * @param aIterator {@link Iterator}}
   * 
   * @return {@link Entry}
   */
  private Entry<Object, Object> getNextProperty(
      final Iterator<Entry<Object, Object>> aIterator)
  {
    return (Map.Entry<Object, Object>) aIterator.next();
  }

  /**
   * @param aEntry {@link Map.Entry}
   * @return {@link String}
   */
  private String getKey(final Map.Entry<Object, Object> aEntry)
  {
    return (String) aEntry.getKey();
  }

  /**
   * @param aEntry {@link Map.Entry}
   * @return {@link Object}
   */
  private Object getValue(final Map.Entry<Object, Object> aEntry)
  {
    return aEntry.getValue();
  }

}