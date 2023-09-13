package log

var defaultLogger = &Logger{Logger: newSlogLogger(LevelInfo, true)}

func GetLogger() LoggerInf {
	return defaultLogger
}

func Infow(msg string, keysAndValues ...any) {
	defaultLogger.Logger.Infow(msg, keysAndValues...)
}

func Warnw(msg string, keysAndValues ...any) {
	defaultLogger.Logger.Warnw(msg, keysAndValues...)
}

func Debugw(msg string, keysAndValues ...any) {
	defaultLogger.Logger.Debugw(msg, keysAndValues...)
}

func Fatalw(msg string, keysAndValues ...any) {
	defaultLogger.Logger.Fatalw(msg, keysAndValues...)
}

func Errorw(msg string, keysAndValues ...any) {
	defaultLogger.Logger.Errorw(msg, keysAndValues...)
}

func Infof(format string, args ...any) {
	defaultLogger.Logger.Infof(format, args...)
}

func Warnf(format string, args ...any) {
	defaultLogger.Logger.Warnf(format, args...)
}

func Debugf(format string, args ...any) {
	defaultLogger.Logger.Debugf(format, args...)
}

func Fatalf(format string, args ...any) {
	defaultLogger.Fatalf(format, args...)
}

func Errorf(format string, args ...any) {
	defaultLogger.Errorf(format, args...)
}
