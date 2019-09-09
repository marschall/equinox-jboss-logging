package com.github.marschall.equinox.jboss.logging;

import java.util.Map;

import org.eclipse.equinox.log.ExtendedLogService;
import org.jboss.logging.Logger;
import org.jboss.logging.LoggerProvider;

public final class EquinoxLoggerProvider implements LoggerProvider {
  
  private ExtendedLogService logService;

  @Override
  public void clearMdc() {
    // TODO Auto-generated method stub
  }

  @Override
  public void clearNdc() {
    // TODO Auto-generated method stub
  }

  @Override
  public Logger getLogger(String name) {
    return new EquinoxLogger(name, this.logService.getLogger(name));
  }

  @Override
  public Object getMdc(String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Object> getMdcMap() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getNdc() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getNdcDepth() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String peekNdc() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String popNdc() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void pushNdc(String message) {
    // TODO Auto-generated method stub

  }

  @Override
  public Object putMdc(String key, Object value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void removeMdc(String key) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setNdcMaxDepth(int maxDepth) {
    // TODO Auto-generated method stub

  }

}
