/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pronoia.junit.asserts.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

/**
 * Assertions for {@link File} objects referring to files in the filesystem.
 */
public final class FileAssert {

  private FileAssert() {}
  /**
   * Asserts that a {@link File} exists and refers to a file.
   *
   * @param fileName expected file name
   */
  public static void assertFileExists(final String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("File name argument cannot be null or empty");
    }

    assertFileExists(new File(fileName));
  }

  /**
   * Asserts that a {@link File} exists and refers to a file.
   *
   * @param file expected file
   */
  public static void assertFileExists(final File file) {
    assertTrue( String.format("File %s does not exist", file), file.exists());
    assertTrue( String.format("%s does not refer to a file", file), file.isFile());
  }

  /**
   * Asserts that a {@link File} does not exist.
   *
   * @param fileName expected file name
   */
  public static void assertFileNotExists(final String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("File name argument cannot be null or empty");
    }
    assertFileNotExists(new File(fileName));
  }

  /**
   * Asserts that a {@link File} does not exist.
   *
   * @param file expected file
   */
  public static void assertFileNotExists(final File file) {
    assertFalse( String.format("%s exists", file), file.exists());
  }
}
