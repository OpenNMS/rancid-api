#!/usr/local/bin/tclsh

#============================================================================
# RWS * RESTful Web Service ServerSide Application Engine (servlet)
#
# Copyright (c) 2009+ Rocco Rionero
# Copyright (c) 2009+ The OpenNMS Group, Inc.
# All rights reserved everywhere.
#
# This program was developed and is maintained by Rocco RIONERO
# ("the author") and is subject to dual-copyright according to
# the terms set in "The OpenNMS Project Contributor Agreement".
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
# USA.
#
# The author can be contacted at the following email address:
#
#	Rocco RIONERO
#	rock(at)rionero.com
#
# (please, specify "rws-application-software" in the subject of your message)
#
#-----------------------------------------------------------------------------
# OpenNMS Network Management System is Copyright by The OpenNMS Group, Inc.
#============================================================================


#-----------------------------------------------------------------------------
#                 UNSUPPORTED BETA-RELEASE SOFTWARE VERSION
#-----------------------------------------------------------------------------



### INCLUDE-BEGIN ("logger.inc.tcl") ########################################

namespace eval ::LOG {
  set LIB_NAME		"LOGGER"
  set LIB_VER		"0.5.2009032201"
  set LIB_INFO		"Message logging functions (subset)"

  array set Levels {
    debug,tag    "DBG"
    debug,lev    0
    info,tag     "INF"
    info,lev     1
    notice,tag   "NOT"
    notice,lev   2
    warning,tag  "WRN"
    warning,lev  3
    error,tag    "ERR"
    error,lev    4
    logging,tag  "LOG"
    logging,lev  5
    disabled,tag "DIS"
    disabled,lev 100
  }

  variable Level_logfile info			# default logfile logging level
  variable Level_stderr  disabled		# default stderr logging level

  variable Channel       ""			# logfile i/o channel


  #--------------------------------------------------------------------------
  # Close { }
  #
  # Arguments:
  #	(none)
  #
  # Description:
  #	shut-down the logging system and close the log file
  #	
  #
  # Returned Value:
  #	0	logging system was not active
  #	1	logging system was correctly shut-down

  proc Close {} {
    variable Channel

    Msg logging "stopped (last message for this run)"

    if { "$Channel" != "" } {
      close $Channel
      set Channel ""
    }

    return 1
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # Open { filename }
  #
  # Arguments:
  #	filename	filename for the logfile
  #
  # Description:
  #	start-up the logging system and open the log file (will create it
  #	if not found).
  #	
  #
  # Returned Value:
  #	1	always

  proc Open { filename } {
    variable Channel
    variable Levels
    variable Level_logfile
    variable Level_stderr

    Close
    if { "$filename" == "" } { return 0 }

    if {[catch {open "$filename" "a" 0640} Channel]} {return 0}

    Msg logging "started on [clock format [clock seconds] -format {%Y-%m-%d %T %Z (UTC%z)}];\
                 logging levels: file=\"$Levels($Level_logfile,tag)\", stderr=\"$Levels($Level_stderr,tag)\""
    return 1
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # Msg { lid message }
  #
  # Arguments:
  #	lid	the level (priority) ID of the logged message
  #	message	the message to be logged
  #
  # Description:
  #	send a message, with a given priority, to the internal logging syste
  #	
  #
  # Returned Value:
  #	0	empty message or invalid level
  #	1	message was logged successfully
  #
  # Line formatting:
  #
  #	- logfile:	Mmm DD HH:MM:SS LEV [-pid-]: message...
  #			-------- 27  chars --------
  #
  #	- stderr:	LEV [-pid-]: message...
  #			-- 11 ch --

  proc Msg { lid message } {
    variable Channel
    variable Levels
    variable Level_logfile
    variable Level_stderr

    if { "$message" == "" } { return 0 }
    if { [info exists Levels($lid,tag)] } {
      if { $Levels($lid,lev) < $Levels(logging,lev) && $Levels($lid,lev) >= $Levels($Level_stderr,lev) } {
        puts stderr "[format "%s \[%05d\]:" $Levels($lid,tag) [pid]] $message"
      }
      if { $Levels($lid,lev) < $Levels(disabled,lev) && $Levels($lid,lev) >= $Levels($Level_logfile,lev) && "$Channel" != "" } {
        puts $Channel "[clock format [clock seconds] -format {%b %e %T}] [format "%s \[%05d\]:" $Levels($lid,tag) [pid]] $message"
      }
      return 1
    }
    Msg logging "bad loglevel-id \"$lid\" (msg: \"$message\")"
    return 0
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # SetLevel_stderr { lid }
  #
  # Arguments:
  #	lid	required logging level
  #
  # Description:
  #	assign the logging level for messages sent to the stderr output
  #	stream (and normally displayed on operator's terminal)
  #	
  #
  # Returned Value:
  #	0	invalid level, logging level unchanged
  #	1	logging level changed successfully

  proc SetLevel_stderr { lid } {
    variable Levels
    variable Level_stderr

    if { [info exists Levels($lid,tag)] } {
      set Level_stderr $lid
      Msg logging "stderr level set to \"$Levels($lid,tag)\""
      return 1
    }

    Msg logging "bad loglevel-id: \"$lid\", stderr level unchanged"
    return 0
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # SetLevel_logfile { lid }
  #
  # Arguments:
  #	lid	required logging level
  #
  # Description:
  #	assign the logging level for messages sent to the logfile
  #	
  #
  # Returned Value:
  #	0	invalid level, logging level unchanged
  #	1	logging level changed successfully

  proc SetLevel_logfile { lid } {
    variable Levels
    variable Level_logfile

    if { [info exists Levels($lid,tag)] } {
      if { "$lid" != "disabled" } {
        set Level_logfile $lid
        Msg logging "logfile level set to \"$Levels($lid,tag)\""
        return 1
      }
    }
    Msg logging "bad loglevel-id: \"$lid\", logfile level unchanged"
    return 0
  }
  #--------------------------------------------------------------------------


}

### INCLUDE-END   ("logger.inc.tcl") ########################################



### INCLUDE-BEGIN ("io.inc.tcl") ############################################

#============================================================================
# The "IO" namespace holds routine and variables used to manage TCL
# input/output channels in non-blocking mode
#

namespace eval ::IO {
  set LIB_NAME		"IO"
  set LIB_VER		"0.6.2009032201"
  set LIB_INFO		"Input/Output functions (subset)"

  array set Error {
    0,msg "success"
    0,pri "debug"

   -2,msg "i/o error"
   -2,pri "error"

   -3,msg "EOF (connection closed)"
   -3,pri "info"

   -4,msg "timeout"
   -4,pri "error"

   -5,msg "failed to get channel mode"
   -5,pri "error"

   -6,msg "failed to set channel mode"
   -6,pri "error"

   -7,msg "fcopy failed"
   -7,pri "error"
  }


  set ERR_BadCh	"invalid i/o channel"
  set ERR_Info	"$ERR_BadCh\n    while executing\n\"{io-channel-check}\""

  array set SavedConfig {}

  variable EventFlag 0


  #----------------------------------------------------------------------------
  proc LogStatus { code tag ch {xinfo ""} } {

    if {"$xinfo" == ""} {
      ::LOG::Msg $::IO::Error($code,pri) "IO::$tag\($ch\): $::IO::Error($code,msg)"
    } else {
      ::LOG::Msg $::IO::Error($code,pri) "IO::$tag\($ch\): $::IO::Error($code,msg): $xinfo"
    }
    return $code
  }
  #----------------------------------------------------------------------------


  #----------------------------------------------------------------------------
  # configure the channel as non-blocking with given EOL translation

  proc SetupChannel { ch mode } {
    set ltag "SetupChannel"

    # the first time channel is set up, save the current configuration

    if {[info exists ::IO::SavedConfig($ch)]} {
      set prev_conf ""
    } else {
      if {[catch {fconfigure $ch} prev_conf]} {
        return [::IO::LogStatus -5 $ltag $ch $prev_conf]
      }
    }

    # (re)configure the channel

    switch -exact -- $mode {
      -eol-auto   { set tra "auto" }
      -eol-crlf   { set tra "crlf" }
      -eol-cr     { set tra "cr"   }
      -eol-lf     -
      -eol-binary -
      default     { set tra "lf"   }
    }

    if {[catch {fconfigure $ch -blocking 0 -buffering full  \
                               -encoding binary -eofchar "" \
                               -translation $tra } result]} {
      return [::IO::LogStatus -6 $ltag $ch $result]
    }

    if {"$prev_conf" != ""} {set ::IO::SavedConfig($ch) $prev_conf}

    return [LogStatus 0 $ltag $ch]
  }
  #----------------------------------------------------------------------------


  #----------------------------------------------------------------------------
  # restore channel's original configuration

  proc RestoreChannel { ch } {
    set ltag "RestoreChannel"

    if {![info exists ::IO::SavedConfig($ch)]} {error $::IO::ERR_BadCh $::IO::ERR_Info}

    set orig_conf $::IO::SavedConfig($ch)

    unset ::IO::SavedConfig($ch)

    
    if {[catch {eval fconfigure $ch $orig_conf} result]} {
      return [LogStatus -6 $ltag $ch $result]
    }

    return [LogStatus 0 $ltag $ch]
  }
  #----------------------------------------------------------------------------



  #----------------------------------------------------------------------------
  # ReadLine { ch timeout __line __count }
  #
  # Arguments:
  #	ch		TCL input channel to read from
  #
  #	timeout		max time allowed for reception (msec)
  #
  #	__line		name of returned line buffer
  #
  #	__count		name of returned data counter (chars in __line)
  #
  #
  # Description:
  #	reads from "ch" an entire "line", that is a block of data terminated
  #	by the EOL sequence (as defined by current channel translation),
  #	within a time of <timeout> mS; the received data are stored into
  #	"__buffer" and the received count is returned in "__buffer_count";
  #	the EOL sequence is NOT stored into "__buffer" (and, as such, NOT
  #	counted in "__buffer_count")
  #
  #     NOTE: channel MUST have been setup in the appropriate line-mode
  #
  # Returned Value:
  #	 0	success (note: if __buffer_count==0 then an empty line
  #		was received, that is, just the EOL sequence)
  #	-2	i/o error
  #	-3	EOF/connection closed by remote
  #	-4	timeout


  proc ReadLine { ch timeout __line __line_len } {
    set ltag  "ReadLine"

    upvar $__line     line
    upvar $__line_len len

    set line ""
    set len  0

    if {![info exists ::IO::SavedConfig($ch)]} {error $::IO::ERR_BadCh $::IO::ERR_Info}

    set ::IO::EventFlag 0

    set timeout_id [after $timeout {set ::IO::EventFlag [expr $::IO::EventFlag | 2]}]
    fileevent $ch readable {set ::IO::EventFlag [expr $::IO::EventFlag | 1]}

    while {1} {
      vwait ::IO::EventFlag
      if {[catch {gets $ch line} ioresp]} {
        set result -2
        break
      }
      if {[set len $ioresp] >= 0} {
        set ioresp ""
        set result 0
        break
      }
      set len    0
      set ioresp ""
      if {[eof $ch]} {
        set result -3
        break
      }
      if {$::IO::EventFlag & 2} {
        set result -4
        break
      }
      set ::IO::EventFlag [expr $::IO::EventFlag & 2]
    }

    fileevent $ch readable {}
    after cancel $timeout_id

    return [LogStatus $result $ltag $ch $ioresp]
  }
  #----------------------------------------------------------------------------


  #----------------------------------------------------------------------------
  # ReadBlock { ch count timeout __buffer __buffer_count }
  #
  # Arguments:
  #	ch		TCL input channel to read from
  #
  #	count		number of chars to read
  #
  #	timeout		max time allowed for reception (msec)
  #
  #	__buffer	name of returned data buffer
  #
  #	__buffer_count	name of returned data counter (chars in __buffer)
  #
  #
  # Description:
  #	reads from "ch" a block of "count" chars, in max "timeout";
  #	the received chars are stored into the __buffer variable and the
  #	number of received chars is stored into the __buffer_count variable;
  #
  # Returned Value:
  #	 0	success, "count" chars received
  #	-2	i/o error
  #	-3	EOF/connection closed by remote
  #	-4	timeout

  proc ReadBlock { ch count timeout __buffer __buffer_count } {
    set ltag "ReadBlock"

    upvar $__buffer       buffer
    upvar $__buffer_count buffer_count

    set buffer ""
    set buffer_count 0

    if {![info exists ::IO::SavedConfig($ch)]} {error $::IO::ERR_BadCh $::IO::ERR_Info}

    set ::IO::EventFlag 0

    set timeout_id [after $timeout {set ::IO::EventFlag [expr $::IO::EventFlag | 2]}]
    fileevent $ch readable {set ::IO::EventFlag [expr $::IO::EventFlag | 1]}

    while {1} {
      vwait ::IO::EventFlag
      if {[catch {read $ch $count} ioresp]} {
        set result -2
        break
      }
      if {[set chunk_size [string length $ioresp]] > 0} {
        append buffer $ioresp
        incr buffer_count $chunk_size
        if {[incr count -$chunk_size] == 0} {
          set ioresp ""
          set result 0
          break
        }
      }
      set ioresp ""
      if {[eof $ch]} {
        set result -3
        break
      }
      if {$::IO::EventFlag & 2} {
        set result -4
        break
      }
      set ::IO::EventFlag [expr $::IO::EventFlag & 2]
    }

    fileevent $ch readable {}
    after cancel $timeout_id

    return [LogStatus $result $ltag $ch $ioresp]
  }
  #----------------------------------------------------------------------------


  #----------------------------------------------------------------------------
  # Write { ch buffer timeout }
  #
  # Arguments:
  #	ch		TCL output channel to write to
  #
  #	buffer		data to send
  #
  #	timeout		max time allowed for transmission (msec)
  #
  #
  # Description:
  #	send the block of data contained into "buffer" within "timeout" ms;
  #	if the recording function is enabled, all sent chars are also sent
  #	to the proper recorder's bucket
  #
  # Returned Value:
  #	 0	success, data sent
  #	-2	i/o error
  #	-3	EOF/connection closed by remote
  #	-4	timeout


  proc Write { ch buffer timeout } {
    set ltag "Write"

    if {![info exists ::IO::SavedConfig($ch)]} {error $::IO::ERR_BadCh $::IO::ERR_Info}

    set ::IO::EventFlag 0
    fileevent $ch writable {set ::IO::EventFlag [expr $::IO::EventFlag | 1]}
    set timeout_id [after $timeout {set ::IO::EventFlag [expr $::IO::EventFlag | 2]}]
    vwait ::IO::EventFlag
    after cancel $timeout_id
    fileevent $ch writable {}

    if {$::IO::EventFlag & 2} {return [LogStatus -4 $ltag $ch]}

    if {[eof $ch]} {return [LogStatus -3 $ltag $ch]}

    if {[catch {puts -nonewline $ch $buffer} ioresp]} {
      return [LogStatus -2 $ltag $ch $ioresp]
    }

    return [LogStatus 0 $ltag $ch]
  }
  #----------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # Flush { ch }
  #
  # Arguments:
  #	ch		TCL output channel to flush
  #
  # Description:
  #	flush the output buffer, ensuring that all sent data get transmitted
  #
  # Returned Value:
  #	 0	success
  #	-2	i/o error

  proc Flush { ch } {
    set ltag "Flush"

    if {![info exists ::IO::SavedConfig($ch)]} {error $::IO::ERR_BadCh $::IO::ERR_Info}

    if {[catch {flush $ch} errmsg]} {return [LogStatus -2 $ltag $ch $errmsg]}

    return [LogStatus 0 $ltag $ch]
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  proc BinaryChannelCopy { chIn chOut numBytes } {

    if {$numBytes == 0} {return 0}

    set ltag "BinaryChannelCopy"


    # set channel mode for direct binary copy

    if {[catch {fconfigure $chIn -blocking 1 -buffering full\
                                 -encoding binary -eofchar ""\
                                 -translation lf } result]} {
      return [LogStatus -6 $ltag $chIn $result]
    }

    if {[catch {fconfigure $chOut -blocking 1 -buffering full\
                                -encoding binary -eofchar ""\
                                -translation lf } result]} {
      return [LogStatus -6 $ltag $chOut $result]
    }


    # do the copy

    if {[catch {fcopy $chIn $chOut -size $numBytes} result]} {
      return [LogStatus -7 $ltag "$chIn -> $chOut" $result]
    }

    return [LogStatus 0 $ltag "$chIn -> $chOut"]
  }
  #--------------------------------------------------------------------------

}
#============================================================================

### INCLUDE-END   ("io.inc.tcl") ############################################




### INCLUDE-BEGIN ("http.inc.tcl") ##########################################

#============================================================================
namespace eval ::HTTP {
  set LIB_NAME		"R2HTTP"
  set LIB_VER		"0.4.2009032501"
  set LIB_INFO		"HTTP server-side functions (subset)"

  set ioTimeout		30000



  #--------------------------------------------------------------------------
  proc GetEnvVar { name } {
    if {[info exists ::env($name)]} {return $::env($name)} else {return ""}
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # UrlDecode
  #
  # Reverts the RFC-2396 encoding, thus returning the original un-encoded
  # string: all '+' are converted back to ' ' and all occurrences of the
  # "escape" sequence "%HH" are substituted with the actual character having
  # hexadecimal code "HH"

  proc UrlDecode { string } {
    regsub -all {%([[:xdigit:]]{2})} "[string map {"+" " "} $string]" \
                {[format %c 0x\1]} string
    return "[subst $string]"
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  proc SplitAssignments { __input __assigned __unassigned } {
    upvar $__input	input
    upvar $__assigned	assigned
    upvar $__unassigned	unassigned

    array unset assigned *
    set unassigned {}

    foreach assignment [split $input "&"] {
      set nvlist [split $assignment "="]
      set name   "[::HTTP::UrlDecode [lindex $nvlist 0]]"
      if {[llength $nvlist] > 1} {
        set assigned($name) "[::HTTP::UrlDecode [lindex $nvlist 1]]"
      } else {
        lappend unassigned  "$name"
      }
    }
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  proc BuildHttpDate { timeval } {
    return "[clock format $timeval -format {%a, %d %b %Y %T GMT} -gmt 1]"
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  proc SetServletName { handle buffer } {
    upvar ${handle}::ServletName  ServletName
    upvar ${handle}::ServletInfo  ServletInfo

    if {$buffer != ""} {
      set ServletInfo "$ServletName; $ServletInfo"
      set ServletName  $buffer
    } else {
      upvar ${handle}::HttpLib     HttpLib
      upvar ${handle}::HttpLibVer  HttpLibVer
      set ServletName	"$HttpLib/$HttpLibVer"
      set ServletInfo	"Tcl $::tcl_patchLevel;\
			 $::tcl_platform(os) $::tcl_platform(osVersion);\
			 $::tcl_platform(platform); $::tcl_platform(machine)"
    }
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # Questa funzione viene chiamata quando il modulo è eseguito come CGI
  # server-script: crea un namespace relativo alla request e ne restituisce
  # il nome. Dal momento che in modalità CGI ogni request comporta una
  # diversa istanza dello script, il namespace è creato con un nome fisso
  # pari a ::REQ_CGI

  proc CGI_SetupRequestStructure {} {

    # crea il namespace ed assegna i valori relativi alla richiesta ed
    # all'header

    namespace eval ::REQ_CGI {
      set TimeStamp	[clock seconds]
      set chIn		"stdin"
      set chOut		"stdout"
      set HttpLib	"${::HTTP::LIB_NAME}-CGI"
      set HttpLibVer	"$::HTTP::LIB_VER"


      # set Servet fields to default values

      ::HTTP::SetServletName "::REQ_CGI" ""


      # CGI/1.1 standard variables (RFC-3875)

      set ServerInterface	[::HTTP::GetEnvVar "GATEWAY_INTERFACE"]
      set ServerSoftware	[::HTTP::GetEnvVar "SERVER_SOFTWARE"]
      set ClientHostName	[::HTTP::GetEnvVar "REMOTE_HOST"]
      set ClientAddress		[::HTTP::GetEnvVar "REMOTE_ADDR"]
      set ClientIdent		[::HTTP::GetEnvVar "REMOTE_IDENT"]
      set ServerAuthType	[::HTTP::GetEnvVar "AUTH_TYPE"]
      set ServerAuthUsername	[::HTTP::GetEnvVar "REMOTE_USER"]
      set Protocol		[::HTTP::GetEnvVar "SERVER_PROTOCOL"]
      set Method		[::HTTP::GetEnvVar "REQUEST_METHOD"]
      set ServerName		[::HTTP::GetEnvVar "SERVER_NAME"]
      set ServerPort		[::HTTP::GetEnvVar "SERVER_PORT"]
      set BaseURI		[::HTTP::GetEnvVar "SCRIPT_NAME"]
      set SubURI		[::HTTP::GetEnvVar "PATH_INFO"]
      set TranslatedSubURI	[::HTTP::GetEnvVar "PATH_TRANSLATED"]
      set QueryString		[::HTTP::GetEnvVar "QUERY_STRING"]
      set RequestContentType	[::HTTP::GetEnvVar "CONTENT_TYPE"]
      set RequestContentLen	[::HTTP::GetEnvVar "CONTENT_LENGTH"]

      # Additional (but commonly-implemented) variables

      set DocumentRoot		[::HTTP::GetEnvVar "DOCUMENT_ROOT"]
      set ScriptFilename  	[::HTTP::GetEnvVar "SCRIPT_FILENAME"]
      set ServerAdmin		[::HTTP::GetEnvVar "SERVER_ADMIN"]
      set ServerAddress		[::HTTP::GetEnvVar "SERVER_ADDR"]
      set ClientPort		[::HTTP::GetEnvVar "REMOTE_PORT"]
      set UniqueID		[::HTTP::GetEnvVar "UNIQUE_ID"]
      set OriginalURI		[::HTTP::GetEnvVar "REQUEST_URI"]



      array set Header	{}

      array set QueryTokensAssigned	{}
      set QueryTokensUnassigned		{}

      # response data

      set ResponseStatusCode		""
      set ResponseStatusPhrase		""
      set ResponseRetryTime		5
      set ResponseHeader		""
      set ResponseContentType		""
      set ResponseContentLen		0
      set ResponseContent		""

      # list of end-of-response call-back handlers

      set EndOfResponseHandlers		{}
    }


    # scans the environment for variables corresponding to additional HTTP
    # request header fields and assigns the corresponding array

    foreach {name value} [array get ::env "HTTP_*"] {
      set name [string range $name 5 end]
      set ::REQ_CGI::Header($name) "$value"
    }


    # set the canonical scheme/host/port portion for the returned absolute URI

    # set default values

    set mhost $::REQ_CGI::ServerName
    set mport $::REQ_CGI::ServerPort

    # try to obtain more specific values from request's data

    if {![regexp -- {^[Hh][Tt][Tt][Pp]://([^:/?]+)(?::([[:digit:]]+))?}\
                    $::REQ_CGI::OriginalURI mall mhost mport]} {

      if {[info exists ::REQ_CGI::Header(HOST)]} {
        regexp -- {^([^:]+)(?::([[:digit:]]+))?$}\
                  $::REQ_CGI::Header(HOST) mall mhost mport
      }

    }

    set ::REQ_CGI::CanonicalSHP "http://$mhost"

    if {"$mport" != "" && "$mport" != "80"} {
      append ::REQ_CGI::CanonicalSHP ":$mport"
    }


    # set the canonical absolute URI

    set ::REQ_CGI::AbsoluteCanonicalURI	"${::REQ_CGI::CanonicalSHP}${::REQ_CGI::BaseURI}${::REQ_CGI::SubURI}"

    # splits the QueryString into the array of assigned tokens and the
    # list of unassigned tokens

    ::HTTP::SplitAssignments "::REQ_CGI::QueryString"\
                             "::REQ_CGI::QueryTokensAssigned"\
                             "::REQ_CGI::QueryTokensUnassigned"


    # log the start of request

    if {[set uinfo $::REQ_CGI::ServerAuthUsername] != ""} {set uinfo "; User: \"${uinfo}\""}

    if {[set qinfo $::REQ_CGI::QueryString] != ""} {set qinfo "?${qinfo}"}

    if {[set cinfo $::REQ_CGI::RequestContentLen] != ""} {set cinfo "; Content-Length: \"$::REQ_CGI::RequestContentLen\""}

    if {$::REQ_CGI::RequestContentType != ""} {append cinfo "; Content-Type: \"$::REQ_CGI::RequestContentType\""}

    ::LOG::Msg notice "HTTP-Request: From: \"${::REQ_CGI::ClientAddress}\"${uinfo};\
                                     Protocol: \"$::REQ_CGI::Protocol\"; Method: \"$::REQ_CGI::Method\";\
                                     URI: \"${::REQ_CGI::BaseURI}${::REQ_CGI::SubURI}${qinfo}\"${cinfo}"

    # returns the name of the created request's namespace

    return "::REQ_CGI"
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  proc SetResponseStatus { handle code phrase } {
    upvar ${handle}::ResponseStatusCode   ResponseStatusCode
    upvar ${handle}::ResponseStatusPhrase ResponseStatusPhrase

    set ResponseStatusCode   $code
    set ResponseStatusPhrase $phrase
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  proc SetResponseContentType { handle content_type } {
    upvar ${handle}::ResponseContentType   ResponseContentType

    set ResponseContentType $content_type
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # Add user-specified content-data to the ResponseContent buffer and updates
  # the ResponseContentLen counter. If the user-specified content-data is
  # empty, the entire ResponseContent buffer is cleared (and the corresponding
  # ResponseContentLen is set to 0)

  proc AddToResponseContent { handle content_data } {
    upvar ${handle}::ResponseContentLen    ResponseContentLen
    upvar ${handle}::ResponseContent       ResponseContent

    if {$content_data != ""} {
      append ResponseContent    $content_data
      incr   ResponseContentLen [string length $content_data]
    } else {
      set ResponseContent    ""
      set ResponseContentLen 0
    }
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # Add one header-line to the ResponseHeader buffer; a trailing <CR><LF>
  # sequence is automatically appended. If the user-specified line is empty,
  # the entire ResponseHeader buffer is cleared.

  proc AddToResponseHeader { handle header_line } {
    upvar ${handle}::ResponseHeader        ResponseHeader

    if {$header_line != ""} {
      append ResponseHeader "$header_line\r\n"
    } else {
      set ResponseHeader ""
    }
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # this is called if, for some reason, the current response content (if any)
  # must be cleared; it usually occurs when an unhandled exception occurs
  # and an error response content must be sent instead of the regular one

  proc ClearResponseFields { handle } {
    upvar ${handle}::ResponseStatusCode    ResponseStatusCode
    upvar ${handle}::ResponseStatusPhrase  ResponseStatusPhrase
    upvar ${handle}::ResponseHeader        ResponseHeader
    upvar ${handle}::ResponseContentLen    ResponseContentLen
    upvar ${handle}::ResponseContentType   ResponseContentType
    upvar ${handle}::ResponseContent       ResponseContent


    # if the application set-up an external response, close the file

    if {$ResponseContentLen == -1 && $ResponseContentType == "__EXTERNAL__"} {
      catch {close [lindex $ResponseContent 0]}
    }

    set ResponseStatusCode   ""
    set ResponseStatusPhrase ""
    set ResponseHeader       ""
    set ResponseContentLen   0
    set ResponseContentType  ""
    set ResponseContent      ""

  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  proc RegisterEndOfResponseHandler { handle args } {

    if {"$args" != ""} {
      upvar ${handle}::EndOfResponseHandlers EndOfResponseHandlers

      set EndOfResponseHandlers [linsert $EndOfResponseHandlers 0 $args]
      ::LOG::Msg debug "registered EOR-handler: $args"
    }
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  proc SendResponse { handle } {
    set ltag "HTTP::SendResponse"

    upvar ${handle}::chOut                 chOut
    upvar ${handle}::ServerInterface       ServerInterface
    upvar ${handle}::ServerSoftware        ServerSoftware
    upvar ${handle}::Protocol              Protocol
    upvar ${handle}::ServletName           ServletName
    upvar ${handle}::ServletInfo           ServletInfo
    upvar ${handle}::AbsoluteCanonicalURI  AbsoluteCanonicalURI
    upvar ${handle}::ResponseStatusCode    ResponseStatusCode
    upvar ${handle}::ResponseStatusPhrase  ResponseStatusPhrase
    upvar ${handle}::ResponseRetryTime     ResponseRetryTime
    upvar ${handle}::ResponseHeader        ResponseHeader
    upvar ${handle}::ResponseContentLen    ResponseContentLen
    upvar ${handle}::ResponseContentType   ResponseContentType
    upvar ${handle}::ResponseContent       ResponseContent
    upvar ${handle}::EndOfResponseHandlers EndOfResponseHandlers
  

    # set-up the actual response header returned

    if { "$ServerInterface" == "" } {
      # request served directly (stand-alone mode)
      set    respHeader "$Protocol $ResponseStatusCode $ResponseStatusPhrase\r\n"
      append respHeader "Date: [::HTTP::BuildHttpDate [clock seconds]]\r\n"
      append respHeader "Server: $ServerSoftware\r\n"
    } else {
      # request served by hosting web-server (CGI mode)
      if { "$ResponseStatusCode" == "" } {
        set respHeader ""
      } else {
        set respHeader "Status: $ResponseStatusCode"
        if {"$ResponseStatusPhrase" != ""} {
          append respHeader " $ResponseStatusPhrase"
        }
        append respHeader "\r\n"
      }
    }

    append respHeader "X-Servlet: $ServletName ($ServletInfo)\r\n"
    append respHeader "Cache-Control: no-transform, no-cache\r\n"
    append respHeader "Pragma: no-cache\r\n"
    append respHeader "Expires: Thu, 01 Jan 1970 00:00:00 GMT\r\n"

    if {"$ResponseStatusCode"=="201"} {
      append respHeader "Location: $AbsoluteCanonicalURI\r\n"
    } elseif {"$ResponseStatusCode"=="503" && $ResponseRetryTime != 0} {
      append respHeader "Retry-After: $ResponseRetryTime\r\n"
    }

    # append to the actual response header the user-specified fields

    append respHeader $ResponseHeader

    # with reference to the response content's source, currently we handle
    # two types of responses: "internal" and "external/file":
    #
    # case 1) INTERNAL
    #
    # The response content was generated/filled-in by the caller which
    # assigned the content-related variables as follows:
    #
    # ResponseContentType
    # value for the Content-Type response header's field. If blank, a
    # default value of "application/octet-stream" will be used
    #
    # ResponseContentLen
    # value for the Content-Lenth response header's field: MUST be the
    # exact number of bytes assigned to the ResponseContent variable
    #
    # ResponseContent
    # actual response's content
    #
    #
    # case 2) EXTERNAL FILE
    #
    # The response content must be sent from an external file; the
    # responder MUST have called the "SetupFileResponseContent"
    # function which checks that the file exists, is readable,
    # calculates its size, opens it and assigns the content-related
    # variables as follows:
    #
    # ResponseContentType
    # set to the "magic" token "__EXTERNAL__"
    #
    # ResponseContentLen
    # set to -1
    #
    # ResponseContent
    # set to a Tcl list of 3 values, in order:
    #
    #	- the Tcl input channel assigned to the external file, open for
    # 	  reading
    #
    #	- the size in bytes of the external file
    #
    #	- the value for the content-type header's field
    #
    # NOTE: this function WILL CLOSE the source file channel when the
    #       response is sent (or in case of errors)

    
    if {$ResponseContentLen == -1 && $ResponseContentType == "__EXTERNAL__"} {

      # "external" response content

      set chIn                [lindex $ResponseContent 0]
      set ResponseContentLen  [lindex $ResponseContent 1]
      set ResponseContentType [lindex $ResponseContent 2]

      ::LOG::Msg debug "will send external response from channel \"$chIn\",\
                        size=\"$ResponseContentLen\",\
                        content-type=\"$ResponseContentType\""

    } else {

      # "internal" response content

      set chIn ""

      if {"$ResponseContentType" == ""} {
        set ResponseContentType "application/octet-stream"
      }

    }

    # complete the header with content-related fields

    append respHeader "Content-Type: $ResponseContentType\r\n"
    append respHeader "Content-Length: $ResponseContentLen\r\n\r\n"


    # RESPONDING TO CLIENT

    # assume success
    
    set ret_code 0

    # set-up output stream

    if {[::IO::SetupChannel $chOut -eol-binary]} {

      ::LOG::Msg error "$ltag: I/O failure, no data will be sent"
      set ret_code -1

    } else {

      # send response header to client

      if {[::IO::Write $chOut $respHeader $::HTTP::ioTimeout]} {

        ::LOG::Msg error "$ltag: I/O failure trying to send response header"
        set ret_code -2

      } else {

        # send response content to client (if any)

        if {$ResponseContentLen == 0} {

          set csource "NONE"
          set cinfo   ""

        } else {

          if { $chIn == "" } {
            set csource "internal"
            if {[::IO::Write $chOut $ResponseContent $::HTTP::ioTimeout]} {
              ::LOG::Msg error "$ltag: I/O failure trying to send internal response content"
              set ret_code -3
            }

          } else {

            set csource "external"
            if {[::IO::BinaryChannelCopy $chIn $chOut $ResponseContentLen]} {
              ::LOG::Msg error "$ltag: I/O failure trying to send external response content"
              set ret_code -4
            }

          }
          set cinfo "; Content-Length: \"$ResponseContentLen\"; Content-Type: \"$ResponseContentType\""

        }

      }

    }

    if { $ret_code == 0 } {
      ::LOG::Msg notice "HTTP-Response: ContentSource: \"$csource\"; Status: \"$ResponseStatusCode $ResponseStatusPhrase\"${cinfo}"
    }

    if { $chIn != "" } {
      if {[catch {close $chIn} retval]} {
        ::LOG::Msg warning "$ltag: failed to close external content-source's handle ($retval)"
      }
    }

    # call end-of-response callback handlers (if any)

    foreach handler $EndOfResponseHandlers {
      ::LOG::Msg debug "$ltag: calling end-of-response handler: \"$handler\""
      if {[catch {eval $handler} retval]} {
        ::LOG::Msg warning "$ltag: failure executing end-of-response handler \"$handler\" ($retval)"
      }
    }

    return $ret_code
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  proc SetupFileResponseContent { handle file_name content_type } {
    upvar ${handle}::ResponseContentLen    ResponseContentLen
    upvar ${handle}::ResponseContentType   ResponseContentType
    upvar ${handle}::ResponseContent       ResponseContent


    # check that the file exists and is readable

    if {![file exists "$file_name"]} {
      return -1
    }


    if {![file readable "$file_name"]} {
      return -2
    }


    # gets size of file

    if {[catch {file size "$file_name"} fsize]} {
      return -3
    }


    # open file for reading

    if {[catch {open "$file_name" "r"} fch]} {
      return -4
    }


    # assigns fields for response


    #set ResponseContentType $content_type
    #set ResponseContentLen  $fsize
    #set ResponseContent     $fch


    set ResponseContent     [list $fch $fsize $content_type]
    set ResponseContentLen  -1
    set ResponseContentType "__EXTERNAL__"

    return 0

  }
  #--------------------------------------------------------------------------


}
#============================================================================


### INCLUDE-END   ("http.inc.tcl") ##########################################




### INCLUDE-BEGIN ("libxtree.inc.tcl") ######################################

#============================================================================
# Namespace ::XTREE
#
# The XTREE library is a collection of functions for the management of
# tree-like memory structures, aimed to generating XML representations.
# It is derived from my own general-purpose TREE library.
#
# The version included here is a XTREE subset with the only functions
# required by the RWS core and its services.
#

namespace eval ::XTREE {

  set	LIB_NAME	"XTREE"
  set	LIB_VER		"0.2.2009032301"
  set	LIB_INFO	"XML tree-management functions (subset)"

  set	INODES 0

  set ERR_BadHandle	"invalid node handle"
  set ERR_Info		"$ERR_BadHandle\n    while executing\n\"{libxtree-node-check}\""


  #--------------------------------------------------------------------------
  # mkroot - creates a new tree
  #
  # Synopsis:
  #	mkroot { root_label }
  #
  # Description:
  #	creates a new tree by allocating a new root-node into the XTREE
  #	namespace and configures the given label; as this library gives
  #	access to any node (including the root ones) only by handle,
  #	different trees may have the same root label without conflicts;
  #
  # Arguments:
  #	root_label	label to be assigned to the root node
  #
  #
  # Returned Value:
  #	<handle>	root's node handle

  proc mkroot { root_label } {

    set root_handle [format "::XTREE::ROOT_%08X" $::XTREE::INODES]
    incr ::XTREE::INODES

    upvar $root_handle root

    set root(PARENT)    $root_handle
    set root(LABEL)     $root_label
    set root(C_LABELS)  {}
    set root(C_HANDLES) {}
    set root(I_LABELS)  {}
    set root(I_VALUES)  {}

    return $root_handle
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # add_Child - creates a new child-node of a given parent-node
  #
  # Synopsis:
  #	add_Child { node_handle child_label }
  #
  # Description:
  #	creates a new child-node of the given parent-node; as this library
  #	gives access to any node only by handle, different childs of a same
  #	parent node may have the same label without conflicts;
  #
  # Arguments:
  #	node_handle	handle to the parent-node
  #
  #	child_label	label for the child-node being created
  #
  #
  # Returned Value:
  #	child_handle

  proc add_Child { node_handle child_label } {
    upvar $node_handle  node

    if {![info exists node(PARENT)]} {error $::XTREE::ERR_BadHandle $::XTREE::ERR_Info}

    # allocate a new node

    set child_handle [format "::XTREE::CHILD_%08X" $::XTREE::INODES]
    incr ::XTREE::INODES

    upvar $child_handle child

    set child(PARENT)    $node_handle
    set child(LABEL)     $child_label
    set child(C_LABELS)  {}
    set child(C_HANDLES) {}
    set child(I_LABELS)  {}
    set child(I_VALUES)	 {}

    # link it as child of given node

    if { [set idx [lsearch -exact $node(C_LABELS) $child_label]] >= 0 } {
      set h_list [lindex $node(C_HANDLES) $idx]
      lappend h_list $child_handle
      set node(C_HANDLES) [lreplace $node(C_HANDLES) $idx $idx $h_list]
    } else {
      lappend node(C_LABELS)  $child_label
      lappend node(C_HANDLES) [list $child_handle]
    }

    return $child_handle
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # set_Item - creates a new item into a given node
  #
  # Synopsis:
  #	set_Item { node_handle item_label item_value }
  #
  # Description:
  #	creates a new item (i.e. a "terminal" element having a value) into
  #	the given node; it is possible to create several items with the
  #	same label into the same node;
  #
  # Arguments:
  #	node_handle	handle to the node
  #
  #	item_label	label for the item being created
  #
  #	item_value	value of the item being created
  #
  #
  # Returned Value:
  #	(none)

  proc set_Item { node_handle item_label item_value } {
    upvar $node_handle node

    if {![info exists node(PARENT)]} {error $::XTREE::ERR_BadHandle $::XTREE::ERR_Info}

    if { [set idx [lsearch -exact $node(I_LABELS) $item_label]] >= 0 } {
      set v_list [lindex $node(I_VALUES) $idx]
      lappend v_list $item_value
      set node(I_VALUES) [lreplace $node(I_VALUES) $idx $idx $v_list]
    } else {
      lappend node(I_LABELS) $item_label
      lappend node(I_VALUES) [list $item_value]
    }

  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # delete_Branch - deletes an entire (sub) tree starting at the given node
  #
  # Synopsis:
  #	delete_Branch { node_handle }
  #
  # Description:
  #     deletes recursively the entire tree-branch starting at the given node
  #	(included); if the given node-handle is a root node, then the entire
  #	tree is deleted
  #
  # Arguments:
  #	node_handle	handle to the tree-branch starting node
  #
  # Returned Value:
  #	(none)
  #
  # Note:
  #	the function actually accepts and additional <at_top> argument which
  #	is used internally to optimize the deletion process: the argument is
  #	given a default value and MUST NOT be specified by the caller, as
  #	such it is not listed into the above synopsis; ignore it: you have
  #	been warned...

  proc delete_Branch { node_handle {at_top 1} } {
    upvar $node_handle node

    # deletion is optimized as follows:
    #
    # - node existence checked only for top one (i.e. caller-specified handle)
    # - parent unlink done only for top node (unless it is a root node)

    if { $at_top } {
      if {![info exists node(PARENT)]} {error $::XTREE::ERR_BadHandle $::XTREE::ERR_Info}
      if { $node_handle != $node(PARENT) } {
        upvar $node(PARENT) parent        
        set cidx 0
        foreach child_handles $parent(C_HANDLES) {
          if {[set hidx [lsearch -exact $child_handles $node_handle]] >= 0} {
            if {[set child_handles [lreplace $child_handles $hidx $hidx]] == {} } {
              set parent(C_HANDLES) [lreplace $parent(C_HANDLES) $cidx $cidx]
              set parent(C_LABELS)  [lreplace $parent(C_LABELS)  $cidx $cidx]
              break
            }
            set parent(C_HANDLES)  [lreplace $parent(C_HANDLES) $cidx $cidx $child_handles]
            break
          }
          incr cidx
        }
      }
    }
    
    # recursive removal of childs

    foreach child_handles $node(C_HANDLES) {
      foreach handle $child_handles {::XTREE::delete_Branch $handle 0}
    }
    
    unset node

  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # walk_XML - generates an XML representation of a (sub) tree
  #
  # Synopsis:
  #	walk_XML { start_node_handle I dumpscript }
  #
  # Description:
  #     walks recursively the entire tree-branch starting at the given node
  #	calling the user-specified <dumpscript> to generate each resulting
  #	XML line. Lines are automatically indented with spaces (2 spaces
  #	indentation for each level of depth)
  #
  #	NOTE: <dumpscript> is NOT called for the current node-label.
  #
  # Arguments:
  #	node_handle	handle to the node
  #
  #	I		initial indentation string
  #
  #	dumpscript	script to be evaluated for each XML line to emit
  #
  #
  # Returned Value:
  #	(none)
  #
  # About <dumpscript> argument
  #	for each XML line to generate, the <dumpscript> argument is evaluated;
  #	the script will access two string variables, "I" and "D", which are
  #	assigned, respectively, the indentation string and the actual XML line
  #	to emit. This gives the <dumpscript> some control over the formatting
  #	of the output. A typical <dumpscript> for displaying XML on stdout
  #	might be as follows: [concat puts {$I$D}]


  proc walk_XML {start_node_handle I dumpscript} {
    upvar $start_node_handle node

    foreach label $node(I_LABELS) sublist $node(I_VALUES) {
      foreach value $sublist {
        #set D "<$label><!\[CDATA\[$value\]\]></$label>"
        set D "<$label>$value</$label>"
        eval $dumpscript
      }
    }

    foreach label $node(C_LABELS) sublist $node(C_HANDLES) {
      foreach handle $sublist {
        set D "<$label>"
        eval $dumpscript
        ::XTREE::walk_XML $handle "  $I" "$dumpscript"
        set D "</$label>"
        eval $dumpscript
      }
    }

  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # dump_XML - generates an entire XML-content document
  #
  # Synopsis:
  #	dump_XML { root_handle dumpscript }
  #
  # Description:
  #	generates an entire XML-document starting at the given root-node;
  #	it generates the required document headers and the label of the root
  #	node, then calls the "walk_XML" function (see above).
  #
  # Arguments:
  #	root_handle	handle to the root node
  #
  #	dumpscript	script to be evaluated for each XML line to emit
  #
  #
  # Returned Value:
  #	(none)
  #
  # Note:
  #	see the "walk_XML" function above for a description of <dumpscript>

  proc dump_XML {root_handle dumpscript} {
    upvar $root_handle root

    if {![info exists root(PARENT)]} {error $::XTREE::ERR_BadHandle $::XTREE::ERR_Info}

    set I ""
    set D {<?xml version="1.0" encoding="ISO-8859-1" ?>}
    eval $dumpscript

    set D "<$root(LABEL)>"
    eval $dumpscript

    ::XTREE::walk_XML $root_handle "  " "$dumpscript"

    set D "</$root(LABEL)>"
    eval $dumpscript
  }
  #--------------------------------------------------------------------------


}
# end of ::XTREE namespace
#============================================================================

### INCLUDE-END   ("libxtree.inc.tcl") ######################################





#============================================================================
namespace eval ::RWS {
  set APP_NAME		"RWS"
  set APP_VER		"0.9.2009033101"
  set APP_INFO		"RESTful Web Service Server-Side Application Engine"


  # this is the directory this executable script is loaded from and
  # from which any dynamic service module will be loaded

  set RWS_MODULE_PATH		[file dirname $::argv0]


  # this list defines the HTTP request methods supported by the RWS

  set HTTP_SUPPORTED_METHODS	{ "GET" "POST" "PUT" "DELETE" }


  # this table defines the RWS response-status ID; each entry is a list
  # which contains:
  #
  #	- a status class (0=SUCCESS, 1=FAILURE)
  #	- the corresponding HTTP Status Code
  #	- the corresponding HTTP Status Reason Phrase
  #	- a status description in human-readable form

  array set STATUS_TABLE {
    Found		 { 0 "200" "OK"                        "Resource successfully accessed" }
    Created		 { 0 "201" "Resource Created"          "Resource successfully created" }
    Updated		 { 0 "200" "Resource Updated"          "Resource successfully updated" }
    NotChanged		 { 0 "200" "Resource Not Changed"      "Resource did not require any update" }
    Deleted		 { 0 "200" "Resource Deleted"          "Resource successfully deleted" }
    Reset		 { 0 "200" "Resource Reset"            "Resource successfully reset to default state" }

    ErrInvalidURI	 { 1 "400" "Bad Request URI"           "Request's URI is invalid or malformed" }
    ErrAuthentication    { 1 "401" "Authentication Required"   "Request requires proper authentication" }
    ErrAuthorization     { 1 "403" "Not Authorized"            "Request requires different authorization level" }
    ErrForbidden	 { 1 "403" "Access Forbidden"          "Access to addressed resource is forbidden" }
    ErrNotFound		 { 1 "404" "Resource Not Found"        "Resource does not exist" }
    ErrContainer	 { 1 "403" "Operation Not Allowed"     "The addressed resource is a container" }
    ErrInvalidMethod	 { 1 "501" "Method Not Implemented"    "Invalid HTTP request-method" }
    ErrBadMethod	 { 1 "405" "Method Not Allowed"        "HTTP request-method not allowed" }
    ErrBusy		 { 1 "503" "Resource Busy"             "Resource is temporarily unvailable" }
    ErrNoContentType	 { 1 "415" "Missing Content-Type"      "\"Content-Type\" field missing in HTTP request-header" }
    ErrBadContentType	 { 1 "415" "Unsupported Content-Type"  "Request has an unsupported Content-Type" }
    ErrMalformedContent	 { 1 "400" "Malformed Request Content" "Request's content is malformed" }
    ErrMissingData	 { 1 "403" "Insufficient Data"         "Missing mandatory content's information(s)" }
    ErrInvalidData	 { 1 "403" "Invalid Data"              "Invalid content's information(s)" }
    ErrExists		 { 1 "409" "Resource Conflict"         "Resource already exists" }

    ErrConfig		 { 1 "500" "Configuration Error"       "Configuration error" }
    ErrFileSystemIO	 { 1 "500" "FileSystem Error"          "FileSystem I/O error" }
    ErrCommIO		 { 1 "500" "Communication Error"       "Communication I/O error" }
    ErrCorrupted	 { 1 "500" "Source Data Error"         "Invalid data from an external source (e.g. corrupted file)" }
    ErrInternal		 { 1 "500" "Internal Error"            "System or application failure"  }
  }




  #--------------------------------------------------------------------------
  proc CheckMethod { list_allowed } {
    upvar RequestHandle			RequestHandle
    upvar ServiceStatus			ServiceStatus
    upvar ServiceStatusMsg		ServiceStatusMsg
    upvar ${RequestHandle}::Method	req_method

    if {[lsearch -exact $::RWS::HTTP_SUPPORTED_METHODS $req_method] < 0} {
      set ServiceStatus    "ErrInvalidMethod"
      set ServiceStatusMsg "$req_method request-method is not supported by this service"
      #::LOG::Msg error "Method not supported: $req_method."
      return -1
    }

    if {[set idx [lsearch -exact $list_allowed $req_method]] < 0} {
      set allowed ""
      foreach met $list_allowed {append allowed " $met,"}
      set allowed "[string trimright $allowed ","]"
      ::HTTP::AddToResponseHeader $RequestHandle "Allow:$allowed"
      set ServiceStatus    "ErrBadMethod"
      set ServiceStatusMsg "$req_method request-method is not allowed by the\
			   addressed resource; allowed method(s):$allowed"
      #::LOG::Msg error "Method not allowed: $req_method; allowed method(s):$allowed"
      return -1
    }

    return $idx
  }
  #--------------------------------------------------------------------------




  #--------------------------------------------------------------------------
  proc ReadRequestContent { __assigned __unassigned } {
    upvar RequestHandle				RequestHandle
    upvar ServiceStatus				ServiceStatus
    upvar ServiceStatusMsg			ServiceStatusMsg

    upvar ${RequestHandle}::chIn		chIn
    upvar ${RequestHandle}::RequestContentType	RequestContentType
    upvar ${RequestHandle}::RequestContentLen	RequestContentLen


    set ltag "RWS::ReadRequestContent"

    set allowed_ctype "application/x-www-form-urlencoded"


    # check if the request has any content

    if {"$RequestContentLen" != ""} {

      if {$RequestContentLen > 0} {

        if {"$RequestContentType" == ""} {
          ::LOG::Msg error "$ltag: missing Content-Type header field"
          set ServiceStatus    "ErrNoContentType"
          set ServiceStatusMsg "Request appears to have a content but no\
			        Content-Type is specified"
          return 1
        }

        if {![regexp -nocase "^${allowed_ctype}(?:;.*)?$" $RequestContentType]} {
          ::LOG::Msg error "$ltag: unsupported Content-Type: \"$RequestContentType\""
          set ServiceStatus    "ErrBadContentType"
          set ServiceStatusMsg "Allowed Content-Type is: $allowed_ctype"
          return 1          
        }

        if {[::IO::SetupChannel $chIn -eol-binary]} {
          ::LOG::Msg error "$ltag: failed to configure input channel"
          set ServiceStatus    "ErrCommIO"
          set ServiceStatusMsg "Failed to configure input channel"
          return 1
        }

        if {[::IO::ReadBlock $chIn $RequestContentLen $::HTTP::ioTimeout content read_count]} {
          ::LOG::Msg error "$ltag: failed to read request content"
          set ServiceStatus    "ErrCommIO"
          set ServiceStatusMsg "Failed to read request's content"
          return 1
        }

        upvar $__assigned    assigned_tokens
        upvar $__unassigned  unassigned_tokens
        ::HTTP::SplitAssignments content assigned_tokens unassigned_tokens

        return 0
      }

    }

    return 0
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  proc RegisterService { service_label service_table_entry } {

    if {![regexp -- {^([[:alnum:]])+([_-]*[[:alnum:]]+)*$} "$service_label"]} {
      #puts stderr "invalid service label: \"$service_label\""
      return -1
    }

    if {[info exists ::RWS::RES_TABLE_MAIN(/$service_label)]} {
      #puts stderr "service label already registered: \"$service_label\""
      return -2
    }

    # prevedere test sulla validità dei parametri ?

    set ::RWS::RES_TABLE_MAIN(/$service_label) "$service_table_entry"

    #puts stderr "service registered as \"$service_label\""
    return 0

  }
  #--------------------------------------------------------------------------



  array set RES_TABLE_MAIN {
    ":self:"	"::RWS::RES_Handler_rws"
    ":any:"	"::RWS::URI_Handler_dynamic_service"
  }



  #--------------------------------------------------------------------------
  # internal-response: restituisce informazioni sul servizio

  proc RES_Handler_rws {} {

    upvar RequestHandle			RequestHandle
    upvar ${RequestHandle}::HttpLib	HttpLib
    upvar ${RequestHandle}::HttpLibVer	HttpLibVer
    upvar ${RequestHandle}::BaseURI	BaseURI
    upvar ${RequestHandle}::TimeStamp	TimeStamp

    upvar ServiceStatus			ServiceStatus
    upvar ServiceStatusMsg		ServiceStatusMsg


    if {[::RWS::CheckMethod "GET"]<0} { return 1 }


    # step 1 : allocare il ResponseContent

    upvar ContentTreeRootHandle	ContentTreeRootHandle

    set ContentHandle [::XTREE::add_Child $ContentTreeRootHandle "ResponseContent"]
    set EntityHandle  [::XTREE::add_Child $ContentHandle         "ResourceEntity"]

    # section "ResponseContent/ResourceEntity/Application"

    set h_app [::XTREE::add_Child $EntityHandle "Application"]

    ::XTREE::set_Item $h_app "Name"                   $::RWS::APP_NAME
    ::XTREE::set_Item $h_app "Version"                $::RWS::APP_VER
    ::XTREE::set_Item $h_app "HttpLibrary"            $HttpLib
    ::XTREE::set_Item $h_app "HttpLibraryVersion"     $HttpLibVer
    ::XTREE::set_Item $h_app "RuntimeLibrary"         "Tcl"
    ::XTREE::set_Item $h_app "RuntimeLibraryVersion"  $::tcl_patchLevel


    # section "ResponseContent/ResourceEntity/HostingWebServer"

    set h_websrv [::XTREE::add_Child $EntityHandle "HostingWebServer"]

    foreach varname {ServerSoftware ServerInterface ServerName ServerAddress ServerPort} {
      upvar ${RequestHandle}::$varname  rvar
      if {"$rvar" != ""} {::XTREE::set_Item $h_websrv "$varname" "$rvar"}
    }

    ::XTREE::set_Item $h_websrv "ServiceBaseURI" "$BaseURI"


    # section "ResponseContent/ResourceEntity/HostingEnvironment

    set h_hostenv [::XTREE::add_Child $EntityHandle "HostingEnvironment"]

    ::XTREE::set_Item $h_hostenv "HardwareType"           $::tcl_platform(machine)


    # section "ResponseContent/ResourceEntity/HostingEnvironment/OperatingSystem

    set h_os [::XTREE::add_Child $h_hostenv "OperatingSystem"]

    ::XTREE::set_Item $h_os "Family"  $::tcl_platform(platform)
    ::XTREE::set_Item $h_os "Name"    $::tcl_platform(os)
    ::XTREE::set_Item $h_os "Version" $::tcl_platform(osVersion)


    # section "ResponseContent/ResourceEntity/HostingEnvironment/CurrentSystemTime

    set h_time [::XTREE::add_Child $h_hostenv "CurrentSystemTime"]

    ::XTREE::set_Item $h_time "SecondsSinceEpoch" "$TimeStamp"
    ::XTREE::set_Item $h_time "UTC"               "[clock format $TimeStamp -format {%a, %d %b %Y %T} -gmt 1]"
    ::XTREE::set_Item $h_time "Local"             "[clock format $TimeStamp -format {%a, %d %b %Y %T} -gmt 0]"
    ::XTREE::set_Item $h_time "TimeZone"          "[clock format $TimeStamp -format {%Z} -gmt 0]"
    ::XTREE::set_Item $h_time "TimeZoneOffset"    "[clock format $TimeStamp -format {%z} -gmt 0]"

    set ServiceStatus    "Found"
    set ServiceStatusMsg "RWS Servlet Informations"

    # generate internal response
    return 1
  }
  #--------------------------------------------------------------------------


  #--------------------------------------------------------------------------
  # This is the dynamic-services handler.
  #
  # Depending on the value of the "s_label" argument, this function either
  # returns a list of all available dynamic RWS services or load a specific
  # service and give it the control over the current request.
  #
  # If the value of "s_label" is equal to "" (i.e. an empty string), this
  # function scans the directory where the main RWS module is located for
  # any file named "<servicename>.rws" and returns a TCL list of all modules
  # found; for each module only the "<servicename>" part is returned, without
  # the trailing ".rws" extension.
  #
  # If the value of "s_label" is "<servicename>" (i.e. non blank), this
  # function try to load and source the file named "<servicename>.rws" from
  # the directory of the main RWS module. The service module must return
  # either the name of a parser table or the name of an init function; the
  # returned value (if valid) is registered into the main RWS service table
  # and the request engine continue its execution from the new entry.
  #

  proc URI_Handler_dynamic_service {s_label} {
    set ltag "RWS::URI_Handler_dynamic_service"

    if { "$s_label" != "" } {
      # try to load and source the service module
      upvar ServiceStatus	ServiceStatus
      upvar ServiceStatusMsg	ServiceStatusMsg
      set ::RWS::DYNAMIC_SERVICE_FILE [file join $::RWS::RWS_MODULE_PATH "${s_label}.rws"]
      if {![file readable $::RWS::DYNAMIC_SERVICE_FILE]} {
        unset ::RWS::DYNAMIC_SERVICE_FILE
        set ServiceStatus    "ErrNotFound"
        set ServiceStatusMsg "There is no internal resource nor any loadable service module with such name"
        return ""
      }
      ::LOG::Msg debug "$ltag: loading: \"$s_label\""
      if {[catch {namespace eval :: { source $::RWS::DYNAMIC_SERVICE_FILE }} result]} {
        unset ::RWS::DYNAMIC_SERVICE_FILE
        set ServiceStatus    "ErrInternal"
        set ServiceStatusMsg "An error occurred while loading the service module \"$s_label\" : $result"
        return ""
      }
      unset ::RWS::DYNAMIC_SERVICE_FILE
      if { "$result" != "" } {
        set logmsg "service \"$s_label\" successfully loaded and registered with the request engine"
        # does the service returned an init handler?
        if { "[info procs $result]" != "" } {
          ::LOG::Msg info "$logmsg (dynamic entry)"
          set ::RWS::RES_TABLE_MAIN(/$s_label) $result
          upvar RequestHandle		RequestHandle
          upvar RespondingService	RespondingService
          return [eval $result $s_label]
        }
        # not an handler: is it a parser table?
        if { [array exists $result] } {
          ::LOG::Msg info "$logmsg (static entry)"
          set ::RWS::RES_TABLE_MAIN(/$s_label) $result
          return $result
        }
      }
      set ServiceStatus    "ErrInternal"
      set ServiceStatusMsg "Failed to register service module \"$s_label\""
      return ""
    }

    # service_label == "" : returns the list of available dynamic services

    set list_of_services {}
    foreach fname [glob -nocomplain -directory $::RWS::RWS_MODULE_PATH -types {f} -- *.rws] {
      set s_label [file rootname [file tail $fname]]
      if {![info exists ::RWS::RES_TABLE_MAIN(/$s_label)]} {
        lappend list_of_services $s_label
      }
    }
    return $list_of_services
  }
  #--------------------------------------------------------------------------



  #--------------------------------------------------------------------------
  # This is the table-driven request-processor engine.
  #
  # Starting from the main table ::RWS::TABLE_MAIN it parses the Request
  # URI and calls the corresponding request's processing routine.

  proc RequestEngine { relative_uri } {
    upvar RequestHandle		RequestHandle
    upvar ServiceStatus		ServiceStatus
    upvar ServiceStatusMsg	ServiceStatusMsg
    upvar ContentTreeRootHandle	ContentTreeRootHandle

    upvar RespondingService	RespondingService
    upvar StatusHandle		StatusHandle

    set   ltag			"RWS::RequestEngine"

    array set Parameters	{}


    ::LOG::Msg debug "$ltag: entering URI parser loop"

    set table_name "::RWS::RES_TABLE_MAIN"

    while {1} {
      upvar $table_name	table

      ::LOG::Msg debug "$ltag: new round: current table: \"$table_name\", sub-URI: \"$relative_uri\""


      if { "$relative_uri" == "" } {
        if {[info exists table(:self:)]} {
          return [eval $table(:self:)]
        } else {
          set ServiceStatus    "ErrContainer"
          set ServiceStatusMsg "No operation is allowed on this resource"
          return 1
        }
      }


      if { "$relative_uri" == "/" } {
        if {[::RWS::CheckMethod "GET"]<0} { return 1 }

        # generate list of resources

        # note: when the dynamic handlers return an empty list of resurces, this
        #	could be due to a "regular" condition (i.e. external database
        #	empty) or an error condition (i.e. access to database failed)
	#	The value of ServiceStatus will be used to differentiate (and
        #	that's why we clear it before entering the engine)

        set ServiceStatus ""

        set res_list {}

        # static resources

        foreach res_name [lsort [array names table "/*"]] {
          if { [set res_name [string trim $res_name "/"]] != ""} {
            lappend res_list $res_name
          }
        }

        # dynamic resources

        if {[info exists table(:any:)]} {
          set res_list [concat $res_list [eval $table(:any:) {""}]]
        }

        if {[llength $res_list]} {
          set ContentHandle [::XTREE::add_Child $ContentTreeRootHandle "ResponseContent"]
          set EntityHandle  [::XTREE::add_Child $ContentHandle         "ResourceEntity"]
          foreach res_name $res_list {
            ::XTREE::set_Item $EntityHandle "Resource" $res_name
          }
          if { "$ServiceStatus" == "" } {
            set ServiceStatus    "Found"
            set ServiceStatusMsg "List of resources"
            return 1
          }
          append ServiceStatusMsg "; the list of resources might be incomplete"
          return 1
        }

        # resource-list is empty

        if { "$ServiceStatus" == "" } {
          set ServiceStatus    "ErrNotFound"
          set ServiceStatusMsg "No resources found"
          return 1
        }

        append ServiceStatusMsg "; unable to get the list of resources"
        return 1
      }

      # not an URI-path terminal point: split it (...a pretty fun regexp, isn't it?)

      ::LOG::Msg debug "$ltag: not a terminal URI component, splitting URI"

      # NOTE: when in CGI mode URI decoding and parsing could have been performed by the
      #       hosting web-server -- probably you should define a configuration flag to
      #       fall-back to a less complex regexp and/or disable URL-decoding

      if {[regexp {^/((?:[[:alnum:]_.!~*'():@&+$-]|(?:%[[:xdigit:]]{2}))+(?:;(?:[[:alnum:]_.!~*'():@&+$-]|(?:%[[:xdigit:]]{2}))+)*)(.*)$}\
                  $relative_uri m_all m_next_label m_next_uri]} {


        ::LOG::Msg debug "$ltag: URI label before decoding: \"$m_next_label\""


        # URL-decode path label

        set m_next_label [::HTTP::UrlDecode $m_next_label]

        ::LOG::Msg debug "$ltag: URI label after decoding: \"$m_next_label\""

        if {[info exists table(/$m_next_label)]} {

          # static entry found: it may be either a table or an handler

          set table_entry $table(/$m_next_label)

          if {[array exists $table_entry]} {
            set table_name $table_entry
            set relative_uri $m_next_uri
            continue
          }

          # not a table, call handler

          if {[set table_name [eval $table_entry $m_next_label]] != ""} {          
            set relative_uri $m_next_uri
            continue
          }

          # handler returned with an empty table name: an error occurred
          # (status variable set)
          return 1

        }

        # not a static entry: check for dynamic handler

        if {[info exists table(:any:)]} {
          # dynamic handler found

          if {[set table_name [eval $table(:any:) $m_next_label]] != ""} {          
            set relative_uri $m_next_uri
            continue
          }

          # handler returned with an empty table name: an error occurred
          # (status variable set)
          return 1

        }

        set ServiceStatus    "ErrNotFound"
        set ServiceStatusMsg "Parsing stopped on URI-label \"$m_next_label\""
        return 1
      }

      set ServiceStatus    "ErrInvalidURI"
      set ServiceStatusMsg "Parsing stopped on URI-component \"$relative_uri\""
      return 1

    }

    # not reached

    set ServiceStatus    "ErrInternal"
    set ServiceStatusMsg "An unexpected condition occurred: you should not be here."
    return 1
  }
  #--------------------------------------------------------------------------




  #--------------------------------------------------------------------------
  proc ProcessRequest { RequestHandle } {

    ::HTTP::SetServletName $RequestHandle "${::RWS::APP_NAME}/${::RWS::APP_VER}"


    # set-up the basic response data and response tree structure

    upvar ${RequestHandle}::SubURI               SubURI
    upvar ${RequestHandle}::AbsoluteCanonicalURI AbsCanonicalURI


    set ServiceStatus		""
    set ServiceStatusMsg	""
    set RespondingService	$::RWS::APP_NAME
    set ContentTreeRootHandle	[::XTREE::mkroot "RWS"]
    set StatusHandle		[::XTREE::add_Child $ContentTreeRootHandle "ResponseStatus"]


    if {[set catch_status [catch {::RWS::RequestEngine $SubURI} engine_status]] != 0} {
      # unhandled exception

      # first,log the condition:

      ::LOG::Msg error "RWS-EXCEPTION: message: \"$engine_status\", code:\
                       \"$::errorCode\"\n--- execution call-stack ---\n$::errorInfo\n--- (end of execution call-stack) ---"


      # reassigns the response fields that might have been set before the exception
      # occurred, in order to correctly send the exception error response.
      # This include clearing the entire XML content-tree.

      # restore the "X-Servlet:" field to RWS info
      ::HTTP::SetServletName $RequestHandle ""
      ::HTTP::SetServletName $RequestHandle "${::RWS::APP_NAME}/${::RWS::APP_VER}"

      # clear the ResponseHeader and the ResponseContent buffers
      ::HTTP::ClearResponseFields  $RequestHandle

      # set the response status fields
      set ServiceStatus		"ErrInternal"
      set ServiceStatusMsg	"The RWS Request Processor Engine aborted the execution due to an unhandled exception."
      set RespondingService	$::RWS::APP_NAME

      # clear the XML tree and setup it again
      ::XTREE::delete_Branch $ContentTreeRootHandle

      set ContentTreeRootHandle	[::XTREE::mkroot "RWS"]
      set StatusHandle		[::XTREE::add_Child $ContentTreeRootHandle "ResponseStatus"]
      set ContentHandle         [::XTREE::add_Child $ContentTreeRootHandle "ResponseContent"]
      set EntityHandle          [::XTREE::add_Child $ContentHandle         "Exception"]

      ::XTREE::set_Item $EntityHandle "ErrorMessage" $engine_status
      ::XTREE::set_Item $EntityHandle "ErrorCode"    $::errorCode

      ::XTREE::set_Item $EntityHandle "ExecutionCallStack" "<!\[CDATA\[\r\n$::errorInfo\r\n\]\]>"

      # set engine_status to 1 so to produce an XML response

      set engine_status 1

    }


    if {$engine_status < 0} {
      # something serious occurred, abort request (no response
      # will be sent). This should be improved now that the
      # general exception handling is implemented.
      return 0
    }

    # assigns HTTP status line and  ResponseStatus object

    if { ! [info exists ::RWS::STATUS_TABLE($ServiceStatus)] } {
      set ServiceStatus "ErrInternal"
    }

    set http_status_code    [lindex $::RWS::STATUS_TABLE($ServiceStatus) 1]
    set http_reason_phrase  [lindex $::RWS::STATUS_TABLE($ServiceStatus) 2]

    ::HTTP::SetResponseStatus $RequestHandle $http_status_code $http_reason_phrase

    if {$engine_status > 0} {

      # XML internal response

      ::XTREE::set_Item $StatusHandle "Code" "$ServiceStatus"

      if {[lindex $::RWS::STATUS_TABLE($ServiceStatus) 0]} {
        ::XTREE::set_Item $StatusHandle "Class" "Failure"
      } else {
        ::XTREE::set_Item $StatusHandle "Class" "Success"
      }

      ::XTREE::set_Item $StatusHandle "Description" "[lindex $::RWS::STATUS_TABLE($ServiceStatus) 3]"

      ::XTREE::set_Item $StatusHandle "ResourceURI" "$AbsCanonicalURI"

      ::XTREE::set_Item $StatusHandle "Service" "$RespondingService"
    
      if { $ServiceStatusMsg != "" } {
        ::XTREE::set_Item $StatusHandle "ServiceMessage" "$ServiceStatusMsg"
      }

      ::HTTP::SetResponseContentType $RequestHandle "application/xml; charset=ISO-8859-1"
      ::XTREE::dump_XML $ContentTreeRootHandle [concat ::HTTP::AddToResponseContent $RequestHandle {$I$D\r\n}]

      # this is an old test -- commented out
      #::HTTP::SetResponseContentType $RequestHandle "text/plain; charset=ISO-8859-1"
      #::TREE::dump_TEXT $ContentTreeRootHandle [concat ::HTTP::AddToResponseContent $RequestHandle {$D\r\n}]

    }

    ::LOG::Msg notice "RWS-ResponseStatus: Code: \"$ServiceStatus\"; Service: \"$RespondingService\";\
                       ServiceMessage: \"$ServiceStatusMsg\""

    # go ahead...

    # (note: the code returned by the SendResponse is currently ignored)

    ::HTTP::SendResponse $RequestHandle

    return 0

  }
  #--------------------------------------------------------------------------




}
#============================================================================



### MAIN IS HERE ###


::LOG::SetLevel_logfile [::HTTP::GetEnvVar "RWS_LOGLEVEL"]

if {! [::LOG::Open [::HTTP::GetEnvVar "RWS_LOGFILE"]]} {
  ::LOG::Open "/tmp/rws-cgi.log"
}

# calls the RWS entry-point passing the handle of the request structure

set exit_code [::RWS::ProcessRequest [::HTTP::CGI_SetupRequestStructure]]

::LOG::Close

exit $exit_code
