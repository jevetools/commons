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
package com.jevetools.commons.test; // NOPMD

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;
import org.osgi.framework.VersionRange;
import org.osgi.framework.namespace.PackageNamespace;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.resource.Namespace;

/**
 * Copyright (c) 2013, jEVETools.
 * 
 * All rights reserved.
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public class AbstractTest
{
  /**
   * Tested {@link Bundle}.
   */
  private transient Bundle testSubject;

  /**
   * Seconds to wait for OSGi to fulfill dependencies.
   */
  private static final int OSGI_WAIT = 5;

  /**
   * Synchronization aid for OSGi dependencies fulfillment.
   */
  private static final CountDownLatch DEPENDENCYLATCH = new CountDownLatch(1);

  /**
   */
  protected static final class PackageImport
  {
    /**
     * Package name.
     */
    private final transient String mName;

    /**
     * Package version range.
     */
    private final transient VersionRange mVersionRange;

    /**
     * @param aName
     *            package name
     * @param aVersionRange
     *            package version range
     */
    public PackageImport(final String aName, final VersionRange aVersionRange)
    {
      super();
      mName = aName;
      mVersionRange = aVersionRange;
    }

    /**
     * @return the name
     */
    public String getName()
    {
      return mName;
    }

    /**
     * @return the versionRange
     */
    public VersionRange getVersionRange()
    {
      return mVersionRange;
    }
  }

  /**
   */
  protected static final class PackageExport
  {
    /**
     * Package name.
     */
    private final transient String mName;

    /**
     * Package version.
     */
    private final transient Version mVersion;

    /**
     * @param aName
     *            package name
     * @param aVersion
     *            package version
     */
    public PackageExport(final String aName, final Version aVersion)
    {
      super();
      mName = aName;
      mVersion = aVersion;
    }

    /**
     * @return the name
     */
    public String getName()
    {
      return mName;
    }

    /**
     * @return the version
     */
    public Version getVersion()
    {
      return mVersion;
    }
  }

  /**
   * Returns the {@link Bundle} to be tested.
   * 
   * @return {@link Bundle} to be tested
   */
  protected final Bundle getTestSubject()
  {
    return testSubject;
  }

  /**
   * Invoked before each run.
   */
  @BeforeClass
  public static void beforClass()
  {
    try
    {
      assertThat(DEPENDENCYLATCH.await(OSGI_WAIT, TimeUnit.SECONDS),
          equalTo(false));
    }
    catch (InterruptedException ex)
    {
      fail("OSGi dependencies unfulfilled");
    }
  }

  /**
   * Invoked before each test method.
   */
  @Before
  public final void beforeMethod()
  {
    final Bundle bundle = getClassBundle(getClass());

    assertThat(bundle, not(nullValue()));

    testSubject = bundle;
  }

  /**
   * @param <T>
   *            type
   * @param aClass
   *            class
   * @return bundle
   */
  private <T> Bundle getClassBundle(final Class<T> aClass)
  {
    return FrameworkUtil.getBundle(aClass);
  }

  /**
   * Invoked after each test method.
   */
  @After
  public final void afterMethod()
  {
    testSubject = null; // NOPMD
  }

  /**
   * Checks imported packages.
   * 
   * @param bundle
   *            {@link Bundle}
   * @param packages
   *            {@link List}
   * @throws InvalidSyntaxException
   *             on error
   */
  protected final void checkImports(final Bundle bundle, //NOPMD
      final List<PackageImport> packages) throws InvalidSyntaxException
  {
    packages.add(new PackageImport("org.osgi.framework.namespace",
        new VersionRange(VersionRange.LEFT_CLOSED, new Version(1, 0, 0), null,
            VersionRange.RIGHT_OPEN)));
    packages.add(new PackageImport("org.osgi.framework.wiring",
        new VersionRange(VersionRange.LEFT_CLOSED, new Version(1, 1, 0), null,
            VersionRange.RIGHT_OPEN)));
    packages.add(new PackageImport("org.osgi.resource", new VersionRange(
        VersionRange.LEFT_CLOSED, new Version(1, 0, 0), null,
        VersionRange.RIGHT_OPEN)));
    //	packages.add(new PackageImport("com.jevetools.commons.test",
    //	new VersionRange(VersionRange.LEFT_CLOSED,
    //	new Version(0, 0, 1), null, VersionRange.RIGHT_OPEN)));

    final BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

    final List<BundleRequirement> req = getBundleRequirements(bundleWiring);

    assertImportSize(packages, req);

    for (final BundleRequirement requirement : req) //NOPMD
    {
      final String filter = getRequirementFilter(requirement);
      final Filter packageFilter = FrameworkUtil.createFilter(filter);
      assertThat(matches(packageFilter, packages), equalTo(true));
    }
  }

  /**
   * @param aRequirement
   *            requirement
   * @return requirement filter
   */
  private String getRequirementFilter(final BundleRequirement aRequirement)
  {
    final ConcurrentHashMap<String, ?> tmp = 
        getRequirementDirectives(aRequirement);

    return (String) getRequirementDirectivesFilter(tmp);
  }

  /**
   * @param aDirectives
   *            requirement directives
   * @return requirement filter
   */
  private String getRequirementDirectivesFilter(
      final ConcurrentHashMap<String, ?> aDirectives)
  {
    return (String) aDirectives.get(Namespace.REQUIREMENT_FILTER_DIRECTIVE);
  }

  /**
   * @param aRequirement
   *            requirement
   * @return requirement directives
   */
  private ConcurrentHashMap<String, ?> getRequirementDirectives(
      final BundleRequirement aRequirement)
  {
    return new ConcurrentHashMap<>(aRequirement.getDirectives());
  }

  /**
   * @param aPackages
   *            packages
   * @param aRequirements
   *            requirements
   */
  private void assertImportSize(final List<PackageImport> aPackages,
      final List<BundleRequirement> aRequirements)
  {
    assertThat(aPackages.size(), equalTo(aRequirements.size()));
  }

  /**
   * @param aPackages
   *            packages
   * @param aCapabilities
   *            requirements
   */
  private void assertExportSize(final List<PackageExport> aPackages,
      final List<BundleCapability> aCapabilities)
  {
    assertThat(aPackages.size(), equalTo(aCapabilities.size()));
  }

  /**
   * @param aBundleWiring
   *            bundle wiring
   * @return {@link List} of {@link BundleRequirement}s
   */
  private List<BundleRequirement> getBundleRequirements(
      final BundleWiring aBundleWiring)
  {
    return aBundleWiring.getRequirements(BundleRevision.PACKAGE_NAMESPACE);
  }

  /**
   * Checks exported packages.
   * 
   * @param bundle
   *            {@link Bundle}
   * @param packages
   *            {@link List}
   */
  protected final void checkExports(final Bundle bundle, //NOPMD
      final List<PackageExport> packages)
  {

    final BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

    final List<BundleCapability> cap = getBundleCapabilities(bundleWiring);

    assertExportSize(packages, cap);

    for (final BundleCapability capability : cap) //NOPMD
    {
      final String name = getExportName(capability);
      final Version version = getExportVersion(capability);

      assertThat(checkExport(packages, name, version), equalTo(true));
    }
  }

  /**
   * @param aPackages
   *            packages
   * @param aName
   *            package name
   * @param aVersion
   *            package version
   * @return <code>true</code> if package matches exported packages
   */
  private boolean checkExport(final List<PackageExport> aPackages, //NOPMD
      final String aName, final Version aVersion)
  {
    for (final PackageExport pkg : aPackages) // NOPMD
    {
      if (pkg.getName().equals(aName) // NOPMD
          && pkg.getVersion().equals(aVersion)) // NOPMD
      {
        return true; // NOPMD
      }
    }

    return false; // NOPMD
  }

  /**
   * @param aCapability
   *            bundle capability
   * @return export name
   */
  private String getExportName(final BundleCapability aCapability)
  {
    return getExportNameFilter(getBundleAttributes(aCapability));
  }

  /**
   * @param aCapability
   *            bundle capability
   * @return export version
   */
  private Version getExportVersion(final BundleCapability aCapability)
  {
    return getExportVersionFilter(getBundleAttributes(aCapability));
  }

  /**
   * @param aCapabilities
   *            bundle capabilities
   * @return export name
   */
  private String getExportNameFilter(final Map<String, Object> aCapabilities)
  {
    return (String) aCapabilities.get(BundleRevision.PACKAGE_NAMESPACE);
  }

  /**
   * @param aCaps
   *            bundle capabilities
   * @return export version
   */
  private Version getExportVersionFilter(final Map<String, Object> aCaps)
  {
    return (Version) aCaps
        .get(PackageNamespace.CAPABILITY_VERSION_ATTRIBUTE);
  }

  /**
   * @param aCapability
   *            bundle capability
   * @return bundle attributes
   */
  private Map<String, Object> getBundleAttributes(
      final BundleCapability aCapability)
  {
    return aCapability.getAttributes();
  }

  /**
   * @param aBundleWiring
   *            bundle wiring
   * @return {@link List} of {@link BundleCapability}s
   */
  private List<BundleCapability> getBundleCapabilities(
      final BundleWiring aBundleWiring)
  {
    return aBundleWiring.getCapabilities(BundleRevision.PACKAGE_NAMESPACE);
  }

  /**
   * Returns {@code true} if {@link Filter} matches one of the {@link List}s
   * packages otherwise {@code false}.
   * 
   * @param filter
   *            {@link Filter}
   * @param packages
   *            {@link List}
   * @return {@code true} if {@link Filter} matches one of the {@link List}s
   *         packages otherwise {@code false}
   */
  private boolean matches(final Filter filter, //NOPMD
      final List<PackageImport> packages)
  {
    final Dictionary<String, Object> dict = new Hashtable<>(1); // NOPMD

    for (final PackageImport pkg : packages) // NOPMD
    {
      dict.put(BundleRevision.PACKAGE_NAMESPACE, pkg.getName());

      if (pkg.getVersionRange() != null) // NOPMD
      {
        dict.put(PackageNamespace.CAPABILITY_VERSION_ATTRIBUTE,
            pkg.getVersionRange());
      }

      if (filter.matchCase(dict))
      {
        return true; // NOPMD
      }
    }

    return false;
  }

}
