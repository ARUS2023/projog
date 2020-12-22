/*
 * Copyright 2020 S. Webber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projog.core.udp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.projog.TestUtils.array;
import static org.projog.TestUtils.atom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.projog.SimpleProjogListener;
import org.projog.TestUtils;
import org.projog.core.CutException;
import org.projog.core.Predicate;
import org.projog.core.PredicateKey;
import org.projog.core.ProjogException;
import org.projog.core.SpyPoints;
import org.projog.core.SpyPoints.SpyPoint;
import org.projog.core.event.ProjogListeners;
import org.projog.core.term.Term;
import org.projog.core.udp.SingleRetryableRulePredicateFactory.RetryableRulePredicate;
import org.projog.core.udp.interpreter.ClauseAction;

public class SingleRetryableRulePredicateFactoryTest {
   private SpyPoints spyPoints;
   private ClauseAction mockAction;
   private SingleRetryableRulePredicateFactory testObject;
   private Predicate mockPredicate;
   private Term[] queryArgs = array(atom("a"), atom("b"), atom("c"));
   private SimpleProjogListener listener;

   @Before
   public void before() {
      this.mockPredicate = mock(Predicate.class);
      this.mockAction = mock(ClauseAction.class);
      when(mockAction.getPredicate(queryArgs)).thenReturn(mockPredicate);

      this.listener = new SimpleProjogListener();
      ProjogListeners observable = new ProjogListeners();
      observable.addListener(listener);
      this.spyPoints = new SpyPoints(observable, TestUtils.createTermFormatter());
      SpyPoint spyPoint = spyPoints.getSpyPoint(new PredicateKey("test", 3));

      this.testObject = new SingleRetryableRulePredicateFactory(mockAction, spyPoint);
      assertTrue(testObject.isRetryable());
   }

   @After
   public void after() {
      verify(mockAction).getPredicate(queryArgs);
      verifyNoMoreInteractions(mockAction, mockPredicate);
   }

   @Test
   public void testSuccess_spy_point_disabled() {
      spyPoints.setTraceEnabled(false);
      when(mockPredicate.evaluate()).thenReturn(true, true, true, false);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);

      assertTrue(result.evaluate());
      assertTrue(result.evaluate());
      assertTrue(result.evaluate());
      assertFalse(result.evaluate());
      assertEquals("", listener.result());

      verify(mockPredicate, times(4)).evaluate();
   }

   @Test
   public void testFailure_spy_point_disabled() {
      spyPoints.setTraceEnabled(false);
      when(mockPredicate.evaluate()).thenReturn(false);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);

      assertFalse(result.evaluate());
      assertEquals("", listener.result());
      verify(mockPredicate).evaluate();
   }

   @Test
   public void testCutException_spy_point_disabled() {
      spyPoints.setTraceEnabled(false);
      when(mockPredicate.evaluate()).thenThrow(CutException.CUT_EXCEPTION);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);

      assertFalse(result.evaluate());
      assertEquals("", listener.result());
      verify(mockPredicate).evaluate();
   }

   @Test
   public void testRuntimeException_spy_point_disabled() {
      spyPoints.setTraceEnabled(false);
      RuntimeException exception = new RuntimeException();
      when(mockPredicate.evaluate()).thenThrow(exception);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);
      try {
         result.evaluate();
         fail();
      } catch (ProjogException e) {
         assertEquals("Exception processing: test/3", e.getMessage());
         assertSame(exception, e.getCause());
      }

      assertEquals("", listener.result());
      verify(mockPredicate).evaluate();
      verify(mockAction).getModel();
   }

   @Test
   public void testSuccess_spy_point_enabled() {
      spyPoints.setTraceEnabled(true);
      when(mockPredicate.evaluate()).thenReturn(true, true, true, false);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);

      assertTrue(result.evaluate());
      assertTrue(result.evaluate());
      assertTrue(result.evaluate());
      assertFalse(result.evaluate());
      assertEquals("CALLtest(a, b, c)EXITtest(a, b, c)REDOtest(a, b, c)EXITtest(a, b, c)REDOtest(a, b, c)EXITtest(a, b, c)REDOtest(a, b, c)FAILtest(a, b, c)", listener.result());
      verify(mockAction, times(3)).getModel();
      verify(mockPredicate, times(4)).evaluate();
   }

   @Test
   public void testFailure_spy_point_enabled() {
      spyPoints.setTraceEnabled(true);
      when(mockPredicate.evaluate()).thenReturn(false);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);

      assertFalse(result.evaluate());
      assertEquals("CALLtest(a, b, c)FAILtest(a, b, c)", listener.result());
      verify(mockPredicate).evaluate();
   }

   @Test
   public void testCutException_spy_point_enabled() {
      spyPoints.setTraceEnabled(true);
      when(mockPredicate.evaluate()).thenThrow(CutException.CUT_EXCEPTION);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);

      assertFalse(result.evaluate());
      assertEquals("CALLtest(a, b, c)FAILtest(a, b, c)", listener.result());
      verify(mockPredicate).evaluate();
   }

   @Test
   public void testRuntimeException_spy_point_enabled() {
      spyPoints.setTraceEnabled(true);
      RuntimeException exception = new RuntimeException();
      when(mockPredicate.evaluate()).thenThrow(exception);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);
      try {
         result.evaluate();
         fail();
      } catch (ProjogException e) {
         assertEquals("Exception processing: test/3", e.getMessage());
         assertSame(exception, e.getCause());
      }

      assertEquals("CALLtest(a, b, c)", listener.result());
      verify(mockPredicate).evaluate();
      verify(mockAction).getModel();
   }

   @Test
   public void testCouldReevaluationSucceed() {
      when(mockPredicate.evaluate()).thenReturn(true);
      when(mockPredicate.couldReevaluationSucceed()).thenReturn(true, false);

      RetryableRulePredicate result = testObject.getPredicate(queryArgs);

      // couldReevaluationSucceed will always return true until evaluate is called
      assertTrue(result.couldReevaluationSucceed());
      assertTrue(result.couldReevaluationSucceed());
      assertTrue(result.couldReevaluationSucceed());
      assertTrue(result.evaluate());
      // after evaluate is called then couldReevaluationSucceed is delegated to wrapped predicate
      assertTrue(result.couldReevaluationSucceed());
      assertFalse(result.couldReevaluationSucceed());

      verify(mockPredicate).evaluate();
      verify(mockPredicate, times(2)).couldReevaluationSucceed();
   }
}
