/*
 * $Id: printPasswd.java,v 1.8 2003/11/07 20:16:23 dfs Exp $
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
import java.util.*;

import org.apache.oro.text.perl.*;

/**
 * This is an example program based on a short example from the Camel book.
 * It demonstrates splits by reading the /etc/passwd file (assuming you're
 * on a Unix system) and printing out the formatted entries.
 *
 * @version @version@
 */
public final class printPasswd {
  public static final String[] fieldNames = {
    "Login: ", "Encrypted password: ", "UID: ", "GID: ", "Name: ",
    "Home: ", "Shell: "
  };

  public static final void main(String args[]) {
    BufferedReader input = null;
    int field, record;
    String line;
    Perl5Util perl;
    ArrayList fields;
    Iterator it;

    try {
      input = new BufferedReader(new FileReader("/etc/passwd"));
    } catch(IOException e) {
      System.err.println("Could not open /etc/passwd.");
      e.printStackTrace();
      System.exit(1);
    }

    perl = new Perl5Util();
    record = 0;

    try {
      fields = new ArrayList();

      while((line = input.readLine()) != null) {
	fields.clear();
	perl.split(fields, "/:/", line);

	it = fields.iterator();
	field = 0;

	System.out.println("Record " + record++); 

	while(it.hasNext() && field < fieldNames.length)
	  System.out.println(fieldNames[field++] + 
			     (String)it.next());

	System.out.print("\n\n");
      }
    } catch(IOException e) {
      System.err.println("Error reading /etc/passwd.");
      e.printStackTrace();
      System.exit(1);
    } finally {
      try {
	input.close();
      } catch(IOException e) {
	System.err.println("Could not close /etc/passwd.");
	e.printStackTrace();
	System.exit(1);
      }
    }

  }

}
