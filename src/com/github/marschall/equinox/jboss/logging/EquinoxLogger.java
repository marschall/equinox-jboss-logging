package com.github.marschall.equinox.jboss.logging;

import static org.osgi.service.log.LogService.LOG_DEBUG;
import static org.osgi.service.log.LogService.LOG_ERROR;
import static org.osgi.service.log.LogService.LOG_INFO;
import static org.osgi.service.log.LogService.LOG_WARNING;

import java.text.MessageFormat;

import org.jboss.logging.Logger;

/**
 * A {@link Logger} that delegates to a {@link org.eclipse.equinox.log.Logger}.
 */
final class EquinoxLogger extends Logger {

  private static final long serialVersionUID = 1L;

  private final org.eclipse.equinox.log.Logger logger;

  EquinoxLogger(String name, org.eclipse.equinox.log.Logger logger) {
    super(name);
    this.logger = logger;
  }

  @Override
  public boolean isEnabled(Level level) {
    int osgiLevel = toOsgiLevel(level);
    return this.logger.isLoggable(osgiLevel);
  }

  private static int toOsgiLevel(Level level) {
    int osgiLevel;
    switch (level) {
    case TRACE:
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
    case FATAL:
      osgiLevel = LOG_ERROR;
      break;
    default:
      throw new IllegalArgumentException("unknown level: " + level);
    }
    return osgiLevel;
  }

  @Override
  protected void doLog(Level level, String loggerClassName, Object message, Object[] parameters, Throwable thrown) {
    if (!isEnabled(level)) {
      return;
    }

    String text;
    if (parameters != null && parameters.length > 0) {
      text = MessageFormat.format(String.valueOf(message), parameters);
    } else {
      text = String.valueOf(message);
    }
    int osgiLevel = toOsgiLevel(level);
    this.logger.log(osgiLevel, text, thrown);
  }

  @Override
  protected void doLogf(Level level, String loggerClassName, String format, Object[] parameters, Throwable thrown) {
    if (!isEnabled(level)) {
      return;
    }
    String text;
    if (parameters != null) {
      text = String.format(format, parameters);
    } else {
      text = String.format(format);
    }
    int osgiLevel = toOsgiLevel(level);
    this.logger.log(osgiLevel, text, thrown);

  }

}
