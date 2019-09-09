package com.github.marschall.equinox.jboss.logging;

import static org.osgi.service.log.LogService.LOG_DEBUG;
import static org.osgi.service.log.LogService.LOG_ERROR;
import static org.osgi.service.log.LogService.LOG_INFO;
import static org.osgi.service.log.LogService.LOG_WARNING;

import java.text.MessageFormat;

import org.jboss.logging.Logger;

final class EquinoxLogger extends Logger {

  /**
   * Anything that is not one of LOG_DEBUG, LOG_ERROR, LOG_INFO, LOG_WARNING is trace.
   */
  private static final int LOG_TRACE = LOG_DEBUG + 1;
  
  private static final int LOG_FATAL = LOG_ERROR - 1;
  
  private final org.eclipse.equinox.log.Logger logger;
  
  EquinoxLogger(String name, org.eclipse.equinox.log.Logger logger) {
    super(name);
    this.logger = logger;
  }

  @Override
  public boolean isEnabled(Level level) {
    int osgiLevel;
    switch (level) {
    case TRACE:
      osgiLevel = LOG_TRACE;
      break;
    case DEBUG:
      osgiLevel = LOG_DEBUG;
      break;
    case INFO:
      osgiLevel = LOG_INFO;
      break;
    case WARN:
      osgiLevel = LOG_WARNING;
      break;
    case ERROR:
      osgiLevel = LOG_ERROR;
      break;
    case FATAL:
      osgiLevel = LOG_FATAL;
      break;
    default:
      break;
    }
    return this.logger.isLoggable(osgiLevel);
  }

  @Override
  protected void doLog(Level level, String loggerClassName, Object message, Object[] parameters, Throwable thrown) {
    if (!isEnabled(level)) {
      return;
    }
    String text = parameters == null || parameters.length == 0 ? String.valueOf(message) : MessageFormat.format(String.valueOf(message), parameters);
    // TODO Auto-generated method stub
  }

  @Override
  protected void doLogf(Level level, String loggerClassName, String format, Object[] parameters, Throwable thrown) {
    if (!isEnabled(level)) {
      return;
    }
    String text = parameters == null ? String.format(format) : String.format(format, parameters);
    // TODO Auto-generated method stub

  }

}
