package com.hariko.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

public class DynamicThresholdLoggerFilter extends TurboFilter {

	public DynamicThresholdLoggerFilter() {
		setName("dynamicFilter");
	}

	@Override
	public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
		String value = null;//MDC.get(MyThreadContext.MDC_LOG_LEVEL);
		if (null == value) {
			return FilterReply.NEUTRAL;
		}
		Level logLvl = Level.toLevel(value, Level.OFF);
		if (logLvl.equals(Level.OFF)) {
			return FilterReply.NEUTRAL;
		}
		if (level.levelInt >= logLvl.levelInt) {
			return FilterReply.ACCEPT;
		} else {
			return FilterReply.DENY;
		}
	}
}
