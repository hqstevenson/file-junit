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

package com.pronoia.junit.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for the  class.
 */
public class TestDirectoryAssertsTest {
  static final String TEST_CHILD_DIRECTORY_NAME = "child-directory";
  static final String TEST_FILE_NAME = "test.txt";

  @Rule
  public TestDirectory instance = new TestDirectory();

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertIsEmpty() throws Exception {
    instance.assertIsEmpty();

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.newFile(TEST_FILE_NAME);
    try {
      instance.assertIsEmpty();
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is not empty - contains [%s, %s]", instance.directory, TEST_FILE_NAME, TEST_CHILD_DIRECTORY_NAME);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertNotEmpty() throws Exception {
    try {
      instance.assertNotEmpty();
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is empty", instance.directory);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newFile(TEST_FILE_NAME);
    instance.assertNotEmpty();
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildCount() throws Exception {
    instance.assertChildCountEquals(0);

    instance.newFile(TEST_FILE_NAME);
    instance.assertChildCountEquals(1);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.assertChildCountEquals(2);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildFileCount() throws Exception {
    instance.assertFileCountEquals(0);

    instance.newFile(TEST_FILE_NAME);
    instance.assertFileCountEquals(1);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.assertFileCountEquals(1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildDirectoryCount() throws Exception {
    instance.assertChildDirectoryCountEquals(0);

    instance.newFile(TEST_FILE_NAME);
    instance.assertChildDirectoryCountEquals(1);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.assertChildDirectoryCountEquals(1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertContainsFile() throws Exception {
    try {
      instance.assertContainsFile(TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s does not exist in directory %s", TEST_FILE_NAME, instance.directory);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    try {
      instance.assertContainsFile(TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a file", TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newFile(TEST_FILE_NAME);
    instance.assertContainsFile(TEST_FILE_NAME);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertNotContainsFile() throws Exception {
    instance.assertNotContainsFile(TEST_FILE_NAME);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    // This test will pass because the entry is not a file (it is a directory), but a warning will be logged
    instance.assertNotContainsFile(TEST_CHILD_DIRECTORY_NAME);

    instance.newFile(TEST_FILE_NAME);
    instance.assertNotContainsFile("non-existent.txt");
    try {
      instance.assertNotContainsFile(TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s exists in directory %s", TEST_FILE_NAME, instance.directory);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertContainsDirectory() throws Exception {
    try {
      instance.assertContainsDirectory(TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist in directory %s", TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.newFile(TEST_FILE_NAME);
    instance.assertContainsDirectory(TEST_CHILD_DIRECTORY_NAME);

    try {
      instance.assertContainsDirectory(TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a directory", TEST_FILE_NAME, instance.directory);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertNotContainsDirectory() throws Exception {
    instance.assertNotContainsDirectory(TEST_CHILD_DIRECTORY_NAME);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.newFile(TEST_FILE_NAME);

    // This test will pass because the entry is not a directory (it is a file), but a warning will be logged
    instance.assertNotContainsDirectory(TEST_FILE_NAME);

    try {
      instance.assertNotContainsDirectory(TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s exists in directory %s", TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }
}