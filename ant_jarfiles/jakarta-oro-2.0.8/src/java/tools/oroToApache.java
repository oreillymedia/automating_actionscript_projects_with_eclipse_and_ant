/*
 * $Id: oroToApache.java,v 1.7 2003/11/07 20:16:26 dfs Exp $
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


package tools;

import java.io.*;
import org.apache.oro.text.regex.*;

/**
 * This is a program you can use to convert older source code that uses
 * the com.oroinc prefixes for the ORO text processing Java classes 
 * to org.apache.  It assumes source files are small enough to store in
 * memory and perform the substitutions.  A small effort is made to not
 * blindly substitute com.oroinc so that code using NetComponents or other
 * ORO software will not have packages like com.oroinc.net become
 * org.apache.net.  However, you will still have to manually fix some
 * code if you use the com.oroinc.io classes from NetComponents.
 *
 * @version @version@
 * @since 2.0
 */
public final class oroToApache {
  public static final String PACKAGE_PATTERN = "com\\.oroinc\\.(io|text|util)";
  public static final String PACKAGE_SUBSTITUTION = "org.apache.oro.$1";
  public static final String OLD_FILE_EXTENSION   = "_old";

  public static final class RenameException extends IOException {
    public RenameException() { }

    public RenameException(String message) {
      super(message);
    }
  }

  public static final class Converter {
    Pattern _sourcePattern;
    Perl5Matcher _matcher;
    Perl5Substitution _substitution;

    public static final int readFully(Reader reader, char[] buffer)
      throws IOException
    {
      int offset, length, charsRead;

      offset = 0;
      length = buffer.length;

      while(offset < buffer.length) {
	charsRead = reader.read(buffer, offset, length);
	if(charsRead == -1)
	  break;
	offset+=charsRead;
	length-=charsRead;
      }

      return offset;
    }

    public Converter(String patternString) throws MalformedPatternException {
      Perl5Compiler compiler;

      _matcher = new Perl5Matcher();
      compiler = new Perl5Compiler();
      _sourcePattern = compiler.compile(patternString);
      _substitution = new Perl5Substitution(PACKAGE_SUBSTITUTION);
    }

    public void convertFile(String filename, String oldExtension)
      throws FileNotFoundException, RenameException, SecurityException, 
	     IOException
    {
      char[] inputBuffer;
      int inputLength;
      File srcFile, outputFile;
      FileReader input;
      FileWriter output;
      String outputData;

      srcFile    = new File(filename);
      input      = new FileReader(srcFile);
      outputFile = 
	File.createTempFile(srcFile.getName(), null,
			    srcFile.getAbsoluteFile().getParentFile());
      output = new FileWriter(outputFile);

      inputBuffer = new char[(int)srcFile.length()];

      inputLength = readFully(input, inputBuffer);
      input.close();

      // new String(inputBuffer) is terribly inefficient because the
      // string ultimately gets converted back to a char[], but if we've
      // got the memory it's expedient.
      outputData = 
	Util.substitute(_matcher, _sourcePattern, _substitution,
			new String(inputBuffer), Util.SUBSTITUTE_ALL);
      output.write(outputData);
      output.close();

      if(!srcFile.renameTo(new File(srcFile.getAbsolutePath() +
				    OLD_FILE_EXTENSION)))
	throw new RenameException("Could not rename " + srcFile.getPath() +
				  ".");

      if(!outputFile.renameTo(srcFile))
	throw new RenameException("Could not rename temporary output file.  " +
				  "Original file is in " +
				  srcFile.getAbsolutePath() +
				  OLD_FILE_EXTENSION);
    }
  }

  public static final void main(String[] args) {
    int file;
    Converter converter;

    if(args.length < 1) {
      System.err.println("usage: oroToApache [file ...]");
      return;
    }

    try {
      converter = new Converter(PACKAGE_PATTERN);
    } catch(MalformedPatternException mpe) {
      // Shouldn''t happen
      mpe.printStackTrace();
      return;
    }

    for(file = 0; file < args.length; file++) {
      try {
	System.out.println("Converting " + args[file]);
	converter.convertFile(args[file], OLD_FILE_EXTENSION);
      } catch(FileNotFoundException fnfe) {
	System.err.println("Error: Could not open file.  Skipping " +
			   args[file]);
      } catch(RenameException re) {
	System.err.println("Error: " + re.getMessage());
      } catch(SecurityException se) {
	System.err.println("Error: Could not rename a file while processing" +
			   args[file] + ".  Insufficient permission.  " +
			   "File may not have been converted.");
      } catch(IOException ioe) {
	ioe.printStackTrace();
	System.err.println("Error: I/O exception while converting " +
			   args[file] + ".  File not converted.");
      }
    }
  }
}
