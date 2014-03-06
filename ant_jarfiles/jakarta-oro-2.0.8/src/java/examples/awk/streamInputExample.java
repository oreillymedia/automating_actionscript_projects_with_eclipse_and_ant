/*
 * $Id: streamInputExample.java,v 1.7 2003/11/07 20:16:23 dfs Exp $
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


package examples.awk;

import java.io.*;
import org.apache.oro.text.regex.*;
import org.apache.oro.text.awk.*;

/**
 * This is a test program demonstrating how to search an input stream
 * with the AwkTools regular expression classes.
 *
 * @version @version@
 */
public final class streamInputExample {

  /**
   * This program extracts sentences containing the word C++ from 
   * the sample file streamInputExample.txt  The regular expression
   * used is not perfect, so focus on AwkStreamInput and not the
   * ability of the regular expression to handle all normal sentences.
   * For those not familiar with the OROMatcher Util class, a use of
   * the Util.substitute method is included.
   */
  public static final void main(String args[]) {

    // A regular expression to extract sentences containing the word C++.
    // We assume sentences can only end in . ! ? and start with a word
    // character \w
    String regex = "(\\w[^\\.?!]*C\\+\\+|C\\+\\+)[^\\.?!]*[\\.?!]";
    String sentence;
    AwkMatcher matcher;
    AwkCompiler compiler;
    Pattern pattern = null, newline = null;
    AwkStreamInput input;
    MatchResult result;
    Reader file = null;

    // Create AwkCompiler and AwkMatcher instances.
    compiler = new AwkCompiler();
    matcher  = new AwkMatcher();

    // Attempt to compile the pattern.  If the pattern is not valid,
    // report the error and exit.
    try {
      pattern
	= compiler.compile(regex, AwkCompiler.CASE_INSENSITIVE_MASK);
      // Compile a pattern representing a string of newlines with other
      // whitespace stuck around the newlines
      newline = compiler.compile("(\\s*[\n\r]\\s*)+");
    } catch(MalformedPatternException e) {
      System.err.println("Bad pattern.");
      System.err.println(e.getMessage());
      System.exit(1);
    }


    // Open input file.
    try {
      file = new FileReader("streamInputExample.txt");
    } catch(IOException e) {
      System.err.println("Error opening streamInputExample.txt.");
      System.err.println(e.getMessage());
      System.exit(1);
    }

    // Create an AwkStreamInput instance to search the input stream.
    input   = new AwkStreamInput(file);

    // We need to put the search loop in a try block because when searching
    // an AwkStreamInput instance, an IOException may occur, and it must be
    // caught.
    try {
      // Loop until there are no more matches left.
      while(matcher.contains(input, pattern)) {
	// Since we're still in the loop, fetch match that was found.
	result = matcher.getMatch();  
	
	// Substitute all newlines in the match with spaces.
	sentence = Util.substitute(matcher, newline,
				   new StringSubstitution(" "),
				   result.toString(), Util.SUBSTITUTE_ALL);
	System.out.println("\nMatch:\n" + sentence);
      }
    } catch(IOException e) {
      System.err.println("Error occurred while reading file.");
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
