/*
 * $Id: strings.java,v 1.3 2003/08/12 18:11:30 dfs Exp $
 *
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
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
 * with the jakarta-oro awk package regular expression classes.  It
 * performs a function similar to the Unix <code>strings</code> command,
 * but is intended to show how matching on a stream is affected by its
 * character encoding.  The most important thing to remember is that
 * AwkMatcher only matches on 8-bit values.  If your input contains
 * Java characters containing values greater than 255, the pattern
 * matching process will result in an ArrayIndexOutOfBoundsException.
 * Therefore, if you want to search a binary file containing arbitrary
 * bytes, you have to make sure you use an 8-bit character encoding
 * like ISO-8859-1, so that the mapping between byte-values and character
 * values will be one to one.  Otherwise, the file will be interpreted
 * as UTF-8 by default, and you will probably wind up with character 
 * values outside of the 8-bit range.
 *
 * @version @version@
 */
public final class strings {

  public static final class StringFinder {
    /**
     * Default string expression.  Looks for at least 4 contiguous
     * printable characters.  Differs slightly from GNU strings command
     * in that any printable character may start a string.
     */
    public static final String DEFAULT_PATTERN =
      "[\\x20-\\x7E]{3}[\\x20-\\x7E]+";

    Pattern pattern;
    AwkMatcher matcher;

    public StringFinder(String regex) throws MalformedPatternException {
      AwkCompiler compiler = new AwkCompiler();
      pattern = compiler.compile(regex, AwkCompiler.CASE_INSENSITIVE_MASK);
      matcher = new AwkMatcher();
    }

    public StringFinder() throws MalformedPatternException {
      this(DEFAULT_PATTERN);
    }

    public void search(Reader input, PrintWriter output) throws IOException {
      MatchResult result;
      AwkStreamInput in = new AwkStreamInput(input);

      while(matcher.contains(in, pattern)) {
        result = matcher.getMatch();  
        output.println(result);
      }
      output.flush();
    }
  }


  public static final String DEFAULT_ENCODING = "ISO-8859-1";

  public static final void main(String args[]) {
    String regex = StringFinder.DEFAULT_PATTERN;
    String filename, encoding = DEFAULT_ENCODING;
    StringFinder finder;
    Reader file = null;

    // Some users thought it would be useful to use the default pattern
    // and just pass the encoding as the second parameter.  Therefore,
    // when two arguments are given and the second argument is not a valid
    // encoding, it is interpreted as a pattern.  This means you can't
    // use a valid encoding name as a pattern without also specifying
    // an encoding as a third argument.
    if(args.length < 1) {
      System.err.println("usage: strings file [pattern|encoding] [encoding]");
      return;
    } else if(args.length > 2) {
      regex = args[1];
      encoding = args[2];
    } else if(args.length > 1)
      encoding = args[1];

    filename = args[0];

    try {
      InputStream fin = new FileInputStream(filename);

      try {
        file = new InputStreamReader(fin, encoding);
      } catch(UnsupportedEncodingException uee) {
        if(args.length == 2) {
          regex    = encoding;
	  encoding = DEFAULT_ENCODING;
	  file     = new InputStreamReader(fin, encoding);
	} else
	  throw uee;
      }

      finder = new StringFinder(regex);
      finder.search(file, new PrintWriter(new OutputStreamWriter(System.out)));
      file.close();
    } catch(Exception e) {
      e.printStackTrace();
      return;
    }
  }
}
