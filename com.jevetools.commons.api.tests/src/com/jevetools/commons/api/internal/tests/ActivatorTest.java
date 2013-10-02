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
package com.jevetools.commons.api.internal.tests;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;
import org.osgi.framework.VersionRange;

import com.jevetools.commons.test.AbstractTest;

/**
 * Copyright (c) 2013, jEVETools.
 * 
 * All rights reserved.
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ActivatorTest
    extends AbstractTest
{
  /**
   * @throws InvalidSyntaxException
   *             on error
   */
  @Test
  public void testImports() throws InvalidSyntaxException
  {
    final List<PackageImport> packages = new ArrayList<>();

    packages.add(new PackageImport("org.osgi.framework", new VersionRange(
        VersionRange.LEFT_CLOSED, new Version(1, 7, 0), null,
        VersionRange.RIGHT_OPEN)));

    packages.add(new PackageImport("org.osgi.service.cm", new VersionRange(
        VersionRange.LEFT_CLOSED, new Version(1, 4, 0), null,
        VersionRange.RIGHT_OPEN)));

    final Bundle testSubject = getTestSubject();

    assertThat(testSubject, not(nullValue()));

    checkImports(testSubject, packages);
  }

  /**
   */
  @Test
  public void testExports()
  {
    final List<PackageExport> packages = new ArrayList<>(1);

    packages.add(new PackageExport("com.jevetools.commons.api", new Version(0,
        0, 1)));

    packages.add(new PackageExport("com.jevetools.commons.api.service",
        new Version(0, 0, 1)));

    final Bundle testSubject = getTestSubject();

    assertThat(testSubject, not(nullValue()));

    checkExports(testSubject, packages);
  }
}
