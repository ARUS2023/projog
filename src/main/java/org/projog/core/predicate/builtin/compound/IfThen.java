/*
 * Copyright 2018 S. Webber
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
package org.projog.core.predicate.builtin.compound;

import org.projog.core.kb.KnowledgeBaseUtils;
import org.projog.core.predicate.AbstractPredicateFactory;
import org.projog.core.predicate.Predicate;
import org.projog.core.predicate.PredicateFactory;
import org.projog.core.predicate.Predicates;
import org.projog.core.predicate.PreprocessablePredicateFactory;
import org.projog.core.predicate.builtin.list.PartialApplicationUtils;
import org.projog.core.predicate.udp.PredicateUtils;
import org.projog.core.term.Term;

/* TEST
 if_then_else_test(1).
 if_then_else_test(2).
 if_then_else_test(3).

 %TRUE 2>1 -> true
 %FALSE 2<1 -> true
 %FALSE 2>1 -> fail

 %QUERY if_then_else_test(X) -> if_then_else_test(X)
 %ANSWER X=1

 %QUERY if_then_else_test(X) -> if_then_else_test(Y)
 %ANSWER
 % X=1
 % Y=1
 %ANSWER
 %ANSWER
 % X=1
 % Y=2
 %ANSWER
 %ANSWER
 % X=1
 % Y=3
 %ANSWER

 %QUERY true -> X=a ; X=b
 %ANSWER X=a

 %QUERY fail -> X=a ; X=b
 %ANSWER X=b

 %QUERY (X=a, 1<2) -> Y=b; Y=c
 %ANSWER
 % X=a
 % Y=b
 %ANSWER

 %QUERY (X=a, 1>2) -> Y=b; Y=c
 %ANSWER
 % X=UNINSTANTIATED VARIABLE
 % Y=c
 %ANSWER

 %QUERY if_then_else_test(X) -> if_then_else_test(X) ; if_then_else_test(X)
 %ANSWER X=1

 %QUERY (if_then_else_test(X), fail) -> if_then_else_test(X) ; if_then_else_test(X)
 %ANSWER X=1
 %ANSWER X=2
 %ANSWER X=3

 %QUERY if_then_else_test(X) -> if_then_else_test(Y) ; Y=b
 %ANSWER
 % X=1
 % Y=1
 %ANSWER
 %ANSWER
 % X=1
 % Y=2
 %ANSWER
 %ANSWER
 % X=1
 % Y=3
 %ANSWER
 */
/**
 * <code>X-&gt;Y</code> - if <code>X</code> succeeds then <code>Y</code> is evaluated.
 * <p>
 * <b>Note:</b> The behaviour of this predicate changes when it is specified as the first argument of a structure of the
 * form <code>;/2</code>, i.e. the <i>"disjunction"</i> predicate. When a <code>-&gt;/2</code> predicate is the first
 * argument of a <code>;/2</code> predicate then the resulting behaviour is a <i>"if/then/else"</i> statement of the
 * form <code>((if-&gt;then);else)</code>.
 * </p>
 *
 * @see Disjunction
 */
public final class IfThen extends AbstractPredicateFactory implements PreprocessablePredicateFactory {
   @Override
   protected Predicate getPredicate(Term conditionTerm, Term thenTerm) {
      Predicate conditionPredicate = KnowledgeBaseUtils.getPredicate(getKnowledgeBase(), conditionTerm);
      if (conditionPredicate.evaluate()) {
         // TODO should we need to call getTerm before calling getPredicate, or should getPredicate contain that logic?
         return KnowledgeBaseUtils.getPredicate(getKnowledgeBase(), thenTerm.getTerm());
      } else {
         return PredicateUtils.FALSE;
      }
   }

   @Override
   public PredicateFactory preprocess(Term term) {
      Term condition = term.getArgument(0);
      Term action = term.getArgument(1);
      if (PartialApplicationUtils.isAtomOrStructure(condition) || PartialApplicationUtils.isAtomOrStructure(action)) {
         Predicates p = getPredicates();
         return new OptimisedIfThen(p.getPreprocessedPredicateFactory(condition), p.getPreprocessedPredicateFactory(action));
      } else {
         return this;
      }
   }

   private static final class OptimisedIfThen implements PredicateFactory {
      private final PredicateFactory condition;
      private final PredicateFactory action;

      OptimisedIfThen(PredicateFactory condition, PredicateFactory action) {
         this.condition = condition;
         this.action = action;
      }

      @Override
      public Predicate getPredicate(Term[] args) {
         Predicate conditionPredicate = condition.getPredicate(args[0].getArgs());
         if (conditionPredicate.evaluate()) {
            // TODO should we need to call getTerm before calling getPredicate, or should getPredicate contain that logic?
            return action.getPredicate(args[1].getTerm().getArgs());
         } else {
            return PredicateUtils.FALSE;
         }
      }

      @Override
      public boolean isRetryable() {
         return action.isRetryable();
      }
   }
}
