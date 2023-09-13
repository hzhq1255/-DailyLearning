package log

import (
	"errors"
	"testing"
)

func getTestSlogLogger() LoggerInf {
	return NewLoggerFromType(LevelInfo, SlogLoggerType)
}

func getTestZapLogger() LoggerInf {
	return NewLoggerFromType(LevelInfo, ZapLoggerType)
}

func TestLogger_global(t *testing.T) {
	// global log
	err := errors.New("test")
	Infof("Info message username: %s", "admin")
	Infow("Info message", "username", "admin")
	Warnf("Warning message username: %s", "admin")
	Warnw("Warning message", "username", "admin")
	Debugf("Debug message username: %s", "admin")
	Debugw("Debug message", "username", "admin")
	Errorf("Error message username: %s", "admin")
	ErrorStackF(err, "Error message username: %s", "admin")
	Errorw("Error message", "username", "admin")
	ErrorStackW(err, "Error message", "username", "admin")
	Fatalf("Fatal message username: %s", "admin")
	Fatalw("Fatal message", "username", "admin")
}

func TestLogger_slog(t *testing.T) {
	l := NewLoggerFromType(LevelInfo, SlogLoggerType).WithName("test")
	err := errors.New("test")
	l.Infof("Info message username: %s", "admin")
	l.Infow("Info message", "username", "admin")
	l.Warnf("Warning message username: %s", "admin")
	l.Warnw("Warning message", "username", "admin")
	l.Debugf("Debug message username: %s", "admin")
	l.Debugw("Debug message", "username", "admin")
	l.Errorf("Error message username: %s", "admin")
	l.ErrorStackF(err, "Error message username: %s", "admin")
	l.Errorw("Error message", "username", "admin")
	l.ErrorStackW(err, "Error message", "username", "admin")
	l.Fatalf("Fatal message username: %s", "admin")
	l.Fatalw("Fatal message", "username", "admin")
}

func TestLogger_zap(t *testing.T) {
	l := NewLoggerFromType(LevelInfo, ZapLoggerType).WithName("test")
	err := errors.New("test")
	l.Infof("Info message username: %s", "admin")
	l.Infow("Info message", "username", "admin")
	l.Warnf("Warning message username: %s", "admin")
	l.Warnw("Warning message", "username", "admin")
	l.Debugf("Debug message username: %s", "admin")
	l.Debugw("Debug message", "username", "admin")
	l.Errorf("Error message username: %s", "admin")
	l.ErrorStackF(err, "Error message username: %s", "admin")
	l.Errorw("Error message", "username", "admin")
	l.ErrorStackW(err, "Error message", "username", "admin")
	l.Fatalf("Fatal message username: %s", "admin")
	l.Fatalw("Fatal message", "username", "admin")
}
