/*
 * $Id: groups.java,v 1.8 2003/11/07 20:16:23 dfs Exp $
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

import org.apache.oro.text.*;
import org.apache.oro.text.regex.*;

/**
 * This is a sample program mimicking the Unix groups command.  It assumes
 * the /etc/group file exists.
 *
 * @version @version@
 */
public final class groups {

  public static final void main(String[] args) {
    int user;
    MatchActionProcessor processor = new MatchActionProcessor();
    final Hashtable groups = new Hashtable();
    Vector users = new Vector();
    Enumeration usersElements;
    MatchAction action = new MatchAction() {
      public void processMatch(MatchActionInfo info) {
	// Add group name to hashtable entry
	((Vector)groups.get(info.match.toString())).addElement(
				       info.fields.get(0));
      }
    };

    if(args.length == 0) {
      // No arguments assumes calling user
      args = new String[1];
      args[0] = System.getProperty("user.name");
    }

    try {
      processor.setFieldSeparator(":");
      for(user = 0; user < args.length; user++) {
	// Screen out duplicates
	if(!groups.containsKey(args[user])) {
	  groups.put(args[user], new Vector());
	  // We assume usernames contain no special characters
	  processor.addAction(args[user], action);
	  // Add username to Vector to preserve argument order when printing
	  users.addElement(args[user]);
	}
      }
    } catch(MalformedPatternException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      processor.processMatches(new FileInputStream("/etc/group"),
			       System.out);
    } catch(IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    usersElements = users.elements();

    while(usersElements.hasMoreElements()) {
      String username;
      Enumeration values;

      username = (String)usersElements.nextElement();
      values = ((Vector)groups.get(username)).elements();

      System.out.print(username + " :");
      while(values.hasMoreElements()) {
	System.out.print(" " + values.nextElement());
      }
      System.out.println();
    }

    System.out.flush();
  }

}
