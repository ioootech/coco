package tech.iooo.boot.core.http;

import lombok.experimental.UtilityClass;

/**
 * Created on 2018/11/21 5:13 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 *
 * https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
 */
@UtilityClass
public class StatusCode {

  /**
   * 2xx Success
   */
  public Integer OK = 200;
  public Integer CREATED = 201;
  public Integer ACCEPTED = 202;
  public Integer NON_AUTHORITATIVE_INFORMATION = 203;
  public Integer NO_CONTENT = 204;
  public Integer RESET_CONTENT = 205;
  public Integer PARTIAL_CONTENT = 206;
  public Integer MULTI_STATUS = 207;
  public Integer ALREADY_REPORTED = 208;
  public Integer IM_USED = 226;

  /**
   * 3xx Redirection
   */
  public Integer MULTIPLE_CHOICES = 300;
  public Integer MOVED_PERMANENTLY = 301;
  public Integer FOUND = 302;
  public Integer SEE_OTHER = 303;
  public Integer NOT_MODIFIED = 304;
  public Integer USE_PROXY = 305;
  public Integer SWITCH_PROXY = 306;
  public Integer TEMPORARY_REDIRECT = 307;
  public Integer PERMANENT_REDIRECT = 308;

  /**
   * 4xx Client errors
   */
  public Integer BAD_REQUEST = 400;
  public Integer UNAUTHORIZED = 401;
  public Integer PAYMENT_REQUIRED = 402;
  public Integer FORBIDDEN = 403;
  public Integer NOT_FOUND = 404;
  public Integer METHOD_NOT_ALLOWED = 405;
  public Integer NOT_ACCEPTABLE = 406;
  public Integer PROXY_AUTHENTICATION_REQUIRED = 407;
  public Integer REQUEST_TIMEOUT = 408;
  public Integer CONFLICT = 409;
  public Integer GONE = 410;
  public Integer LENGTH_REQUIRED = 411;
  public Integer PRECONDITION_FAILED = 412;
  public Integer PAYLOAD_TOO_LARGE = 413;
  public Integer URI_TOO_LONG = 414;
  public Integer UNSUPPORTED_MEDIA_TYPE = 415;
  public Integer RANGE_NOT_SATISFIABLE = 416;
  public Integer EXPECTATION_FAILED = 417;
  public Integer I_AM_A_TEAPOT = 418;
  public Integer MISDIRECTED_REQUEST = 421;
  public Integer UNPROCESSABLE_ENTITY = 422;
  public Integer LOCKED = 423;
  public Integer FAILED_DEPENDENCY = 424;
  public Integer UPGRADE_REQUIRED = 426;
  public Integer PRECONDITION_REQUIRED = 428;
  public Integer TOO_MANY_REQUESTS = 429;
  public Integer REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
  public Integer UNAVAILABLE_FOR_LEGAL_REASONS = 451;

  /**
   * 5xx Server errors
   */
  public Integer INTERNAL_SERVER_ERROR = 500;
  public Integer NOT_IMPLEMENTED = 501;
  public Integer BAD_GATEWAY = 502;
  public Integer SERVICE_UNAVAILABLE = 503;
  public Integer GATEWAY_TIMEOUT = 504;
  public Integer HTTPVERSION_NOT_SUPPORTED = 505;
  public Integer VARIANT_ALSO_NEGOTIATES = 506;
  public Integer INSUFFICIENT_STORAGE = 507;
  public Integer LOOP_DETECTED = 508;
  public Integer NOT_EXTENDED_ = 510;
  public Integer NETWORK_AUTHENTICATION_REQUIRED = 511;

  /**
   * Unofficial codes
   */
  public Integer UNOFFICIAL_CODES_CHECKPOINT = 103;
  public Integer UNOFFICIAL_CODES_THIS_IS_FINE = 218;
  public Integer UNOFFICIAL_CODES_PAGE_EXPIRED = 419;
  public Integer UNOFFICIAL_CODES_METHOD_FAILURE = 420;
  public Integer UNOFFICIAL_CODES_ENHANCE_YOUR_CALM = 420;
  public Integer UNOFFICIAL_CODES_BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS = 450;
  public Integer UNOFFICIAL_CODES_INVALID_TOKEN = 498;
  public Integer UNOFFICIAL_CODES_TOKEN_REQUIRED = 499;
  public Integer UNOFFICIAL_CODES_BANDWIDTH_LIMIT_EXCEEDED = 509;
  public Integer UNOFFICIAL_CODES_INVALID_SSL_CERTIFICATE = 526;
  public Integer UNOFFICIAL_CODES_SITE_IS_FROZEN = 530;
  public Integer UNOFFICIAL_CODES_NETWORK_READ_TIMEOUT_ERROR = 598;

  /**
   * Internet Information Services
   */
  public Integer INTERNET_INFORMATION_SERVICES_LOGIN_TIME_OUT = 440;
  public Integer INTERNET_INFORMATION_SERVICES_RETRY_WITH = 449;
  public Integer INTERNET_INFORMATION_SERVICES_REDIRECT = 451;

  /**
   * Nginx
   */
  public Integer NGINX_NO_RESPONSE = 444;
  public Integer NGINX_REQUEST_HEADER_TOO_LARGE = 494;
  public Integer NGINX_SSL_CERTIFICATE_ERROR = 495;
  public Integer NGINX_SSL_CERTIFICATE_REQUIRED = 496;
  public Integer NGINX_HTTP_REQUEST_SENT_TO_HTTPS_PORT = 497;
  public Integer NGINX_CLIENT_CLOSED_REQUEST = 499;

  /**
   * Cloudflare
   */
  public Integer CLOUDFLARE_UNKNOWN_ERROR = 520;
  public Integer CLOUDFLARE_WEB_SERVER_IS_DOWN = 521;
  public Integer CLOUDFLARE_CONNECTION_TIMED_OUT = 522;
  public Integer CLOUDFLARE_ORIGIN_IS_UNREACHABLE = 523;
  public Integer CLOUDFLARE_A_TIMEOUT_OCCURRED = 524;
  public Integer CLOUDFLARE_SSL_HANDSHAKE_FAILED = 525;
  public Integer CLOUDFLARE_INVALID_SSL_CERTIFICATE = 526;
  public Integer CLOUDFLARE_RAILGUN_ERROR = 527;
  public Integer CLOUDFLARE_ORIGIN_DNS_ERROR = 530;
}
