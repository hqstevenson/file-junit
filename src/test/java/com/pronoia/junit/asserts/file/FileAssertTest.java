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
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the FileAssert assertions.
 */
public class FileAssertTest {
  static final File TEST_DIRECTORY = new File("target/file-test");
  static final File TEST_FILE = new File(TEST_DIRECTORY, "test.txt");

  @Before
  public void setUp() throws Exception {
    if (TEST_DIRECTORY.exists()) {
      FileUtils.deleteQuietly(TEST_DIRECTORY);
    }

    TEST_DIRECTORY.mkdirs();
  }

  @After
  public void tearDown() throws Exception {
    FileUtils.deleteQuietly(TEST_DIRECTORY);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertFileExists() throws Exception {
    try {
      FileAssert.assertFileExists(TEST_FILE);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s does not exist", TEST_FILE);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    try {
      FileAssert.assertFileExists(TEST_DIRECTORY);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s does not refer to a file", TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_FILE.createNewFile();

    FileAssert.assertFileExists(TEST_FILE);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertFileNotExists() throws Exception {
    FileAssert.assertFileNotExists(TEST_FILE);

    TEST_FILE.createNewFile();

    for (File fileOrDirectory : Arrays.asList(TEST_FILE, TEST_DIRECTORY)) {
      try {
        FileAssert.assertFileNotExists(fileOrDirectory);
        fail("Assertion should have failed");
      } catch (AssertionError expectedFailure) {
        final String expectedAssertionMessage = String.format("%s exists", fileOrDirectory);
        assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
      }
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertFileExistsWithString() throws Exception {
    for (String fileName : Arrays.asList("", null)) {
      try {
        FileAssert.assertFileExists(fileName);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "File name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    TEST_FILE.createNewFile();

    FileAssert.assertFileExists(TEST_FILE.toString());
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertFileNotExistsWithString() throws Exception {
    for (String fileName : Arrays.asList("", null)) {
      try {
        FileAssert.assertFileNotExists(fileName);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "File name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    FileAssert.assertFileNotExists(TEST_FILE.toString());

    try {
      FileAssert.assertFileNotExists(TEST_DIRECTORY.toString());
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s exists", TEST_DIRECTORY.toString());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }
}