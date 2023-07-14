package com.github.marschall.equinox.jboss.logging;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.log.ExtendedLogService;
import org.jboss.logging.Logger;
import org.jboss.logging.LoggerProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A {@link LoggerProvider} that brides to {@link ExtendedLogService}.
 */
public final class EquinoxLoggerProvider implements LoggerProvider {

  private final ThreadLocal<Deque<Entry>> ndcStack = new ThreadLocal<>();

  private final ThreadLocal<Map<String, Object>> mdcMap = new ThreadLocal<>();

  private final ExtendedLogService logService;

  /**
   * Called by JBoss Logging.
   */
  public EquinoxLoggerProvider() {
    // this is a bit hairy
    // since we are fragment and not a bundle we can't have an activator we have to work around this

    Bundle bundle = FrameworkUtil.getBundle(EquinoxLoggerProvider.class);
    // start the bundle so that we have a bundle context
    // maybe the bundle is not started because it has no Bundle-ActivationPolicy: lazy
    if (bundle.getState() == Bundle.RESOLVED) {
      try {
        bundle.start();
      } catch (BundleException e) {
        throw new RuntimeException("could not start bundle", e);
      }
    }
    // reimplement BundleActivator#start()
    BundleContext context = bundle.getBundleContext();
    ServiceTracker<?, ExtendedLogService> serviceTracker =
        new ServiceTracker<>(context, ExtendedLogService.class, null);

    serviceTracker.open();
    // reimplement BundleActivator#stop()
    context.addBundleListener((BundleEvent event) -> {
      if (event.getBundle().getBundleId() == bundle.getBundleId()
          && event.getType() == BundleEvent.STOPPING) {
        serviceTracker.close();
      }
    });


    this.logService = serviceTracker.getService();
  }

  @Override
  public Logger getLogger(String name) {
    return new EquinoxLogger(name, this.logService.getLogger(name));
  }

  @Override
  public void clearMdc() {
    Map<String, Object> map = mdcMap.get();
    if (map != null) {
      map.clear();
    }
    // not clearing the thread local should not introduce as Classloader leak
    // as the only objects reference are from the system classloader
  }

  @Override
  public Object getMdc(String key) {
    Map<String, Object> map = mdcMap.get();
    if (map == null) {
      return null;
    }
    return map.get(key);
  }

  @Override
  public Map<String, Object> getMdcMap() {
    Map<String, Object> map = mdcMap.get();
    return map == null ? Collections.emptyMap() : map;
  }

  @Override
  public Object putMdc(String key, Object value) {
    Map<String, Object> map = mdcMap.get();
    if (map == null) {
      map = new HashMap<>();
      mdcMap.set(map);
    }
    return map.put(key, value);
  }

  @Override
  public void removeMdc(String key) {
    Map<String, Object> map = mdcMap.get();
    if (map != null) {
      map.remove(key);
    }
  }

  @Override
  public void clearNdc() {
    Deque<Entry> stack = ndcStack.get();
    if (stack != null) {
      stack.clear();
    }
  }

  @Override
  public String getNdc() {
    Deque<Entry> stack = ndcStack.get();
    if (stack == null || stack.isEmpty()) {
      return null;
    } else {
      return stack.peek().merged;
    }
  }

  @Override
  public int getNdcDepth() {
    Deque<Entry> stack = ndcStack.get();
    return stack == null ? 0 : stack.size();
  }

  @Override
  public String peekNdc() {
    Deque<Entry> stack = ndcStack.get();
    if (stack == null || stack.isEmpty()) {
      return "";
    } else {
      return stack.peek().current;
    }
  }

  @Override
  public String popNdc() {
    Deque<Entry> stack = ndcStack.get();
    if (stack == null || stack.isEmpty()) {
      return "";
    } else {
      return stack.pop().current;
    }
  }

  @Override
  public void pushNdc(String message) {
    Deque<Entry> stack = ndcStack.get();
    if (stack == null) {
      stack = new ArrayDeque<>();
      ndcStack.set(stack);
    }
    Entry entry;
    if (stack.isEmpty()) {
      entry = new Entry(message);
    } else {
      entry = new Entry(stack.peek(), message);
    }
    stack.push(entry);
  }

  @Override
  public void setNdcMaxDepth(int maxDepth) {
    Deque<Entry> stack = ndcStack.get();
    if (stack != null) {
      while (stack.size() > maxDepth) {
        stack.pop();
      }
    }
  }

  private static class Entry {

    private String merged;
    private String current;

    Entry(String current) {
      merged = current;
      this.current = current;
    }

    Entry(Entry parent, String current) {
      merged = parent.merged + ' ' + current;
      this.current = current;
    }
  }

}
