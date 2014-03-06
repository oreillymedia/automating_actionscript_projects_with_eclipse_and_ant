/*
 * $Id: matchesContainsExample.java,v 1.7 2003/11/07 20:16:23 dfs Exp $
 *
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Jakarta-Oro" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Jakarta-Oro", nor may "Apache" or "Jakarta-Oro" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */


package examples;

import org.apache.oro.text.regex.*;

/**
 * This is a test program demonstrating the difference between the
 * matches() and contains() methods.
 *
 * @version @version@
 */
public final class matchesContainsExample {

  /**
   * A common mistake is to confuse the behavior of the matches() and
   * contains() methods.  matches() tests to see if a string exactly
   * matches a pattern whereas contains() searches for the first pattern
   * match contained somewhere within the string.  When used with a
   * PatternMatcherInput instance, the contains() method allows you to
   * search for every pattern match within a string by using a while loop.
   */
  public static final void main(String args[]) {
    int matches = 0;
    String numberExpression = "\\d+";
    String exactMatch = "2010";
    String containsMatches =
   "  2001 was the movie before 2010, which takes place before 2069 the book ";
    Pattern pattern   = null;
    PatternMatcherInput input;
    PatternCompiler compiler;
    PatternMatcher matcher;
    MatchResult result;

    // Create Perl5Compiler and Perl5Matcher instances.
    compiler = new Perl5Compiler();
    matcher  = new Perl5Matcher();

    // Attempt to compile the pattern.  If the pattern is not valid,
    // report the error and exit.
    try {
      pattern = compiler.compile(numberExpression);
    } catch(MalformedPatternException e) {
      System.err.println("Bad pattern.");
      System.err.println(e.getMessage());
      System.exit(1);
    }

    // Here we show the difference between the matches() and contains()
    // methods().  Compile the program and study the output to reinforce
    // in your mind what the methods do.

    System.out.println("Input: " + exactMatch);

    // The following should return true because exactMatch exactly matches
    // numberExprssion.

    if(matcher.matches(exactMatch, pattern))
      System.out.println("matches() Result: TRUE, EXACT MATCH");
    else
      System.out.println("matches() Result: FALSE, NOT EXACT MATCH");

    System.out.println("\nInput: " + containsMatches);

    // The following should return false because containsMatches does not
    // exactly match numberExpression even though its subparts do.

    if(matcher.matches(containsMatches, pattern))
      System.out.println("matches() Result: TRUE, EXACT MATCH");
    else
      System.out.println("matches() Result: FALSE, NOT EXACT MATCH");


    // Now we call the contains() method.  contains() should return true
    // for both strings.

    System.out.println("\nInput: " + exactMatch);

    if(matcher.contains(exactMatch, pattern)) {
      System.out.println("contains() Result: TRUE");

      // Fetch match and print.
      result = matcher.getMatch();
      System.out.println("Match: " + result);
    } else
      System.out.println("contains() Result: FALSE");

    System.out.println("\nInput: " + containsMatches);

    if(matcher.contains(containsMatches, pattern)) {
      System.out.println("contains() Result: TRUE");
      // Fetch match and print.
      result = matcher.getMatch();
      System.out.println("Match: " + result);
    } else
      System.out.println("contains() Result: FALSE");


    // In the previous example, notice how contains() will fetch only first 
    // match in a string.  If you want to search a string for all of the
    // matches it contains, you must create a PatternMatcherInput object
    // to keep track of the position of the last match, so you can pick
    // up a search where the last one left off.

    input   = new PatternMatcherInput(containsMatches);

    System.out.println("\nPatternMatcherInput: " + input);
    // Loop until there are no more matches left.
    while(matcher.contains(input, pattern)) {
      // Since we're still in the loop, fetch match that was found.
      result = matcher.getMatch();  

      ++matches;

      System.out.println("Match " + matches + ": " + result);
    }
  }
}
