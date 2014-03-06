This is the first very beta release of JGrowl. A (most) pure Java
implementation of Growl (see http://growl.info). This package
provides a number of things.

1) A Growl Java object that can be used in a program to send and
   receive growl notifications.

2) A program called JGrowl that can be run on your desktop that
   will behave like Growl on the Mac. If the proper library is
   available, a tray icon will be in the installed in the system
   tray. (see https://jdic.dev.java.net/documentation/incubator/tray/index.html)
   Otherwise JGrowl will only become visible when display notifications.

3) A program called NcidToGrowl that can be used to connect to an ncid server
   (see ncid.sourceforge.net). This program will receive ncid caller ID
   messages and then broadcast them to a growl host or to a subnet.

Release Notes.

Initial beta - October 26, 2006.
- I have only tested this program on Linux and it seems to work ok for me.
  If you want to run it on other platforms, you will probably want to get
  a libtray.so or hand drawn facimile for your platform. If don't have one
  JGrowl should work ok other than whining that it can't find the library
  at start time. The only down side is that you will no UI to terminate it.

- The bin directory contains shell scripts for running JGrowl and NcidToGrowl,
  you should be able to use these to figure out how to do it on other platforms.

- JGrowl is run as: ./JGrowl -p <password>
     where <password> is your Growl network password if you use one

- NcidToGrowl is run as: ./NcidToGrowl [-p <password>] <ncid server> <growl host>
    where <password> is as above.
          <ncid server> it the hostname or IP address of your ncid server
          <growl host> is the host name or IP address of the host that you want to
                       forward the growl messages to. This can be a netword address
                       such as 192.168.1.255 to broadcast the messages to your subnet.

Acknowledgments, etc.

- I have included an icon lifted from the Growl project, which i believe is ok according the
  terms of their license which is also included.

- This code has no relationship to the Growl project other than for interoperability purposes
  and is not endorsed by them.

- The code was written with the help of http://the.taoofmac.com/space/Projects/netgrowl.py
  and protocol documentation found at http://growl.info/documentation/developer/protocol

If you have questions comments or improvement you can contact me at jgrowl@binaryblizzard.com.

Copyright 2006 Stephen Martin, Binary Blizzard Software. All rights reserved.
