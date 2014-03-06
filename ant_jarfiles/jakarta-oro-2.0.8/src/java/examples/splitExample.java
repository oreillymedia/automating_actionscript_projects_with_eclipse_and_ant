/*
 * $Id: splitExample.java,v 1.8 2003/11/07 20:16:23 dfs Exp $
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

import java.util.*;
import org.apache.oro.text.regex.*;

/**
 * This is a test program demonstrating the use of the Util.split() method.
 *
 * @version @version@
 */
public final class splitExample {

  /**
   * A good way for you to understand the split() method is to play around
   * with it by using this test program.  The program takes 2 or 3 arguments
   * as follows:
   *      java splitExample regex input [split limit]
   * regex - A regular expression used to split the input.
   * input - A string to be used as input for split().
   * split limit - An optional argument limiting the size of the list returned
   *               by split().  If no limit is given, the limit used is
   *               Util.SPLIT_ALL.  Setting the limit to 1 generally doesn't
   *               make any sense.
   *
   * Try the following two command lines to see how split limit works:
   *          java splitExample '[:|]' '1:2|3:4'
   *          java splitExample '[:|]' '1:2|3:4' 3
   *
   */
  public static final void main(String args[]) {
    int limit, i;
    String regularExpression, input;
    List results = new ArrayList();
    Pattern pattern = null;
    PatternMatcher matcher;
    PatternCompiler compiler;
    Iterator elements;

    // Make sure there are sufficient arguments
    if(args.length < 2) {
      System.err.println("Usage: splitExample regex input [split limit]");
      System.exit(1);
    }

    regularExpression = args[0];
    input = args[1];

    if(args.length > 2)
      limit = Integer.parseInt(args[2]);
    else
      limit = Util.SPLIT_ALL;

    // Create Perl5Compiler and Perl5Matcher instances.
    compiler = new Perl5Compiler();
    matcher  = new Perl5Matcher();

    // Attempt to compile the pattern.  If the pattern is not valid,
    // report the error and exit.
    try {
      pattern = compiler.compile(regularExpression);
      System.out.println("split regex: " + regularExpression);
    } catch(MalformedPatternException e){
      System.err.println("Bad pattern.");
      System.err.println(e.getMessage());
      System.exit(1);
    }

    // Split the input and print the resulting list.
    System.out.println("split results: ");
    Util.split(results, matcher, pattern, input, limit);
    elements = results.iterator();

    i = 0;
    while(elements.hasNext())
      System.out.println("item " + i++ + ": " + (String)elements.next());

  }
}
