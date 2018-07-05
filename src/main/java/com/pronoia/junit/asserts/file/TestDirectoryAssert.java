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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.pronoia.junit.file.TestDirectory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assertions for the contents of {@link com.pronoia.junit.file.TestDirectory} objects.
 */
public final class TestDirectoryAssert {
  static final Logger LOG = LoggerFactory.getLogger(TestDirectoryAssert.class);

  private TestDirectoryAssert() {}

  /**
   * Asserts that a {@link TestDirectory} does not contain any children.
   *
   * @param testDirectory test directory to test
   */
  public static void assertIsEmpty(final TestDirectory testDirectory) {
    DirectoryAssert.assertDirectoryIsEmpty(testDirectory.toFile());
  }

  /**
   * Asserts that a {@link TestDirectory} contains children.
   *
   * @param testDirectory test directory to test
   */
  public static void assertNotEmpty(final TestDirectory testDirectory) {
    DirectoryAssert.assertDirectoryNotEmpty(testDirectory.toFile());
  }

  /**
   * Asserts that a {@link TestDirectory} contains a specific number of children.
   *
   * @param testDirectory test directory to test
   * @param expectedChildCount expected number of children in directory
   */
  public static void assertChildCountEquals(final TestDirectory testDirectory, int expectedChildCount) {
    DirectoryAssert.assertDirectoryChildCountEquals(testDirectory.toFile(), expectedChildCount);
  }

  /**
   * Asserts that a {@link TestDirectory} contains a specific number of files.
   *
   * @param testDirectory test directory to test
   * @param expectedFileCount expected number of files in directory
   */
  public static void assertFileCountEquals(final TestDirectory testDirectory, int expectedFileCount) {
    DirectoryAssert.assertDirectoryChildFileCountEquals(testDirectory.toFile(), expectedFileCount);
  }

  /**
   * Asserts that a {@link TestDirectory} contains a specific number of child directories.
   *
   * @param testDirectory test directory to test
   * @param expectedDirectoryCount expected number of child directories in directory
   */
  public static void assertChildDirectoryCountEquals(final TestDirectory testDirectory, int expectedDirectoryCount) {
    DirectoryAssert.assertDirectoryChildFileCountEquals(testDirectory.toFile(), expectedDirectoryCount);
  }

  /**
   * Asserts that a file exists in the {@link TestDirectory}.
   *
   * @param testDirectory test directory to test
   * @param fileName expected file name
   */
  public static void assertContainsFile(final TestDirectory testDirectory, final String fileName) {
    DirectoryAssert.assertDirectoryContainsFile(testDirectory.toFile(), fileName);
  }

  /**
   * Asserts that a file does not exist in the {@link TestDirectory}.
   *
   * @param testDirectory test directory to test
   * @param fileName expected file name
   */
  public static void assertNotContainsFile(final TestDirectory testDirectory, final String fileName) {
    DirectoryAssert.assertDirectoryNotContainsFile(testDirectory.toFile(), fileName);
  }

  /**
   * Asserts that a directory exists in the {@link TestDirectory}.
   *
   * @param testDirectory test directory to test
   * @param directoryName expected directory name
   */
  public static void assertContainsDirectory(final TestDirectory testDirectory, final String directoryName) {
    DirectoryAssert.assertDirectoryContainsDirectory(testDirectory.toFile(), directoryName);
  }

  /**
   * Asserts that a directory does not exist in the {@link TestDirectory}.
   *
   * @param testDirectory test directory to test
   * @param directoryName expected directory name
   */
  public static void assertNotContainsDirectory(final TestDirectory testDirectory, final String directoryName) {
    DirectoryAssert.assertDirectoryNotContainsDirectory(testDirectory.toFile(), directoryName);
  }

}
