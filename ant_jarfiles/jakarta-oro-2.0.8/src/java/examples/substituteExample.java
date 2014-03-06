/*
 * $Id: substituteExample.java,v 1.9 2003/11/07 20:16:23 dfs Exp $
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
 * This is a test program demonstrating the use of the Util.substitute()
 * method.
 *
 * @version @version@
 */
public final class substituteExample {

  /**
   * A good way for you to understand the substitute() method is to play around
   * with it by using this test program.  The program takes 3 to 5 arguments
   * as follows:
   *   java substituteExample
   *     regex substitution input [sub limit] [interpolation limit]
   * regex - A regular expression used to find parts of the input to be
   *         substituted.
   * sub limit - An optional argument limiting the number of substitutions.
   *             If no limit is given, the limit used is Util.SUBSTITUTE_ALL.
   * input - A string to be used as input for substitute().
   * interpolation limit - An optional argument limiting the number of
   *             interpolations performed.
   *
   * Try the following command line for a simple example of subsitute().
   * It changes (2,3) to (3,2) in the input.
   *          java substituteExample '\(2,3\)' '(3, 2)' '(1,2) (2,3) (4,5)'
   *
   * The following command line shows the substitute limit at work.  It
   * changed the first four 1's in the input to 4's.
   *          java substituteExample "1" "4" "381298175 1111" "4"
   *
   * The next command line shows how to use interpolations.  Suppose we
   * want to reverse the coordinates of the first 3 entries in the input
   * and then have all the rest of the coordinates be equal to the new 3rd
   * entry:
java substituteExample '\((\d+),(\d+)\)' '($2,$1)' '(1,2) (2,3) (4,5) (8,8) (9,2)' 5 3
   *
   */
  public static final void main(String args[]) {
    int limit, interps;
    PatternMatcher matcher = new Perl5Matcher();
    Pattern pattern = null;
    PatternCompiler compiler = new Perl5Compiler();
    String regularExpression, input, sub, result;

    // Make sure there are sufficient arguments
    if(args.length < 3) {
      System.err.println("Usage: substituteExample regex substitution " +
			 "input [sub limit] [interpolation limit]");
      System.exit(1);
    }

    limit = Util.SUBSTITUTE_ALL;
    interps = Perl5Substitution.INTERPOLATE_ALL;

    regularExpression = args[0];
    sub               = args[1];
    input             = args[2];

    if(args.length > 3)
      limit = Integer.parseInt(args[3]);

    if(args.length > 4)
      interps = Integer.parseInt(args[4]);

    try {
      pattern = compiler.compile(regularExpression);
      System.out.println("substitute regex: " + regularExpression);
    } catch(MalformedPatternException e){
      System.err.println("Bad pattern.");
      System.err.println(e.getMessage());
      System.exit(1);
    }

    // Perform substitution and print result.
    result = Util.substitute(matcher, pattern,
			     new Perl5Substitution(sub, interps),
			     input, limit);
    System.out.println("result: " + result);
  }
}
