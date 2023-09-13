package log

var defaultLogger = newSlogLogger(LevelInfo, true)

func Infow(msg string, keysAndValues ...any) {
	defaultLogger.Infow(msg, keysAndValues...)
}

func Warnw(msg string, keysAndValues ...any) {
	defaultLogger.Warnw(msg, keysAndValues...)
}

func Debugw(msg string, keysAndValues ...any) {
	defaultLogger.Debugw(msg, keysAndValues...)
}

func Fatalw(msg string, keysAndValues ...any) {
	defaultLogger.Fatalw(msg, keysAndValues...)
}

func Errorw(msg string, keysAndValues ...any) {
	defaultLogger.Errorw(msg, keysAndValues...)
}

func Infof(format string, args ...any) {
	defaultLogger.Infof(format, args...)
}

func Warnf(format string, args ...any) {
	defaultLogger.Warnf(format, args...)
}

func Debugf(format string, args ...any) {
	defaultLogger.Debugf(format, args...)
}

func Fatalf(format string, args ...any) {
	defaultLogger.Fatalf(format, args...)
}

func Errorf(format string, args ...any) {
	defaultLogger.Errorf(format, args...)
}
