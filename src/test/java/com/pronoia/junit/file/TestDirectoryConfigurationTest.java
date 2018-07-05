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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;

/**
 * Configuration tests for the TestDirectory class.
 */
public class TestDirectoryConfigurationTest {

  @Rule
  public TestDirectory instance = new TestDirectory();

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testConstructorWithString() throws Exception {
    for (String string : Arrays.asList("", null)) {
      try {
        new TestDirectory(string);
        fail("Constructor should have failed");
      } catch (IllegalArgumentException expectedEx) {
        assertEquals("The directory name argument for the test directory cannot be null or empty", expectedEx.getMessage());
      }
    }

    String string = "src/test/data/test.txt";
    try {
      new TestDirectory(string);
      fail("Constructor should have failed");
    } catch (IllegalArgumentException expectedEx) {
      final String expectedMessage = String.format("The specified directory name '%s' does not refer to a directory", string);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    new TestDirectory(TestDirectory.DEFAULT_DIRECTORY);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testConstructorWithFile() throws Exception {
    File file = null;
    try {
      new TestDirectory(file);
      fail("Constructor should have failed");
    } catch (IllegalArgumentException expectedEx) {
      assertEquals("The directory argument for the test directory cannot be null", expectedEx.getMessage());
    }

    file = new File("src/test/data/test.txt");
    try {
      new TestDirectory(file);
      fail("Constructor should have failed");
    } catch (IllegalArgumentException expectedEx) {
      final String expectedMessage = String.format("The test directory File object argument '%s' does not refer to a directory", file);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    new TestDirectory(new File(TestDirectory.DEFAULT_DIRECTORY));
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testToFile() throws Exception {
    assertSame(instance.directory, instance.toFile());
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testIsDeleteAfterTest() throws Exception {
    assertEquals("Unexpected default value", false, instance.isDeleteAfterTest());

    instance.deleteAfterTest = true;
    assertTrue(instance.isDeleteAfterTest());
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSetDeleteAfterTest() throws Exception {
    instance.setDeleteAfterTest(true);
    assertTrue(instance.deleteAfterTest);

    instance.setDeleteAfterTest(false);
    assertFalse(instance.deleteAfterTest);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testDeleteAfterTest() throws Exception {
    assertFalse("Unexpected default value", instance.deleteAfterTest);

    instance.deleteAfterTest();
    assertTrue(instance.deleteAfterTest);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testDeleteAfterTestWithArgument() throws Exception {
    assertFalse("Unexpected default value", instance.deleteAfterTest);

    instance.deleteAfterTest(true);
    assertTrue(instance.deleteAfterTest);

    instance.deleteAfterTest(false);
    assertFalse(instance.deleteAfterTest);
  }
}