/*
 * $Id: matchResultExample.java,v 1.7 2003/11/07 20:16:23 dfs Exp $
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
 * This is a test program demonstrating the methods of the OROMatcher
 * MatchResult class.
 *
 * @version @version@
 */
public final class matchResultExample {

  /**
   * Takes a regular expression and string as input and reports all the
   * pattern matches in the string.
   * <p>
   * @param args[]  The array of arguments to the program.  The first
   *    argument should be a Perl5 regular expression, and the second
   *    should be an input string.
   */
  public static final void main(String args[]) {
    int groups;
    PatternMatcher matcher;
    PatternCompiler compiler;
    Pattern pattern = null;
    PatternMatcherInput input;
    MatchResult result;

    // Must have at least two arguments, else exit.
    if(args.length < 2) {
      System.err.println("Usage: matchResult pattern input");
      return;
    }

    // Create Perl5Compiler and Perl5Matcher instances.
    compiler = new Perl5Compiler();
    matcher  = new Perl5Matcher();

    // Attempt to compile the pattern.  If the pattern is not valid,
    // report the error and exit.
    try {
      pattern = compiler.compile(args[0]);
    } catch(MalformedPatternException e) {
      System.err.println("Bad pattern.");
      System.err.println(e.getMessage());
      return;
    }

    // Create a PatternMatcherInput instance to keep track of the position
    // where the last match finished, so that the next match search will
    // start from there.  You always create a PatternMatcherInput instance
    // when you want to search a string for all of the matches it contains,
    // and not just the first one.
    input   = new PatternMatcherInput(args[1]);


    // Loop until there are no more matches left.
    while(matcher.contains(input, pattern)) {
      // Since we're still in the loop, fetch match that was found.
      result = matcher.getMatch();  

      // Perform whatever processing on the result you want.
      // Here we just print out all its elements to show how the
      // MatchResult methods are used.
  
      // The toString() method is provided as a convenience method.
      // It returns the entire match.  The following are all equivalent:
      //     System.out.println("Match: " + result);
      //     System.out.println("Match: " + result.toString());
      //     System.out.println("Match: " + result.group(0));
      System.out.println("Match: " + result.toString());

      // Print the length of the match.  The length() method is another
      // convenience method.  The lengths of subgroups can be obtained
      // by first retrieving the subgroup and then calling the string's
      // length() method.
      System.out.println("Length: " + result.length());

      // Retrieve the number of matched groups.  A group corresponds to
      // a parenthesized set in a pattern.
      groups = result.groups();
      System.out.println("Groups: " + groups);

      // Print the offset into the input of the beginning and end of the
      // match.  The beinOffset() and endOffset() methods return the
      // offsets of a group relative to the beginning of the input.  The
      // begin() and end() methods return the offsets of a group relative
      // the to the beginning of a match.
      System.out.println("Begin offset: " + result.beginOffset(0));
      System.out.println("End offset: " + result.endOffset(0));
      System.out.println("Groups: ");

      // Print the contents of each matched subgroup along with their
      // offsets relative to the beginning of the entire match.

      // Start at 1 because we just printed out group 0
      for(int group = 1; group < groups; group++) {
	System.out.println(group + ": " + result.group(group));
	System.out.println("Begin: " + result.begin(group));
	System.out.println("End: " + result.end(group));
      }
    }
  }
}
