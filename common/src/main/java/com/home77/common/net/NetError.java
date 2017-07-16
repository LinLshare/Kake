package com.home77.common.net;

import com.home77.common.base.lang.RV;

/**
 * This file contains the list of network errors.
 * Ranges:
 * 0-99 System related errors
 * 100-199 Connection related errors
 * 200-299 Certificate errors
 * 300-399 HTTP errors
 * 400-499 Cache errors
 * 500-599 ?
 * 600-699 FTP errors
 * 700-799 Certificate manager errors
 * 800-899 DNS resolver errors
 */
public class NetError {
  public static final int OK = RV.OK;

  // An asynchronous IO operation is not yet complete.  This usually does not
  // indicate a fatal error.  Typically this error will be generated as a
  // notification to wait for some external notification that the IO operation
  // finally completed.
  public static final int NET_ERR_IO_PENDING = -1;

  // A generic failure occurred.
  public static final int NET_ERR_FAILED = -2;

  // An operation was aborted (due to user action).
  public static final int NET_ERR_ABORTED = -3;

  // An argument to the function is incorrect.
  public static final int NET_ERR_INVALID_ARGUMENT = -4;

  // The handle or file descriptor is invalid.
  public static final int NET_ERR_INVALID_HANDLE = -5;

  // The file or directory cannot be found.
  public static final int NET_ERR_FILE_NOT_FOUND = -6;

  // An operation timed out.
  public static final int NET_ERR_TIMED_OUT = -7;

  // The file is too large.
  public static final int NET_ERR_FILE_TOO_BIG = -8;

  // An unexpected error.  This may be caused by a programming mistake or an
  // invalid assumption.
  public static final int NET_ERR_UNEXPECTED = -9;

  // Permission to access a resource, other than the network = was denied.
  public static final int NET_ERR_ACCESS_DENIED = -10;

  // The operation failed because of unimplemented functionality.
  public static final int NET_ERR_NOT_IMPLEMENTED = -11;

  // There were not enough resources to complete the operation.
  public static final int NET_ERR_INSUFFICIENT_RESOURCES = -12;

  // Memory allocation failed.
  public static final int NET_ERR_OUT_OF_MEMORY = -13;

  // The file upload failed because the file's modification time was different
  // from the expectation.
  public static final int NET_ERR_UPLOAD_FILE_CHANGED = -14;

  // The socket is not connected.
  public static final int NET_ERR_SOCKET_NOT_CONNECTED = -15;

  // The file already exists.
  public static final int NET_ERR_FILE_EXISTS = -16;

  // The path or file name is too long.
  public static final int NET_ERR_FILE_PATH_TOO_LONG = -17;

  // Not enough room left on the disk.
  public static final int NET_ERR_FILE_NO_SPACE = -18;

  // The file has a virus.
  public static final int NET_ERR_FILE_VIRUS_INFECTED = -19;

  // The client chose to block the request.
  public static final int NET_ERR_BLOCKED_BY_CLIENT = -20;

  // The network changed.
  public static final int NET_ERR_NETWORK_CHANGED = -21;

  // The request was blocked by the URL blacklist configured by the domain
  // administrator.
  public static final int NET_ERR_BLOCKED_BY_ADMINISTRATOR = -22;

  // The socket is already connected.
  public static final int NET_ERR_SOCKET_IS_CONNECTED = -23;

  // The request was blocked because the forced reenrollment check is still
  // pending. This error can only occur on ChromeOS.
  // The error can be emitted by code in chrome/browser/policy/policy_helpers.cc.
  public static final int NET_ERR_BLOCKED_ENROLLMENT_CHECK_PENDING = -24;

  // The upload failed because the upload stream needed to be re-read, due to a
  // retry or a redirect, but the upload stream doesn't support that operation.
  public static final int NET_ERR_UPLOAD_STREAM_REWIND_NOT_SUPPORTED = -25;

  // A connection was closed (corresponding to a TCP FIN;.
  public static final int NET_ERR_CONNECTION_CLOSED = -100;

  // A connection was reset (corresponding to a TCP RST;.
  public static final int NET_ERR_CONNECTION_RESET = -101;

  // A connection attempt was refused.
  public static final int NET_ERR_CONNECTION_REFUSED = -102;

  // A connection timed out as a result of not receiving an ACK for data sent.
  // This can include a FIN packet that did not get ACK'd.
  public static final int NET_ERR_CONNECTION_ABORTED = -103;

  // A connection attempt failed.
  public static final int NET_ERR_CONNECTION_FAILED = -104;

  // The host name could not be resolved.
  public static final int NET_ERR_NAME_NOT_RESOLVED = -105;

  // The Internet connection has been lost.
  public static final int NET_ERR_INTERNET_ERR_DISCONNECTED = -106;

  // An SSL protocol error occurred.
  public static final int NET_ERR_SSL_PROTOCOL_ERROR = -107;

  // The IP address or port number is invalid (e.g., cannot connect to the IP
  // address 0 or the port 0).
  public static final int NET_ERR_ADDRESS_INVALID = -108;

  // The IP address is unreachable.  This usually means that there is no route to
  // the specified host or network.
  public static final int NET_ERR_ADDRESS_UNREACHABLE = -109;

  // The client and server don't support a common SSL protocol version or
  // cipher suite.
  public static final int NET_ERR_SSL_VERSION_OR_CIPHER_MISMATCH = -113;

  // A connection attempt timed out.
  public static final int NET_ERR_CONNECTION_TIMED_OUT = -118;

  // There are too many pending DNS resolves, so a request in the queue was
  // aborted.
  public static final int NET_ERR_HOST_RESOLVER_QUEUE_TOO_LARGE = -119;

  // Failed establishing a connection to the SOCKS proxy server for a target host.
  public static final int NET_ERR_SOCKS_CONNECTION_FAILED = -120;

  // The SOCKS proxy server failed establishing connection to the target host
  // because that host is unreachable.
  public static final int NET_ERR_SOCKS_CONNECTION_HOST_UNREACHABLE = -121;

  // The request to negotiate an alternate protocol failed.
  public static final int NET_ERR_NPN_NEGOTIATION_FAILED = -122;

  // We've hit the max socket limit for the socket pool while preconnecting.  We
  // don't bother trying to preconnect more sockets.
  public static final int NET_ERR_PRECONNECT_MAX_SOCKET_LIMIT = -133;

  // The permission to use the SSL client certificate's private key was denied.
  public static final int NET_ERR_SSL_CLIENT_AUTH_PRIVATE_KEY_ACCESS_DENIED = -134;

  // An error occurred when trying to do a name resolution (DNS;.
  public static final int NET_ERR_NAME_RESOLUTION_FAILED = -137;

  // Permission to access the network was denied. This is used to distinguish
  // errors that were most likely caused by a firewall from other access denied
  // errors. See also ERR_ACCESS_DENIED.
  public static final int NET_ERR_NETWORK_ACCESS_DENIED = -138;

  // The request throttler module cancelled this request to avoid DDOS.
  public static final int NET_ERR_TEMPORARILY_THROTTLED = -139;

  // The message was too large for the transport.  (for example a UDP message
  // which exceeds size threshold).
  public static final int NET_ERR_MSG_TOO_BIG = -142;

  // Returned when attempting to bind an address that is already in use.
  public static final int NET_ERR_ADDRESS_IN_USE = -147;

  // The scheme of the URL is disallowed.
  public static final int NET_ERR_DISALLOWED_URL_SCHEME = -301;

  // The scheme of the URL is unknown.
  public static final int NET_ERR_UNKNOWN_URL_SCHEME = -302;

  // Attempting to load an URL resulted in too many redirects.
  public static final int NET_ERR_TOO_MANY_REDIRECTS = -310;

  // Attempting to load an URL resulted in an unsafe redirect (e.g., a redirect
  // to file:// is considered unsafe).
  public static final int NET_ERR_UNSAFE_REDIRECT = -311;

  // Attempting to load an URL with an unsafe port number.  These are port
  // numbers that correspond to services, which are not robust to spurious input
  // that may be constructed as a result of an allowed web construct (e.g., HTTP
  // looks a lot like SMTP, so form submission to port 25 is denied).
  public static final int NET_ERR_UNSAFE_PORT = -312;

  // The server's response was invalid.
  public static final int NET_ERR_INVALID_RESPONSE = -320;

  // Error in chunked transfer encoding.
  public static final int NET_ERR_INVALID_CHUNKED_ENCODING = -321;

  // The server did not support the request method.
  public static final int NET_ERR_METHOD_NOT_SUPPORTED = -322;

  // The response was 407 (Proxy Authentication Required), yet we did not send
  // the request to a proxy.
  public static final int NET_ERR_UNEXPECTED_PROXY_AUTH = -323;

  // The server closed the connection without sending any data.
  public static final int NET_ERR_EMPTY_RESPONSE = -324;

  // The headers section of the response is too large.
  public static final int NET_ERR_RESPONSE_HEADERS_TOO_BIG = -325;

  // The PAC requested by HTTP did not have a valid status code (non-200).
  public static final int NET_ERR_PAC_STATUS_NOT_OK = -326;

  // The evaluation of the PAC script failed.
  public static final int NET_ERR_PAC_SCRIPT_FAILED = -327;

  // The response was 416 (Requested range not satisfiable) and the server cannot
  // satisfy the range requested.
  public static final int NET_ERR_REQUEST_RANGE_NOT_SATISFIABLE = -328;

  // The identity used for authentication is invalid.
  public static final int NET_ERR_MALFORMED_IDENTITY = -329;

  // Content decoding of the response body failed.
  public static final int NET_ERR_CONTENT_DECODING_FAILED = -330;

  // An operation could not be completed because all network IO
  // is suspended.
  public static final int NET_ERR_NETWORK_IO_SUSPENDED = -331;

  // FLIP data received without receiving a SYN_REPLY on the stream.
  public static final int NET_ERR_SYN_REPLY_NOT_RECEIVED = -332;

  // Converting the response to target encoding failed.
  public static final int NET_ERR_ENCODING_CONVERSION_FAILED = -333;

  // The server sent an FTP directory listing in a format we do not understand.
  public static final int NET_ERR_UNRECOGNIZED_FTP_DIRECTORY_LISTING_FORMAT = -334;

  // Detecting the encoding of the response failed.
  public static final int NET_ERR_ENCODING_DETECTION_FAILED = -340;

  // The environment was not set up correctly for authentication (for
  // example, no KDC could be found or the principal is unknown).
  public static final int NET_ERR_MISCONFIGURED_AUTH_ENVIRONMENT = -343;

  // An undocumented SSPI or GSSAPI status code was returned.
  public static final int NET_ERR_UNDOCUMENTED_SECURITY_LIBRARY_STATUS = -344;

  // The HTTP response was too big to drain.
  public static final int NET_ERR_RESPONSE_BODY_TOO_BIG_TO_DRAIN = -345;

  // The HTTP response contained multiple distinct Content-Length headers.
  public static final int NET_ERR_RESPONSE_HEADERS_MULTIPLE_CONTENT_LENGTH = -346;

  // The HTTP response contained multiple Content-Disposition headers.
  public static final int NET_ERR_RESPONSE_HEADERS_MULTIPLE_CONTENT_DISPOSITION = -349;

  // The HTTP response contained multiple Location headers.
  public static final int NET_ERR_RESPONSE_HEADERS_MULTIPLE_LOCATION = -350;

  // The HTTP response body transferred fewer bytes than were advertised by the
  // Content-Length header when the connection is closed.
  public static final int NET_ERR_CONTENT_LENGTH_MISMATCH = -354;

  // The HTTP response body is transferred with Chunked-Encoding, but the
  // terminating zero-length chunk was never sent when the connection is closed.
  public static final int NET_ERR_INCOMPLETE_CHUNKED_ENCODING = -355;

  // The HTTP headers were truncated by an EOF.
  public static final int NET_ERR_RESPONSE_HEADERS_TRUNCATED = -357;
}
