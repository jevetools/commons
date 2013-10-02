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
import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * Copyright (c) 2013, jEVETools.
 * 
 * All rights reserved.
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class AbstractUtility // NOPMD
{
  /**
   * @param aConfiguration
   *            {@link Configuration}
   * @param aProperties
   *            {@link Dictionary}
   * 
   * @throws IOException
   *             on error
   * 
   * @since 0.0.1
   */
  protected final void updateConfiguration(final Configuration aConfiguration,
      final Dictionary<String, Object> aProperties) throws IOException
  {
    aConfiguration.update(aProperties);
  }

  /**
   * @param aService
   *            {@link ConfigurationAdmin}
   * @param aPid
   *            pid
   * 
   * @return {@link Configuration}
   * 
   * @throws IOException
   *             on error
   * 
   * @since 0.0.1
   */
  protected final Configuration getSrvConfiguration(
      final ConfigurationAdmin aService, final String aPid) throws IOException
  {
    return aService.getConfiguration(aPid);
  }

  /**
   * @param <T>
   *            type
   * @param aBundleContext
   *            {@link BundleContext}
   * @param aClass
   *            type
   * 
   * @return {@link ServiceReference}
   * 
   * @since 0.0.1
   */
  protected final <T> ServiceReference<T> getSrvReference(
      final BundleContext aBundleContext, final Class<T> aClass)
  {
    return aBundleContext.getServiceReference(aClass);
  }

  /**
   * @param <T>
   *            type
   * @param aBundleContext
   *            {@link BundleContext}
   * @param aServiceReference
   *            {@link ServiceReference}
   * 
   * @return service
   * 
   * @since 0.0.1
   */
  protected final <T> T getService(final BundleContext aBundleContext,
      final ServiceReference<T> aServiceReference)
  {
    return aBundleContext.getService(aServiceReference);
  }
}