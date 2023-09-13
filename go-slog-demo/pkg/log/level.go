package log

import (
	"go.uber.org/zap/zapcore"
	"golang.org/x/exp/slog"
)

type Level int8

const (
	LevelInfo Level = iota
	LevelWarn
	LevelDebug
	LevelError
	LevelFatal
)

type internalLevels struct {
	slogLevel slog.Level
	zapLevel  zapcore.Level
}

var (
	internalInfoLevels  = internalLevels{slogLevel: slog.LevelInfo, zapLevel: zapcore.InfoLevel}
	internalWarnLevels  = internalLevels{slogLevel: slog.LevelWarn, zapLevel: zapcore.WarnLevel}
	internalDebugLevels = internalLevels{slogLevel: slog.LevelDebug, zapLevel: zapcore.DebugLevel}
	internalErrorLevels = internalLevels{slogLevel: slog.LevelError, zapLevel: zapcore.ErrorLevel}
	internalFatalLevels = internalLevels{slogLevel: slog.LevelError, zapLevel: zapcore.FatalLevel}
)

var levelMap = map[Level]internalLevels{
	LevelInfo:  internalInfoLevels,
	LevelWarn:  internalWarnLevels,
	LevelDebug: internalDebugLevels,
	LevelError: internalErrorLevels,
	LevelFatal: internalFatalLevels,
}

func (l *Level) getLevels() internalLevels {
	if l == nil {
		return internalErrorLevels
	}
	if v, ok := levelMap[*l]; ok {
		return v
	}
	return internalErrorLevels
}

func (l *Level) GetSlogLevel() slog.Level {
	return l.getLevels().slogLevel
}

func (l *Level) GetZapLevel() zapcore.Level {
	return l.getLevels().zapLevel
}
