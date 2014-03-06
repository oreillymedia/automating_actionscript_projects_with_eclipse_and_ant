/*
 * $Id: jdfix.java,v 1.8 2003/11/07 20:16:23 dfs Exp $
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

import java.io.*;

import org.apache.oro.text.perl.*;


/**
 * This is an example program demonstrating how to use the PerlTools
 * match and substitute methods.
 *
 * @version @version@
 */
public final class jdfix {

  /**
   * This program performs the exact same function as this Perl script.
   * Notice that the Java program is only so much longer because of all
   * of the I/O exception handling and InputStream creation.  The core
   * while loop is EXACTLY the same length as the while loop in the Perl
   * script.  The number of substitutions performed is printed to standard
   * output as additional information.  Note, this is not an efficient way
   * to do this job; it is better to first read the entire file into a
   * character array.
   * <p>
   * This is a simple program that takes a javadoc generated HTML file as
   * input and produces as output the same HTML file, except with a white
   * background color for the body.
   * <p>
   * <pre>
   * #!/usr/bin/perl
   *
   * $#ARGV >= 1 || die "Usage: jdfix input output\n";
   *
   * open(INPUT, $ARGV[0]) || warn "Couldn't open $ARGV[0]\n";
   * open(OUTPUT, ">$ARGV[1]") || warn "Couldn't open $ARGV[1]\n";
   *
   * while(<INPUT>){
   *     s/<body>/<body bgcolor="#ffffff">/;
   *     print OUTPUT;
   * }
   * 
   * close(INPUT);
   * close(OUTPUT);
   * </pre>
   */
  public static final void main(String args[]) {
    String line;
    BufferedReader input = null;
    PrintWriter output    = null;
    Perl5Util perl;
    StringBuffer result = new StringBuffer();
    int numSubs = 0;

    if(args.length < 2) {
      System.err.println("Usage: jdfix input output");
      return;
    }

    try {
      input = 
	new BufferedReader(new FileReader(args[0]));
    } catch(IOException e) {
      System.err.println("Error opening input file: " + args[0]);
      e.printStackTrace();
      return;
    }

    try {
      output =
	new PrintWriter(new FileWriter(args[1]));
    } catch(IOException e) {
      System.err.println("Error opening output file: " + args[1]);
      e.printStackTrace();
      return;
    } 

    perl = new Perl5Util();

    try {
      while((line = input.readLine()) != null) {
	numSubs+=perl.substitute(result,
				 "s/<body>/<body bgcolor=\"#ffffff\">/", line);
	result.append('\n');
      }
      output.print(result.toString());
      System.out.println("Substitutions made: " + numSubs);
    } catch(IOException e) {
      System.err.println("Error reading from input: " + args[1]);
      e.printStackTrace();
      return;
    } finally {
      try {
	input.close();
	output.close();
      } catch(IOException e) {
	System.err.println("Error closing files.");
	e.printStackTrace();
	return;
      }
    }
  }

}
