package log

type LoggerInf interface {
	WithName(name string)
	WithKeysAndValues(keysAndValues ...any)
	StructLoggerInf
	FormatLoggerInf
}

type StructLoggerInf interface {
	Infow(msg string, keysAndValues ...any)
	Warnw(msg string, keysAndValues ...any)
	Debugw(msg string, keysAndValues ...any)
	Fatalw(msg string, keysAndValues ...any)
	Errorw(msg string, keysAndValues ...any)
}

type FormatLoggerInf interface {
	Infof(format string, args ...any)
	Warnf(format string, args ...any)
	Debugf(format string, args ...any)
	Fatalf(format string, args ...any)
	Errorf(format string, args ...any)
}
